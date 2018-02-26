from django.shortcuts import render
from django.http import HttpResponse
from .models import Faculty
# Create your views here.

def details(request):
	ctx = {}
	teachers = Faculty.objects.all()
	ctx['teachers'] = teachers
	return render(request, 'details.html',ctx)
