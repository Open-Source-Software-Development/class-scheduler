from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.models import User, Group
from django.contrib.auth import login, authenticate, logout
from django import template
import logging
from scheduler.dataAPI import *
from django.urls import reverse
from .models import *

def upload_csv(request):
    data = {}
    if "GET" == request.method:
        return render(request, "import_data.html", data)
    csv_type = request.POST['css-tabs']
    return {"courses": upload_csv_course,
        "blocks": upload_csv_time_block,
        "professors": upload_csv_professor,
        "rooms": upload_csv_room,
        "divisions": upload_csv_division
    }[csv_type](request)

## TODO: Time Block Data
def upload_csv_time_block(request):
    try:
        csv_file = request.FILES["csv_file"]
        if not csv_file.name.endswith('.csv'):
            messages.error(request,'File is not CSV type')
            return HttpResponseRedirect(reverse("upload"))
        file_data = csv_file.read().decode("utf-8")
        lines = file_data.split("\n")
        #loop over the lines and save them in db. If error , store as string and then display
        for line in lines:
            fields = line.split(",")
            block_id = fields[0]
            day = fields[1]
            start_time = fields[2]
            end_time = fields[3]
            try:
                block, created = Block.objects.get_or_create(
                    block_id = block_id,
                    day = day,
                    start_time = start_time,
                    end_time = end_time,
                )
                if created:
                    block.save()
                else:
                    logging.getLogger("error_logger").error(form.errors.as_json())
            except Exception as e:
                #logging.getLogger("error_logger").error(repr(e))
                pass
    except Exception as e:
        logging.getLogger("error_logger").error("Unable to upload file. "+repr(e))
    return HttpResponseRedirect(reverse("upload"))

## TODO: Course Data
def upload_csv_course(request):
    """Upload course data.
    Keyword arguments:
    request - get POST

    Table: Course
    Primary Key: Composite (program, identifier)
    Columns:
        division: Foreign Key (Division)
            - The course's division code (ex: ITS)
        program: CharField (Max Length 10)
            -  The program identifier (ex: CSI)
        title: CharFierld (Max Length 30)
            - The course title (ex: "intro to computer science" )
        section_capacity: Positive Integer
            - The maximum amout of registerable students in this course
            - Used to generate number of sections needed
        ins_method (instructional Method): CharField (Max Lenght 20)
            - The courses instructional method (ex STN)
        style: Charfield (Max Length 20)
            - The style of the course (ex: studio)
    """
    try:
        csv_file = request.FILES["csv_file"]
        if not csv_file.name.endswith('.csv'):
            messages.error(request,'File is not CSV type')
            return HttpResponseRedirect(reverse("upload"))
        file_data = csv_file.read().decode("utf-8")
        lines = file_data.split("\n")
        #loop over the lines and save them in db. If error , store as string and then display
        for line in lines:
            fields = line.split(",")
            division = fields[0]
            program = fields[1]
            style = fields[2]
            title = fields[3]
            ins_method = fields[4]
            section_capacity = fields[5]
            try:
                course, created = Course.objects.get_or_create(
                    division = division,
                    program = program,
                    style = style,
                    title = title,
                    ins_method = ins_method,
                    section_capacity = section_capacity,
                )
                if created:
                    course.save()
                else:
                    logging.getLogger("error_logger").error(form.errors.as_json())
            except Exception as e:
                #logging.getLogger("error_logger").error(repr(e))
                pass
    except Exception as e:
        logging.getLogger("error_logger").error("Unable to upload file. "+repr(e))
    return HttpResponseRedirect(reverse("upload"))

