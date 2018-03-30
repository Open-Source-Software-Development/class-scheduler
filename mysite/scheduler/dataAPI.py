from scheduler.models import *

"""
import django
django.setup()
cd scheduler


from dataAPI import *
d = DataAPI()
d.make_dict_for_block_display()

"""

class DataAPI() :
    """
        Interact with the database here:
        
        Functionality:
            
    
    """
    
    def get_blocks(self):
        """
            returns the block table
        """
        return Block.objects.all()
      
    def get_blocks_by_day(self, day):
        """
            get blocks that occur on day.
        """
        return Block.objects.filter(day=day).values("block_id", "day", "start_time")
    def get_unique_times(self, day):
        """
            get blocks that occor on day.
        """
        
        return Block.objects.filter().values("start_time", "end_time").distinct()
    
    
    def make_dict_for_block_display(self):
        """
            for tynan in views
        """
        masterdict = {}
        days = Block.objects.filter().values("day").distinct()
        for day in days:
            day = day["day"]
            byday = self.get_blocks_by_day(day)
            for item in byday:

                timedict = {}

                #byday.values()[item]["block_id"]()
                #print(str(byday.values()[item]["start_time"])[0:5])
                #print(byday.values()[item]["day"])
                timedict[str(item["start_time"])[0:5]] =item["block_id"]
            masterdict [item["day"]] = timedict
        return masterdict 
    
    def clear_professor(self, prof_first, prof_last):
        prof = Professor.objects.get(first = prof_first, last = prof_last)
        ProfessorConstraint.objects.filter(professor=prof).delete()
    
    def insert_professor_avalible(self, prof_first, prof_last, timeblock, value):
        """
            Insert professor constriants into the proper location. 
        """
        prof = Professor.objects.get(first = prof_first, last = prof_last)
        try:
            block = Block.objects.get(block_id = timeblock)
            nc = ProfessorConstraint(professor=prof, block=block, value=value)
            nc.save()
        except Block.DoesNotExist:
            pass
        
    def get_professor_avalible(self, prof_first, prof_last):
        """
            Insert professor constriants into the proper location. 
        """
        prof = Professor.objects.get(first = prof_first, last = prof_last)
        constraints = ProfessorConstraint.objects.filter(professor = prof).select_related("block").distinct()
        return {constraint.block.block_id: constraint.value for constraint in constraints}
        