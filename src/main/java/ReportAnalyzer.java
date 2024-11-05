import minpq.MinPQ;
import minpq.OptimizedHeapMinPQ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Display the most commonly-reported WCAG recommendations.
 */
public class ReportAnalyzer {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("data/wcag.tsv");
        Map<String, String> wcagDefinitions = new LinkedHashMap<>();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            String index = "wcag" + line[0].replace(".", "");
            String title = line[1];
            wcagDefinitions.put(index, title);
        }

        Pattern re = Pattern.compile("wcag\\d{3,4}");
        List<String> wcagTags = Files.walk(Paths.get("data/reports"))
                .map(path -> {
                    try {
                        return Files.readString(path);
                    } catch (IOException e) {
                        return "";
                    }
                })
                .flatMap(contents -> re.matcher(contents).results())
                .map(MatchResult::group)
                .toList();

        //testFunction();

        //System.out.println(wcagTags);
        Map<String, Double> map = new HashMap<>();

        for(String s : wcagTags) {
            if (map.containsKey(s)) {
                map.replace(s, map.get(s) - 1);
            }
            else {
                map.put(s, wcagTags.size() * 1.0);
            }
        }
        //Assemble tags and how often they occur into array, then

        //throw everything into here
        MinPQ<String> testing = new OptimizedHeapMinPQ<>(map/* this should contain a map with correct priority values*/);

        System.out.println(testing.removeMin());
        System.out.println(testing.removeMin());
        System.out.println(testing.removeMin());

        //Use removeMin() three times to get the target data
    }


    public static void testFunction() {
        String[] data = {"1", "2", "2", "3", "3", "3", "4", "4", "4", "4", "5", "5", "5"};

        Map<String, Double> map = new HashMap<>();

        for(String s : data) {
            if (map.containsKey(s)) {
                map.replace(s, map.get(s) - 1);
            }
            else {
                map.put(s, data.length * 1.0);
            }
        }
        //Assemble tags and how often they occur into array, then

        //throw everything into here
        MinPQ<String> testing = new OptimizedHeapMinPQ<>(map/* this should contain a map with correct priority values*/);


        System.out.println(testing.removeMin());
        System.out.println(testing.removeMin());
        System.out.println(testing.removeMin());
        System.out.println(testing.removeMin());
        System.out.println(testing.removeMin());
    }
}