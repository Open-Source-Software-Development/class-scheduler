#!/bin/bash

function exists {
    if (which "$1" >/dev/null 2>&1); then
        EXE="$1"
	return 0
    fi
    return 1
}

function fail {
	echo "ERROR: Could not find Python executable!"
	exit 1
}

exists "python3" || exists "py3" || exists "python" || exists "py" || fail

echo "found python executable: $EXE"

cd mysite

echo "setting up database..."
$EXE manage.py makemigrations &&\
$EXE manage.py migrate &&\
echo "launching server..." &&\
$EXE manage.py runserver
