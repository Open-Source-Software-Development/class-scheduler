from scheduler.models import *

class BlockCalendar():

    def __init__(self, professor):
        self.professor = professor

    def get_blocks(self):
        return Block.objects.all()

    def get_days(self):
        return Block.objects.all().values("day").distinct()

    def get_blocks_by_day(self, day):
        """Get all the blocks that occur on a given day."""
        return Block.objects.filter(day=day).values("block_id", "day", "start_time")

    def get_unique_times(self):
        """Get all the unique (start_time, end_time) pairs."""
        return Block.objects.all().values("start_time", "end_time").distinct()

    def clear_professor(self):
        ProfessorConstraint.objects.filter(professor=self.professor).delete()

    def insert_professor_available(self, timeblock, value):
        """
            Insert professor constriants into the proper location. 
        """
        try:
            block = Block.objects.get(block_id = timeblock)
            nc = ProfessorConstraint(professor=self.professor, block=block, value=value)
            nc.save()
        except Block.DoesNotExist:
            pass

    def get_professor_available(self):
        """
            Insert professor constriants into the proper location. 
        """
        constraints = ProfessorConstraint.objects.filter(professor=self.professor).select_related("block").distinct()
        return {constraint.block.block_id: constraint.value for constraint in constraints}
