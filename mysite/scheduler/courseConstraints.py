from scheduler.models import *

class CourseLevel():
    """
    This is used by views in Polls/views.py.  This has functions for managing grade_level data in the database.

    Avalible functions:
        get_grade_level()
        get_grade_by_year(year)
        get_course_by_id(id)
        insert_grade_level(course_title, grade_level)
        get_course_by_title(title)
        remove_courselevel(course_title, grade_level)
    """
    def __init__(self):
        pass

    def get_grade_level(self):
        """
            Pre: None
            Post: Returns all of the rows in the GradeLevel table
        """
        return GradeLevel.objects.all()

    def get_grade_by_year(self, year):
        """
            Pre: Requires a grade year such as "first"
            Post: Returns all of the rows in the GradeLevel with a given year
        """
        return GradeLevel.objects.all().filter(grade_level = year)

    def get_course_by_id(self, id) :
        """
            Pre: Requires a course Id
            Post: Returns course with given id
        """
        return Course.objects.get(pk=id)

    def insert_grade_level(self, course_title, grade_level):
        """
            Pre: Requires a course title and desired grade level
            Post: new row added to grade level based on given info.
        """

        course = Course.objects.all().get(title = course_title)
        ngl = GradeLevel(course=course, grade_level = grade_level)
        ngl.save()

    def get_course_by_title(self, title):
        """
            Pre: Requires title of a course
            Post: Returns the course object with given title.
            ################################################################
            note: Some titles are not unique and this will need to be fixed.
        """
        return Course.objects.get(title = title)

    def remove_courselevel(self, course_title, grade_level):
        """
            Pre: Requires course title and grade year
            Post: Removes desired object from GradeLevel table
        """
        course = Course.objects.get(title = course_title.strip())
        gl = GradeLevel.objects.get(course = course.id,grade_level=grade_level)
        gl.delete()

