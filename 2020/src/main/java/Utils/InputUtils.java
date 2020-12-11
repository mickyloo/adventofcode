package Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputUtils {

    public static List<String> readFile(String name) {
        InputStream is = InputUtils.class.getClassLoader().getResourceAsStream(name);
        ArrayList<String> lines = new ArrayList<>();

        try (Scanner scan = new Scanner((is))) {
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
        }

        return lines;
    }
}
