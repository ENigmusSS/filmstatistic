package ua.holovchenko;

import com.jayway.jsonpath.JsonPath;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class JsonFileStatsCalculator implements Callable<Map<String, Integer>> {
    private final File jsonFile;
    private final String attribute;

    public JsonFileStatsCalculator(File jsonFile, String attribute) {
        this.jsonFile = jsonFile;
        this.attribute = attribute;
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        List<List<String>> attributeValues = JsonPath.read(jsonFile, "$[*]." + attribute);
        Map<String, Integer> statistic = new HashMap<>();
        attributeValues.forEach(list -> list.forEach(it -> {
            if (statistic.containsKey(it)) {
                statistic.put(it, statistic.get(it) + 1);
            } else statistic.put(it, 1);
        }));
        return statistic;
    }
}
