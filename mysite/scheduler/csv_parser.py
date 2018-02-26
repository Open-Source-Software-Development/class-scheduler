import csv
import models

def create_models_for_type(filename, parse_mode):
    with open(filename) as f:
        reader = csv.reader(f)
        header = None
        file_format = None
        for row in reader:
            if file_format is None:
                file_format = row
            elif header is None:
                header = row
                parse_mode.set_header(header)
            else:
                model = parse_mode.add_row(row)
    return parse_mode.get_models()

class ParseMode:

    def set_header(self, header):
        pass

    def add_row(self, row):
        raise NotImplementedError()

    def get_models(self):
        raise NotImplementedError()

class BlockParser(ParseMode):

    def __init__(self):
        self.block_count = 0

    def add_row(self, row):
        self.block_count += 1

    def get_models(self):
        blocks = {}
        # Count backwards so we can set up the next_block associations
        old_block_b = None
        old_block_a = None
        for i in range(self.block_count, 0, -1):
            block_b = models.Block(name=("{}B".format(i)), next_block=old_block_b, paired_with=None)
            block_a = models.Block(name=("{}A".format(i)), next_block=old_block_a, pared_with=block_b)
            old_block_b = block_b
            old_alock_a = alock_a
        return blocks.values()

class RoomParser(ParseMode):

    def __init__(self):
        self.rooms = []
        self.room_types = {}

    def add_row(self, row):
        name = "{}-{}".format(row["building"], row["capacity"])
        room_type = self._get_room_type(row["type"])
        division = self._get_division(row["division"])
        model = models.Room(name=name, room_type=room_type, division=division)
        self.rooms.add(model)

    def get_models(self):
        return list(self.rooms) + list(self.room_types.values())

    def _get_room_type(self, name):
        if name in self.room_types:
            return self.room_types[name]
        try:
            return RoomType.objects.get(name=name)
        except RoomType.DoesNotExist:
            pass
        room_type = models.RoomType(name=name)
        self.room_types[name] = room_type
        return room_type

    def _get_division(self, name):
        try:
            return Division.objects.get(name=name)
        except Division.DoesNotExist:
            return None
