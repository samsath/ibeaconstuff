from django.db import models
from django_extensions.db.fields import AutoSlugField

class Beacons(models.Model):
    name = models.CharField(max_length=100, blank=True, null=True)
    uuid = models.CharField(max_length=100, verbose_name=u'UUID for beacon')
    major = models.IntegerField(verbose_name=u'Major value for beacon', blank=True, null=True)
    minor = models.IntegerField(verbose_name=u'Minor value for beacon', blank=True, null=True)
    descript = models.TextField(blank=True, null=True, verbose_name=u'Description', help_text=u'Could be ')

    def __unicode__(self):
        if self.name != u'':
            return self.name
        else:
           return '{}-M{},m{}'.format(self.uuid, self.major, self.minor)

    class Meta:
        verbose_name = u'Beacons'
        verbose_name_plural = u'Beacons'

class ContentType(models.Model):
    title = models.CharField(max_length=100)

    def __unicode__(self):
        return self.title

class Content(models.Model):
    title = models.TextField()
    slug = AutoSlugField(unique=True, populate_from=('title',))
    datetime = models.DateTimeField(blank=True, null=True)
    beacons = models.ManyToManyField(Beacons, help_text=u'Click on the Beacon you want to relate this content to')
    type = models.ForeignKey(ContentType)
    content = models.TextField()

    def __unicode__(self):
        return self.title
