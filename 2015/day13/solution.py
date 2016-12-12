from itertools import permutations
import re

people = set()
happiness = {}


def load():
    r = re.compile(r'(?P<P1>[A-Z])\w* would (?P<sign>gain|lose) (?P<happy>\d+) happiness units by sitting next to (?P<P2>[A-Z])\w*')
    with open('input.txt') as f:
        for line in f:
            match = re.match(r, line)
            if not match:
                continue

            people.add(match.group('P1'))
            pair = (match.group('P1'), match.group('P2'))
            happiness[pair] = int(match.group('happy')) * (1 if match.group('sign') == 'gain' else -1)
    return people, happiness


def score(order):
    order = list(order)
    order.append(order[0])
    pairs = list(zip(order, order[1:]))
    pairs += [(p[1], p[0]) for p in pairs]
    return sum([happiness.get(p, 0) for p in pairs])

if __name__ == "__main__":
    load()

    #part1
    happiness_scores = [score(p) for p in permutations(people)]
    print(max(happiness_scores))

    #part2
    people.add('X')
    happiness_scores = [score(p) for p in permutations(people)]
    print(max(happiness_scores))

