from django.test import TestCase

from .models import Entry

from scheduler.models import Named
# Create your tests here.

class testModels():

    def test_model_string_representaion(self):
        entry = Entry(title="My entry title")
        self.assertEqual(str(entry), entry.title)
        
    
