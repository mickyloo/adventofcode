from __future__ import print_function
import re

DISC_RE = r'Disc #\d has (\d+) positions; at time=0, it is at position (\d+).'

def disc_checker(positions, starting, open_pos=0):
    def inner(t):
        pos = (starting + t) % positions
        return pos == open_pos
    return inner


class Part1(object):
    def __init__(self, lines):
        self.discs = []
        for line in lines:
            match = re.match(DISC_RE, line)
            positions, starting = map(int, match.groups())
            self.discs.append(disc_checker(positions, starting))

    def check(self, time):
        #reaches first disc at t+1
        checks = [checkfunc(time+i+1) for (i, checkfunc) in enumerate(self.discs)]
        return checks

    def find_min_time(self):
        time = 0
        while True:
            checks = self.check(time)
            if all(checks):
                break
            time += 1
        return time


class Part2(Part1):
    def add_disc(self, positions, starting):
        self.discs.append(disc_checker(positions, starting))


def load():
    with open('input.txt') as f:
        lines = f.read().splitlines()
    return lines


if __name__ == "__main__":
    lines = load()
    part1 = Part1(lines)
    print(part1.find_min_time())

    part2 = Part2(lines)
    part2.add_disc(11, 0)
    print(part2.find_min_time())
