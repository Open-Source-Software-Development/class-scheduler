from django.db import models
from django.contrib.contenttypes.fields import GenericForeignKey
from django.contrib.contenttypes.models import ContentType
import six

# Input


class Named(models.Model):
    name = models.CharField(max_length=200)

    class Meta:
        abstract = True

    def __str__(self):
        return six.text_type(self.name)


class Division(Named):
    pass


class RoomType(Named):
    pass


class Professor(Named):
    division = models.ForeignKey(Division, on_delete=models.CASCADE)


class Course(Named):
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    section_count = models.IntegerField()


class Room(Named):
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    room_type = models.ForeignKey(RoomType, on_delete=models.CASCADE)


class Block(Named):
    next_block = models.OneToOneField('self', on_delete=models.CASCADE, related_name="previous_block", null=True, blank=True)
    paired_with = models.ForeignKey('self', on_delete=models.CASCADE, related_name="paired_with_reverse", null=True, blank=True)


class PregenSection(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    suffix = models.CharField(max_length=50)

    def __str__(self):
        return six.text_type("{}{}").format(str(self.course), self.suffix)


# Input : User preferences/constraints


@six.python_2_unicode_compatible
class UserPreferenceOrConstraint(models.Model):
    left_argument_type = models.ForeignKey(ContentType, on_delete=models.CASCADE, related_name="+")
    left_argument_id = models.PositiveIntegerField()
    left_argument_object = GenericForeignKey('left_argument_type', 'left_argument_id')
    right_argument_type = models.ForeignKey(ContentType, on_delete=models.CASCADE, related_name="+")
    right_argument_id = models.PositiveIntegerField()
    right_argument_object = GenericForeignKey('right_argument_type', 'right_argument_id')

    class Meta:
        abstract = True

    def __str__(self):
        return six.text_type("{} and {}: {}").format(str(self.left_argument_object), str(self.right_argument_object), self.describe())

    def describe(self):
        raise NotImplementedError()


class UserPreference(UserPreferenceOrConstraint):
    score = models.IntegerField()

    def describe(self):
        label = "Good ({})" if self.score >= 0 else "Bad ({})"
        return six.text_type(label).format(self.score)


class UserConstaint(UserPreferenceOrConstraint):

    is_blacklist = models.BooleanField()
    
    def describe(self):
        if self.is_blacklist:
            return six.text_type("Blacklisted")
        return six.text_type("Whitelisted")


# Output


@six.python_2_unicode_compatible
class Section(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    suffix = models.CharField(max_length=50)

    def __str__(self):
        return six.text_type("{}{}").format(str(self.course), self.suffix)


@six.python_2_unicode_compatible
class Hunk(models.Model):
    section = models.ForeignKey(Section, on_delete=models.CASCADE)
    professor = models.ForeignKey(Professor, on_delete=models.CASCADE)
    room = models.ForeignKey(Room, on_delete=models.CASCADE)
    block = models.ForeignKey(Block, on_delete=models.CASCADE)

    def __str__(self):
        return six.text_type("Hunk({}, {}, {}, {})").format(self.section, self.professor, self.room, self.block)
