from itertools import count


class Unit(object):
    def __init__(self, hp, damage, armor):
        self.max_hp = hp
        self.hp = hp
        self.damage = damage
        self.armor = armor

    def defend(self, attacker):
        self.hp -= max(1, attacker.damage - self.armor)


def load():
    with open('input.txt') as f:
        inputs = f.read().splitlines()
    return inputs


def fight(player, enemy):
    attacker, defender = player, enemy
    for r in count(start=1):
        defender.defend(attacker)
        print(player.hp, enemy.hp)
        if defender.hp <= 0:
            break
        attacker, defender = defender, attacker
        

    return player.hp > 0, r, player.hp, enemy.hp


if __name__ == "__main__":
    player = Unit(100, 8, 3)
    boss = Unit(109, 8, 2)

    print(fight(player, boss))

