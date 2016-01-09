from collections import Counter


def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


def calculate_floor(s):
    count = Counter(s)
    return count['('] - count[')']


def first_basement(s):
    count = Counter(s)
    for i, c in enumerate(s):
        count[c] += 1
        if count[')'] > count['(']:
            break
    return i+1


if __name__ == "__main__":
    input_string = load()
    print(calculate_floor(input_string))
    print(first_basement(input_string))
