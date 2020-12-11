import Utils.InputUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {

    private final List<Integer> data;

    public Day10(String filename) {
        data = InputUtils.readFile(filename)
                .stream()
                .map(line -> Integer.parseInt(line))
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
        Day10 solution = new Day10("day10");
        solution.part1();
        solution.part2();
    }

    private List<Integer> makeDiffs() {
        List<Integer> diffs = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            int i1 = data.get(i + 1) - data.get(i);
            diffs.add(i1);
        }

        return diffs;
    }

    private List<Integer> part1() {
        var diffs = makeDiffs();
        int ones = Collections.frequency(diffs, 1) + 1;
        int threes = Collections.frequency(diffs, 3) + 1;

        System.out.println("Answer: " + (ones * threes));
        return diffs;
    }

    private void part2() {
        var diffs = makeDiffs();
        diffs.add(0, 1);
        diffs.add(3);

        long combinations = 1;
        int chain = 0;

        for (var i = 0; i < diffs.size() - 1; i++) {
            if (diffs.get(i) == 3) {
                switch (chain) {
                    case 1 -> combinations *= 1;
                    case 2 -> combinations *= 2;
                    case 3 -> combinations *= 4;
                    case 4 -> combinations *= 7;
                    case 5 -> combinations *= 13;
                }
                chain = 0;
            } else {
                chain++;
            }
        }
        System.out.println("Combinations: " + combinations);
    }
}
