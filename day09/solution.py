from itertools import permutations
import re

cities = set()
paths = {}


def load():
    r = re.compile(r'(?P<start>\w+) to (?P<end>\w+) = (?P<dist>\d+)')
    with open('input.txt') as f:
        for line in f:
            if line:
                match = re.match(r, line)
                cities.add(match.group('start'))
                cities.add(match.group('end'))
                paths[(match.group('start'), match.group('end'))] = int(match.group('dist'))
                paths[(match.group('end'), match.group('start'))] = int(match.group('dist'))
    return cities, paths


def distance(path):
    return sum([paths[p] for p in zip(path, path[1:])])


if __name__ == "__main__":
    load()
    distances = [distance(p) for p in permutations(cities)]

    print(min(distances))
    print(max(distances))