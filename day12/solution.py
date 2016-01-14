import re
import json


def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


def part1(s):
    ints = re.findall(r'(-?\d+)', s)
    return sum(map(int, ints))


def scrub(iterable):
    if isinstance(iterable, list):
        for i, val in enumerate(iterable):
            iterable[i] = scrub(val)
    elif isinstance(iterable, dict):
        if 'red' in iterable.values():
            return ''
        else:
            for key, val in iterable.items():
                iterable[key] = scrub(val)

    return iterable


def part2(s):
    j = json.loads(s)
    scrub(j)
    return part1(json.dumps(j))

if __name__ == "__main__":
    inputs = load()
    print(part1(inputs))
    print(part2(inputs))
    
    

