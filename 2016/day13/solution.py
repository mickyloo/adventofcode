from __future__ import print_function
from collections import namedtuple, deque

Point = namedtuple('Point', ['x', 'y'])
Vertex = namedtuple('Vertex', ['point', 'path'])

def load():
    with open('input.txt') as f:
        inputs = int(f.read())
    return inputs

class Maze(object):
    def __init__(self, num):
        self.num = num
    
    def is_open_space(self, p):
        if p.x < 0 or p.y < 0:
            return False

        n = p.x**2 + 3*p.x + 2*p.x*p.y + p.y + p.y**2 + self.num
        bits = bin(n).count('1')
        return bool(bits % 2 == 0)

    def moves(self, p):
        possible = [
            Point(p.x - 1, p.y),
            Point(p.x + 1, p.y),
            Point(p.x, p.y - 1),
            Point(p.x, p.y + 1)
        ]
        return set(filter(self.is_open_space, possible))


class Part1(object):
    def __init__(self, maze):
        self.maze = maze

    def bfs(self, start, end):
        queue = deque()
        queue.append(Vertex(point=start, path=()))
        while queue:
            vertex = queue.popleft()
            for move in self.maze.moves(vertex.point) - set(vertex.path):
                new_path = vertex.path + (move,)
                if move == end:
                    yield new_path
                else:
                    queue.append(Vertex(point=move, path=new_path))
    
    def shortest_path(self, start, end):
        try:
            return next(self.bfs(start, end))
        except StopIteration:
            return None


class Part2(object):
    def __init__(self, maze):
        self.maze = maze

    def dfs(self, start, distance=50):
        stack = [Vertex(point=start, path=(start,))]
        while stack:
            vertex = stack.pop()
            moves = self.maze.moves(vertex.point) - set(vertex.path)
            if not moves or len(vertex.path) == distance + 1:
                yield vertex.path
            else:
                for move in moves:
                    new_path = vertex.path + (move,)
                    stack.append(Vertex(point=move, path=new_path))
    
    def locations(self, start, distance=50):
        locations = set((start,))
        for path in self.dfs(start, distance):
            locations |= set(path)
        return locations



if __name__ == "__main__":
    num = load()
    m = Maze(num)
    part1 = Part1(m)
    shortest = part1.shortest_path(Point(1, 1), Point(31, 39))
    print(len(shortest))

    part2 = Part2(m)
    locations = part2.locations(Point(1, 1), distance=50)
    print(len(locations))
