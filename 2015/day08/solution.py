from ast import literal_eval
import re

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs


def esc(s):
    return '"{}"'.format(re.sub(r'([\"\\])', r'\\\1', s))


def part1(strings):
    return sum([len(s) for s in strings]) - sum([len(literal_eval(s)) for s in strings])


def part2(strings):
    return sum([len(esc(s)) for s in strings]) - sum([len(s) for s in strings])

if __name__ == "__main__":
    strings = load()

    print(part1(strings))
    print(part2(strings))
