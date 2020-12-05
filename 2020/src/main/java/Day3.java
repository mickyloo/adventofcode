import Utils.InputUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 {
    private final List<String> lines;
    private final InfiniteForest forest;

    public Day3(String input) {
        lines = InputUtils.readFile(input);
        forest = new InfiniteForest(lines);
    }

    public static void main(String[] args) {
        Day3 solution = new Day3("day3");
        solution.part1();
        solution.part2();
    }

    public void part1() {
        System.out.println("Num Trees: " + treeChecker(3, 1));
    }

    public void part2() {
        int mult = 1;
        mult *= treeChecker(1, 1);
        mult *= treeChecker(3, 1);
        mult *= treeChecker(5, 1);
        mult *= treeChecker(7, 1);
        mult *= treeChecker(1, 2);
        System.out.println("Mult Trees: " + mult);
    }

    private int treeChecker(int slopeX, int slopeY) {
        int x = 0;
        int y = 0;
        int trees = 0;

        Marker m = forest.getCell(x, y);
        while (m != Marker.OUT_OF_BOUNDS) {
            x += slopeX;
            y += slopeY;

            m = forest.getCell(x, y);
            if (Marker.TREE.equals(m)) {
                trees += 1;
            }
        }

        return trees;
    }

    enum Marker {
        OPEN('.'),
        TREE('#'),
        OUT_OF_BOUNDS(null);

        private static final Map<Character, Marker> BY_SYMBOL = new HashMap<>();

        static {
            for (Marker e : values()) {
                BY_SYMBOL.put(e.symbol, e);
            }
        }

        private final Character symbol;

        Marker(Character symbol) {
            this.symbol = symbol;
        }

        public static Marker valueOfSymbol(char symbol) {
            return BY_SYMBOL.get(symbol);
        }
    }

    class InfiniteForest {
        private final int height;
        private final int width;
        private final List<String> forest;

        public InfiniteForest(List<String> forest) {
            this.forest = forest;
            height = forest.size();
            width = forest.get(0).length();
        }

        public Marker getCell(int x, int y) {
            if (y >= height) {
                return Marker.OUT_OF_BOUNDS;
            }
            String row = forest.get(y);
            char symbol = row.charAt(x % width);

            return Marker.valueOfSymbol(symbol);
        }
    }
}