## TODO: Professor Data
def upload_csv_professor(request):
    """Upload professor data.
    Keyword arguments:
        request - get POST

    Table: Professor
    PrimaryKey: Autogenerated ID
    Columns:
        division: ForeignKey Division
        first: CharField(max_length=20)
            - Professors First Name (ex: James)
        last: CharField(max_length=20)
            -Professor Last Name (ex: Wilson)

    """
    try:
        csv_file = request.FILES["csv_file"]
        if not csv_file.name.endswith('.csv'):
            messages.error(request,'File is not CSV type')
            return HttpResponseRedirect(reverse("upload"))

        file_data = csv_file.read().decode("utf-8")

        lines = file_data.split("\n")
        #loop over the lines and save them in db. If error , store as string and then display
        for line in lines:
            fields = line.split(",")
            division = Division.objects.get(division=fields[0])
            first = fields[1]
            last = fields[2]
            try:
                professor, created = Professor.objects.get_or_create(
                    division = division,
                    first = first,
                    last = last,
                )
                if created:
                    professor.save()
                else:
                    logging.getLogger("error_logger").error(form.errors.as_json())
            except Exception as e:
                #logging.getLogger("error_logger").error(repr(e))
                pass

    except Exception as e:
        logging.getLogger("error_logger").error("Unable to upload file. "+repr(e))

    return HttpResponseRedirect(reverse("upload"))

## TODO: Room Data
def upload_csv_room(request):
    """Upload professor data.
    Keyword arguments:
        request - get POST

    Table: Room
    PrimaryKey: Composite (building, room_number)
    Columns:
        room_number: PositiveInteger
            - The number label for a room (ex. 220)
        building: CharField(Max Lenghth: 20)
            - The name of the building the room is in (ex. Joyce)
        division: Foreign Key (Division)
        subject: CharField(Max Lenghth: 20)
            - The subject taught in the room (ex. EGP)
            - This field constrains the classes taught down to the programs listed.
        style: CharField(Max Lenghth: 20)
            - The style of course taught in a room (ex. Studio)
        room_type: Foreign Key (Room Type)
    """
    try:
        csv_file = request.FILES["csv_file"]
        if not csv_file.name.endswith('.csv'):
            messages.error(request,'File is not CSV type')
            return HttpResponseRedirect(reverse("upload"))
        file_data = csv_file.read().decode("utf-8")
        lines = file_data.split("\n")
        #loop over the lines and save them in db. If error , store as string and then display
        for line in lines:
            fields = line.split(",")
            building = fields[0]
            room_number = fields[1]
            room_capacity = fields[2]
            room_type = fields[3]
            division = fields[4]
            subject = fields[5]
            course_number = fields[6]
            try:
                room, created = Room.objects.get_or_create(
                    building = building,
                    room_number = room_number,
                    room_capacity = room_capacity,
                    room_type = room_type,
                    division = division,
                    subject = subject,
                    course_number = course_number,
                )
                if created:
                    room.save()
                else:
                    logging.getLogger("error_logger").error(form.errors.as_json())
            except Exception as e:
                #logging.getLogger("error_logger").error(repr(e))
                pass
    except Exception as e:
        logging.getLogger("error_logger").error("Unable to upload file. "+repr(e))
    return HttpResponseRedirect(reverse("upload"))

## TODO: Division Data
def upload_csv_division(request):
    try:
        csv_file = request.FILES["csv_file"]
        if not csv_file.name.endswith('.csv'):
            messages.error(request,'File is not CSV type')
            return HttpResponseRedirect(reverse("upload"))
        file_data = csv_file.read().decode("utf-8")
        lines = file_data.split("\n")
        #loop over the lines and save them in db. If error , store as string and then display
        for line in lines:
            fields = line.split(",")
            division = fields[0]
            try:
                divis, created = Division.objects.get_or_create(
                    division = division,
                )
                if created:
                    divis.save()
                else:
                    logging.getLogger("error_logger").error(form.errors.as_json())
            except Exception as e:
                #logging.getLogger("error_logger").error(repr(e))
                pass
    except Exception as e:
        logging.getLogger("error_logger").error("Unable to upload file. "+repr(e))
    return HttpResponseRedirect(reverse("upload"))
