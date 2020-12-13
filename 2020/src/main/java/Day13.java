import Utils.InputUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
    public static final String OUT_OF_SERVICE = "x";

    private final int minDepartTime;
    private final List<String> busIDs;

    public Day13(String filename) {
        var lines = InputUtils.readFile(filename);
        minDepartTime = Integer.parseInt(lines.get(0));
        busIDs = Arrays.asList(lines.get(1).split(","));
    }

    public static void main(String[] args) {
        Day13 solution = new Day13("day13");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        int minWait = Integer.MAX_VALUE;
        int busToTake = 0;

        for (var bus : busIDs) {
            if (bus.equals(OUT_OF_SERVICE)) continue;
            int busId = Integer.parseInt(bus);
            int wait = busId - (minDepartTime % busId);
            if (wait < minWait) {
                minWait = wait;
                busToTake = busId;
            }
        }

        System.out.println(String.format("Bus id %d, wait time %d, answer %d", busToTake, minWait, busToTake * minWait));
    }

    // Chinese remainder theorem
    private void part2() {
        List<Long> divisors = new ArrayList<>();
        List<Long> remainders = new ArrayList<>();

        for (int i = 0; i < busIDs.size(); i++) {
            if (busIDs.get(i).equals(OUT_OF_SERVICE)) continue;

            long id = Long.parseLong(busIDs.get(i));
            divisors.add(id);
            remainders.add(id - i);
        }

        long product = divisors.stream().reduce(1L, (a, b) -> a * b);
        List<Long> partialProducts = divisors.stream().map(i -> product / i).collect(Collectors.toList());

        long sum = 0;
        for (int i = 0; i < divisors.size(); i++) {
            sum += remainders.get(i) * multInverseModulo(partialProducts.get(i), divisors.get(i)) * partialProducts.get(i);
        }

        System.out.println("Earliest timestamp: " + (sum % product));
    }

    private long multInverseModulo(long a, long b) {
        long b0 = b;
        long x0 = 0;
        long x1 = 1;

        if (b == 1)
            return 1;

        while (a > 1) {
            long q = a / b;
            long amb = a % b;
            a = b;
            b = amb;
            long xqx = x1 - q * x0;
            x1 = x0;
            x0 = xqx;
        }

        if (x1 < 0)
            x1 += b0;

        return x1;
    }
}
