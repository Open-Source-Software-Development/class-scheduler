This script will do three things when run

1. It will call 'python3'(The script will run through common python environment variables and try to call the one your system uses) manage.py makemigrations'.
This will prepare the relational database statements that will be required to create database tables based upon the models in our models.py file

2. It will apply these changes to the database by calling 'python3 manage.py migrate'

3. Finally it will attempt to run the django server by calling 'python3 manage.py runserver'
This command can be customized to run on a certain IP or port, ex 'python3 manage.py runserver 192.168.1.1 8005' will run the server on 192.168.1.1 on port 8001
The default run settings, without any arguments, are localhost(127.0.0.1) and port 8000. 