from django.conf.urls.static import static
from django.conf import settings
from . import views
from django.conf.urls import url, include
import polls.views
from django.contrib import admin
import faculty_data.views
import room_data.views

urlpatterns = [
    url(r'^$', polls.views.loginUser, name='login'),
    url(r'^signup/', polls.views.signup, name='signup'),
    url(r'^professor_settings/', polls.views.professor_settings, name='professor_settings'),
    url(r'^blank/', polls.views.blank, name='blank'),
    url(r'^index/', polls.views.index, name='index'),
    url(r'details/$', faculty_data.views.details, name='student_details'),
]

if settings.DEBUG:
	urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
