from __future__ import print_function
from hashlib import md5

def load():
    with open('input.txt') as f:
        inputs = f.read()
    return inputs

class Part1(object):
    CHECK = '00000'

    def __init__(self, door_id):
        self.door_id = door_id

    def find_index(self, index=0):
        while True:
            s = '{}{}'.format(self.door_id, index)
            h = md5(s.encode('utf-8')).hexdigest()
            index += 1
            if h.startswith(self.CHECK):
                break
        return index, h

    def get_password(self, length=8):
        password = []
        index = 0
        for _ in range(length):
            index, pw = self.find_index(index)
            password.append(pw[5])
        return "".join(password)

class Part2(Part1):
    def get_password(self, length=8):
        password = [None] * length
        index = 0
        while None in password:
            index, pw = self.find_index(index)
            try:
                pos = int(pw[5])
                if password[pos] is None:
                    password[pos] = pw[6]
                    print(password)
            except (IndexError, ValueError):
                pass

        return "".join(password)

if __name__ == "__main__":
    door_id = load()
    part1 = Part1(door_id)
    print(part1.get_password())

    part2 = Part2(door_id)
    print(part2.get_password())
