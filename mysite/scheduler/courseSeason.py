from scheduler.models import *

class CourseSeason():
    def get_recent_season(self):
        return Season.objects.latest('id')
    def get_courses_from_recent_season(self):
        season = self.get_recent_season()
        return season.courses
    
    def add_course_season(self, course_title):
        course = Course.objects.get(title = course_title.strip())
        season = self.get_recent_season()
        season.courses.add(course)
        season.save()

    def remove_course_season(self, course_title):
        course = Course.objects.get(title = course_title.strip())
        season = self.get_recent_season()
        season.courses.remove(course)