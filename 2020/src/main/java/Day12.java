import Utils.InputUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

enum Action {
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W"),
    LEFT("L"),
    RIGHT("R"),
    FORWARD("F");

    public static final List<Action> DIRECTIONS = List.of(EAST, SOUTH, WEST, NORTH);
    public static final Set<Action> ROTATIONS = EnumSet.of(LEFT, RIGHT);
    private static final Map<String, Action> BY_STRING = new HashMap<>();

    static {
        for (var a : values()) {
            BY_STRING.put(a.action, a);
        }
    }

    private final String action;

    Action(String action) {
        this.action = action;
    }

    public static Action valueOfAction(String action) {
        return BY_STRING.get(action);
    }
}

interface IShip {
    void move(Action a, int distance);

    void rotate(Action a, int distance);

    int distance();
}

public class Day12 {
    private final List<Pair<Action, Integer>> instructions;

    public Day12(String filename) {
        instructions = InputUtils.readFile(filename).stream()
                .map(line -> Pair.of(
                        Action.valueOfAction(line.substring(0, 1)),
                        Integer.parseInt(line.substring(1)))
                )
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
        Day12 solution = new Day12("day12");
        solution.part1();
        solution.part2();
    }

    private void executeInstructions(IShip ship) {
        instructions.stream()
                .forEach(instr -> {
                    Action a = instr.getLeft();
                    if (Action.ROTATIONS.contains(a)) {
                        ship.rotate(a, instr.getRight());
                    } else {
                        ship.move(a, instr.getRight());
                    }
                });
    }

    private void part1() {
        class Ship implements IShip {
            int x = 0;
            int y = 0;
            Action direction = Action.EAST;

            public void move(Action a, int distance) {
                switch (a) {
                    case NORTH -> y += distance;
                    case SOUTH -> y -= distance;
                    case EAST -> x += distance;
                    case WEST -> x -= distance;
                    case FORWARD -> move(direction, distance);
                }
            }

            public void rotate(Action a, int degrees) {
                int rotations = degrees / 90;
                switch (a) {
                    case LEFT -> rotate(Action.RIGHT, 360 - degrees);
                    case RIGHT -> direction = Action.DIRECTIONS.get((Action.DIRECTIONS.indexOf(direction) + rotations) % 4);
                }
            }

            public int distance() {
                return Math.abs(x) + Math.abs(y);
            }
        }

        Ship ship = new Ship();
        executeInstructions(ship);
        System.out.println("Manhattan Distance: " + ship.distance());
    }

    private void part2() {

        class WaypointShip implements IShip {
            int x = 0;
            int y = 0;
            int waypoint_x = 10;
            int waypoint_y = 1;
            final Action direction = Action.EAST;

            public void move(Action a, int distance) {
                switch (a) {
                    case NORTH -> waypoint_y += distance;
                    case SOUTH -> waypoint_y -= distance;
                    case EAST -> waypoint_x += distance;
                    case WEST -> waypoint_x -= distance;
                    case FORWARD -> {
                        int times = distance;
                        x += (waypoint_x * times);
                        y += (waypoint_y * times);
                    }
                }
            }

            public void rotate(Action a, int degrees) {
                if (a.equals(Action.LEFT)) {
                    degrees = 360 - degrees;
                }
                int temp_x = waypoint_x;
                int temp_y = waypoint_y;
                switch (degrees) {
                    case 90 -> {
                        waypoint_x = temp_y;
                        waypoint_y = -1 * temp_x;
                    }
                    case 180 -> {
                        waypoint_x = -1 * temp_x;
                        waypoint_y = -1 * temp_y;
                    }
                    case 270 -> {
                        waypoint_x = -1 * temp_y;
                        waypoint_y = temp_x;
                    }
                }
            }

            public int distance() {
                return Math.abs(x) + Math.abs(y);
            }

            @Override
            public String toString() {
                return "WaypointShip{" +
                        "x=" + x +
                        ", y=" + y +
                        ", waypoint_x=" + waypoint_x +
                        ", waypoint_y=" + waypoint_y +
                        '}';
            }
        }

        IShip ship = new WaypointShip();
        executeInstructions(ship);
        System.out.println("Manhattan Distance: " + ship.distance());
    }
}
