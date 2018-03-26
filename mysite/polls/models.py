from django.db import models
from django.contrib.contenttypes.fields import GenericForeignKey
from django.contrib.contenttypes.models import ContentType
import six

# Create your models here.
class Named(models.Model):
    """
        Gives better naming to models that inherit it.
    """
    name = models.CharField(max_length=200, null=True)

    class Meta:
        abstract = True

    def __str__(self):
        return six.text_type(self.name)

class Block(Named):
    """
        Table: Block
        Primary Key: Composite (Block_id, day)
        Columns:
            block_id: Positive Ineger
                - There should be two of each block id in the database with different days.
            day: CharField (max length 15)
                - The day of the week a block happens on
            start_time: TimeField (input in HH:MM)
                - The start time of the block (ex: 8:00)
            end_time: TimeField (input in HH:MM)
                - The end time of the block (ex: 9:30)


    """

    ids = models.CharField(max_length=15)
    block = models.CharField(max_length=15)
    day = models.CharField(max_length=15)
    block_id = models.CharField(max_length=15)

    def __str__(self):
        return self.day
