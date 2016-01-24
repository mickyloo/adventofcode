import re
from operator import lt, eq, gt

AUNTS = []
TICKER = {
    'children': 3,
    'cats': 7,
    'samoyeds': 2,
    'pomeranians': 3,
    'akitas': 0,
    'vizslas': 0,
    'goldfish': 5,
    'trees': 3,
    'cars': 2,
    'perfumes': 1,
}


def load():
    r = re.compile(r'Sue (\d+): (\w+): (\d+), (\w+): (\d+), (\w+): (\d+)')
    with open('input.txt') as f:
        for line in f:
            match = re.match(r, line)
            if not match:
                continue
            AUNTS.append({
                match.group(2): int(match.group(3)),
                match.group(4): int(match.group(5)),
                match.group(6): int(match.group(7)),
            })


def score(aunt):
    return sum([eq(TICKER[k], v) for k, v in aunt.items()])


def score2(aunt):
    """
    the cats and trees readings indicates that there are greater than that many
    the pomeranians and goldfish readings indicate that there are fewer than that many
    """
    ops = {
        'cats': gt,
        'trees': gt,
        'pomeranians': lt,
        'goldfish': lt,
    }
    return sum([ops.get(k, eq)(v, TICKER[k]) for k, v in aunt.items()])


def aunt_finder(score_function):
    aunts = [score_function(a) for a in AUNTS]
    best_fit = max(aunts)
    return aunts.index(best_fit) + 1


if __name__ == "__main__":
    load()

    print(aunt_finder(score))
    print(aunt_finder(score2))
