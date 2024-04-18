package ua.holovchenko;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Instant start = Instant.now();
        final String dirPath = args[0];
        final String attribute = args[1];
        final int threadNum = 4;
        File dir = new File(dirPath);
        validateDir(dir);
        List<File> jsonFiles = chooseJsons(dir);
        List<JsonFileStatsCalculator> calculators = jsonFiles.stream()
                .map(it -> new JsonFileStatsCalculator(it, attribute)).toList();
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        List<Future<Map<String, Integer>>> calculationsList = executor.invokeAll(calculators);
        List<Map<String, Integer>> filesResults = calculationsList.stream().map(it -> {
            try {
                return it.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        Map<String, Integer> result = summarize(filesResults);
        Map<String, Integer> sortedResult = sortMapByValue(result);
        XmlStatsWriter writer = new XmlStatsWriter(attribute);
        writer.writeStatsToXML(sortedResult);
        System.out.println("Program finished in " + (Instant.now().compareTo(start)));
        System.exit(0);
    }

    private static Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
        Map<String, Integer> sorted = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(it -> sorted.put(it.getKey(), it.getValue()));
        return sorted;
    }

    private static Map<String, Integer> summarize(List<Map<String, Integer>> filesResults) {
        Map<String, Integer> result = new TreeMap<>();
        filesResults.forEach(map -> map.forEach((key, value) -> {
            if (result.containsKey(key)) {
                result.put(key, result.get(key) + value);
            } else result.put(key, value);
        }));
        return result;
    }


    private static void validateDir(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Invalid directory provided!");
            System.exit(1);
        }
    }

    private static List<File> chooseJsons(File dir) {
        List<File> filteredList = Arrays.stream(dir.listFiles())
                .filter(it -> it.getName().endsWith(".json")).toList();
        if (filteredList.isEmpty()) {
            System.err.println("No JSON files found in provided directory!");
            System.exit(2);
        }
        return filteredList;
    }
}