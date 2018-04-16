Notes on the install_and_run bat script

Assumptions: Python v3 is installed and accessible from your Path, Java SDK is installed and accessible from your Path, Maven is installed and accessible from your Path

First the script will check what the name of your python environment variable is (py, python etc)
It will then update pip to insure you are getting up to data packages.

After pip is updated the script will install Django


The script will prepare sql statements to create tables based 
on models in models.py in each of the app folders Ex ('scheduler/models.py') and then apply them to the database
specified in the mysite/settings.py. 
 
The script will start an instance of the server at port 8000 at localhost
Type 'localhost:8000' in a browser to navigate to the site. 
The server will live update as you make changes so no need to restart the server whenever a change is made. 