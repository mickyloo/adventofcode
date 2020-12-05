import Utils.InputUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4 {

    public static final String BIRTH_YEAR = "byr";
    public static final String ISSUE_YEAR = "iyr";
    public static final String EXPIRATION_YEAR = "eyr";
    public static final String HEIGHT = "hgt";
    public static final String HAIR_COLOR = "hcl";
    public static final String EYE_COLOR = "ecl";
    public static final String PASSPORT_ID = "pid";
    public static final String COUNTRY_ID = "cid";
    public static final Set<String> REQUIRED_FIELDS = Stream
            .of(BIRTH_YEAR, ISSUE_YEAR, EXPIRATION_YEAR, HEIGHT, HAIR_COLOR, EYE_COLOR, PASSPORT_ID)
            .collect(Collectors.toCollection(HashSet::new));

    private final List<String> lines;
    private final List<List<String>> passports = new ArrayList<>();

    public Day4(String input) {
        lines = InputUtils.readFile(input);

        List<String> passport = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty()) {
                passport.addAll(Arrays.asList(line.split(" ")));
            } else {
                this.passports.add(passport);
                passport = new ArrayList<>();
            }
        }
        if (!passport.isEmpty()) {
            this.passports.add((passport));
        }
    }

    public static void main(String[] args) {
        Day4 solution = new Day4("day4");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        int valid = (int) passports.stream()
                .map(passport -> passport.stream()
                        .map(field -> field.split(":")[0])
                        .collect(Collectors.toCollection(HashSet::new)))
                .filter(fields -> fields.containsAll(REQUIRED_FIELDS))
                .count();

        System.out.println("Valid Passports " + valid);
    }

    private boolean validateField(String field) {
        String[] parts = field.split(":");
        boolean valid = false;
        switch (parts[0]) {
            case BIRTH_YEAR -> {
                int byr = Integer.parseInt(parts[1]);
                valid = byr >= 1920 && byr <= 2002;
            }
            case ISSUE_YEAR -> {
                int iyr = Integer.parseInt(parts[1]);
                valid = iyr >= 2010 && iyr <= 2020;
            }
            case EXPIRATION_YEAR -> {
                int eyr = Integer.parseInt(parts[1]);
                valid = eyr >= 2020 && eyr <= 2030;
            }
            case HEIGHT -> {
                Pattern hgt_pattern = Pattern.compile("(\\d+)(cm|in)");
                Matcher hgt_matcher = hgt_pattern.matcher(parts[1]);
                if (hgt_matcher.matches()) {
                    int hgt = Integer.parseInt(hgt_matcher.group(1));
                    String unit = hgt_matcher.group(2);
                    if (unit.equals("cm")) {
                        valid = hgt >= 150 && hgt <= 193;
                    } else if (unit.equals("in")) {
                        valid = hgt >= 59 && hgt <= 76;
                    }
                }
            }
            case HAIR_COLOR -> valid = Pattern.matches("\\#[0-9a-f]{6}", parts[1]);
            case EYE_COLOR -> valid = Pattern.matches("amb|blu|brn|gry|grn|hzl|oth", parts[1]);
            case PASSPORT_ID -> valid = Pattern.matches("[0-9]{9}", parts[1]);
            case COUNTRY_ID -> valid = true;
        }

        return valid;
    }

    private void part2() {
        int valid = (int) passports.stream()
                .map(passport -> passport.stream()
                        .filter(this::validateField)
                        .map(field -> field.split(":")[0])
                        .collect(Collectors.toCollection(HashSet::new)))
                .filter(fields -> fields.containsAll(REQUIRED_FIELDS))
                .count();

        System.out.println("Valid Passports " + valid);
    }
}
