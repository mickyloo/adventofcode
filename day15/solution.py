from collections import namedtuple, OrderedDict
import re

Ingredient = namedtuple('Ingredient', ['capacity', 'durability', 'flavor', 'texture', 'calories'])
INGREDIENTS = OrderedDict()
MAXCAL = 500


def load():
    r = re.compile(r'(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)')
    with open('input.txt') as f:
        for line in f:
            match = re.match(r, line)
            if not match:
                continue
            properties = [int(p) for p in match.groups()[1:]]
            INGREDIENTS[match.group(1)] = Ingredient(*properties)


def recipes(ingredient_names, n=100):
    if len(ingredient_names) == 1:
        yield {ingredient_names[0]: n}
        return

    for num in range(n, -1, -1):
        r = {ingredient_names[0]: num}
        for r_others in recipes(ingredient_names[1:], n-num):
            r.update(r_others)
            yield r


def score(recipe):
    capacity, durability, flavor, texture = 0, 0, 0, 0
    for key, value in recipe.items():
        capacity += INGREDIENTS[key].capacity * value
        durability += INGREDIENTS[key].durability * value
        flavor += INGREDIENTS[key].flavor * value
        texture += INGREDIENTS[key].texture * value

    score = max(0, capacity) * max(0, durability) * max(0, flavor) * max(0, texture)
    return score


def calories(recipe):
    return sum([INGREDIENTS[key].calories * val for key, val in recipe.items()])


def part1():
    max_score = 0
    names = list(INGREDIENTS.keys())
    for rec in recipes(names, n=100):
        max_score = max(max_score, score(rec))

    print(max_score)


def part2():
    max_score = 0
    names = list(INGREDIENTS.keys())
    for rec in filter(lambda x: calories(x) == MAXCAL, recipes(names, n=100)):
        max_score = max(max_score, score(rec))
    print(max_score)

if __name__ == "__main__":
    load()
    part1()
    part2()
