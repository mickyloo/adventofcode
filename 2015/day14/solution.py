from itertools import cycle, islice
from collections import Counter
import re

RACE = 2503
deers = {
}

def load():
    r = re.compile(r'(?P<deer>\w*) can fly (?P<speed>\d+) km/s for (?P<duration>\d+) seconds, but then must rest for (?P<rest>\d+) seconds')
    with open('input.txt') as f:
        for line in f:
            match = re.match(r, line)
            if not match:
                continue

            movement = [int(match.group('speed'))] * int(match.group('duration')) + [0] * int(match.group('rest'))
            deers[match.group('deer')] = cycle(movement)


def part1():
    distances = []
    for deer, movement in deers.items():
        distances.append(sum(islice(movement, RACE)))

    return max(distances)


def part2():
    points = Counter()
    distances = Counter()
    for _ in range(0, RACE):
        leader = 0
        for deer, movement in deers.items():
            distances[deer] += next(movement)
            leader = max(leader, distances[deer])

        for deer, dist in distances.items():
            points[deer] += int(dist == leader)

    return max(points.values())


if __name__ == "__main__":
    load()
    print(part1())

    load()
    print(part2())