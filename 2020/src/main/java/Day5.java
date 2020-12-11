import Utils.InputUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day5 {

    private final List<Seat> seats;

    public Day5(String input) {
        seats = InputUtils.readFile(input)
                .stream()
                .map(Seat::from)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Day5 solution = new Day5("day5");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        int maxSeatId = seats.stream()
                .map(Seat::getId)
                .max(Integer::compare)
                .get();

        System.out.println("Max Seat Id: " + maxSeatId);
    }

    private void part2() {
        Set<Integer> ids = seats.stream()
                .map(Seat::getId)
                .collect(Collectors.toCollection(HashSet::new));

        for (int i = 0; i < 1024; i++) {
            if (ids.contains(i)) {
                continue;
            }
            if (ids.contains(i - 1) && ids.contains((i + 1))) {
                System.out.println("My Seat: " + i);
                break;
            }
        }
    }
}

final class Seat {
    private final int row;
    private final int column;

    private Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Seat from(String code) {
        String[] bits = code.split("");

        int row = 0;
        for (int i = 0; i < 7; i++) {
            if (bits[i].equals("B")) {
                int foo = 1 << (6 - i);
                row += 1 << (6 - i);
            }
        }

        int column = 0;
        for (int i = 0; i < 3; i++) {
            if (bits[i + 7].equals("R")) {
                column += 1 << (2 - i);
            }
        }

        return new Seat(row, column);
    }

    public int getId() {
        return row * 8 + column;
    }
}