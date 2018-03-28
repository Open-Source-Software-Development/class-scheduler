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
            get blocks that occor on day.
        """
        return Block.objects.filter(day=day).values("block_id", "day", "start_time")
    
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
            
    def insert_professor_avalible(self, prof_name, timeblock, value):
        """
            Insert professor constriants into the proper location. 
        """
        
        prof = Professor.objects.get(first = prof_name)
        block = Block.objects.get(name = timeblock)
        
        nc = ProfessorConstraint(professor=prof, block=block)
        nc.save()
        