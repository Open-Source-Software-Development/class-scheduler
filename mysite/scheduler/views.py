from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.models import User, Group
from django.contrib.auth import login, authenticate, logout
from django import template
import logging
from scheduler.blockcalendar import *
from django.urls import reverse
from .models import *
from .csv_parser import parse
import polls.run
import json

def upload_csv(request):
    """Parses models from a CSV and saves them.

    Args:
        request: a request with the CSV file attached.

    Returns:
        an HTTP redirect response sending the user back to the upload
        page.
    """
    # TODO: show the user meaningful feedback on success/failure
    data = {}
    if "GET" == request.method:
        return render(request, "import_data.html", data)
    csv_file = request.FILES["csv_file"]
    if not csv_file.name.endswith('.csv'):
        raise ParseError("File should have .csv extension")
    csv_type = request.POST['css-tabs']
    result = parse(csv_file, csv_type)
    for model in result:
        model.save()
    return HttpResponseRedirect(reverse("upload"))

def status(request):
    """Returns a JSON dictionary indicating whether the algorithm is running.

    The returned dictionary has a single key, `status`, which is `True` if the
    algorithm is running and `False` otherwise.

    Args:
        request: an HTTP request (unused)

    Returns:
        an HTTP response with the above JSON
    """
    data = {'status': polls.run.is_running()}
    data_str = json.dumps(data)
    return HttpResponse(data_str, content_type='application/json')
