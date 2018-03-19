Make sure you have mysql installed, I use MariaDB

'sudo apt-get install mysql-server -y'

I logged into the DB shell and created a db called myproject and a user called myproject user with password 'password'

'sudo apt-get install libmariadbclient-dev-compat'

While in the virtualenv run 'pip install mysqlclient'

Then just move the my.cnf file to etc/mysql and modify settings.py to match your system.

In your virtualenv and in the root folder of your project run 'python manage.py migrate' to ensure it is working correctly. 

