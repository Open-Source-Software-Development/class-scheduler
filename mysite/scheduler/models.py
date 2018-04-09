from django.db import models
from django.contrib.contenttypes.fields import GenericForeignKey
from django.contrib.contenttypes.models import ContentType
import six
from datetime import time
import os, signal

# Input


class Named(models.Model):
    """
        Gives better naming to models that inherit it.
    """
    name = models.CharField(max_length=200, null=True)

    class Meta:
        abstract = True

    def __str__(self):
        return six.text_type(self.name)


class Division(models.Model):
    """
        Table: Division
        Primary Key: Autogenerated identifier.
        Columns:
            division: CharField (Max Length: 20)
                - This should be all of the division identifiers (ex: ITS)
    """
    name = models.CharField(max_length=30)
    
    def __str__(self):
        return self.name


class RoomType(models.Model):
    """
        Table: Room_Type
        Primary Key: Autogenerated ID
    """
    name = name = models.CharField(max_length=30)

    def __str__(self):
        return self.name


class Block(models.Model):
    """
        Table: Block
        Primary Key: Block_id
        Columns:
            block_id: CharField (max length 10)
                - Blocks should be entered in pairs, labeled <n>A and <n>B, where n is a positive int
            day: CharField (max length 15)
                - The day of the week a block happens on
            start_time: TimeField (input in HH:MM)
                - The start time of the block (ex: 8:00)
            end_time: TimeField (input in HH:MM)
                - The end time of the block (ex: 9:30)
            next: Nullable Foreign Key (Block)
                - The next block in the same day
    """
    block_id = models.CharField(max_length=10, blank=True, unique=True)
    day = models.CharField(max_length=15)
    start_time = models.TimeField(auto_now=False, auto_now_add=False)
    end_time = models.TimeField(auto_now=False, auto_now_add=False)

    def __str__(self):
        return self.block_id

    @classmethod
    def calendar(cls):
        result = {}
        for block in cls.objects.all():
            day = block.day
            hour = block.start_time.hour
            if not day in result:
                result[day] = result
            result[day][hour] = block
        return result

    def paired_with(self):
        label = self.block_id[0:-1]
        suffix = self.block_id[-1]
        other_suffix = 'B' if suffix == 'A' else 'A'
        return Block.objects.get(block_id = label + other_suffix)

    def next_block(self):
        m = self.end_time.minute + 15
        h = self.end_time.hour
        if m >= 60:
            m -= 60
            h += 1
        if h >= 24:
            h -= 24
        next_block_start_time = time(h, m, 0)
        try:
            return Block.objects.get(day = self.day, start_time = next_block_start_time)
        except Block.DoesNotExist:
            return None

    def next(self):
        block = self.next_block()
        if block == None:
            raise StopIteration
        return block

    def __iter__(self):
        return self

    def __next__(self):
        return self.next()


class Course(models.Model):
    """
        Table: Course
        Primary Key: Composite (program, identifier)
        Columns:
            division: Foreign Key (Division)
                - The course's division code (ex: ITS)
            program: CharField (Max Length 10)
                -  The program identifier (ex: CSI)
            style: Charfield (Max Length 20)
                - The style of the course (ex: studio)
                - This somehow seems to actually be used for the course ID...?
            base_section_count: Positive Integer
                - If nonzero, the number of sections needed for the course
            title: CharFierld (Max Length 30)
                - The course title (ex: "intro to computer science" )
            section_capacity: Positive Integer
                - The maximum number of registerable students in this course
                - Used to generate number of sections needed
            ins_method (instructional Method): CharField (Max Lenght 20)
                - The courses instructional method (ex STN)

    """
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    program = models.CharField(max_length=10)
    style = models.CharField(max_length=20, blank=True)
    base_section_count = models.PositiveIntegerField()
    title = models.CharField(max_length=30, blank=True)
    ins_method = models.CharField(max_length=20, null=True, blank=True)
    section_capacity = models.PositiveIntegerField()

    def __str__(self):
        return self.title


class Professor(models.Model):
    """
        Table: Professor
        PrimaryKey: Autogenerated ID
        Columns:
            division: ForeignKey Division
            first: CharField(max_length=20)
                - Professors First Name (ex: James)
            last: CharField(max_length=20)
                -Professor Last Name (ex: Wilson)
            qualifications: ManyToManyField with Course
                -Each professor teaches multiple courses
    """
    #name = models.CharField(max_length=30)
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    first = models.CharField(max_length=20, null=True, blank=True)
    last = models.CharField(max_length=20, null=True, blank=True)

    def __str__(self):
        return self.first + " " + self.last


@six.python_2_unicode_compatible
class PregenSection(models.Model):
    """
        TODO: Finish Table
        TODO: Documentation
    """

    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    suffix = models.CharField(max_length=50)

    def __str__(self):
        return six.text_type("{}{}").format(str(self.course), self.suffix)



