Django includes users and user groups out of the box. It also includes an administrator G.U.I that allows
for database manipulation, permissions granting and more. To get started you should already have django
installed and able to run (Not running right now!) with a database schema, i.e makemigrations and migrate are run
(See django_runserver for more detail). 

One should start by creating a super user account, i.e god privileges by calling 'python manage.py createsuperuser'
This will ask you to enter in various pieces of data such as a username and password. once this is accomplished you can
start the server by running 'python manage.py runserver' and navigating to the IP of your website appended with /admin/ 
ex. ' http://127.0.0.1:8000/admin/'. This will bring up a login page where you can enter the credentials you have previously input.
Once you are logged in you will see a number of items in your dashboard, such as the names of models in your models.py, but you
should also be able to see users and groups. By clicking on the '+' sign next to these will create a new user or group. Permissions can be granted 
overall, such as create user privileges, or specified to individual models, ex. 'the registrar group has the 'auth create Courses' attribute for their group'
For more information about the admin dashboard and commands refer to the documentation here 'https://docs.djangoproject.com/en/2.0/ref/django-admin/'  