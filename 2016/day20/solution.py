from __future__ import print_function
from collections import namedtuple
from itertools import filterfalse


Block = namedtuple('Block', ['lower', 'upper'])

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs

class Part1(object):
    IP_MAX = 4294967295

    def __init__(self, lines):
        self.ip_blocks = []
        for line in lines:
            lower, upper = map(int, line.split('-'))
            self.ip_blocks.append(Block(lower, upper))
        self.ip_blocks.sort(key=lambda ip: ip.lower)

    def find_unblocked(self):
        i = 0
        while i <= self.IP_MAX:
            blocked = False
            for j, block in enumerate(self.ip_blocks):
                if block.lower <= i <= block.upper:
                    del self.ip_blocks[j]
                    i = block.upper
                    blocked = True
                    break

            if not blocked:
                yield i
            else:
                i += 1


class Part2(Part1):
    def _merge_ip_ranges(self):
        ips = []
        for ip in self.ip_blocks:
            ips.append((ip.lower, 'lower'))
            ips.append((ip.upper, 'upper'))
        ips.sort(key=lambda x: x[0])

        merged = []
        groups = groupby(ips, key=lambda x: x[1])
        for _, lower_group in groups:
            lower = min([x[0] for x in lower_group])
            _, upper_group = next(groups)
            upper = max([x[0] for x in upper_group])
            merged.append(Block(lower, upper))

        return merged


    def _merge(self, ip_blocks):
        merged = []
        sorted_ips = sorted(ip_blocks, key=lambda x: x[0])
        i = 0
        while i < len(sorted_ips):
            block = sorted_ips[i]
            for next_block in sorted_ips[i+1:]:
                overlap = block.lower <= next_block.upper and block.upper >= next_block.lower
                if overlap:
                    block = Block(min(block.lower, next_block.lower), max(block.upper, next_block.upper))
                    i += 1
                else:
                    break

            merged.append(block)
            i += 1

        if len(merged) < len(ip_blocks):
            return self._merge(merged)
        else:
            return ip_blocks


    def num_allowed(self):
        blocks = self._merge(self.ip_blocks)
        total = blocks[0].lower - 0
        for i, block in enumerate(blocks):
            try:
                if block.upper < blocks[i+1].lower:
                    total += blocks[i+1].lower - block.upper - 1
            except IndexError:
                total += self.IP_MAX - block.upper
        return total


if __name__ == "__main__":
    lines = load()
    part1 = Part1(lines)
    print(next(part1.find_unblocked()))

    part2 = Part2(lines)
    print(part2.num_allowed())
