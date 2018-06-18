# OSD Class Scheduler

The OSD class scheduling software can schedule batches of courses given constraints of room amount, available faculty (and their schedules), and other specified criteria. It is a proof of concept for a more developed project, designed quickly and made to convince Chanplain College administration that students should be funded to make software for the college (under certain circumstances). 

# Documentation

All code-generated documentation can be found at https://open-source-software-development.github.io/class-scheduler/html/. This README is presently the only actual document.

There is however doucmentation from the development process, which is stored on Google Drive. It can be viewed with a Champlain College google authorized account at [Google Drive](https://drive.google.com/drive/folders/0By8UcckBpf_zT3E2SE41UjJrZ00?usp=sharing).

# Installing

By following these steps, you will setup the environment for the project to be able to be run. This includes installing Python 3.x and the Django framework. If you dont already have mysql installed, you will need that as well (probably).

- Clone this repo
- Navigate to the project directory
- Install Python 3.x (required by the Django framework version)
- Install the Django framework ("pip install django")
- Check to ensure django was installed ("python -m django --version")

# Setup

After installing, you will need to setup the project in order for it to run.
- Navigate to the mysite directory ("cd mysite")
- Make database migrations ("python manage.py makemigrations")
- Migrate necessary data ("python manage.py migrate")
- Make the admin user ("python3 manage.py createsuperuser") (you MUST have one of these - I suggest using Username:"admin" Password:"swordfish" if you are not planning on putting this project into production, but the username and password used are irrelevant)

# Running

Now you can run the server! Just use "python manage.py runserver" while in the "mysite" directory. This will launch the project on your localhost on port 8000. You can log in using the admin user at "127.0.0.1:8000", or access the admin backend at "127.0.0.1:8000/admin".

# Frontend vs Backend

The frontend (accessed at "127.0.0.1:8000") is where all non-IT/admin users will go to use the product. Here you can find the panels to access the faculty schedules, what courses are being run per season/semester. If you have the pertinent roles, you can even start a scheduling pass or upload data from the correct file formats to populate the database.

The backend (accessed at "127.0.0.1:8000/admin") is used for the nitty gritty. If you need to manipulate specific data entries for users or scheduling data, this is where you go to do it.

# Algorithm overview

The scheduler algorithm is a variant of [backtracking search](https://en.wikipedia.org/wiki/Backtracking). In brief, we start with an empty schedule, and repeatedly add entries to it. If the schedule ever gets into an invalid state - if all the professors who can teach a class are unavailable, and that class still has unscheduled sections, for example - the algorithm backs up until it gets back to a valid state. In order to minimize the number of backtracks needed, the algorithm tries to pick the hardest courses and schedule them first.

Consule the various `package-info.java` files for specifics. The root package (`osd`) is a good place to start.
