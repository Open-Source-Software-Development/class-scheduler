"""
Import json data from CSV file to Datababse
"""
import os
import csv
from room_data.models import Room
from django.core.management.base import BaseCommand
from datetime import datetime
from mysite.settings import BASE_DIR


class Command(BaseCommand):
    def import_room_from_csv_file(self):
        data_folder = os.path.join(BASE_DIR, 'room_data', 'resources/csv_file')
        print(data_folder, 'data_folder')
        for data_file in os.listdir(data_folder):
            with open(os.path.join(data_folder, data_file), encoding='utf-8') as data_file:
                data = csv.reader(data_file)
                for data_object in data:
                    building = data_object[0]
                    room = data_object[1]
                    capacity = data_object[2]
                    type_room = data_object[3]
                    division = data_object[4]
                    subject = data_object[5]
                    course_style = data_object[6]


                    try:
                        rooms, created = Room.objects.get_or_create(
                            building = building,
                            room = room,
                            capacity = capacity,
                            type_room = type_room,
                            division = division,
                            subject = subject,
                            course_style = course_style,
                        )
                        if created:
                            rooms.save()
                            display_format = "\nrooms, {}, has been saved."
                            print(display_format.format(rooms))
                    except Exception as ex:
                        print(str(ex))
                        msg = "\n\nSomething went wrong saving this rooms: {}\n{}".format(building, str(ex))
                        print(msg)


    def handle(self, *args, **options):
        """
        Call the function to import data
        """
        self.import_room_from_csv_file()
