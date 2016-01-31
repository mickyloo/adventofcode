from math import sqrt, ceil
from itertools import count


def load():
    with open('input.txt') as f:
        n = f.read().strip()
    return int(n)


def factors(n):
    facts = set()
    step = 2 if n % 2 else 1
    for i in range(1, ceil(sqrt(n)) + 1, step):
        q, r = divmod(n, i)
        if r == 0:
            facts.add(int(q))
            facts.add(int(n/q))
    return facts


def part1(num_presents):
    """
    Rough lower bound on the start value of house
    x * ln(x) = num_presents/10
    """
    for house in count(start=271727):
        elves = list(factors(house))
        presents = sum(elves) * 10
        if presents >= num_presents:
            print(house)
            break
    print()


def part2(num_presents):
    """
    Rough lower bound on the start value of house
    x * ln(x) = num_presents/11
    """    
    for house in count(start=248778):
        elves = list(filter(lambda x: x * 50 > house, factors(house)))
        presents = sum(elves) * 11
        if presents >= num_presents:
            print(house)
            break
    print()


if __name__ == "__main__":
    num_presents = load()
    #part1(num_presents)
    part2(num_presents)