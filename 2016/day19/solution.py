from __future__ import print_function
from collections import deque

def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return int(inputs)

class Part1(object):
    def __init__(self, num_elves):
        self.elves = list(range(1, num_elves + 1))

    def reduce_to_one(self, elves=None):
        if elves is None:
            elves = self.elves

        num_elves = len(elves)
        if num_elves == 1:
            return elves[0]
        else:
            new_elves = elves[::2]
            if num_elves % 2 == 1:
                new_elves.insert(0, new_elves.pop())

            return self.reduce_to_one(new_elves)

class Part2(object):
    def __init__(self, num_elves):
        self.elves = deque(range(1, num_elves + 1))

    def reduce_to_one(self):
        num_elves = len(self.elves)
        while num_elves > 1:
            steal_from = num_elves // 2
            del self.elves[steal_from]
            self.elves.rotate(-1)
            num_elves -= 1

        return self.elves[0]


if __name__ == "__main__":
    elves = load()
    part1 = Part1(elves)
    print(part1.reduce_to_one())

    part2 = Part2(elves)
    print(part2.reduce_to_one())
