package ua.holovchenko;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JsonFileStatsCalculatorTest {
    @Test
    public void jsonFileStatsCalculatorTest() throws ExecutionException, InterruptedException, IOException {
        //before
        InputStream inputStream = getClass().getResourceAsStream("/TestFile.json");
        if (inputStream == null) {
            throw new FileNotFoundException("TestFile.json not found in test resources.");
        }
        File testFile = convertInputStreamToFile(inputStream);
        JsonFileStatsCalculator testCalculator = new JsonFileStatsCalculator(testFile, "atr");
        ExecutorService exe = Executors.newSingleThreadExecutor();
        Map<String, Integer> expected = Map.of("a", 4, "b", 3, "c", 2, "d", 1);
        //then
        Future<Map<String, Integer>> future = exe.submit(testCalculator);
        Map<String, Integer> result = future.get();
        //after
        Assertions.assertEquals(expected, result);
    }
    private File convertInputStreamToFile(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("temp", ".json");
        tempFile.deleteOnExit();
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
        return tempFile;
    }
}
