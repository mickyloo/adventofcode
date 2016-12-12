from __future__ import print_function
from collections import Counter
import re

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs


class IPV7(object):
    ABBA = re.compile(r'([a-z])([a-z])\2\1')
    ABA = re.compile(r'(?=([a-z])([a-z])\1)')
    HYPERNET = re.compile(r'\[([a-z]*)\]')

    def __init__(self, ipv7):
        self.supernets = re.sub(IPV7.HYPERNET, ' ', ipv7).split(' ')
        self.hypernets = re.findall(IPV7.HYPERNET, ipv7)

    def _is_abba(self, s):
        abbas = re.findall(IPV7.ABBA, s)
        valid_abbas = [ab for ab in abbas if ab[0] != ab[1]]
        return bool(valid_abbas)

    def is_tls(self):
        abba = any([self._is_abba(s) for s in self.supernets])
        abba_square = any([self._is_abba(s) for s in self.hypernets])
        return abba and not abba_square

    def is_ssl(self):
        for supernet in self.supernets:
            for aba in re.findall(IPV7.ABA, supernet):
                if aba[0] == aba[1]:
                    continue

                bab = "{1}{0}{1}".format(*aba)
                for hypernet in self.hypernets:
                    if bab in hypernet:
                        return True
        return False

class Part1(object):
    def __init__(self):
        self.counter = Counter()

    def check_address(self, address):
        ipv7 = IPV7(address)
        self.counter[ipv7.is_tls()] += 1

    @property
    def valid(self):
        return self.counter[True]

class Part2(Part1):
    def check_address(self, address):
        ipv7 = IPV7(address)
        self.counter[ipv7.is_ssl()] += 1

if __name__ == "__main__":
    ipv7s = load()
    part1 = Part1()
    for ipv7 in ipv7s:
        part1.check_address(ipv7)
    print(part1.valid)

    part2 = Part2()
    for ipv7 in ipv7s:
        part2.check_address(ipv7)
    print(part2.valid)
