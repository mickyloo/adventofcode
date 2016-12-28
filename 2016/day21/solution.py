from __future__ import print_function
from functools import partial
from collections import deque
import re
import string

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs


class Part1(object):
    swap_p = re.compile(r'swap position (\d+) with position (\d+)')
    swap_l = re.compile(r'swap letter (\w) with letter (\w)')
    rotate_l = re.compile(r'rotate left (\d+) step')
    rotate_r = re.compile(r'rotate right (\d+) step')
    rotate_p = re.compile(r'rotate based on position of letter (\w)')
    reverse_p = re.compile(r'reverse positions (\d+) through (\d+)')
    move_p = re.compile(r'move position (\d+) to position (\d+)')

    def __init__(self, inputs, seed):
        self.password = seed
        self.funcs = []

        for line in inputs:
            if line.startswith('swap position'):
                x, y = re.match(self.swap_p, line).groups()
                kwargs = {'x': int(x), 'y': int(y)}
                self.funcs.append((self.swap_position, kwargs))

            elif line.startswith('swap letter'):
                x, y = re.match(self.swap_l, line).groups()
                kwargs = {'x': x, 'y': y}
                self.funcs.append((self.swap_letter, kwargs))

            elif line.startswith('rotate right'):
                x, = re.match(self.rotate_r, line).groups()
                kwargs = {'x': int(x)}
                self.funcs.append((self.rotate_right, kwargs))

            elif line.startswith('rotate left'):
                x, = re.match(self.rotate_l, line).groups()
                kwargs = {'x': int(x)}
                self.funcs.append((self.rotate_left, kwargs))

            elif line.startswith('rotate based'):
                x, = re.match(self.rotate_p, line).groups()
                kwargs = {'x': x}
                self.funcs.append((self.rotate_position, kwargs))

            elif line.startswith('reverse position'):
                x, y = re.match(self.reverse_p, line).groups()
                kwargs = {'x': int(x), 'y': int(y)}
                self.funcs.append((self.reverse_position, kwargs))

            elif line.startswith('move position'):
                x, y = re.match(self.move_p, line).groups()
                kwargs = {'x': int(x), 'y': int(y)}
                self.funcs.append((self.move_position, kwargs))

            else:
                raise ValueError("invalid line", line)

    def swap_position(self, x, y):
        s = list(self.password)
        s[x], s[y] = s[y], s[x]
        self.password = ''.join(s)

    def swap_letter(self, x, y):
        try:
            trans = string.maketrans(x+y, y+x)
        except AttributeError:
            trans = str.maketrans(x+y, y+x)
        self.password = self.password.translate(trans)

    def _rotate(self, x):
        d = deque(self.password)
        d.rotate(x)
        self.password = ''.join(d)

    def rotate_right(self, x):
        self._rotate(x)

    def rotate_left(self, x):
        self._rotate(-x)

    def rotate_position(self, x):
        index = self.password.index(x)
        index += 1 if index < 4 else 2
        self.rotate_right(index)

    def reverse_position(self, x, y):
        s = list(self.password)
        bound = x-1 if x-1 >= 0 else None
        s[x:y+1] = s[y:bound:-1]
        self.password = ''.join(s)

    def move_position(self, x, y):
        s = list(self.password)
        s.insert(y, s.pop(x))
        self.password = ''.join(s)
        2

    def execute(self):
        for func, kwargs in self.funcs:
            func(**kwargs)
        return self.password


class Part2(Part1):
    def __init__(self, inputs, seed):
        super(Part2, self).__init__(inputs, seed)
        self.funcs = self.funcs[::-1]

    def rotate_right(self, x):
        self._rotate(-x)

    def rotate_left(self, x):
        self._rotate(x)

    def move_position(self, x, y):
        super(Part2, self).move_position(y, x)

    def rotate_position(self, x):
        index = self.password.index(x)
        sub = 1 if index % 2 else 2
        if index % 2 or index == 0:
            previous_index = (index - sub) / 2
        else:
            previous_index = (index - sub + len(self.password)) / 2
        rotation = sub + previous_index
        self._rotate(-rotation)


if __name__ == "__main__":
    lines = load()
    part1 = Part1(lines, 'abcdefgh')
    scrambled = part1.execute()
    print(scrambled)

    part2 = Part2(lines, 'fbgdceah')
    print(part2.execute())