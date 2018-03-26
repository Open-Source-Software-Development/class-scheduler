from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth.models import User, Group
from django.contrib.auth import login, authenticate, logout
from django import template
import logging
from scheduler.dataAPI import *

def blank(request):
	return render(request, 'blank.html')
	
def index(request):
	return render(request, 'index.html')

## TODO: Documentation
#
def professor_settings(request):
    if request.method == 'POST':

        schedule_info = (request.POST.copy()).dict()
        del schedule_info['csrfmiddlewaretoken']
		
        first = request.user.first_name
        last = request.user.last_name

        D = DataAPI()

        for key, value in schedule_info.items():
            if value == '1':
                D.insert_professor_avalible(first, key, value)
            if value == '2':
                D.insert_professor_avalible(first, key, value)

        return render(request, 'profSettings.html', {'message': 'Settings Applied', 'data': schedule_info})
    else:
        return render(request, 'profSettings.html')
		
def PD_professor_settings(request):
	professor_names = [li['first_name'] + ' ' + li['last_name'] for li in list(User.objects.filter(groups__name='Professor').values('first_name', 'last_name'))]
	
	if request.GET.get('prof'):
		selected = request.GET.get('prof')
	else:
		selected = 'None Selected'
	
	if request.method == 'POST':	
		if selected == 'None Selected':
			return render(request, 'PDProfSettings.html', {'profs': professor_names, 'selected': selected, 'error': 'Please Select a Professor'})			
		else:
			schedule_info = request.POST.copy()
			schedule_info['first'] = selected.split()[0]
			schedule_info['last'] = selected.split()[1]
			
			return render(request, 'PDProfSettings.html', {'profs': professor_names, 'selected': selected, 'message': 'Settings Applied', 'data': schedule_info})
	else:
		return render(request, 'PDProfSettings.html', {'profs': professor_names, 'selected': selected})

def course_selection(request):
	return render(request, 'PDcoursesSelector.html')
		
def course_review(request):
	return render(request, 'PDcoursesReview.html')

def simple_upload(request):
	return render(request, 'import_data.html')

def run(request):
	return render(request, 'run.html')

def history(request):
	return render(request, 'history.html')


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
