from operator import and_, or_, invert, rshift, lshift
from functools import partial

expressions = {}
values = {}


def load():
    with open('input.txt') as f:
        for line in f:
            expression, var = line.strip().split(' -> ')
            try:
                values[var] = int(expression)
            except Exception as e:
                expressions[var] = parse(expression)


def save_value(var, val):
    try:
        values[var] = int(val)
    except:
        pass


def factory(f, *args):
    def lazy(*args):
        evaluated_args = []
        for arg in args:
            try:
                val = values[arg]
            except KeyError:
                val = expressions[arg]()
                save_value(arg, val)
            evaluated_args.append(val)
        return f(*evaluated_args)
    return partial(lazy, *args)


def parse(expression):
    """
    :param expression:
    :return: a callable that represents the expression
    """
    if 'NOT' in expression:
        _, x = expression.split(' ')
        return factory(invert, x)
    elif 'AND' in expression:
        x, _, y = expression.split(' ')
        save_value(x, x)
        save_value(y, y)
        return factory(and_, x, y)
    elif 'OR' in expression:
        x, _, y = expression.split(' ')
        save_value(x, x)
        save_value(y, y)
        return factory(or_, x, y)
    elif 'LSHIFT' in expression:
        x, _, y = expression.split(' ')
        save_value(x, x)
        save_value(y, y)
        return factory(lshift, x, y)
    elif 'RSHIFT' in expression:
        x, _, y = expression.split(' ')
        save_value(x, x)
        save_value(y, y)
        return factory(rshift, x, y)
    else:
        # assignment
        return factory(lambda x: x, expression)


if __name__ == "__main__":
    load()

    # part 1
    # print(expressions['a']())

    # part 2
    values['b'] = 3176
    print(expressions['a']())


