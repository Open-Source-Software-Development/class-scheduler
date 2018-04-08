from scheduler.models import *

class CourseLevel():

    def __init__(self):
        pass

    def get_grade_level(self):
        return GradeLevel.objects.all()
        
    def get_grade_by_year(self, year):
        return GradeLevel.objects.all().filter(grade_level = year)

    def insert_grade_level(self, course_title, grade_level):
        """
            hmm. 
        """
        
        course = Course.objects.all().get(title = course_title)
        ngl = GradeLevel(course=course, grade_level = grade_level)
        ngl.save()
        

   