from __future__ import print_function
from collections import namedtuple
import re

Action = namedtuple('Action', ['direction', 'distance'])

RIGHT = 'R'
LEFT = 'L'

def load():
    pattern = re.compile(r"(R|L)(\d+)")
    actions = []
    with open('input.txt') as f:
        for s in f.read().split(','):
            match = re.search(pattern, s)
            a = Action(
                direction=match.group(1),
                distance=int(match.group(2))
            )
            actions.append(a)
    return actions


class Part1(object):
    MASKS = [(0,1), (1,0), (0,-1), (-1,0)]

    def __init__(self):
        self.displacement = (0,0) # x,y coordinate displacement from origin
        self.mask_index = 0 # pointing north to start

    def _turn(self, direction):
        turn = 1 if direction == RIGHT else -1
        self.mask_index = (self.mask_index + turn) % 4

    def _move(self, distance):
        mask = Part1.MASKS[self.mask_index]
        x = mask[0] * distance + self.displacement[0]
        y = mask[1] * distance + self.displacement[1]
        self.displacement = (x, y)

    def do_action(self, action):
        self._turn(action.direction)
        self._move(action.distance)

    @property
    def shortest_path(self):
        return abs(self.displacement[0]) + abs(self.displacement[1])


class Part2(Part1):
    """
    Part 2 tracks the location we have already visited.
    """
    def __init__(self):
        super(Part2, self).__init__()
        self.found = False
        self.visited = []

    def _move_one(self):
        mask = Part2.MASKS[self.mask_index]
        if not self.found:
            x = mask[0] + self.displacement[0]
            y = mask[1] + self.displacement[1]
            new_location = (x, y)
            if new_location in self.visited:
                self.found = True
            else:
                self.visited.append(new_location)
            self.displacement = new_location

    def _move(self, distance):
        for _ in range(0, distance):
            self._move_one()


if __name__ == "__main__":
    actions = load()
    part1 = Part1()
    for a in actions:
        part1.do_action(a)
    print(part1.shortest_path)

    part2 = Part2()
    for a in actions:
        part2.do_action(a)
    print(part2.shortest_path)

