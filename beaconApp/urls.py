from django.conf.urls.defaults import *
from .views import *

urlpatterns = patterns('',
    url(r'^beacon/get/(?P<pk>[^/]+)/$', get_content, name='get-beacon'),
    url(r'^beacon/all/$', get_all_beacon, name='all-beacons'),
)