from __future__ import print_function
from collections import Counter
import re
from string import lowercase as LOWERCASE

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs

class RoomChecker(object):
    PATTERN = re.compile(r'([a-z-]+)-(\d+)\[([a-z]+)\]')

    def __init__(self, line):
        match = re.match(self.PATTERN, line)
        self.room = match.group(1)
        self.cipher = Counter(self.room.replace('-', '')).items()
        self.code = int(match.group(2))
        self.checksum = match.group(3)
        self.decoded = self._decode()

    def is_valid(self):
        top5 = sorted(self.cipher, key=lambda x: (-x[1], x[0]))[:5]
        computed = "".join([x[0] for x in top5])
        return computed == self.checksum

    def _decode(self):
        name = []
        for c in self.room:
            if c == '-':
                name.append(' ')
            else:
                new_index = (LOWERCASE.index(c) + self.code) % 26
                name.append(LOWERCASE[new_index])
        return "".join(name)


class Part1(object):
    def __init__(self):
        self.sum_of_ids = 0

    def check_room(self, line):
        room = RoomChecker(line)
        if room.is_valid():
            self.sum_of_ids += room.code
            
        if "north" in room.decoded or "pole" in room.decoded:
            print(room.code)

if __name__ == "__main__":
    room_codes = load()
    part1 = Part1()
    for room_code in room_codes:
        part1.check_room(room_code)
    print(part1.sum_of_ids)
