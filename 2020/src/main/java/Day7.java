import Utils.InputUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 {

    private final String MY_BAG = "shiny gold";

    private final Map<String, List<Pair<String, Integer>>> bags = new HashMap<>();

    public Day7(String input) {
        Pattern pattern = Pattern.compile("(\\d+) ([a-z\\s]+) bags?.?");

        for (var line : InputUtils.readFile(input)) {
            String[] parts = line.strip().split(" bags contain ");
            if (parts[1].equals("no other bags.")) {
                bags.put(parts[0], Collections.EMPTY_LIST);
            } else {
                List<Pair<String, Integer>> contents = Arrays.stream(parts[1].split(", "))
                        .map(b -> {
                            Matcher m = pattern.matcher(b);
                            m.matches();
                            return Pair.of(m.group(2), Integer.parseInt(m.group(1)));
                        })
                        .collect(Collectors.toList());

                bags.put(parts[0], contents);
            }
        }
    }

    public static void main(String[] args) {
        Day7 solution = new Day7("day7");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        Map<String, Set<String>> heldBy = new HashMap<>();

        for (var bagEntry : bags.entrySet()) {
            for (Pair<String, Integer> content : bagEntry.getValue()) {
                String held = content.getLeft();
                if (!heldBy.containsKey(held)) {
                    heldBy.put(held, new HashSet<String>());
                }
                heldBy.get(held).add(bagEntry.getKey());
            }
        }

        Set<String> containers = new HashSet<>() {
        };
        Stack<String> stack = new Stack<>();
        stack.push(MY_BAG);

        while (!stack.empty()) {
            String bag = stack.pop();
            containers.add(bag);

            for (String b : heldBy.getOrDefault(bag, new HashSet<>())) {
                if (!containers.contains(b)) {
                    stack.push(b);
                }
            }
        }
        containers.remove(MY_BAG);
        System.out.println("Can contain shiny gold bag: " + containers.size());
    }

    private void part2() {
        int count = getBagsInside(MY_BAG);
        System.out.println("Shiny gold bag holds: " + count);
    }

    private int getBagsInside(String bag) {
        List<Pair<String, Integer>> contains = bags.get(bag);
        if (contains.equals(Collections.EMPTY_LIST)) {
            return 0;
        }

        int count = 0;
        for (Pair<String, Integer> contained : contains) {
            count += contained.getRight() + contained.getRight() * getBagsInside(contained.getLeft());
        }
        return count;
    }
}
