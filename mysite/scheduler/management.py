from django.db.models import signals
from django.contrib.auth.models import Group, Permission
from django.dispatch import receiver
from django.db.models.signals import post_migrate
import scheduler.models as models

scheduler_groups = {
    "Professor": [ ],
    "Division head": [ ],
    "Program director": [ ],
    "Registrar": [ ],
}

@receiver(post_migrate)
def create_scheduler_groups(sender, verbosity, **kwargs):
    for group_name in scheduler_groups:
        group, created = Group.objects.get_or_create(name=group_name)
        if verbosity > 0 and created:
            print("Created " + group_name)
        for permission in scheduler_groups[group_name]: 
            group.permissions.add(Permission.objects.get(codename=permission))
            if verbosity > 1:
                print("Gave %s permission to %s".format(permission, group_name))
        group.save()
