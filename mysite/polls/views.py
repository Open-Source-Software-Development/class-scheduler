from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.models import User, Group
from django.contrib.auth import login, authenticate, logout
from django import template
from django.template.response import TemplateResponse
from scheduler.blockcalendar import *
from django.urls import reverse
from .models import Block
from scheduler.models import Season
from scheduler.models import Course
from scheduler.models import Professor
from scheduler.models import Block
from scheduler.models import Division
from scheduler.models import ProfessorConstraint
from scheduler.models import GradeLevel
from scheduler.models import Room
from scheduler.courseConstraints import CourseLevel
from scheduler.courseSeason import CourseSeason
from polls.templatetags.poll_extras import register
from collections import OrderedDict
import polls.run
from django.db.models import Count, Q
import json
from django.db.models import Sum


def blank(request):
    return render(request, 'blank.html')

DAYS = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]

## TODO: Documentation
#
def professor_settings(request):
    #Get the Professor instance representing the current user
    first = request.user.first_name
    last = request.user.last_name
    professor = Professor.objects.get(first=first, last=last)
    return render(request, 'profSettings.html', professor_settings_helper(professor, request))

def PD_professor_settings(request):
    selected = request.GET.get('prof')
    template_args = None
    if not selected:
        template_args = {'error': 'Please select a professor'}
    else:
        names = selected.split()
        professor = Professor.objects.get(first=names[0], last=names[1])
        template_args = professor_settings_helper(professor, request)
    template_args['profs'] = Professor.objects.all()
    template_args['selected'] = selected
    template_args['pd'] = True
    return render(request, 'profSettings.html', template_args, selected)




def professor_settings_helper(professor, request):
    if request.method == 'POST':
        return update_professor_constraints(professor, request.POST)
    else:
        return get_professor_constraints(professor)


def get_professor_constraints(professor):
    #Get list of current professors constraints
    calendar = BlockCalendar(professor)
    constraints = calendar.get_professor_available()

    blocks = list(Block.objects.all().values('start_time', 'end_time', 'day', 'block_id'))
    blocks_by_time = OrderedDict()
    end_times = {}
    for block in sorted(blocks, key=lambda b: b['start_time']):
        day = block['day']
        time = block['start_time']
        if not time in blocks_by_time:
            blocks_by_time[time] = {}
        blocks_by_time[time][day] = block
        end_times[time] = block['end_time']
    return {'data': constraints, 'blocks_by_time': blocks_by_time, 'days': DAYS, 'end_times':end_times}

def update_professor_constraints(professor, post_data):
    result = get_professor_constraints(professor)
    constraints = result['data']

    schedule_info = post_data.copy().dict()
    del schedule_info['csrfmiddlewaretoken']

    calendar = BlockCalendar(professor)
    calendar.clear_professor()
    for key, value in schedule_info.items():
        constraints[key] = value
        if value != '0':
            calendar.insert_professor_available(key, value)

    result['message'] = 'Settings applied'
    return result

def course_selection(request):
    courses = Course.objects.all()
    running_filter = request.GET.get('running-list')
    if running_filter == None:
        running_filter = 'None'
    course_filter = request.GET.get('course-list')
    if course_filter == None:
        course_filter = 'None'

    programs = [i['program'] for i in list(Course.objects.order_by().values('program').distinct())]
    selected = request.POST.getlist('Courses')

    first = request.user.first_name
    last = request.user.last_name
    remove_course = []


    removed = request.POST.getlist('Removed')

    for title in removed:
        remove_course.append(CourseLevel().get_course_by_title(title.strip()))#theres trailing spaces from somewhere

    excluded_courses = CourseLevel().get_grade_by_year(year).values('course')
    if course_filter != 'None':
        courses = Course.objects.exclude(id__in=excluded_courses).filter(program=course_filter)
    else:
        courses = Course.objects.exclude(id__in=excluded_courses)

    if running_filter != 'None':
        program_restriction = Course.objects.filter(program=running_filter).values('id')
        season = CourseSeason().get_courses_from_recent_season()
        running = season.filter(courses__in=program_restriction)
    else:
        program_restriction = Course.objects.filter().values('id')
        running = CourseLevel().get_grade_by_year(year).filter(course__in=program_restriction)

    for course in selected:
        CourseSeason().add_course_season(course)
    for course in removed:
        CourseSeason().remove_course_season(course)
    return render(request, 'PDcoursesSelector.html', {'courses': courses, 'selected':selected, 'running': running, 'removed': removed, 'programs': programs, 'running_filter': running_filter,'course_filter': course_filter})


