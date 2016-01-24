from itertools import chain, combinations

TOTAL = 150
CONTAINERS = []


def load():
    with open('input.txt') as f:
        for i in f.read().splitlines():
            CONTAINERS.append(int(i))


def part1():
    combos = []
    for i in range(4, len(CONTAINERS) + 1):
        combos.append(combinations(CONTAINERS, i))

    all_combinations = filter(lambda x: sum(x) == TOTAL, chain(*combos))
    valids = list(all_combinations)
    print(len(valids))


def part2():
    min_combinations = filter(lambda x: sum(x) == TOTAL, combinations(CONTAINERS, 4))
    valids = list(min_combinations)
    print(len(valids))


if __name__ == "__main__":
    load()
    part1()
    part2()
