import subprocess, threading, time

active_run = None

def start_run():
    """Starts a new scheduler run, canceling any already active."""
    cancel_run()
    threading.Thread(target=runHelper).start()

def cancel_run():
    """Cancels the active scheduler run, if any."""
    global active_run
    if active_run is not None:
        active_run.terminate()
        active_run = None

def is_running():
    """Determines whether a scheduling algorithm is currently running."""
    global active_run
    return active_run is not None
	

def runHelper():
    """Internal use only. Kicks off a scheduling job in a separate thread."""
    global active_run
    active_run = subprocess.Popen(["java", "-jar",
        "../java/target/Scheduler-jar-with-dependencies.jar"])
    while active_run is not None and active_run.poll() is None:
        time.sleep(0.1)
    active_run = None

