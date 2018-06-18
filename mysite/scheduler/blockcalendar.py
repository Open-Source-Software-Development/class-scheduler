from scheduler.models import *

class BlockCalendar():
    """
        This class interacts with Polls/views.py's professor settings page.

        Avalible functions:
            get_blocks()
            get_days()
            get_blocks_by_day(day)
            get_unique_times()
            clear_professor()
            insert_professor_available(timeblock, value)
            get_professor_available(s
    """
    def __init__(self, professor):
        self.professor = professor

    def get_blocks(self):
        """
            Pre: None
            Post: Return all of the blocks in the database
        """
        return Block.objects.all()

    def get_days(self):
        """
            Pre: None
            Post: Get all of the distinct day values from the database
        """
        return Block.objects.all().values("day").distinct()

    def get_blocks_by_day(self, day):
        """Get all the blocks that occur on a given day."""
        return Block.objects.filter(day=day).values("block_id", "day", "start_time")

    def get_unique_times(self):
        """Get all the unique (start_time, end_time) pairs."""
        return Block.objects.all().values("start_time", "end_time").distinct()

    def clear_professor(self):
        """
            Pre: valid Professor __init__
            Post: remvoes all professors constraints from the ProfessorConstraint tab;le
        """
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
