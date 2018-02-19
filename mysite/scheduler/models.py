from django.db import models

# Input


class Division(models.Model):
    name = models.CharField(max_length=50)


class RoomType(models.Model):
    name = models.CharField(max_length=50)


class SchedulingElement(models.Model):
    name = models.CharField(max_length=200)


class Professor(SchedulingElement):
    professor_id = models.OneToOneField(SchedulingElement, primary_key=True, on_delete=models.CASCADE)
    division = models.ForeignKey(Division, on_delete=models.CASCADE)


class Course(SchedulingElement):
    course_id = models.OneToOneField(SchedulingElement, primary_key=True, on_delete=models.CASCADE)
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    section_count = models.IntegerField()


class Room(SchedulingElement):
    course_id = models.OneToOneField(SchedulingElement, primary_key=True, on_delete=models.CASCADE)
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    room_type = models.ForeignKey(RoomType, on_delete=models.CASCADE)


class Block(SchedulingElement):
    block_id = models.OneToOneField(SchedulingElement, primary_key=True, on_delete=models.CASCADE)
    next_block = models.OneToOneField('self', on_delete=models.CASCADE, related_name="previous_block", null=True)
    paired_with = models.ForeignKey('self', on_delete=models.CASCADE, related_name="paired_with_reverse")


class PregenSection(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    suffix = models.CharField(max_length=50)


# Input : User preferences/constraints


class UserPreferenceOrConstraint(models.Model):
    left = models.ForeignKey(SchedulingElement, on_delete=models.CASCADE, related_name="+")
    right = models.ForeignKey(SchedulingElement, on_delete=models.CASCADE, related_name="+")


class UserPreference(UserPreferenceOrConstraint):
    score = models.IntegerField()


class UserConstaint(UserPreferenceOrConstraint):
    pass


# Output


class Section(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    suffix = models.CharField(max_length=50)


class Hunk(models.Model):
    section = models.ForeignKey(Section, on_delete=models.CASCADE)
    professor = models.ForeignKey(Professor, on_delete=models.CASCADE)
    room = models.ForeignKey(Room, on_delete=models.CASCADE)
    block = models.ForeignKey(Block, on_delete=models.CASCADE)
