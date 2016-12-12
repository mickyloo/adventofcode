import re


def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs


def vowels(s):
    matches = re.findall(r'[aeiou]', s)
    return len(matches) >= 3


def double_char(s):
    return bool(re.search(r'(.)\1', s))


def no_restricted(s):
    return not bool(re.search(r'ab|cd|pq|xy', s))


def is_nice(s):
    return all([vowels(s), double_char(s), no_restricted(s)])


def pair_repeat(s):
    return bool(re.search(r'(..).*\1', s))


def single_repeat_with_separator(s):
    return bool(re.search(r'(.).\1', s))


def is_nice_2(s):
    return all([pair_repeat(s), single_repeat_with_separator(s)])

if __name__ == "__main__":
    strings = load()
    checklist = [is_nice(s) for s in strings]
    print(checklist.count(True))

    checklist = [is_nice_2(s) for s in strings]
    print(checklist.count(True))
