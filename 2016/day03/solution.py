from __future__ import print_function
from collections import Counter
from itertools import izip_longest, chain
import re


def grouper(iterable, n, fillvalue=None):
    "Collect data into fixed-length chunks or blocks"
    # grouper('ABCDEFG', 3, 'x') --> ABC DEF Gxx
    args = [iter(iterable)] * n
    return izip_longest(fillvalue=fillvalue, *args)

def load():
    pattern = re.compile(r'(\d+)\s+(\d+)\s+(\d+)')
    edges = []
    c1 = []
    c2 = []
    c3 = []
    with open('input.txt') as f:
        for line in f.read().splitlines():
            match = re.search(pattern, line)
            e1, e2, e3 = match.groups()
            edges.append((int(e1), int(e2), int(e3)))

    columns = zip(*edges)
    single_column = chain(*columns)
    edges2 = grouper(single_column, 3)
    
    return edges, edges2


class Triangle(object):
    def __init__(self, *args):
        self.edges = args

    def _check(self, e1, e2, e3):
        return (e1 + e2) > e3

    def is_valid(self):
        """
        combinations
            e1 e2 > e3
            e1 e3 > e2
            e2 e3 > e1
        """
        return all([
            self._check(self.edges[0], self.edges[1], self.edges[2]),
            self._check(self.edges[0], self.edges[2], self.edges[1]),
            self._check(self.edges[1], self.edges[2], self.edges[0])
        ])

class Part1(object):
    def __init__(self):
        self.counter = Counter()

    def check_edges(self, edges):
        triangle = Triangle(*edges)
        self.counter[triangle.is_valid()] += 1

    def num_valid(self):
        return self.counter[True]

if __name__ == "__main__":
    edges, edges2 = load()
    part1 = Part1()
    for e in edges:
        part1.check_edges(e)
    print(part1.num_valid())

    part2 = Part1()
    for e in edges2:
        part2.check_edges(e)
    print(part2.num_valid())

