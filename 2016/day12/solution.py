from __future__ import print_function
from collections import defaultdict
import re

RE_CPY = re.compile(r'cpy (\d+|[a-d]) ([a-d])')
RE_JNZ = re.compile(r'jnz (\d+|[a-d]) (-?\d+)')
RE_INC_DEC = re.compile(r'(?:inc|dec) ([a-d])')

class Part1(object):
    REGISTERS = ('a', 'b', 'c', 'd')

    def __init__(self, instructions):
        self.register = dict((x, 0) for x in self.REGISTERS)
        self.instructions = instructions

    def execute(self):
        i = 0
        while True:
            jump = False
            try:
                instr = self.instructions[i]
            except IndexError:
                break

            command = instr[:3]
            if command == "cpy":
                val, reg = re.match(RE_CPY, instr).groups()
                if val in self.REGISTERS:
                    self.register[reg] = self.register[val]
                else:
                    self.register[reg] = int(val)
            elif command == "inc":
                reg, = re.match(RE_INC_DEC, instr).groups()
                self.register[reg] += 1
            elif command == "dec":
                reg, = re.match(RE_INC_DEC, instr).groups()
                self.register[reg] -= 1
            elif command == "jnz":
                val, move = re.match(RE_JNZ, instr).groups()
                try:
                    jump = bool(self.register[val])
                except KeyError:
                    jump = bool(int(val))

                if jump:
                    i += int(move)
            else:
                raise ValueError("unknown command")

            if not jump:
                i += 1

class Part2(Part1):
    def __init__(self, *args, **kwargs):
        super(Part2, self).__init__(*args, *kwargs)
        self.register['c'] = 1

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs


if __name__ == "__main__":
    inputs = load()
    part1 = Part1(inputs)
    part1.execute()
    print(part1.register.get('a'))

    part2 = Part2(inputs)
    part2.execute()
    print(part2.register.get('a'))
