from __future__ import print_function
from collections import namedtuple, deque
from hashlib import md5


MOVES = (
    (0, -1, 'U'),
    (0, 1, 'D'),
    (-1, 0, 'L'),
    (1, 0, 'R')
)  # up, down, left, right

OPEN_DOOR = 'bcdef'

Room = namedtuple('Room', ['x','y'])


class Vertex(namedtuple('Vertex', ['room', 'path'])):
    __slots__ = ()

    def get_moves(self, prefix):
        passcode = md5((prefix + self.path).encode()).hexdigest()
        directions = passcode[:4]
        for dir_check, movement in zip(directions, MOVES):
            new_room = Room(self.room.x + movement[0], self.room.y + movement[1])
            if dir_check in OPEN_DOOR and (0 <= new_room.x <= 3) and (0 <= new_room.y <= 3):
                yield Vertex(room=new_room, path=self.path + movement[2])

def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


class Part1(object):
    def __init__(self, prefix):
        self.prefix = prefix
        self.start = Room(0,0)
        self.end = Room(3, 3) # lower right corner

    def bfs(self, start, end):
        queue = deque()
        queue.append(Vertex(room=start, path=''))
        while queue:
            vertex = queue.popleft()
            for new_vertex in vertex.get_moves(self.prefix):
                if new_vertex.room == self.end:
                    yield new_vertex.path
                else:
                    queue.append(new_vertex)
    
    def shortest_path(self):
        try:
            return next(self.bfs(self.start, self.end))
        except StopIteration:
            return None


class Part2(Part1):
    def longest_path(self):
        paths = list(self.bfs(self.start, self.end))
        return paths[-1]


if __name__ == "__main__":
    prefix = load()
    part1 = Part1(prefix)
    print(part1.shortest_path())

    part2 = Part2(prefix)
    print(len(part2.longest_path()))
