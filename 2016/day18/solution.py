from __future__ import print_function

SAFE_TILE = '.'
TRAP_TILE = '^'

TRAP = (
    ('^', '^', '.'),
    ('.', '^', '^'),
    ('^', '.', '.'),
    ('.', '.', '^'),
)

class MineField(object):
    def __init__(self, initial):
        self.rows = [initial]
        self.num_cols = len(initial)

    def _generate(self, row):
        seed = self.rows[row-1]
        for col in range(self.num_cols):
            center = seed[col]
            left = seed[col-1] if col > 0 else SAFE_TILE
            try:
                right = seed[col + 1]
            except IndexError:
                right = SAFE_TILE

            is_trap = (left, center, right) in TRAP
            yield TRAP_TILE if is_trap else SAFE_TILE


    def add_rows(self, num):
        for row in range(1, num + 1):
            self.rows.append("".join(self._generate(row)))

    def num_safe(self):
        field = "".join(self.rows)
        return field.count(SAFE_TILE)

    def print_me(self):
        for row in self.rows:
            print(row)

def load():
    with open('input.txt') as f:
        inputs = f.read().strip()
    return inputs


if __name__ == "__main__":
    seed = load()
    part1 = MineField(seed)
    part1.add_rows(num=39)
    print(part1.num_safe())

    part2 = MineField(seed)
    part2.add_rows(num=400000-1)
    print(part2.num_safe())

