from collections import namedtuple
import re
from itertools import product

Point = namedtuple('Point', ['x', 'y'])
Action = namedtuple('Action', ['action', 'start', 'end'])

ACTION_FUNCTIONS = {
    'turn off': lambda x: 0,
    'turn on': lambda x: 1,
    'toggle': lambda x: int(not x)
}

ACTION_FUNCTIONS_2 = {
    'turn off': lambda x: max(0, x-1),
    'turn on': lambda x: x + 1,
    'toggle': lambda x: x + 2
}

#inclusive range
irange = lambda start, end: range(start, end+1)


def load(funcs=None):
    if funcs is None:
        funcs = ACTION_FUNCTIONS

    actions = []
    r = re.compile(r'(?P<action>turn on|turn off|toggle) (?P<x0>\d+),(?P<y0>\d+) through (?P<x1>\d+),(?P<y1>\d+)')
    with open('input.txt') as f:
        for line in f:
            if line:
                match = re.match(r, line)
                a = Action(
                    action=funcs.get(match.group('action')),
                    start=Point(int(match.group('x0')), int(match.group('y0'))),
                    end=Point(int(match.group('x1')), int(match.group('y1')))
                )
                actions.append(a)

    return actions


def part_1():
    grid = {}
    for action in load():
        for light in product(irange(action.start.x, action.end.x), irange(action.start.y, action.end.y)):
            grid[light] = action.action(grid.get(light, 0))

    print(len([status for point, status in grid.items() if status]))


def part_2():
    grid = {}
    for action in load(funcs=ACTION_FUNCTIONS_2):
        for light in product(irange(action.start.x, action.end.x), irange(action.start.y, action.end.y)):
            grid[light] = action.action(grid.get(light, 0))

    print(sum([status for point, status in grid.items()]))


if __name__ == "__main__":
    #part_1()
    part_2()