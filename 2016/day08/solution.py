from __future__ import print_function
from itertools import product, repeat
from collections import deque, Counter
import re


class Screen(object):
    ROWS = 6
    COLS = 50

    def __init__(self):
        self.screen = {}
        self._set_pixels(self.ROWS, self.COLS)

    def _set_pixels(self, rows, cols, state=False):
        for pixel in product(range(rows), range(cols)):
            self.screen[pixel] = state

    def _shift(self, pixels, num):
        dq = deque(pixels)
        dq.rotate(num)
        return list(dq)

    def turn_on(self, rows, cols):
        self._set_pixels(rows, cols, state=True)

    def rotate_row(self, row, num):
        coords = zip(repeat(row), range(self.COLS))
        values = [self.screen[xy] for xy in coords]
        shifted = self._shift(values, num)
        for col, value in enumerate(shifted):
            self.screen[(row, col)] = value

    def rotate_col(self, col, num):
        coords = zip(range(self.ROWS), repeat(col))
        values = [self.screen[xy] for xy in coords]
        shifted = self._shift(values, num)
        for row, value in enumerate(shifted):
            self.screen[(row, col)] = value

    def print_me(self):
        for row in range(self.ROWS):
            vals = []
            for col in range(self.COLS):
                val = "#" if self.screen[(row, col)] else "."
                vals.append(val)
            print("".join(vals))
        print("")

    def count(self, state=True):
        state_counter = Counter(self.screen.values())
        return state_counter[state]

class Part1(object):
    def __init__(self):
        self.screen = Screen()

    def do_action(self, action):
        if action.startswith("rect"):
            params = re.search("(?P<col>\d+)x(?P<row>\d+)", action)
            col, row = params.groups()
            self.screen.turn_on(int(row), int(col))
        elif action.startswith("rotate"):
            params = re.search("(column|row).*=(?P<index>\d+) by (?P<num>\d+)", action)
            rotate_type, index, num = params.groups() 
            if rotate_type == "column":
                func = self.screen.rotate_col
            elif rotate_type == "row":
                func = self.screen.rotate_row
            else: 
                raise ValueError("unsupported rotate action")
            
            func(int(index), int(num))
        else:
            raise ValueError("unsupported action")

    def num_lit(self):
        self.screen.print_me()
        return self.screen.count(True)
        
def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs

if __name__ == "__main__":
    actions = load()
    part1 = Part1()
    for action in actions:
        part1.do_action(action)
    print(part1.num_lit())

