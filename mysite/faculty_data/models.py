from django.db import models

# Create your models here.
class Faculty(models.Model):
    division = models.CharField(max_length=20)
    first_name = models.CharField(max_length=200)
    last_name = models.CharField(max_length=200)
    subject = models.CharField(max_length=200)
    course = models.CharField(max_length=200)

    def __str__(self):
        return self.first_name +" "+ self.last_name
