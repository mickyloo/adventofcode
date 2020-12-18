import Utils.InputUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day16 {
    private final List<Integer> myticket;
    private final List<List<Integer>> nearby = new ArrayList<>();
    private final Map<String, Predicate<Integer>> fieldRules = new HashMap<>();
    private Predicate<Integer> allRules = x -> false;

    public Day16(String filename) {
        List<String> lines = InputUtils.readFile(filename);

        int i;
        for (i = 0; !lines.get(i).isEmpty(); i++) {
            String field = lines.get(i).split(": ")[0];
            String[] rules = lines.get(i).split(": ")[1].split(" or ");

            var rule1 = makeRule(rules[0]);
            var rule2 = makeRule(rules[1]);
            fieldRules.put(field, rule1.or(rule2));
            allRules = allRules.or(rule1).or(rule2);
        }

        myticket = Arrays.stream(lines.get(i + 2).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        for (var j = i + 5; j < lines.size(); j++) {
            nearby.add(Arrays.stream(lines.get(j).split(",")).map(Integer::parseInt).collect(Collectors.toList()));
        }
    }

    public static void main(String[] args) {
        Day16 solution = new Day16("day16");

        solution.part1();
        solution.part2();
    }

    private Predicate<Integer> makeRule(String rule) {
        String[] parts = rule.split("-");
        int lower = Integer.parseInt(parts[0]);
        int upper = Integer.parseInt(parts[1]);
        return n -> n >= lower && n <= upper;
    }

    private void part1() {
        int sum = nearby.stream()
                .flatMap(Collection::stream)
                .filter(x -> !allRules.test(x))
                .collect(Collectors.summingInt(Integer::intValue));

        System.out.println("Answer: " + sum);
    }

    private void part2() {
        List<List<Integer>> validTickets = nearby.stream()
                .filter(list -> list.stream().allMatch(allRules))
                .collect(Collectors.toList());

        // transpose rows of tickets to columns of fields
        List<List<Integer>> columns = new ArrayList<>();
        for (int i = 0; i < myticket.size(); i++) {
            int pos = i;
            columns.add(validTickets.stream().map(list -> list.get(pos)).collect(Collectors.toList()));
        }

        // map field to columns satisfied
        Map<String, Set<Integer>> satisfy = new HashMap<>();
        for (var fieldEntry : fieldRules.entrySet()) {
            String name = fieldEntry.getKey();

            Set<Integer> valid = new HashSet<>();
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i).stream().allMatch(fieldEntry.getValue())) {
                    valid.add(i);
                }
            }
            satisfy.put(name, valid);
        }

        // build position list
        String[] fields = new String[myticket.size()];
        Optional<Map.Entry<String, Set<Integer>>> found;
        for (int i = 0; i < myticket.size(); i++) {
            found = satisfy.entrySet().stream().filter(e -> e.getValue().size() == 1).findFirst();
            Integer pos = (Integer) found.get().getValue().toArray()[0];
            String name = found.get().getKey();
            fields[pos] = name;

            satisfy.entrySet().stream().forEach(e -> e.getValue().remove(pos));
            satisfy.remove(name);
        }

        long answer = 1L;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].startsWith("departure")) {
                answer *= myticket.get(i);
            }
        }

        System.out.println("Answer: " + answer);
    }
}