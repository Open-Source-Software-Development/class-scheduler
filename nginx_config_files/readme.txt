modify mysite_nginx.conf to match the system you are using

run 'sudo ln -s ~/path/to/your/mysite/mysite_nginx.conf /etc/nginx/sites-enabled/' with the path to your project folder

start nginx
run 'uwsgi --ini mysite_uwsgi.ini' in the project directory
navigate to localhost:8000


I used this link as a resource 'http://uwsgi-docs.readthedocs.io/en/latest/tutorials/Django_and_nginx.html'
