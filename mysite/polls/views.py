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
from polls.templatetags.poll_extras import register
from collections import OrderedDict

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
    courses = Course.objects.filter()
    divisions = Division.objects.filter()
    filter = request.GET.get('division')
    if filter == None:
        filter = 'All'
    year = request.GET.get('year')
    if year == None:
        year = 'Select Year'
   
    selected = request.POST.getlist('Courses')
    first = request.user.first_name
    last = request.user.last_name
    
        
    
    return render(request, 'PDcoursesSelector.html', {'courses': courses, 'divisions': divisions, 'filter': filter, 'selected':selected, 'year': year})

def course_review(request):
	return render(request, 'PDcoursesReview.html')

def simple_upload(request):
	return render(request, 'import_data.html')

def history(request):
	return render(request, 'history.html')

def view_history(request):
	query_results = Hunk.objects.all()
	
	return render(request, 'view_history.html', {'query_results': query_results})

def run(request):
	return render(request, 'run.html')

## TODO: Documentation
def results(request):
	query_results = Hunk.objects.all()
	
	return render(request, 'results.html', {'query_results': query_results})
	
	
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

## TODO: Documentation
def upload_csv_time_block(request):
	data = {}
	if "GET" == request.method:
		return render(request, "import_data.html", data)
    # if not GET, then proceed
	try:
		csv_file = request.FILES["csv_file"]
		if not csv_file.name.endswith('.csv'):
			messages.error(request,'File is not CSV type')
			return HttpResponseRedirect(reverse("upload"))

		file_data = csv_file.read().decode("utf-8")

		lines = file_data.split("\n")
		#loop over the lines and save them in db. If error , store as string and then display
		for line in lines:
			fields = line.split(",")
			data_dict = {}
			ids = fields[0]
			block = fields[1]
			day = fields[2]
			block_id = fields[3]
			try:
				block, created = Block.objects.get_or_create(
					ids = id,
					block = block,
					day = day,
					block_id = block_id,
				)
				if created:
					block.save()
				else:
					logging.getLogger("error_logger").error(form.errors.as_json())
			except Exception as e:
				logging.getLogger("error_logger").error(repr(e))
				pass

	except Exception as e:
		logging.getLogger("error_logger").error("Unable to upload file. "+repr(e))

	return HttpResponseRedirect(reverse("upload"))
