from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth.models import User
from django.contrib.auth import login, authenticate, logout

def professor_settings(request):
	return render(request, 'profSettings.html')
	
def index(request):
	return render(request, 'index.html')

def signup(request):
    if request.method == 'POST':
        if request.POST['password1'] == request.POST['password2']:
            try:
                user = User.objects.get(username=request.POST['username'])
                return render(request, 'signup.html', {'error': 'username already taken'} )
            except User.DoesNotExist:
                user = User.objects.create_user(request.POST['username'], password=request.POST['password1'])
                login(request, user)
                return render(request, 'Login.html')
        else:
            match = "password didn't match"
            content = {'error': match}
            return render(request, 'signup.html', content)
    else:
        return render(request, 'signup.html')
		
def logout_view(request):
    logout(request, user)
    return render(request, 'Login.html')		

def loginUser(request):
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