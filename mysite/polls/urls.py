from django.conf.urls.static import static
from django.conf import settings
from . import views
from django.conf.urls import url, include
import polls.views
from django.contrib import admin
import faculty_data.views
import room_data.views

## TODO: Documentation
#
urlpatterns = [
    url(r'^$', polls.views.loginUser, name='login'),
    url(r'^index/', polls.views.index, name='index'),
	url(r'^signup/', polls.views.signup, name='signup'),
	url(r'^professor_settings/', polls.views.professor_settings, name='professor_settings'),
	url(r'^blank/', polls.views.blank, name='blank'),
    url(r'^admin/', admin.site.urls),
	url(r'^run/', polls.views.run, name='run'),
	url(r'^history/', polls.views.history, name='history'),
	url(r'^view_history/', polls.views.view_history, name='view_history'),
	url(r'^PDProfSettings/', polls.views.PDProfSettings, name='PDProfSettings'),
	url(r'^course_review/', polls.views.course_review, name='course_review'),
	url(r'^upload/', polls.views.simple_upload, name='upload'),
	url(r'^userSettings/', polls.views.userSettings, name='userSettings'),
    url(r'^logout/', polls.views.logout_view, name='logout'),
	url(r'^course_selection/', polls.views.course_selection, name='course_selection'),
]

if settings.DEBUG:
	urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
