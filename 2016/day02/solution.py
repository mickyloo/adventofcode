from __future__ import print_function

DIRECTIONS = {
    "U": (0, -1),
    "D": (0, 1),
    "L": (-1, 0),
    "R": (1, 0)
}

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs

class Part1(object):
    KEYPAD = {
        (0, 0): "1",
        (1, 0): "2",
        (2, 0): "3",
        (0, 1): "4",
        (1, 1): "5",
        (2, 1): "6",
        (0, 2): "7",
        (1, 2): "8",
        (2, 2): "9"
    }
    
    def __init__(self):
        self.position = (1, 1) # start at key 5
        self._code = []

    def move(self, direction):
        change = DIRECTIONS[direction]
        new_position = self.position[0] + change[0], self.position[1] + change[1]
        if new_position in self.KEYPAD:
            self.position = new_position

    def do_line(self, line):
        for direction in line:
            self.move(direction)
        self._code.append(self.key)

    @property
    def key(self):
        return self.KEYPAD.get(self.position)

    @property
    def code(self):
        return "".join(self._code)


class Part2(Part1):
    KEYPAD = {
        (2, 0): "1",
        (1, 1): "2",
        (2, 1): "3",
        (3, 1): "4",
        (0, 2): "5",
        (1, 2): "6",
        (2, 2): "7",
        (3, 2): "8",
        (4, 2): "9",
        (1, 3): "A",
        (2, 3): "B",
        (3, 3): "C",
        (2, 4): "D",
    }

    def __init__(self):
        super(Part2, self).__init__()
        self.position = (0, 2) # start at key 5


if __name__ == "__main__":
    instructions = load()
    part1 = Part1()
    for i in instructions:
        part1.do_line(i)
    print(part1.code)

    part2 = Part2()
    for i in instructions:
        part2.do_line(i)
    print(part2.code)

