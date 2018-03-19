

@echo off

WHERE python3
IF %ERRORLEVEL% EQU 0 (
	python3 -m pip install --upgrade pip
	pip install virtualenv
	pip install virtualenvwrapper-win
	pause
	exit /b 0
)


WHERE py3
IF %ERRORLEVEL% EQU 0 (

	py3 -m pip install --upgrade pip
	pip install virtualenv
	pip install virtualenvwrapper-win
	pause
	exit /b 0
)



WHERE python
IF %ERRORLEVEL% EQU 0 (

	python -m pip install --upgrade pip
	pip install virtualenv
	pip install virtualenvwrapper-win
	pause
	exit /b 0


)
WHERE py
IF %ERRORLEVEL% EQU 0 (

	py -m pip install --upgrade pip
	pip install virtualenv
	pip install virtualenvwrapper-win
	pause
	exit /b 0
	

)

echo "ERROR: Couldn't find Python executable"
exit /b 1




REM Start cmd prompt as Admin
REM cd into folder containing script
REM type the name of this file then hit enter
REM Assumptions, python3 and pip are already installed
REM First line updates pip if neccessary 
REM second line installs virtual env 
REM third installs the windows wrapper
REM final two lines pause then exit the program