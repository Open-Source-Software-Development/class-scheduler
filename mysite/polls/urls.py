from django.urls import path
from django.conf.urls.static import static
from django.conf import settings
from . import views
from django.conf.urls import url, include
import polls.views
from django.contrib import admin

## TODO: Documentation
#
urlpatterns = [
    url(r'^$', polls.views.loginUser, name='login'),
	url(r'^signup/', polls.views.signup, name='signup'),
	url(r'^professor_settings/', polls.views.professor_settings, name='professor_settings'),
	url(r'^blank/', polls.views.blank, name='blank'),
	url(r'^index/', polls.views.index, name='index'),
    url(r'^admin/', admin.site.urls),
	url(r'^run/', polls.views.run, name='run'),
	url(r'^history/', polls.views.history, name='history'),
	url(r'^results/', polls.views.results, name='results'),
	url(r'^view_history/', polls.views.view_history, name='view_history'),
	url(r'^PDProfSettings/', polls.views.PDProfSettings, name='PDProfSettings'),
	url(r'^course_review/', polls.views.course_review, name='course_review'),
	url(r'^simple_upload/', polls.views.simple_upload, name='simple_upload'),
	url(r'^userSettings/', polls.views.userSettings, name='userSettings'),
]

if settings.DEBUG:
	urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
