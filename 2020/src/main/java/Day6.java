import Utils.InputUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day6 {

    private final List<Group> groups = new ArrayList<>();

    public Day6(String input) {
        List<String> lines = InputUtils.readFile(input);

        Group group = new Group();
        for (String line : lines) {
            if (!line.isEmpty()) {
                group.addPerson(line);
            } else {
                this.groups.add(group);
                group = new Group();
            }
        }
        if (!group.isEmpty()) {
            this.groups.add(group);
        }
    }

    public static void main(String[] args) {
        Day6 solution = new Day6("day6");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        long count = groups.stream()
                .map(group -> group.getAnswers().stream().distinct().count())
                .mapToLong(Long::longValue)
                .sum();

        System.out.println("Distinct anyone answers " + count);
    }

    private void part2() {
        long count = 0;
        for (var group : groups) {
            int groupSize = group.getSize();

            var frequency = group.getAnswers().stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            count += frequency.values().stream().filter(i -> i == groupSize).count();
        }
        System.out.println("Distinct everyone answers " + count);
    }

    class Group {
        List<String> answers = new ArrayList<>();
        int size = 0;

        public void addPerson(String answer) {
            answers.addAll(Arrays.asList(answer.split("")));
            size++;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public int getSize() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }
}
