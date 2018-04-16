::Assumes python is installed
::Installs packages to run Django in a virtual enviroment called myproject
::If you start a new command prompt, you’ll need to activate the environment again using 'workon myproject'
::If pip fails with a type error you may need to run 'conda install pip' or another command to update pip according to your package manager
::For troubleshooting look here 'https://docs.djangoproject.com/en/2.0/howto/windows/'
::To get started look here 'https://docs.djangoproject.com/en/2.0/intro/tutorial01/' 


@echo off

WHERE python3
IF %ERRORLEVEL% EQU 0 (
	cd ../java
	mvn clean install
	cd ../mysite
	python3 -m pip install --upgrade pip
	pip install django
	cd
    python3 manage.py makemigrations
    python3 manage.py migrate
    python3 manage.py runserver
    cd ..
    exit /b 0
)

WHERE py3
IF %ERRORLEVEL% EQU 0 (
	cd ../java
	mvn clean install
	cd ../mysite
	py3 -m pip install --upgrade pip
	pip install django
	cd
    py3 manage.py makemigrations
    py3 manage.py migrate
    py3 manage.py runserver
    cd ..
    exit /b 0
)

WHERE python
IF %ERRORLEVEL% EQU 0 (
	cd ../java
	mvn clean install
	cd ../mysite
	python -m pip install --upgrade pip
	pip install django
	cd
    python manage.py makemigrations
    python manage.py migrate
    python manage.py runserver
    cd ..
    exit /b 0
)

WHERE py
IF %ERRORLEVEL% EQU 0 (
	cd ../java
	mvn clean install
	cd ../mysite
	py -m pip install --upgrade pip
	pip install django
	cd
    py manage.py makemigrations
    py manage.py migrate
    py manage.py runserver
    cd ..
    exit /b 0
)

echo "ERROR: Couldn't find Python executable"
cd ..
exit /b 1

