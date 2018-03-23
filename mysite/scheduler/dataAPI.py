from scheduler.models import *

class DataAPI() :
    """
        Interact with the database here:
        
        Functionality:
            
    
    """
    
    def get_blocks():
        """
            returns the block table
        """
        return Block.objects.all()
        
    def insert_professor_avalible(prof_name, timeblock):
        """
            Insert professor constriants into the proper location. 
        """
        
        prof = Professor.object.get(name=prof_name)
        block = Block.object.get(name=timeblock)
        constraint = ProfessorConstraint.objects.create(professor=prof, block=block)