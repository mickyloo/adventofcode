from __future__ import print_function
from collections import Counter

def load():
    with open('input.txt') as f:
        lines = f.read().splitlines()
    return zip(*lines)

class Part1(object):
    def __init__(self, columns):
        self.columns = columns
    
    def _error_correction(self, column):
        counter = Counter(column)
        return counter.most_common(1)

    def get_message(self):
        message = []
        for column in self.columns:
            corrected = self._error_correction(column)[0]
            message.append(corrected[0])
        return "".join(message)

class Part2(Part1):
    def _error_correction(self, column):
        counter = Counter(column)
        return counter.most_common()[-1]

if __name__ == "__main__":
    columns = load()
    part1 = Part1(columns)
    print(part1.get_message())

    part2 = Part2(columns)
    print(part2.get_message())

