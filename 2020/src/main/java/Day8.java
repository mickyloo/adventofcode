import Utils.InputUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

enum Operation {
    NOP,
    ACC,
    JMP
}

public class Day8 {

    private final List<Pair<Operation, List<Integer>>> bootCode;
    private GameConsole console;

    public Day8(String filename) {
        bootCode = InputUtils.readFile(filename)
                .stream()
                .map(l -> {
                    String[] parts = l.split(" ");
                    List<Integer> params = new ArrayList<>();
                    for (int i = 1; i < parts.length; i++) {
                        params.add(Integer.parseInt(parts[i]));
                    }
                    return Pair.of(Operation.valueOf(parts[0].toUpperCase()), params);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
        Day8 solution = new Day8("day8");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        console = new GameConsole(bootCode);
        boolean success = console.executeTilHalt();
        System.out.println("Accumulator: " + console.getAccumulator());
    }

    private void part2() {
        boolean booted = false;
        int hackIndex = 0;
        while (!booted) {
            var instruction = bootCode.get(hackIndex);
            if (instruction.getLeft() == Operation.ACC) {
                hackIndex++;
                continue;
            }

            if (instruction.getLeft() == Operation.NOP) {
                bootCode.set(hackIndex, Pair.of(Operation.JMP, instruction.getRight()));
            } else {
                bootCode.set(hackIndex, Pair.of(Operation.NOP, instruction.getRight()));
            }

            console = new GameConsole(bootCode);
            booted = console.executeTilHalt();

            bootCode.set(hackIndex, instruction);
            hackIndex++;
        }

        System.out.println("Accumulator: " + console.getAccumulator());
    }
}

class GameConsole {

    private final List<Pair<Operation, List<Integer>>> bootCode;
    private int accumulator = 0;
    private int pointer = 0;

    public GameConsole(List<Pair<Operation, List<Integer>>> bootCode) {
        this.bootCode = bootCode;
    }

    public int getAccumulator() {
        return accumulator;
    }

    public void executeNext() {
        var instruction = bootCode.get(pointer);
        switch (instruction.getLeft()) {
            case NOP -> nop(instruction.getRight());
            case ACC -> acc(instruction.getRight());
            case JMP -> jmp(instruction.getRight());
        }
    }

    public boolean executeTilHalt() {
        Set<Integer> visited = new HashSet<>();
        while (!visited.contains(pointer) && !booted()) {
            visited.add(pointer);
            executeNext();
        }

        return booted();
    }

    public boolean booted() {
        return pointer == bootCode.size();
    }

    private void nop(List<Integer> params) {
        this.pointer += 1;
    }

    private void acc(List<Integer> params) {
        this.accumulator += params.get(0);
        this.pointer += 1;
    }

    private void jmp(List<Integer> params) {
        this.pointer += params.get(0);
    }

}


