::Assumes python is installed
::Installs packages to run Django in a virtual enviroment called myproject
::If you start a new command prompt, you’ll need to activate the environment again using 'workon myproject'
::If pip fails with a type error you may need to run 'conda install pip' or another command to update pip according to your package manager
::For troubleshooting look here 'https://docs.djangoproject.com/en/2.0/howto/windows/'
::To get started look here 'https://docs.djangoproject.com/en/2.0/intro/tutorial01/' 
python --version
PAUSE
pip install virtualenvwrapper-win
PAUSE
mkvirtualenv myproject
PAUSE
pip install django
PAUSE
python -m django --version
ECHO Install Succesfull
PAUSE