from django.contrib import admin
from .models import Faculty
from django.core import management
from django.shortcuts import redirect


admin.site.register(Faculty)
