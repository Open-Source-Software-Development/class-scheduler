from django.db import models

# Create your models here.
class Room(models.Model):
    building = models.CharField(max_length=20)
    room = models.CharField(max_length=20)
    capacity = models.CharField(max_length=20)
    type_room = models.CharField(max_length=100)
    division = models.CharField(max_length=100)
    subject = models.CharField(max_length=100)
    course_style = models.CharField(max_length=100)

    def __str__(self):
        return self.building +" "+ self.division
