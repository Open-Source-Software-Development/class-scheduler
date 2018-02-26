from django.shortcuts import render
from django.http import HttpResponse
from .models import Room
# Create your views here.

def details(request):
	ctx = {}
	rooms = Room.objects.all()
	ctx['rooms'] = rooms
	return render(request, 'details.html',ctx)
