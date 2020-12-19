import Utils.InputUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day18 {

    List<Expression> expressions;

    public Day18(String filename) {
        expressions = InputUtils.readFile(filename)
                .stream()
                .map(s -> new Expression(s.replaceAll(" ", "")))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Day18 solution = new Day18("day18");

        solution.part1();
        solution.part2();
    }

    private void part1() {
        long ans = expressions.stream().map(Expression::evaluate).mapToLong(Long::longValue).sum();
        System.out.println("Sum: " + ans);
    }

    private void part2() {
        long ans = expressions.stream().map(Expression::advancedEvaluate).mapToLong(Long::longValue).sum();
        System.out.println("Sum: " + ans);
    }

    enum Operation {
        ADD,
        MULTIPLY
    }
}

class Expression {

    private static final Map<Day18.Operation, BiFunction<Long, Long, Long>> ops = Map.of(
            Day18.Operation.ADD, (Long x, Long y) -> x + y,
            Day18.Operation.MULTIPLY, ((Long x, Long y) -> x * y)
    );
    private static final Pattern pattern = Pattern.compile("\\d");

    private final String expression;
    private int pointer;

    public Expression(String expression) {
        this.expression = expression;
        pointer = 0;
    }

    public long evaluate() {
        String chr;

        Day18.Operation operation = Day18.Operation.ADD;
        long result = 0L;

        while (pointer < expression.length()) {
            chr = Character.toString(expression.charAt(pointer));

            pointer += 1;
            if (pattern.matcher(chr).matches()) {
                result = ops.get(operation).apply(result, Long.parseLong(chr));
            } else {
                switch (chr) {
                    case "+" -> {
                        operation = Day18.Operation.ADD;
                    }
                    case "*" -> {
                        operation = Day18.Operation.MULTIPLY;
                    }
                    case "(" -> {
                        result = ops.get(operation).apply(result, evaluate());
                    }
                    case ")" -> {
                        return result;
                    }
                }
            }
        }
        pointer = 0;
        return result;
    }

    public long advancedEvaluate() {
        String advanced = expression
                .replaceAll("\\(", "((")
                .replaceAll("\\)", "))")
                .replaceAll("\\*", ")*(");
        advanced = "(" + advanced + ")";

        return new Expression(advanced).evaluate();
    }

}
