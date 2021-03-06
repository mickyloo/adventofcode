import Utils.InputUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum State {
    INACTIVE, ACTIVE
}

interface Point {
    List<Point> neighbors();
}

public class Day17 {
    private static final int BOOTUP_ITERATION = 6;

    private final Map<Point, State> space = new HashMap<>();
    private final Map<Point, State> hyperspace = new HashMap<>();

    public Day17(String filename) {
        var lines = InputUtils.readFile(filename);
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                State state = (line.charAt(x) == '#') ? State.ACTIVE : State.INACTIVE;
                space.put(new P3(x, y, 0), state);
                hyperspace.put(new P4(x, y, 0, 0), state);
            }
        }
    }

    public static void main(String[] args) {
        Day17 solution = new Day17("day17");

        solution.part1();
        solution.part2();
    }

    private void part1() {
        Map<Point, State> result = bootUp(space);
        long numActive = result.values().stream().filter(x -> x.equals(State.ACTIVE)).count();
        System.out.println("Number active: " + numActive);
    }

    private void part2() {
        Map<Point, State> result = bootUp(hyperspace);
        long numActive = result.values().stream().filter(x -> x.equals(State.ACTIVE)).count();
        System.out.println("Number active: " + numActive);
    }

    private Map<Point, State> bootUp(Map<Point, State> initial) {
        Map<Point, State> current, next;
        current = new HashMap<Point, State>(initial);

        for (int i = 0; i < BOOTUP_ITERATION; i++) {
            next = new HashMap<>();
            for (var e : current.entrySet()) {
                var point = e.getKey();
                next.put(point, nextState(current, point));

                for (var neighbor : point.neighbors()) {
                    next.put(neighbor, nextState(current, neighbor));
                }
            }
            current = next;
        }
        return current;
    }

    private State nextState(Map<Point, State> space, Point point) {
        int activeNeighbors = (int) point.neighbors().stream()
                .map(p -> space.getOrDefault(p, State.INACTIVE).equals(State.ACTIVE))
                .filter(x -> x)
                .count();

        State state = space.getOrDefault(point, State.INACTIVE);
        if (state.equals(State.ACTIVE) && (activeNeighbors == 2 || activeNeighbors == 3)) {
            return State.ACTIVE;
        } else if (state.equals(State.INACTIVE) && activeNeighbors == 3) {
            return State.ACTIVE;
        }

        return State.INACTIVE;
    }
}

record P3(int x, int y, int z) implements Point {
    static List<Integer> near = List.of(-1, 0, 1);

    public List<Point> neighbors() {
        List<Point> neighbors = new ArrayList<>();
        for (int i : near) {
            for (int j : near) {
                for (int k : near) {
                    if (i == 0 && j == 0 && k == 0) continue;
                    neighbors.add(new P3(x + i, y + j, z + k));
                }
            }
        }
        return neighbors;
    }
}

record P4(int x, int y, int z, int w) implements Point {
    static List<Integer> near = List.of(-1, 0, 1);

    public List<Point> neighbors() {
        List<Point> neighbors = new ArrayList<>();
        for (int i : near) {
            for (int j : near) {
                for (int k : near) {
                    for (int l : near) {
                        if (i == 0 && j == 0 && k == 0 && l == 0) continue;
                        neighbors.add(new P4(x + i, y + j, z + k, w + l));
                    }
                }
            }
        }
        return neighbors;
    }
}
