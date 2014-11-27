from .models import *
import json
from django.http import HttpResponse

def get_content(request,pk):
    value = Content.objects.filter(beacons=Beacons.objects.get(id=pk))
    content = []

    for v in value:
        b = {'id': v.id, 'title': v.title, 'type': v.type.title, 'content': v.content }
        content.append(b)

    return HttpResponse(json.dumps(content), content_type="application/json")

def get_all_beacon(request):

    value = Beacons.objects.all()
    content = []

    for v in value:
        b = {'id': v.id, 'uuid': v.uuid, 'major': v.major, 'minor': v.minor }
        content.append(b)

    return HttpResponse(json.dumps(content), content_type="application/json")