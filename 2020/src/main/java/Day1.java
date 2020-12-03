import Utils.InputUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day1 {

    private static final int SUM = 2020;
    private final List<String> lines;

    public Day1(String input) {
        lines = InputUtils.readFile(input);
    }

    public static void main(String[] args) {
        Day1 solution = new Day1("day1");
        solution.part1();
        solution.part2();
    }

    public void part1() {
        Set<Integer> entries = lines
                .stream()
                .map(x -> Integer.parseInt(x))
                .collect(Collectors.toCollection(HashSet::new));

        for(Integer entry: entries) {
            int complement = SUM - entry;
            if (entries.contains(complement)) {

                System.out.println(
                        String.format(
                                "entry=%d, complement=%d, mult=%d",
                                entry,
                                complement,
                                entry * complement
                        )
                );
                break;
            }
        }
    }

    public void part2() {
        Set<Integer> entries = lines
                .stream()
                .map(x -> Integer.parseInt(x))
                .collect(Collectors.toCollection(HashSet::new));

        outer:
        for(Integer entry1: entries) {
            for(Integer entry2: entries) {
                int complement = SUM - entry1 - entry2;
                if (entries.contains(complement)) {
                    System.out.println(
                            String.format(
                                    "entry1=%d, entry2=%d, complement=%d, mult=%d",
                                    entry1,
                                    entry2,
                                    complement,
                                    entry1 * entry2 * complement
                            )
                    );
                    break outer;
                }
            }
        }
    }
}


