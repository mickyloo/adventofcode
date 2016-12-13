from __future__ import print_function
from collections import namedtuple
import re

MARKER_START = "("
MARKER_END = ")"
MARKER_PATTERN = re.compile(r'(\d+)x(\d+)')

def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


class Part1(object):
    def process_substring(self, substring):
        return len(substring)

    def decompress(self, input_string):
        i = 0
        while i < len(input_string):
            val = input_string[i]
            if val == MARKER_START:
                end = input_string.find(MARKER_END, i)
                marker = input_string[i:end]
                length, repeat = map(int, re.search(MARKER_PATTERN, marker).groups())
                i = end + 1
                substring = input_string[i:i+length]
                yield self.process_substring(substring) * repeat
                i += length
            else:
                yield 1
                i += 1

    def length(self, input_string):
        return sum(self.decompress(input_string))


class Part2(Part1):
    def process_substring(self, substring):
        return self.length(substring)


if __name__ == "__main__":
    input_string = load()
    part1 = Part1()
    print(part1.length(input_string))

    part2 = Part2()
    print(part2.length(input_string))
