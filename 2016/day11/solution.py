from __future__ import print_function
from collections import namedtuple, deque
from itertools import combinations

CHIPS = ('TH', 'PL', 'ST', 'PR', 'RU')
GENERATORS = ('TH_G', 'PL_G', 'ST_G', 'PR_G', 'RU_G')
TECH = CHIPS + GENERATORS
TRANSITIONS = {
    1: (2,),
    2: (3, 1),
    3: (4, 2),
    4: (3,)
}

class State(namedtuple('State', ('elevator',) + TECH)):
    @property
    def is_valid(self):
        bounds = [1 <= v <= 4 for v in self]
        if not all(bounds):
            return False
        
        for chip, generator in zip(CHIPS, GENERATORS):
            chip_floor = getattr(self, chip)
            if chip_floor != getattr(self, generator):
                for g in GENERATORS:
                    if getattr(self, g) == chip_floor:
                        return False
        return True

    @property
    def moves(self):
        cur_floor = self.elevator
        moves = []

        for new_floor in TRANSITIONS.get(cur_floor):
            #if new_floor < cur_floor:
            if False:
                movable = [tech for tech in CHIPS if getattr(self, tech) == cur_floor]
            else:
                movable = [tech for tech in TECH if getattr(self, tech) == cur_floor]

            for techs in combinations(movable + [None], 2):
                new_config = {'elevator': new_floor}
                for tech in filter(bool, techs):
                    new_config[tech] = new_floor
                new_state = self._replace(**new_config)
                if new_state.is_valid:
                    moves.append(new_state)
        return set(moves)

    def __repr__(self):
        return str(tuple(self))

Vertex = namedtuple('Vertex', ['state', 'path'])
END_STATE = State(*((4,) * 11))  # everything on the 4th floor

def load():
    with open('input.txt') as f:
        inputs = int(f.read())
    return inputs

class Part1(object):
    def __bfs(self, start, end):
        visited = {start: 0}
        queue = deque()
        queue.append(Vertex(state=start, path=0))
        while queue:
            vertex = queue.popleft()
            print(vertex)
            visited[vertex.state] = vertex.path
            for move in vertex.state.moves:
                new_path = vertex.path + 1
                if move == END_STATE:
                    yield new_path
                elif move not in visited or visited[move] > new_path:
                    queue.append(Vertex(state=move, path=new_path))

    def bfs(self, start, end):
        def heuristic(move):
            chips = [getattr(move, k) for k in CHIPS]
            generators = [getattr(move, k) * 4 for k in GENERATORS]
            return sum(chips) + sum(generators)

        visited = {start: 0}
        queue = deque()
        queue.append(Vertex(state=start, path=(start,)))
        while queue:
            vertex = queue.popleft()
            visited[vertex.state] = len(vertex.path)

            print(vertex.state, len(vertex.path))
            moves = list(vertex.state.moves - set(vertex.path))
            sorted_moves = sorted(moves, key=lambda x: heuristic(x), reverse=True)
            for move in sorted_moves[:3]:
                new_path = vertex.path + (move,)
                if move == END_STATE:
                    print('yay')
                    yield new_path
                elif move not in visited or visited[move] > len(new_path):
                    queue.append(Vertex(state=move, path=new_path))
    

    def shortest_path(self, start, end):
        try:
            while True:
                nex = next(self.bfs(start, end))
                #print(nex)
                print(len(nex))
        except StopIteration:
            return None



if __name__ == "__main__":
    part1 = Part1()
    start = State(1, 1, 2, 2, 3, 3, 1, 1, 1, 3, 3)
    p = part1.shortest_path(start, END_STATE)