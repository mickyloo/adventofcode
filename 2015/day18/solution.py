from collections import namedtuple
from itertools import product, filterfalse

Point = namedtuple('Point', ['x', 'y'])

BOARD = {}
SIZE = 100


def load():
    with open('input.txt') as f:
        for i, line in enumerate(f):
            for j, col in enumerate(line.strip()):
                BOARD[Point(j, i)] = col == "#"

    assert len(BOARD.keys()) == SIZE * SIZE, "Incorrect board size"


def neighbors(point):
    nine_grid = product(range(point.x - 1, point.x + 2), range(point.y - 1, point.y + 2))
    neighbors = filterfalse(lambda p: p[0] == point.x and p[1] == point.y, nine_grid)
    in_bounds = filter(lambda p: p[0] >= 0 and p[0] < SIZE and p[1] >= 0 and p[1] < SIZE, neighbors)
    return in_bounds


def next_state(point):
    neighbor_points = [BOARD[p] for p in neighbors(point)]
    if BOARD[point]:
        return 2 <= neighbor_points.count(True) <= 3
    else:
        return neighbor_points.count(True) == 3


def next_board():
    b = {}
    for point in BOARD.keys():
        b[point] = next_state(point)
    return b


def stuck_lights(board):
    board[(0, 0)] = True
    board[(0, SIZE-1)] = True
    board[(SIZE-1, 0)] = True
    board[(SIZE-1, SIZE-1)] = True


if __name__ == "__main__":
    load()
    for _ in range(0, 100):
        BOARD = next_board()
    print(list(BOARD.values()).count(True))

    load()
    stuck_lights(BOARD)
    for _ in range(0, 100):
        BOARD = next_board()
        stuck_lights(BOARD)
    print(list(BOARD.values()).count(True))
