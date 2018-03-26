from scheduler.models import *

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
        
    def insert_professor_avalible(self, prof_name, timeblock, value):
        """
            Insert professor constriants into the proper location. 
        """
        
        prof = Professor.objects.get(first = prof_name)
        block = Block.objects.get(name = timeblock)
        
        nc = ProfessorConstraint(professor=prof, block=block)
        nc.save()
        