from scheduler.models import *

class CourseLevel():

    def __init__(self, course_name):
        self.course = Course.objects.get(title = course_name)

    def get_grade_level(self):
        return GradeLevel.objects.all()
        
    def get_grade_by_year(self, year):
        return GradeLevel.objects.all().filter(year = year)

    def insert_grade_level(self, grade_level):
        """
            hmm. 
        """
        
        
        ngl = GradeLevel(course=self.course, grade_level = grade_level)
        ngl.save()
        

   