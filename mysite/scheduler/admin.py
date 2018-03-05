from django.contrib import admin
from scheduler.models import *

admin.site.register(Division)
admin.site.register(RoomType)
admin.site.register(Block)
admin.site.register(Course)
admin.site.register(Professor)
admin.site.register(ProfessorCourses)
admin.site.register(PregenSection)
admin.site.register(Room)
admin.site.register(ConstraintPreferenceList)
admin.site.register(CourseConstraint)
admin.site.register(ProfessorConstraint)
admin.site.register(UserPreference)
admin.site.register(UserConstraint)
admin.site.register(Section)
admin.site.register(Hunk)