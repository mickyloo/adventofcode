from collections import namedtuple, Counter

Coord = namedtuple('Coord', ['x', 'y'])
directions = {
    '^': Coord(0, 1),
    'v': Coord(0, -1),
    '>': Coord(1, 0),
    '<': Coord(-1, 0),
}


class Position(Coord):
    def __add__(self, other):
        return Position(self.x + other.x, self.y + other.y)


def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


def santa(moves):
    current_pos = Position(0, 0)
    presents = Counter({Position(0, 0): 1})
    for c in moves:
        current_pos += directions.get(c)
        presents[current_pos] += 1

    num_houses = len(presents)
    return num_houses


def santa_with_robo(moves):
    santa = Position(0, 0)
    robo = Position(0, 0)

    movers = {
        0: santa,
        1: robo
    }

    presents = Counter({Position(0, 0): 1})
    for i, c in enumerate(moves):
        idx = i % 2
        current_pos = movers.get(idx)
        current_pos += directions.get(c)
        presents[current_pos] += 1
        movers[idx] = current_pos

    num_houses = len(presents)
    return num_houses



if __name__ == "__main__":
    moves = load()

    num_houses = santa(moves)
    print(num_houses)

    num_houses = santa_with_robo(moves)
    print(num_houses)





