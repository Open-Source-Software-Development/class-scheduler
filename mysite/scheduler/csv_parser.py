import csv
from .models import *
from django.core.exceptions import *


def parse(csv_file, filetype):
    """Gets an appropriate CSV parser and has it parse this file."""
    parser = Parser.for_name(filetype)
    reader = csv.reader([line.decode("utf-8") for line in csv_file])
    for row in reader:
        if len(row) > 0:
            parser.parse(row)
    return parser.get_models()

def get_named(model, name):
    """Gets a model with a given name field. Raises a ParseError if no
    such model can be found.

    Args:
        model: the model class to search for an instance of.
        name: the desired name.
    """
    try:
        return model.objects.get(name = name)
    except model.DoesNotExist:
        raise ParseError("No {} named {}".format(model, name))


class ParseError(Exception):
    def __init__(self, message):
        super(ParseError, self).__init__(message)


class Parser:

    # All the parsers that have been registered.
    parsers = {}

    @classmethod
    def register(cls, modelcls, name, *args):
        """Automatically registers this parser. Decorate Parser
        subclasses with this so parse() can find them."""
        def callback(parsecls):
            def callback2():
                return parsecls(modelcls, args)
            cls.parsers[name] = callback2
            return parsecls
        return callback

    @classmethod
    def for_name(cls, name):
        """Get a parser for a given type of file."""
        return cls.parsers[name]()

    def __init__(self, model, fields):
        self.model = model
        self.fields = fields
        self.models = []

    def parse(self, row):
        """Parse a row into a model. That model will be included when
        get_models() is called."""
        model_arguments = {}
        for i in range(len(self.fields)):
            v = self.convert(i, row[i])
            model_arguments[self.fields[i]] = v
        model, created = self.model.objects.get_or_create(**model_arguments)
        if not created:
            raise ParseError("Duplicate {}: {}".format(self.model, model))
        self.models.append(model)

    def get_models(self):
        """Return all the models this parser has parsed."""
        return self.models

@Parser.register(Professor, "professors", "division", "first", "last")
class ProfessorParser(Parser):
    def convert(self, index, value):
        if index == 0:
            return get_named(Division, value)
        return value


@Parser.register(Room, "rooms", "building", "room_number", "room_capacity", "room_type", "division", "subject", "course_number")
class RoomParser(Parser):
    def convert(self, index, value):
        if index == 3: #room type
            return get_named(RoomType, value)
        if index == 4: #division
            return get_named(Division, value)
        return value

@Parser.register(Course, "courses", "division", "program", "style", "title", "ins_method", "section_capacity")
class Courses(Parser):
    def convert(self, index, value):
        if index == 0:
            return get_named(Division, value)
        return value

@Parser.register(Division, "divisions", "name")
class DivisionParser(Parser):
    def convert(self, index, value):
        return value


@Parser.register(Qualification, "qualifications", "course", "professor")
class QualificationParser(Parser):
    def convert(self, index, value):
        if index == 0:
            program, number = value.split("-")
            try:
                return Course.objects.get(program = program, style = number)
            except Course.DoesNotExist:
                raise ParseError("No course named {}".format(value))
        if index == 1:
            names = value.split()
            first = " ".join(names[0:len(names) - 1])
            last = names[-1]
            try:
                return Professor.objects.get(first = first, last = last)
            except Professor.DoesNotExist:
                raise ParseError("No professor named {}".format(value))
        return value #future-proofing



@Parser.register(Block, "blocks", "block_id", "day", "start_time", "end_time")
class BlockParser(Parser):
    def convert(self, index, value):
        return value
