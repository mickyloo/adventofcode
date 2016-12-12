from itertools import groupby


def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


def look_say(s):
    next = []
    for v, group in groupby(s):
        next.append("{}{}".format(len(list(group)), v))
    return ''.join(next)


def look_say_repeater(s, n=40):
    for _ in range(0, n):
        s = look_say(s)
    return len(s)


if __name__ == "__main__":
    inputs = load()
    print(look_say_repeater(inputs))
    print(look_say_repeater(inputs, n=50))