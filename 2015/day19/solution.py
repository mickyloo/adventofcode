import re
from collections import namedtuple

Translate = namedtuple('Translate', ['key', 'replacement'])

TRANS = []
LEN_INPUT = 506

def load():
    with open('input.txt') as f:
        r = re.compile(r'(\w+) => (\w+)')
        for line in f.read().splitlines():
            match = re.match(r, line)
            if not match:
                continue
            TRANS.append(Translate(match.group(1), match.group(2)))

    with open('input2.txt') as f:
        INPUT = f.read().strip()

    return TRANS, INPUT


def part1(s):
    molecules = set()
    for key, replacement in TRANS:
        parts = s.split(key)
        for i in range(1, len(parts)):
            trans = "{}{}{}".format(key.join(parts[:i]), replacement, key.join(parts[i:]))
            molecules.add(trans)
    return molecules


min_depth = 9999
DEPTHS = {'e': 9999}


def reverse_molecule(s, depth=0):
    if s in DEPTHS and (depth >= DEPTHS[s] or depth >= DEPTHS['e']):
        # repeat loop
        return False
    else:
        DEPTHS[s] = depth

    if s == 'e':
        return True

    for from_s, to_s in XLATE.items():
        if from_s in s:
            replaced = s.replace(from_s, to_s, 1)
            res = reverse_molecule(replaced, depth + 1)
            if res:
                print("new min path", depth+1)


def part2(s):
    reverse_molecule(s)


if __name__ == "__main__":
    TRANS, INPUT = load()
    XLATE = dict([(v, k) for k, v in TRANS])

    print(len(part1(INPUT)))
    print(len(INPUT))
    part2(INPUT)
    print(DEPTHS['e'])