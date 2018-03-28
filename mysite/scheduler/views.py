from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.models import User, Group
from django.contrib.auth import login, authenticate, logout
from django import template
import logging
from scheduler.dataAPI import *
from django.urls import reverse
from .models import Block

## TODO: Documentation
def upload_csv_time_block(request):
	data = {}
	if "GET" == request.method:
		return render(request, "import_data.html", data)
    # if not GET, then proceed
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
			data_dict = {}
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
