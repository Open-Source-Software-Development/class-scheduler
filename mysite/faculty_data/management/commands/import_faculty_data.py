"""
Import json data from CSV file to Datababse
"""
import os
import csv
from faculty_data.models import Faculty
from django.core.management.base import BaseCommand
from datetime import datetime
from mysite.settings import BASE_DIR


class Command(BaseCommand):
    def import_faculty_from_csv_file(self):
        data_folder = os.path.join(BASE_DIR, 'faculty_data', 'resources/csv_file')
        print(data_folder, 'data_folder')
        for data_file in os.listdir(data_folder):
            with open(os.path.join(data_folder, data_file), encoding='utf-8') as data_file:
                data = csv.reader(data_file)
                for data_object in data:
                    division = data_object[0]
                    first_name = data_object[1]
                    last_name = data_object[2]
                    subject = data_object[3]
                    course = data_object[4]

                    try:
                        faculty, created = Faculty.objects.get_or_create(
                            division = division,
                            first_name = first_name,
                            last_name = last_name,
                            subject = subject,
                            course = course,
                        )
                        if created:
                            faculty.save()
                            display_format = "\nfaculty, {}, has been saved."
                            print(display_format.format(faculty))
                    except Exception as ex:
                        print(str(ex))
                        msg = "\n\nSomething went wrong saving this faculty: {}\n{}".format(division, str(ex))
                        print(msg)


    def handle(self, *args, **options):
        """
        Call the function to import data
        """
        self.import_faculty_from_csv_file()
