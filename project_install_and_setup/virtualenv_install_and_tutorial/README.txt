For the batch file 'install_virtual_env.bat'


Assumptions: python3 and pip are already installed and avaliable in the environment path

How to install:

Start cmd prompt as Admin, I use anaconda as the environment path already includes pip. If you use the normal command line ensure that pip
is accessible by simply running 'pip' you should get a help message instead of an error message saying 'no such command'
cd into folder containing script
Type the name of this file then hit enter
INFO: errors will occur as the script checks which python environment variable your system uses

How to use:

run 'mkvirtualenv "my_environment_name"' where "my_environment_name" is the what you want to name your env
this will create a directory
You will automatically be placing into your new virtualenv, this is where we will install Django and other related packages

You can tell if you are in a venv if the name of the venv is the the left of the console cursor
to leave the virtualenv type 'deactivate'
to enter a venv or change your current one type 'workon "my_environment_name"'