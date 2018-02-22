from django.urls import path
from django.conf.urls.static import static
from django.conf import settings
from . import views
from django.conf.urls import url, include
import polls.views
from django.contrib import admin

urlpatterns = [
    url(r'^$', polls.views.loginUser, name='login'),
	url(r'^signup/', polls.views.signup, name='signup'),
    url(r'^admin/', admin.site.urls),
]

if settings.DEBUG:
	urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)