def course_review(request):
    return redirect(course_selection)
#    programs = [i['program'] for i in list(Course.objects.order_by().values('program').distinct())]
#    program = request.GET.get('program')
#    if program == 'All' or program == None:
#        program = 'All'
#    running_filter = request.GET.get('running-list')
#    if running_filter == None:
#        running_filter = 'None'
#    course_filter = request.GET.get('course-list')
#    if course_filter == None:
#        course_filter = 'None'
#    year = request.GET.get('year')
#    if year == None:
#        year = 'First'
#
#    selected = request.POST.getlist('Courses')
#    remove_course = []
#    removed = request.POST.getlist('Removed')
#
#    for title in removed:
#        remove_course.append(CourseLevel().get_course_by_title(title.strip()))#theres trailing spaces from somewhere
#    excluded_courses = CourseLevel().get_grade_by_year(year).values('course')
#    if course_filter != 'None':
#        courses = Course.objects.exclude(id__in=excluded_courses).filter(program=course_filter)
#    else:
#        courses = Course.objects.exclude(id__in=excluded_courses)
#
#    if program != 'All':
#        program_restriction = Course.objects.filter(program=program).values('id')
#        running = CourseLevel().get_grade_by_year(year).filter(course__in=program_restriction)
#    else:
#        program_restriction = Course.objects.filter().values('id')
#        running = CourseLevel().get_grade_by_year(year).filter(course__in=program_restriction)
#
#
#    for course in selected:
#        CourseLevel().insert_grade_level(course, year)
#    for course in removed:
#        CourseLevel().remove_courselevel(course, year)
#
#    return render(request, 'PDcoursesReview.html', {'courses': courses, 'selected':selected, 'year': year, 'running': running, 'programs': programs, 'program': program, 'course_filter': course_filter, 'running_filter': running_filter})

def simple_upload(request):
    return render(request, 'import_data.html')

#Collect all objects from the Seasons table and display them in the drop-down
#list on the history page
def history(request):
	seasonList = Season.objects.all

	return render(request, 'history.html', {'seasonList': seasonList})

#Take the selected Season from the drop-down list and display objects from the
#Hunks table which belong to that Season
def view_history(request):
	if request.method == 'POST':
		chooseSeason = request.POST['item']
		if chooseSeason == "":
			return render(request, 'view_history.html')
		else:
			query_results = Hunk.objects.filter(section__run__season__id__contains = chooseSeason)
			return render(request, 'view_history.html', {'selected_item':chooseSeason, 'query_results': query_results})
	else:
		return render(request, 'view_history.html')

#Call the start_run function from run.py when the start button is clicked and
#call the cancel_run function from run.py when the cancel button is clicked
def run(request):
    action = request.GET.get('action')
    if action:
        {"run": polls.run.start_run,
         "cancel": polls.run.cancel_run,
        }[action]()
        return HttpResponseRedirect(reverse("run"))
    return render(request, 'run.html')

#Display the objects from Hunks that belong to the most recent Run
def results(request):
	latestRun = Run.objects.latest('id').id
	algo_results = Hunk.objects.filter(section__run__id__contains = latestRun)
	print(algo_results)

	return render(request, 'results.html', {'algo_results': algo_results})

