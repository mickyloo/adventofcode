import Utils.InputUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 {

    private final String[] initial;
    private Map<Integer, List<Integer>> game;

    public Day15(String filename) {
        initial = InputUtils.readFile(filename).get(0).split(",");
    }

    public static void main(String[] args) {
        Day15 solution = new Day15("day15");
        solution.part1();
        solution.part2();
    }

    private void setUpGame() {
        game = new HashMap<>();
        for (int i = 0; i < initial.length; i++) {
            int turn = i + 1;
            int guess = Integer.parseInt(initial[i]);
            insert(guess, turn);
        }
    }

    private int play(int endTurn) {
        int last_guess = Integer.parseInt(initial[initial.length - 1]);
        int guess;
        for (int turn = initial.length + 1; turn < endTurn + 1; turn++) {
            if (game.get(last_guess).size() == 1) {
                insert(0, turn);
                last_guess = 0;
            } else {
                guess = game.get(last_guess).get(0) - game.get(last_guess).get(1);
                insert(guess, turn);
                last_guess = guess;
            }
        }
        return last_guess;
    }

    private void insert(int guess, int turn) {
        if (!game.containsKey(guess)) {
            game.put(guess, new ArrayList<>());
        }
        List<Integer> list = game.get(guess);
        list.add(0, turn);
        if (list.size() > 2) {
            list.remove(2);
        }
    }

    private void part1() {
        setUpGame();
        int answer = play(2020);
        System.out.println("Answer: " + answer);
    }

    private void part2() {
        setUpGame();
        int answer = play(30000000);
        System.out.println("Answer: " + answer);
    }
}
