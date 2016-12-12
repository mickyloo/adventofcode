import hashlib
import itertools


def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


def starts_with_zeroes(key, num=5):
    prefix = '0' * num
    for i in itertools.count():
        s = "{}{}".format(key, i)
        hex = hashlib.md5(s.encode('utf-8')).hexdigest()
        if hex.startswith(prefix):
            break
    return i


if __name__ == "__main__":
    key = load()
    print(starts_with_zeroes(key))
    print(starts_with_zeroes(key, num=6))
