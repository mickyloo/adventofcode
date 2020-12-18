import Utils.InputUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {

    private final Map<String, List<String>> data = new LinkedHashMap<>();
    private final Pattern pattern = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");

    public Day14(String filename) {
        String mask = "";
        for (String line : InputUtils.readFile(filename)) {
            if (line.startsWith("mask") && !data.containsKey(line)) {
                mask = line;
                data.put(mask, new ArrayList<>());
            } else {
                data.get(mask).add(line);
            }
        }
    }

    public static void main(String[] args) {
        Day14 solution = new Day14("day14");
        solution.part1();
        solution.part2();
    }

    private void part1() {
        Map<Integer, Long> memory = new HashMap<>();

        Matcher m;
        ValueMask mask;

        for (var datum : data.entrySet()) {
            for (String instr : datum.getValue()) {
                m = pattern.matcher(instr);
                m.matches();
                int addr = Integer.parseInt(m.group(1));
                mask = new ValueMask(datum.getKey());
                long val = mask.apply(Long.parseLong(m.group(2)));
                memory.put(addr, val);
            }
        }

        long sum = memory.values().stream().reduce(0L, Long::sum);
        System.out.println("Answer: " + sum);
    }

    private void part2() {
        Map<Long, Long> memory = new HashMap<>();

        Matcher m;
        AddressMask mask;
        for (var datum : data.entrySet()) {
            for (String instr : datum.getValue()) {
                m = pattern.matcher(instr);
                m.matches();
                int addr = Integer.parseInt(m.group(1));
                long val = Long.parseLong(m.group(2));

                mask = new AddressMask(datum.getKey());
                for (var a : mask.apply(addr)) {
                    memory.put(a, val);
                }
            }
        }

        long sum = memory.values().stream().reduce(0L, Long::sum);
        System.out.println("Answer: " + sum);
    }

    class ValueMask {
        private long enable = 0;
        private long disable = -1;

        public ValueMask(String m) {
            String[] bits = m.split("");
            int len = bits.length;
            for (int i = 0; i < bits.length; i++) {
                switch (bits[i]) {
                    case "1" -> {
                        enable = enable | 1L << (len - 1 - i);
                    }
                    case "0" -> {
                        disable = disable & ~(1L << (len - 1 - i));
                    }
                }
            }
        }

        public long apply(long val) {
            return (val | enable) & disable;
        }
    }

    class AddressMask {
        public final List<Integer> floatBits = new ArrayList<>();
        public long enable = 0;

        public AddressMask(String m) {
            String[] bits = m.split("");
            int len = bits.length;
            for (int i = 0; i < bits.length; i++) {
                switch (bits[i]) {
                    case "1" -> {
                        enable = set(enable, len - 1 - i);
                    }
                    case "X" -> {
                        floatBits.add(len - 1 - i);
                    }
                }
            }
        }

        public List<Long> apply(long val) {
            List<Long> addresses = new ArrayList<>();
            val = val | enable;
            for (int i = 0; i < (1 << floatBits.size()); i++) {
                long address = val;
                for (int j = 0; j < floatBits.size(); j++) {
                    int bitVal = i & (1 << j);
                    int bit = floatBits.get(j);
                    if (bitVal > 0) {
                        address = set(address, bit);
                    } else {
                        address = unset(address, bit);
                    }
                }
                addresses.add(address);
            }
            return addresses;
        }

        private long set(long val, int bit) {
            return val | (1L << bit);
        }

        private long unset(long val, int bit) {
            return val & ~(1L << bit);
        }
    }
}
