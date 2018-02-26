from django.conf.urls.static import static
from django.conf import settings
from . import views
from django.conf.urls import url, include
import polls.views
from django.contrib import admin

urlpatterns = [
    url(r'details/$', views.details, name='student_details'),
    url(r'details/$', views.details2, name='student_details'),
]

if settings.DEBUG:
	urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
