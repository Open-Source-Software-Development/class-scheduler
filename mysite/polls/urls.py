from django.conf.urls.static import static
from django.conf import settings
from . import views
from django.conf.urls import url, include
import polls.views
from django.contrib import admin
import scheduler.views

#URL patterns that will be loaded by the website if added onto the URL. (Ex. www.website.com/[pattern])
urlpatterns = [
    url(r'^$', polls.views.loginUser, name='login'),
    url(r'^index/', polls.views.index, name='index'),
    url(r'^signup/', polls.views.signup, name='signup'),
    url(r'^professor_settings/', polls.views.professor_settings, name='professor_settings'),
    url(r'^blank/', polls.views.blank, name='blank'),
    url(r'^admin/', admin.site.urls),
    url(r'^run/', polls.views.run, name='run'),
    url(r'^history/', polls.views.history, name='history'),
    url(r'^results/', polls.views.results, name='results'),
    url(r'^view_history/', polls.views.view_history, name='view_history'),
    url(r'^PD_professor_settings/', polls.views.PD_professor_settings, name='PD_professor_settings'),
    url(r'^course_review/', polls.views.course_selection, name='course_review'),
    url(r'^upload/', scheduler.views.upload_csv, name='upload'),
    url(r'^status/', scheduler.views.status, name='status'),
    url(r'^userSettings/', polls.views.userSettings, name='userSettings'),
    url(r'^logout/', polls.views.logout_view, name='logout'),
    url(r'^course_selection/', polls.views.course_selection, name='course_selection'),
]

if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
