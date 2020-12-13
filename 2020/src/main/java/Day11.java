import Utils.InputUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day11 {

    public static final String EMPTY = "L";
    public static final String SEATED = "#";
    private final List<String> lines;
    private final int rows;
    private final int cols;

    public Day11(String filename) {
        lines = InputUtils.readFile(filename);
        rows = lines.size();
        cols = lines.get(0).length();
    }

    public static void main(String[] args) {
        Day11 solution = new Day11("day11");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        BiFunction<Grid, Pair<Integer, Integer>, Integer> adjacentNeighbors = (grid, cell) -> {
            int num = 0;
            int row = cell.getLeft();
            int col = cell.getRight();

            num += grid.safeGet(row, col + 1).equals(SEATED) ? 1 : 0;
            num += grid.safeGet(row, col - 1).equals(SEATED) ? 1 : 0;
            num += grid.safeGet(row + 1, col).equals(SEATED) ? 1 : 0;
            num += grid.safeGet(row - 1, col).equals(SEATED) ? 1 : 0;

            num += grid.safeGet(row - 1, col - 1).equals(SEATED) ? 1 : 0;
            num += grid.safeGet(row - 1, col + 1).equals(SEATED) ? 1 : 0;
            num += grid.safeGet(row + 1, col - 1).equals(SEATED) ? 1 : 0;
            num += grid.safeGet(row + 1, col + 1).equals(SEATED) ? 1 : 0;
            return num;
        };


        Grid grid = Grid.from(lines);
        Grid next = nextIteration(grid, adjacentNeighbors, 4);

        while (!stabilized(grid, next)) {
            grid = next;
            next = nextIteration(grid, adjacentNeighbors, 4);
        }

        System.out.println("Filled seats: " + StringUtils.countMatches(next.toString(), "#"));
    }

    private void part2() {
        BiFunction<Grid, Pair<Integer, Integer>, Integer> visibleNeighbors = (grid, cell) -> {
            int num = 0;
            int row = cell.getLeft();
            int col = cell.getRight();

            num += grid.getDirection(cell, 0, 1).equals(SEATED) ? 1 : 0;
            num += grid.getDirection(cell, 0, -1).equals(SEATED) ? 1 : 0;
            num += grid.getDirection(cell, 1, 0).equals(SEATED) ? 1 : 0;
            num += grid.getDirection(cell, -1, 0).equals(SEATED) ? 1 : 0;

            num += grid.getDirection(cell, -1, -1).equals(SEATED) ? 1 : 0;
            num += grid.getDirection(cell, -1, 1).equals(SEATED) ? 1 : 0;
            num += grid.getDirection(cell, 1, -1).equals(SEATED) ? 1 : 0;
            num += grid.getDirection(cell, 1, 1).equals(SEATED) ? 1 : 0;

            return num;
        };


        Grid grid = Grid.from(lines);
        Grid next = nextIteration(grid, visibleNeighbors, 5);

        while (!stabilized(grid, next)) {
            grid = next;
            next = nextIteration(grid, visibleNeighbors, 5);
        }

        System.out.println("Filled seats: " + StringUtils.countMatches(next.toString(), "#"));
    }

    private Grid nextIteration(Grid grid, BiFunction<Grid, Pair<Integer, Integer>, Integer> neighbors, int threshold) {
        Grid next = Grid.newInstance(grid);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                var cell = Pair.of(r, c);

                if (grid.safeGet(r, c).equals(EMPTY) && neighbors.apply(grid, cell) == 0) {
                    next.set(r, c, SEATED);
                } else if (grid.safeGet(r, c).equals(SEATED) && neighbors.apply(grid, cell) >= threshold) {
                    next.set(r, c, EMPTY);
                } else {
                    next.set(r, c, grid.safeGet(r, c));
                }
            }
        }
        return next;
    }

    private boolean stabilized(Grid first, Grid second) {
        return first.toString().equals(second.toString());
    }
}

class Grid {
    public static String OUT = "";

    public final String[][] grid;

    private Grid(String[][] grid) {
        this.grid = grid;
    }

    public static Grid from(List<String> lines) {
        int cols = lines.get(0).length();
        String[][] newGrid = new String[lines.size()][cols];

        int r = 0;
        for (String line : lines) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = String.valueOf(line.charAt(c));
            }
            r++;
        }

        return new Grid(newGrid);
    }

    public static Grid newInstance(Grid original) {
        int rows = original.grid.length;
        int cols = original.grid[0].length;

        String[][] newGrid = new String[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = original.grid[r][c];
            }
        }
        return new Grid(newGrid);
    }

    public String safeGet(int row, int col) {
        try {
            return grid[row][col];
        } catch (IndexOutOfBoundsException error) {
            return OUT;
        }
    }

    public String getDirection(Pair<Integer, Integer> cell, int row_delta, int col_delta) {
        int r = cell.getLeft();
        int c = cell.getRight();
        String val;
        while (true) {
            val = safeGet(r + row_delta, c + col_delta);
            if (val.equals(OUT) || val.equals(Day11.EMPTY) || val.equals(Day11.SEATED)) {
                break;
            }
            r += row_delta;
            c += col_delta;
        }

        return val;
    }

    public void set(int row, int col, String val) {
        grid[row][col] = val;
    }

    @Override
    public String toString() {
        var rows = Arrays.stream(grid).map(row -> String.join("", row)).collect(Collectors.toList());
        return String.join("\n", rows);
    }
}