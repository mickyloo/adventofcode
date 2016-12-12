from string import ascii_lowercase as lc
from itertools import filterfalse


def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


STRAIGHTS = list(map(lambda x: ''.join(x), zip(lc, lc[1:], lc[2:])))
INVALIDS = ['i', 'o', 'l']
PAIRS = list(map(lambda x: ''.join(x), zip(lc, lc)))


class PasswordIterator(object):
    get_char = lambda x: lc[x]

    def __init__(self, password):
        self.password = [lc.index(c) for c in password]

    def increment(self, index):
        self.password[index] += 1
        if self.password[index] > 25:
            self.password[index] = 0
            self.increment(index-1)

    def __iter__(self):
        return self

    def __next__(self):
        self.increment(-1)
        return self.value()

    def value(self):
        return ''.join(map(PasswordIterator.get_char, self.password))


if __name__ == "__main__":
    password = PasswordIterator(load())

    password = filter(lambda x: any([c in x for c in STRAIGHTS]), password)
    password = filterfalse(lambda x: any([c in x for c in INVALIDS]), password)
    password = filter(lambda x: [c in x for c in PAIRS].count(True) > 1, password)

    print(next(password))
    print(next(password))