## TODO: Documentation
#
def userSettings(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        first = request.POST['first']
        last = request.POST['last']
        email = request.POST['email']

        user = request.user

        if username:
            if username != request.user.username:
                try:
                    user = User.objects.get(username=request.POST['username'])
                    return render(request, 'userSettings.html', {'error': 'Username Already Taken'} )
                except User.DoesNotExist:
                    user.username = username

        if password:
            user.set_password(password)

        user.first_name = first

        user.last_name = last

        if email:
            user.email = email

        user.save()
        return render(request, 'userSettings.html', {'message': 'Settings Applied'} )

    else:
        return render(request, 'userSettings.html')

## TODO: Documentation
#
def signup(request):
    if request.method == 'POST':
        if request.POST['password1'] == request.POST['password2']:
            try:
                user = User.objects.get(username=request.POST['username'])
                return render(request, 'signup.html', {'error': 'Username Already Taken'} )
            except User.DoesNotExist:
                user = User.objects.create_user(request.POST['username'], password=request.POST['password1'])
                return render(request, 'Login.html', {'message': 'Account Created'} )
        else:
            return render(request, 'signup.html', {'error': 'Passwords Did\'t Match'})
    else:
        return render(request, 'signup.html')

## TODO: Documentation
#
def logout_view(request):
    logout(request)
    return render(request, 'Login.html')

## TODO: Documentation
#
def loginUser(request):
    if request.user.is_authenticated:
        return index(request)
    else:
        if request.method == 'POST':
            username = request.POST['username']
            password = request.POST['password']
            user = authenticate(request, username=username, password=password)
            if user is not None:
                login(request, user)
                return render(request, 'index.html')
            else:
                failed = "Unable to login in"
                content = {'error':failed}
                return render(request, 'Login.html', content)
        else:
            return render(request, 'Login.html')


## Create views for custom 404 and 500 error pages
def handler404(request):

    return render(request, 'error_404.html', status=404)

def handler500(request):

    return render(request, 'error_500.html', status=500)

#Algorithim stats views

#Helper methods
def get_room_use(room):
	return len(room.hunk_set.all()) #Individual room use is determined by how many hunks it has

def get_total_use(rooms):
	roomTotalUse = 0
	for r in rooms:
		roomTotalUse = roomTotalUse + get_room_use(r)
	return roomTotalUse

#View methods
def show_building(request):
    rooms = Room.objects.all()
    build_list = []
    for room in rooms:
        #build_list.append(room.building)
            matching_list = [b for b in build_list if b == room.building]
            if len(matching_list) == 0:
                build_list.append(room.building)
    return build_list

def algo_stats_total():
	rooms = Room.objects.all()
	blocks = Block.objects.all()
	algo_results = (get_total_use(rooms)/(len(rooms)*len(blocks))) #Divide total room usages by the total number of blocks multiplied by the total number of rooms
	return round(algo_results, 4) #The name of the context object is algo_stats, this is the object we would access in the template

def algo_stats_by_building(room_building): # I setup this method to take a room_building to determine what building we are getting data for,
	rooms = Room.objects.filter(building=room_building) #Get all rooms in this building, could replace room_building with request.POST['building'] or request.GET.get('building')
	building_stats = {} #Empty dictionary to store {Room number : Room utilization}
	for r in rooms:
		building_stats.update({str(r.room_number): get_room_use(r)}) #places the individual room utilization in a dict with the room_number
	return building_stats #The name of the context object is building_stats, this is the object we would access in the template

def index(request):
    """
        create room data: (this is simple graph)
        values: count the number of builds
        annotate: add hover label to show data, capacity_count is variable
                  that holds the sum of all room_capacity
        order_by: order by building data
    """
    if request.method == 'POST':
        builds = request.POST['select_building']
        if builds == 'Select Building':
            return render(request, {'error':'No selection made'})
        dataset = algo_stats_by_building(builds)

        """
            store list of data
            categories: contains category data
            room_capacity_data: contains room capacity
        """
        categories = list()
        room_capacity_data = list()

        for key, util_value in dataset.items():
            # append building data to categories list
            categories.append(builds+" "+str(key))
            # append the room capacity using 'capacity_count' variable
            #room_capacity_data.append(entry['capacity_count'])
            room_capacity_data.append(util_value)

        """
            defined data on our graph
            name: graph name
            data: the room capacity by each building
            color: the color of the graph
        """
        room_capacity_data = {
            'name': 'Overall Room Cap.',
            'data': room_capacity_data,
            'color': '#0095ff'
        }

        """
            defined chart
            chart: defined chart type
            title, xAxis, yAxis:
            series: plot the data
        """
        chart = {
            'chart': {'type': 'column'},
            'title': {'text': 'Room Capacity by each building'},
            'xAxis': {'categories': categories},
            'yAxis': { 'title': { 'text': 'Room Capacity'}, 'visible': 'true' },
            'series': [room_capacity_data],
            }

        # dump our graph data into json and use tha for calling in our html page
        dump = json.dumps(chart)
        return render(request, 'index.html', {
        'chart': dump,
        'algo_stats':algo_stats_total,
        'building': show_building(request)})
    else:
        return render(request, 'index.html', {'algo_stats':algo_stats_total,
        'building': show_building(request)})
