import Utils.InputUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 {

    public static final Pattern NUMBER = Pattern.compile("(\\d)+");

    List<String> messages;
    Map<String, String> rules = new HashMap<>();

    public Day19(String filename) {
        var lines = InputUtils.readFile(filename);
        int divider = lines.indexOf("");
        messages = lines.subList(divider + 1, lines.size());

        // build raw rules
        for(var line: lines.subList(0, divider)) {
            String[] parts = line.split(":");
            String key = parts[0];

            if (parts[1].contains("a")) {
                rules.put(parts[0], "a");
            } else if (parts[1].contains("b")) {
                rules.put(parts[0], "b");
            } else if (parts[1].contains("|")) {
                rules.put(parts[0], "(" + parts[1].replaceAll(" \\| ", ")|(") + ")");
            } else {
                rules.put(parts[0], parts[1]);
            }
        }
    }

    public static void main(String[] args) {
        Day19 solution = new Day19("day19");

        solution.part1();
        solution.part2();
    }

    private void part1() {
        Set<String> evaluated = new HashSet<>();
        while(evaluated.size() != rules.size()) {
            for(var entry: rules.entrySet()) {
                List<String> numbers = NUMBER.matcher(entry.getValue())
                        .results()
                        .map(MatchResult::group)
                        .collect(Collectors.toList());

                if (evaluated.containsAll(numbers)) {
                    String rule = entry.getValue();
                    for(var num: numbers) {
                        rule = rule.replaceAll(num, rules.get(num) );
                    }
                    evaluated.add(entry.getKey());
                    rule = rule.replaceAll(" ", "")
                            .replaceAll("\\(a\\)", "a")
                            .replaceAll("\\(b\\)", "b");
                    rule = "(" + rule + ")";

                    rules.put(entry.getKey(), rule);
                }
            }
        }

        String rule0 = rules.get("0");
        System.out.println(rule0);

        int matches = (int) messages.stream()
                .filter(m -> m.matches(rule0))
                .count();
        System.out.println("Num valid " + matches);

        for(String m: messages) {
            System.out.println(String.format("%b: %s", m.matches(rule0),m));
        }
    }

    private void part2() {
    }

}
