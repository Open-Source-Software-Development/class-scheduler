from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.models import User, Group
from django.contrib.auth import login, authenticate, logout
from django import template
from scheduler.blockcalendar import *
from django.urls import reverse
from .models import Block
from scheduler.models import Course
from scheduler.models import Professor
from scheduler.models import Block
from scheduler.models import Division
from scheduler.models import ProfessorConstraint
from scheduler.models import GradeLevel
from scheduler.courseConstraints import CourseLevel
from polls.templatetags.poll_extras import register
from collections import OrderedDict
import subprocess
import threading

def blank(request):
	return render(request, 'blank.html')

def index(request):
	return render(request, 'index.html')

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
    for block in sorted(blocks, key=lambda b: b['start_time']):
        day = block['day']
        time = block['start_time']
        if not time in blocks_by_time:
            blocks_by_time[time] = {}
        blocks_by_time[time][day] = block
    return {'data': constraints, 'blocks_by_time': blocks_by_time, 'days': DAYS}

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
    divisions = Division.objects.filter()
   
    year = request.GET.get('year')
    if year == None:
        year = 'First'
        
    running = CourseLevel().get_grade_by_year(year)
    selected = request.POST.getlist('Courses')
    first = request.user.first_name
    last = request.user.last_name
    
    excluded_courses = CourseLevel().get_grade_by_year(year).values('course')
    courses = Course.objects.exclude(id__in=excluded_courses)
    
    for course in selected:
        CourseLevel().insert_grade_level(course, year)
    
    return render(request, 'PDcoursesSelector.html', {'courses': courses, 'divisions': divisions, 'selected':selected, 'year': year, 'running': running})

def course_review(request):
	return render(request, 'PDcoursesReview.html')

def simple_upload(request):
	return render(request, 'import_data.html')

def history(request):
	return render(request, 'history.html')

def runHelper():
	subprocess.run(["java", "-jar", "../java/target/Scheduler-jar-with-dependencies.jar"])
	
## TODO: Documentation
def run(request):
	action = request.GET.get('action')
	if action == "run":
		if len(Run.objects.filter(active = True)) == 0:
			threading.Thread(target=runHelper).start()
	elif action == "cancel":
		for run in Run.objects.all():
			run.terminate()
	return render(request, 'run.html')
	
## TODO: Documentation
def view_history(request):
	query_results = Hunk.objects.filter()
	
	return render(request, 'view_history.html', {'query_results': query_results})

## TODO: Documentation
def results(request):
	algo_results = Hunk.objects.filter()
	
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