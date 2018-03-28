WINDOWS:
Windows users should use the standard mysql installer wizard

	Your file may be named my.ini
	To find where 'my.ini' is stored on your computer use Win+R( shortcut for run) type services.msc and hit enter
	Find and entry like "MySQL" right click on it and select properties and find the file path
	Modify the my.ini in aforementioned windows directory to match the my.cnf, located in the mysql_config/mysql_root directory  
	While in the project virtualenv run 
		'pip install mysqlclient'
	Django needs this library to configure its' ORM to use MySQL
LINUX:


Make sure you have mysql installed, I use MariaDB

	'sudo apt-get install mysql-server -y'

I logged into the DB shell and created a db called myproject and a user called myproject user with password 'password'

	'sudo apt-get install libmariadbclient-dev-compat'

While in the virtualenv run 
	'pip install mysqlclient'

Then just move the my.cnf file to 'etc/mysql', or the equivilant path on windows and modify the 'databases' section of settings.py, in the mysite/mysite directory of Django, to match your system.

In your virtualenv and in the root folder of your project run 'python manage.py migrate' to ensure it is working correctly. 

