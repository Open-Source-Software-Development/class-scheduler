import csv
import models


def parse_csv(filename):
    with open(filename) as f:
        reader = csv.reader(f)
        file_format = None
        header = None
        parse_mode = None
        for row in reader:
            if file_format is None:
                file_format = row
            elif header is None:
                header = row
                parse_mode = ParseMode.for_header(header)
            else:
                model = parse_mode.add_row(row)
    return parse_mode.get_models()


class ParseMode:

    modes = []

    @classmethod
    def register(cls, mode):
        cls.modes.append(mode)
        return mode

    def __init__(self, header):
        data = {}
        for k in range(len(header)):
            v = header[k]
            data[v] = k
        self.header = data

    @classmethod
    def can_parse(cls, header):
        for i in cls.columns:
            if i not in header:
                return False
        return True

    @classmethod
    def for_header(cls, header):
        for mode in cls.modes:
            if mode.can_parse(header):
                return mode(header)
        raise IndexError("couldn't determine what type of CSV this is - check the header")

    def add_row(self, row):
        return self.add_row_impl(self._parse_row(row))

    def get_models(self):
        raise NotImplementedError()

    def add_row_impl(self, row):
        raise NotImplementedError()

    def _parse_row(self, row):
        parsed = {}
        for k, v in self.header.items():
            parsed[k] = row[v] if v < len(row) else ""
        return parsed

@ParseMode.register
class BlockParser(ParseMode):

    columns = ("day", "start", "end")

    def __init__(self, header):
        parse_mode.set_header(header)
        self.block_count = 0

    def add_row_impl(self, row):
        self.block_count += 1

    def get_models(self):
        blocks = []
        # Count backwards so we can set up the next_block associations
        old_block_b = None
        old_block_a = None
        for i in range(self.block_count, 0, -1):
            block_b = models.Block(name=("{}B".format(i)), next_block=old_block_b, paired_with=None)
            block_a = models.Block(name=("{}A".format(i)), next_block=old_block_a, pared_with=block_b)
            old_block_b = block_b
            old_block_a = block_a
            blocks.append(block_b)
            blocks.append(block_a)
        return {models.Block: blocks}

@ParseMode.register
class RoomParser(ParseMode):

    columns = ("building", "room", "type", "division")

    def __init__(self, header):
        super().__init__(header)
        self.rooms = []
        self.room_types = {}

    def add_row_impl(self, row):
        name = "{}-{}".format(row["building"], row["room"])
        room_type = self._get_room_type(row["type"])
        division = self._get_division(row["division"])
        model = models.Room(name=name, room_type=room_type, division=division)
        self.rooms.append(model)

    def get_models(self):
        return {models.Room: list(self.rooms), models.RoomType: list(self.room_types.values())}

    def _get_room_type(self, name):
        if name in self.room_types:
            return self.room_types[name]
        try:
            return models.RoomType.objects.get(name=name)
        except models.RoomType.DoesNotExist:
            pass
        room_type = models.RoomType(name=name)
        self.room_types[name] = room_type
        return room_type

    def _get_division(self, name):
        try:
            return models.Division.objects.get(name=name)
        except models.Division.DoesNotExist:
            return None