@six.python_2_unicode_compatible
class Room(models.Model):
    """
        Table: Room
        PrimaryKey: Composite (building, room_number)
        Columns:
            room_number: PositiveInteger
                - The number label for a room (ex. 220)
            building: CharField(Max Lenghth: 20)
                - The name of the building the room is in (ex. Joyce)
            division: Foreign Key (Division)
            subject: CharField(Max Lenghth: 20)
                - The subject taught in the room (ex. EGP)
                - This field constrains the classes taught down to the programs listed.
            style: CharField(Max Lenghth: 20)
                - The style of course taught in a room (ex. Studio)
            room_type: Foreign Key (Room Type)
    """

    building = models.CharField(max_length=20)
    room_number = models.PositiveIntegerField()
    room_capacity = models.PositiveIntegerField()
    room_type = models.ForeignKey(RoomType, on_delete=models.CASCADE)
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    subject = models.CharField(max_length=10, null=True, blank=True)
    course_number = models.CharField(max_length=10, null=True, blank=True)

    def __str__(self):
        return "{}-{}".format(self.building, self.room_number)


@six.python_2_unicode_compatible
class Qualification(models.Model):
    """Semantic constraint that whitelists class/professor combinations.
    nb. when turning this into a constraint, *make sure to use the
    course as the first element*."""
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    professor = models.ForeignKey(Professor, on_delete=models.CASCADE)

    def __str__(self):
        return six.text_type("{} can teach {}").format(self.professor, self.course)

class GradeLevel(models.Model):
    """
        TODO Documentation
    """
    
    class Meta:
        unique_together = (("course", "grade_level"),)
    
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    grade_level = models.CharField(max_length=20)
    
    def __str__(self):
        return "{} ".format(self.course)
    
class ProfessorConstraint(models.Model):
    """
        TODO Documentation
    """
    class Meta:
        unique_together = (('professor', 'block'),)
        
    professor = models.ForeignKey(Professor, on_delete=models.CASCADE)
    block = models.ForeignKey(Block, on_delete=models.CASCADE)
    value = models.PositiveIntegerField()
    # block = models.ForeignKey(Block, on_delete=models.CASCADE)
    
    def __str__(self):
        return "({}, {}, {})".format(self.professor, self.block, self.value)

    def __str__(self):
        return "({}, {}, {})".format(self.professor, self.block)

# Input : User preferences/constraints

@six.python_2_unicode_compatible
class UserPreferenceOrConstraint(models.Model):
    """
        ABC for preferences and constraints. Both are stored as pairs
        of items, which may be of any (scheduler) type.
        Primary Key: Autogenerated ID
        Columns:
            left_argument_type: ForeignKey ContentType
            left_argument_id: Primary key of left argument
            left_argument_object: GenericForeignKey
            right_argument_type: ForeignKey ContentType
            right_argument_id: Primary key of right argument
            right_argument_object: GenericForeignKey
    """
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
    """
        Table: UserPreference
        Primary Key: Autogenerated ID
        Columns:
            score: Int
    """
    score = models.IntegerField()

    def describe(self):
        label = "Good ({})" if self.score >= 0 else "Bad ({})"
        return six.text_type(label).format(self.score)


class UserConstraint(UserPreferenceOrConstraint):

    """
        Table: UserConstraint
        Primary Key: Autogenerated ID
        Columns:
            is_blacklist: Bool
    """

    is_blacklist = models.BooleanField()

    def describe(self):
        if self.is_blacklist:
            return six.text_type("Blacklisted")
        return six.text_type("Whitelisted")



# Output


class Season(models.Model):
    pass


class Run(models.Model):

    season = models.ForeignKey(Season, on_delete=models.CASCADE)
    pid = models.PositiveIntegerField()
    active = models.BooleanField()

    def terminate(self):
        try:
            os.kill(self.pid, signal.SIGTERM)
        except OSError as e:
            print(e)
            pass
        if self.active:
            self.active = False
            self.save()


@six.python_2_unicode_compatible
class Section(models.Model):
    """
        Used for output sections
        TODO:
            Documentation
    """
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    section_identifier = models.CharField(max_length=50)
    run = models.ForeignKey(Run, on_delete=models.CASCADE)

    def __str__(self):
        return six.text_type("{}-{}").format(str(self.course), self.section_identifier)

@six.python_2_unicode_compatible
class Hunk(models.Model):
    """
        TODO:
            Documentation
    """
    section = models.ForeignKey(Section, on_delete=models.CASCADE)
    professor = models.ForeignKey(Professor, on_delete=models.CASCADE)
    room = models.ForeignKey(Room, on_delete=models.CASCADE)
    block = models.ForeignKey(Block, on_delete=models.CASCADE)

    def __str__(self):
        return six.text_type("Hunk({}, {}, {}, {})").format(self.section, self.professor, self.room, self.block)
