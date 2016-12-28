from __future__ import print_function
from collections import namedtuple
from itertools import permutations
import re

Node = namedtuple('Node', ['x', 'y', 'size', 'used', 'avail'])
LS_RE = re.compile(r'.+node\-x(\d+)\-y(\d+)\s+(\d+)T\s+(\d+)T\s+(\d+)T\s+\d+')

def load():
    with open('input.txt') as f:
        inputs = f.read().strip().splitlines()

    return inputs[2:]


class Part1(object):
    def __init__(self, lines):
        self.nodes = {}
        for line in lines:
            match = map(int, re.match(LS_RE, line).groups())
            node = Node(*match)
            self.nodes[(node.x, node.y)] = node

    def viable_pairs(self):
        for a, b in permutations(self.nodes.values(), 2):
            conditions = (
                a.used > 0,
                (a.x, a.y) != (b.x, b.y),
                a.used <= b.avail,
            )
            if all(conditions):
                yield (a, b)


    def num_viable_pairs(self):
        return sum(1 for _ in self.viable_pairs())

if __name__ == "__main__":
    lines = load()
    part1 = Part1(lines)
    print(part1.num_viable_pairs())

