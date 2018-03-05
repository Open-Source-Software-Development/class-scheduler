# Generated by Django 2.0.2 on 2018-02-28 19:18

from django.db import migrations, models
import django.db.models.deletion
import scheduler.models


class Migration(migrations.Migration):

    dependencies = [
        ('scheduler', '0002_auto_20180226_1422'),
    ]

    operations = [
        migrations.AddField(
            model_name='block',
            name='block_id',
            field=models.IntegerField(default=scheduler.models.Block.block_default),
        ),
        migrations.AddField(
            model_name='block',
            name='day',
            field=models.CharField(default=scheduler.models.Block.day_default, max_length=15),
        ),
        migrations.AddField(
            model_name='block',
            name='end_time',
            field=models.TimeField(default=scheduler.models.Block.time_default),
        ),
        migrations.AddField(
            model_name='block',
            name='start_time',
            field=models.TimeField(default=scheduler.models.Block.time_default),
        ),
        migrations.AlterField(
            model_name='room',
            name='division',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='scheduler.Division'),
        ),
        migrations.RemoveField(
            model_name='block',
            name='name',
        ),
        migrations.RemoveField(
            model_name='block',
            name='next_block',
        ),
        migrations.RemoveField(
            model_name='block',
            name='paired_with',
        ),
        migrations.AlterUniqueTogether(
            name='block',
            unique_together={('block_id', 'day')},
        ),
    ]
