from django.db import models
from django.contrib.contenttypes.fields import GenericForeignKey
from django.contrib.contenttypes.models import ContentType
import six

# Input

#This is not in first normal form,  it probably should be, will require some refactoring but mostly copy paste edits
#Plan: Fill out as we have it, refactor as problems arise or desire to improve performance increases.  


#Also need to be wary of duplicating data.


class Named(models.Model):
    name = models.CharField(max_length=200)

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
    def division_default():
        return "ClassroomError"
    division = models.CharField(max_length=20, default=division_default)
    
class RoomType(models.Model):
    """
        TODO: Documentation
    """
    def room_type_default():
        return "ClassroomError"
    
    room_type = models.CharField(max_length=20, default=room_type_default)
    
class Block(models.Model):
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
    class Meta:
        unique_together = (('block_id', 'day'),)
        
    def block_default():
        return 0
	
    def day_default():
        return "ErrorDay"
	
    def time_default():
        return "0:00"
	
    block_id = models.PositiveIntegerField(default=block_default)
    day = models.CharField(max_length=15, default=day_default)
    start_time = models.TimeField(auto_now=False, auto_now_add=False, default=time_default)
    end_time = models.TimeField(auto_now=False, auto_now_add=False, default=time_default)
    #next_block = models.OneToOneField('self', on_delete=models.CASCADE, related_name="previous_block", null=True, blank=True)
    #paired_with = models.ForeignKey('self', on_delete=models.CASCADE, related_name="paired_with_reverse", null=True, blank=True)
    
class Course(models.Model):
    """
        Table: Course
        Primary Key: Composite (program, identifier)
        Columns:
            division: Foreign Key (Division)
                - The course's division code (ex: ITS)
            program: CharField (Max Length 10)
                -  The program identifier (ex: CSI)
            identifier: CharField (Max length 20)
                - The course's identifying code (ex: 320 like it CSI-320)
            title: CharFierld (Max Length 30) 
                - The course title (ex: "intro to computer science" )
            section_capacity: Positive Integer
                - The maximum amout of registerable students in this course
                - Used to generate number of sections needed
            ins_method (instructional Method): CharField (Max Lenght 20)
                - The courses instructional method (ex STN)
            style: Charfield (Max Length 20)
                - The style of the course (ex: studio)
            section count: Positive Integer
                -generated data, not input
    """
    class Meta:
        unique_together = (('program', 'identifier'),)
    
    def program_default():
        return "ProgramError"
    
    def identifier_default():
        return "IndentifierError"
        
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    program = models.CharField(max_length=10, default=program_default)
    identifier = models.CharField(max_length=20, default=identifier_default)
    title = models.CharField(max_length=30, null=True, blank=True)
    ins_method = models.CharField(max_length=20, null=True, blank=True)
    section_capacity = models.PositiveIntegerField(null=True)
    style = models.CharField(max_length=20, null=True, blank=True)
    # TODO: Move to seperate table most likely.
    section_count = models.PositiveIntegerField()



class Professor(Named):
    """
        Table: Professor
        PrimaryKey: Autogenerated ID
        Columns:
            division: ForeignKey Division
            first: CharField(max_length=20)
                - Professors First Name (ex: James)
            last: CharField(max_length=20)
                -Professor Last Name (ex: Wilson)
    """
    division = models.ForeignKey(Division, on_delete=models.CASCADE)
    first = models.CharField(max_length=20, null=True, blank=True)
    last = models.CharField(max_length=20, null=True, blank=True)
  
   

class professor_courses(models.Model):
    """
        Table: Professor_Courses
        PrimaryKey: Autogenerated id
        Columns: 
            professor: ForeignKey Professor
            subject: CharField(max_length=20)
                -The course subject that they teach (ex: CSI)
            identifier: CharField(max_length=20)
                -The course identifier (ex 320 in CSI-320)
    
    """
    def professor_default():
        return (0)
    professor = models.ForeignKey(Professor, on_delete=models.CASCADE, null=True)
    subject = models.CharField(max_length=20, null=True, blank=True)
    identifier = models.CharField(max_length=20, null=True, blank=True)

    
class PregenSection(models.Model):
    """
        TODO: Finish Table
        TODO: Documentation
    """
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    suffix = models.CharField(max_length=50)

    def __str__(self):
        return six.text_type("{}{}").format(str(self.course), self.suffix)



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
    
    class Meta:
        unique_together = (('building', 'room_number'),)
        
    def room_number_default():
        return 0   
    
    def building_default():
        return "BuildingNotFound"
    
    room_number = models.PositiveIntegerField(default=room_number_default)
    building = models.CharField(max_length=20, default=building_default)
    division = models.ForeignKey(Division, on_delete=models.CASCADE, null=True, blank=True)
    subject = models.CharField(max_length=10, null=True, blank=True)
    style = models.CharField(max_length=10, null=True, blank=True)
    room_type = models.ForeignKey(RoomType, on_delete=models.CASCADE)




# Input : User preferences/constraints

##########################################
##                                       #
##  Havent Touched Below This point      #
##                                       #  
##########################################

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


class UserConstraint(UserPreferenceOrConstraint):

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
