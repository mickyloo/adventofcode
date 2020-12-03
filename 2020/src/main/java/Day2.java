import Utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {
    private final List<String> lines;
    private final static Pattern pattern = Pattern.compile("(\\d+)-(\\d+) ([a-z]): ([a-z]+)");

    public Day2(String input) {
        lines = InputUtils.readFile(input);
    }

    public static void main(String[] args) {
        Day2 solution = new Day2("day2");
        solution.part1();
        solution.part2();
    }

    public void part1() {
        long validCount = lines.stream().filter(line -> {
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            char c = matcher.group(3).charAt(0);
            String password = matcher.group(4);

            long count = StringUtils.countMatches(password, c);

            return count >= min && count <= max;
        }).count();
        System.out.println("Valid Passwords " + validCount);
    }

    public void part2() {
        long validCount = lines.stream().filter(line -> {
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            int position1 = Integer.parseInt(matcher.group(1)) - 1;
            int position2 = Integer.parseInt(matcher.group(2)) - 1;
            int c = matcher.group(3).charAt(0);
            String password = matcher.group(4);

            boolean validPosition1 = password.charAt(position1) == c;
            boolean validPosition2 = password.charAt(position2) == c;

            return (validPosition1 || validPosition2) && !(validPosition1 && validPosition2);
        }).count();
        System.out.println("Valid Passwords " + validCount);
    }
}


