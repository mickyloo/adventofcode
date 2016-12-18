from __future__ import print_function
from string import maketrans

def load():
    with open('input.txt') as f:
        inputs = f.read()
    return inputs


class Part1(object):
    def __init__(self, length, seed):
        self.length = length
        self.seed = seed
        self.inverter = maketrans('10', '01')

    def dragon_curve(self, a):
        b = a[::-1]
        return '{}0{}'.format(a, b.translate(self.inverter))

    def _checksum(self, s):
        chunks = [s[i:i+2] for i in range(0, len(s), 2)]
        checksum = ['1' if a == b else '0' for (a, b) in chunks]
        return ''.join(checksum)

    def fill(self):
        fill = self.seed
        while len(fill) < self.length:
            fill = self.dragon_curve(fill)
        return fill[:self.length]

    def checksum(self):
        fill = self.fill()
        checksum = self._checksum(fill)
        while len(checksum) % 2 == 0:
            checksum = self._checksum(checksum)
        return checksum


class Part2(Part1):
    def _checksum(self, s):
        def chunks(s):
            for i in range(0, len(s), 2):
                a, b = s[i:i+2]
                yield '1' if a == b else '0'

        checksum = chunks(s)
        return ''.join(checksum)


if __name__ == "__main__":
    seed = load()
    part1 = Part1(272, seed)
    print(part1.checksum())

    part2 = Part2(35651584, seed)
    print(part2.checksum())
