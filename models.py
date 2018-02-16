from django.db import models

# Input


class Division(models.Model):
    name = models.CharField(max_length=50)


class RoomType(models.Model):
    name = models.CharField(max_length=50)


class SchedulingElement(models.Model):
    name = models.CharField(max_length=200)
    
    class Meta:
        abstract = True


class Professor(SchedulingElement):
    professor_id = models.ForeignKey(SchedulingElement, primary_key=True)
    division = models.ForeignKey(Division)


class Course(SchedulingElement):
    course_id = models.ForeignKey(SchedulingElement, primary_key=True)
    division = models.ForeignKey(Division)
    section_count = models.IntegerField()


class Room(SchedulingElement):
    course_id = models.ForeignKey(SchedulingElement, primary_key=True)
    division = models.ForeignKey(Division)
    room_type = models.ForeignKey(RoomType)


class Block(SchedulingElement):
    block_id = models.ForeignKey(SchedulingElement, primary_key=True)
    next_block = models.OneToOneField(Block, on_delete=models.CASCADE, related_name="previous_block", null=True)
    paired_with = models.ForeignKey(Block)


class PregenSection(models.Model):
    course = models.ForeignKey(Course)
    suffix = models.CharField(max_length=50)


# Input : User preferences/constraints


class UserPreferenceOrConstraint(models.Model):
    left = models.ForeignKey(SchedulingElement)
    right = models.ForeignKey(SchedulingElement)


class UserPreference(UserPreferenceOrConstraint):
    score = models.IntField()


class UserConstaint(UserPreferenceOrConstraint):
    pass


# Output


class Section(models.Model):
    course = models.ForeignKey(Course)
    suffix = models.CharField(max_length=50)


class Hunk(models.Model):
    section = models.ForeignKey(Section)
    professor = models.ForeignKey(Professor)
    room = models.ForeignKey(Room)
    block = models.ForeignKey(Block)
