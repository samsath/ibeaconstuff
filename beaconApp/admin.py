from django.contrib import admin
from .models import *

class ContentAdmin(admin.ModelAdmin):
    model = Content
    list_display = ('title', 'type', 'datetime',)
    save_on_top = True
    fields = (
        'title',
        'datetime',
        'beacons',
        'type',
        'content',
    )
    filter_horizontal = (
        'beacons',
    )

class ContentTypeAdmin(admin.ModelAdmin):
    model = ContentType
    list_display = ('title',)
    fields = ('title',)

class BeaconsAdmin(admin.ModelAdmin):
    model=Beacons
    list_display = ('uuid', 'major', 'minor', 'name')
    save_on_top = True
    fields = (
        'name',
        'uuid',
        'major',
        'minor',
        'descript',
    )

admin.site.register(Content, ContentAdmin)
admin.site.register(ContentType, ContentTypeAdmin)
admin.site.register(Beacons, BeaconsAdmin)