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

#View to show template page for the website that just contains the navigation bars
def blank(request):
    return render(request, 'blank.html')

DAYS = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]

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

#Allows user to select courses from a master list, and move them into a running list that will then be updated based on
#what is in the running list when applied. Populates both lists by course objects that are in the master list of course
#objects, and the course objects in the season in the database.
def course_selection(request):
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

    # excluded_courses = CourseLevel().get_grade_by_year(year).values('course')
    # if course_filter != 'None':
        # courses = Course.objects.exclude(id__in=excluded_courses).filter(program=course_filter)
    # else:
        # courses = Course.objects.exclude(id__in=excluded_courses)

    season = CourseSeason().get_courses_from_recent_season()
    running = season.filter()
    
    already_running = CourseSeason().get_courses_from_recent_season().values('id')
    courses = Course.objects.exclude(id__in=already_running)

    for course in selected:
        CourseSeason().add_course_season(course)
    for course in removed:
        CourseSeason().remove_course_season(course)
    return render(request, 'PDcoursesSelector.html', {'courses': courses, 'selected': selected, 'running': running, 'removed': removed, 'programs': programs, 'running_filter': running_filter, 'course_filter': course_filter})

#Currently not in use, when creating this page we found out that selection of courses does not change when viewed by a PD or DH.
#This page would allow for the user to select a year, and add courses to each years running list from the master list. This was
#depreciated for the reason above. Currently redirects to course_selection view.
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

#Currently not compatable with user account and professor object relation, as when a user changes their account
#name, the relation between the professor object and account is lost, as the relation is based on name. Allows
#user to enter in desired password, username, first name, last name, and email, and will change current ones to
#entered ones. If desired username is taken, an error will be thrown.
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

#Called when user wants to create an account. This is mainly used for testing purposes, as accounts will be entered
#in manually by the IT team to make sure accounts relate to professor objects. Takes entered credentials, and creates
#an account with desired username and password. If passwords do not match, or username is taken, page will reload with error
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

#Called when user wants to log out, calls Django logout function with user object, then redirects to login page
def logout_view(request):
    logout(request)
    return render(request, 'Login.html')

#Called when user tries to access website when not logged in, tries to log in with entered credentials
#if entered username and password does not match, reload page with error. if successful then redirect to index
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
def normalize_rate(rate):
    pct = rate * 100
    # Round to 2 decimal places
    return int(pct * 100) / 100

def room_utilization_number(room):
    latestRun = Run.objects.latest('id').id
    hunks_from_run = Hunk.objects.filter(section__run__id__contains = latestRun)

    # Since scheduling is by block pairs, we need to multiply by 2
    # to get the "actual" utilization number.
    return 2 * len([hunk for hunk in hunks_from_run if hunk.room == room])

def room_utilization_rate(room):
    blocks = Block.objects.all()
    rate = room_utilization_number(room) / len(blocks)
    return normalize_rate(rate)

def total_utilization_number(rooms):
    return sum([room_utilization_number(room) for room in rooms])

def total_utilization_rate(latest_run):
    blocks = Block.objects.all()
    rooms = Room.objects.all()
    hunks_from_run = Hunk.objects.filter(section__run__id__contains = latest_run)
    rooms_from_run = set([hunk.room for hunk in hunks_from_run])
    rate = total_utilization_number(rooms_from_run) / (len(blocks) * len(rooms))
    return normalize_rate(rate)

def utilization_rates_by_building(room_building):
    rooms = Room.objects.filter(building=room_building)
    building_stats = {}
    for room in rooms:
        key = str(room.room_number)
        building_stats[key] = room_utilization_rate(room)
    return building_stats

def index(request):
    template_parameters = {
        'algo_stats': 0,
        'building': set([room.building for room in Room.objects.all()])
    }

    try:
        latest_run = Run.objects.latest('id').id
    except Run.DoesNotExist:
        template_parameters['error'] = 'The scheduler hasn\'t been run yet'
        return render(request, 'index.html', template_parameters)

    try:
        template_parameters['algo_stats'] = total_utilization_rate(latest_run)
    except ZeroDivisionError:
        template_parameters['error'] = 'No rooms and/or time blocks present in the system'
        return render(request, 'index.html', template_parameters)

    if request.method != 'POST':
        return render(request, 'index.html', template_parameters)

    building_name = request.POST['select_building']
    if building_name == 'Select Building':
        template_paremters['error'] = 'Please select a building'
        return render(request, 'index.html', template_parameters)
    dataset = utilization_rates_by_building(building_name)

    room_names = []
    room_utilization_rates = []
    for key, rate in dataset.items():
        room_names.append(building_name + " " + str(key))
        room_utilization_rates.append(rate)

    room_utilization_rates = {
        'name': 'Utilization rate',
        'data': room_utilization_rates,
        'color': '#0095ff'
    }

    chart = {
        'chart': {'type': 'column'},
        'title': {'text': 'Room utilization for {}'.format(building_name)},
        'xAxis': {'categories': room_names},
        'yAxis': { 'title': { 'text': 'Utilization rate'}, 'visible': 'true' },
        'series': [room_utilization_rates],
    }

    # dump our graph data into json and use tha for calling in our html page
    template_parameters['chart'] = json.dumps(chart)
    return render(request, 'index.html', template_parameters)
