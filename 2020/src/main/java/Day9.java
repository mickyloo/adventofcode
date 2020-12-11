import Utils.InputUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day9 {
    private final List<Long> data;

    public Day9(String filename) {
        data = InputUtils.readFile(filename)
                .stream()
                .map(line -> Long.parseLong(line))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
        Day9 solution = new Day9("day9");
        solution.part1();
        solution.part2();
    }

    private long part1() {
        int size = 25;

        Set<Long> preamble;
        for (int i = size; i < data.size(); i++) {
            preamble = new HashSet<>(data.subList(i - size, i));
            boolean isSumofTwo = false;
            long target = data.get(i);
            for (long num : preamble) {
                long complement = target - num;
                if (complement != num && preamble.contains(complement)) {
                    isSumofTwo = true;
                    break;
                }
            }
            if (!isSumofTwo) {
                System.out.println("Is not sum of two: " + target);
                return target;
            }
        }
        return 0;
    }

    private void part2() {
        long target = part1();
        Deque<Long> deque = new ArrayDeque<>();
        long sum = 0;
        int i = 0;
        while (true) {
            if (sum == target) {
                var sorted = deque.stream().sorted().collect(Collectors.toCollection(ArrayDeque::new));
                System.out.println("Answer: " + (sorted.getFirst() + sorted.getLast()));
                break;
            }

            if (sum < target) {
                deque.add(data.get(i));
                sum += deque.getLast();
                i++;
            }
            if (sum > target) {
                sum -= deque.removeFirst();
            }
        }
    }
}
