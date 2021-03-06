import Utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day20 {

    List<Tile> tiles = new ArrayList<>();

    public Day20(String filename) {
        var lines = InputUtils.readFile(filename);

        List<String> tileLines = new ArrayList<>();
        int tileId = 0;
        for (var line: lines) {
            if (line.startsWith("Tile")) {
                tileLines = new ArrayList<>();
                tileId = Integer.parseInt(line.replaceAll("(Tile )|:", ""));
            } else if (line.isBlank()) {
                tiles.add(new Tile(tileId, tileLines));
            } else {
                tileLines.add(line);
            }
        }
    }

    public static void main(String[] args) {
        Day20 solution = new Day20("day20test");

        solution.part1();
        solution.part2();
    }

    private void part1() {
        var edgeCount = tiles.stream()
                .flatMap(tile -> tile.getEdges().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(edgeCount);

    }

    private void part2() {
    }

    class Tile {
        private int id;
        private List<String> tile;
        private Set<String> edges;

        public Tile(int id, List<String> tile) {
            this.id = id;
            this.tile = tile;
            edges = new HashSet<>();

            final int size = tile.size();

            edges.add(tile.get(0));
            edges.add(tile.get(size - 1));
            edges.add(tile.stream().map(s -> s.substring(0, 1)).collect(Collectors.joining()));
            edges.add(tile.stream().map(s -> s.substring(size - 1, size)).collect(Collectors.joining()));
        }

        public List<Set<String>> getEdges() {
            final int size = tile.size();

            String e1 = tile.get(0);
            String e2 = tile.get(size - 1);
            String e3 = tile.stream().map(s -> s.substring(0, 1)).collect(Collectors.joining());
            String e4 = tile.stream().map(s -> s.substring(size - 1, size)).collect(Collectors.joining());

            return List.of(
                    Set.of(e1, e2, e3, e4),
                    Set.of(StringUtils.reverse(e1), StringUtils.reverse(e2), e3, e4),
                    Set.of(e1, e2, StringUtils.reverse(e3), StringUtils.reverse(e4)),
                    Set.of(StringUtils.reverse(e1), StringUtils.reverse(e2), StringUtils.reverse(e3), StringUtils.reverse(e4))
            );
        }


        public int getId() {
            return id;
        }

    }
}
