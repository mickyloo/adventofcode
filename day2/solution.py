from collections import namedtuple


class Dimension(namedtuple('Dimension', ['l','w','h'])):
    @property
    def wrapping(self):
        areas = (self.l * self.w, self.w * self.h, self.h * self.l)
        return 2 * sum(areas) + min(areas)

    @property
    def volume(self):
        return self.l * self.w * self.h

    @property
    def ribbon(self):
        sides = [self.l, self.w, self.h]
        sides.remove(max(sides))
        return 2 * sum(sides)


def load():
    boxes = []
    with open('input.txt') as f:
        for l in f:
            line = map(int, l.strip().split('x'))
            boxes.append(Dimension(*line))
    return boxes


if __name__ == "__main__":
    boxes = load()
    total_area = sum([b.wrapping for b in boxes])
    print(total_area)

    total_ribbon = sum([b.volume + b.ribbon for b in boxes])
    print(total_ribbon)

