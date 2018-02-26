from django.test import TestCase

from scheduler.models import *
# Create your tests here.

class TestModels(TestCase):

    def test_named_string_representaion(self):
        named = Named(name="name")
        self.assertEqual(str(named), named.name)
        
