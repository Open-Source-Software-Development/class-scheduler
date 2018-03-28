from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.models import User, Group
from django.contrib.auth import login, authenticate, logout
from django import template
import logging
from scheduler.dataAPI import *
from django.urls import reverse
from .models import *
from .csv_parser import parse

def upload_csv(request):
    data = {}
    if "GET" == request.method:
        return render(request, "import_data.html", data)
    csv_file = request.FILES["csv_file"]
    if not csv_file.name.endswith('.csv'):
        raise ParseError("File should have .csv extension")
    csv_type = request.POST['css-tabs']
    models = parse(csv_file, csv_type)
    for model in models:
        model.save()
    return HttpResponseRedirect(reverse("upload"))
