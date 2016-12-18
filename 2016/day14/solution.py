from __future__ import print_function
from hashlib import md5
import re

CHECK = re.compile(r'(.)\1\1')

def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


class Part1(object):
    def __init__(self, salt):
        self.salt = salt
        self.cache = {}
    
    def hash(self, i):
        s = '{}{}'.format(self.salt, i)
        try:
            return self.cache[s]
        except KeyError:
            h = md5(s.encode()).hexdigest()
            self.cache[s] = h
            return h

    def gen(self):
        i = 0
        while True:
            h = self.hash(i)
            check = re.search(CHECK, h)
            if check:
                check2 = re.compile(check.group(1) * 5)
                for j in range(i+1, i+1001):
                    h = self.hash(j)
                    if re.search(check2, h):
                        yield i
                        break
            i += 1

    def generate_keys(self, count=64):
        generator = self.gen()
        for i in range(count):
            key = next(generator)
            print(i, key)
            yield key


class Part2(Part1):
    def hash(self, i):
        s = '{}{}'.format(self.salt, i)
        try:
            return self.cache[s]
        except KeyError:
            for _ in range(2017):
                h = md5(s.encode()).hexdigest()
                s = h
            self.cache[s] = h
            return h


if __name__ == "__main__":
    salt = load()
    part1 = Part1(salt)
    keys = list(part1.generate_keys(count=64))
    print(keys[-1])

    part2 = Part2(salt)
    keys = list(part2.generate_keys(count=64))
    print(keys[-1])