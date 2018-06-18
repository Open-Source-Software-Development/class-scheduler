@echo off

WHERE python3
IF %ERRORLEVEL% EQU 0 (
    cd ../../mysite
    python3 manage.py makemigrations
    python3 manage.py migrate
    python3 manage.py runserver
    cd ..
    exit /b 0
)

WHERE py3
IF %ERRORLEVEL% EQU 0 (
    cd ../../mysite
    py3 manage.py makemigrations
    py3 manage.py migrate
    py3 manage.py runserver
    cd ..
    exit /b 0
)

WHERE python
IF %ERRORLEVEL% EQU 0 (
    cd ../../mysite
    python manage.py makemigrations
    python manage.py migrate
    python manage.py runserver
    cd ..
    exit /b 0
)

WHERE py
IF %ERRORLEVEL% EQU 0 (
    cd ../../mysite
    py manage.py makemigrations
    py manage.py migrate
    py manage.py runserver
    cd ..
    exit /b 0
)

echo "ERROR: Couldn't find Python executable"
cd ..
exit /b 1
