from __future__ import print_function
from collections import defaultdict, deque
import re

def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()

    bots = [x for x in inputs if x.startswith("bot")]
    values = [x for x in inputs if x.startswith("value")]
    return bots, values

class BotExecutionValueError(Exception): pass

class Bot(object):
    def __init__(self, low_bot=None, high_bot=None):
        self.low_bot = low_bot
        self.high_bot = high_bot
        self.values = []

    def set_value(self, value):
        self.values.append(value)
        self.values.sort()

    @property
    def has_work(self):
        return len(self.values) == 2

    def execute(self):
        if len(self.values) < 2:
            raise BotExecutionValueError()
        else:
            log = tuple(self.values)
            self.low_bot.set_value(self.values[0])
            self.high_bot.set_value(self.values[1])
            self.values = []
            return log

class Output(object):
    def set_value(self, value):
        self.value = value

    def __rmul__(self, a):
        return self.value * a

    def __mul__(self, a):
        return self.value * a


class Part1(object):
    def __init__(self, initials, bot_configs):
        self.factory = defaultdict(lambda: Bot())
        self.queue = deque()
        self.logger = {}
        self.outputs = defaultdict(lambda: Output())

        bot_re = re.compile(r'bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)')
        types = {
            "output": self.outputs,
            "bot": self.factory
        }
        for conf in bot_configs:
            parsed = re.search(bot_re, conf)
            num, low_num, high_num = map(int, parsed.group(1, 3, 5))
            low_type, high_type = parsed.group(2, 4)
            bot = self.factory[num]
            bot.low_bot = types.get(low_type)[low_num]
            bot.high_bot = types.get(high_type)[high_num]

        init_re = re.compile(r'value (\d+) goes to bot (\d+)')
        for initial in initials:
            val, num = map(int, re.search(init_re, initial).groups())
            self.factory[num].set_value(val)
            if self.factory[num].has_work:
                self.queue.appendleft(num)

    def run_factory(self):
        while self.queue:
            num = self.queue.pop()
            working = self.factory[num]
            try:
                log = working.execute()
                self.logger[log] = num
            except BotExecutionValueError:
                pass

            for i, bot in self.factory.items():
                if bot.has_work and (i not in self.queue):
                    self.queue.appendleft(i)

if __name__ == "__main__":
    bots, values = load()
    
    part1 = Part1(values, bots)
    part1.run_factory()
    print(part1.logger[(17, 61)])

    print(part1.outputs[0] * part1.outputs[1] * part1.outputs[2])