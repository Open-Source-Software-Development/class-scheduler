from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth.models import User
from django.contrib.auth import login, authenticate, logout

def simple_upload(request):
	return render(request, 'import_data.html')

def course_review(request):
	return render(request, 'course_review.html')

def PDProfSettings(request):
	return render(request, 'PDProfSettings.html')

def run(request):
	return render(request, 'run.html')

def history(request):
	return render(request, 'history.html')

def view_history(request):
	return render(request, 'view_history.html')


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
def professor_settings(request):
	return render(request, 'profSettings.html')

## TODO: Documentation
#
def index(request):
	return render(request, 'index.html')

## TODO: Documentation
#
def blank(request):
	return render(request, 'blank.html')

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
