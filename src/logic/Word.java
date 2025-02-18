package logic;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Word {

    private static final URL WORDS_FILE = Word.class.getResource("/words.txt");
    private static final Map<String, List<String>> wordCategories = new HashMap<>();

    public static void loadWords() {

        wordCategories.clear();


            InputStream inputStream = Word.class.getResourceAsStream("/words.txt");

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    // Split the line into category and words
                    String[] parts = line.split(" ", 2);
                    if (parts.length < 2) continue; // Skip invalid lines

                    String category = parts[0]; // e.g., "english_easy"
                    String[] words = parts[1].split(" "); // Words split by space

                    wordCategories.put(category, new ArrayList<>(Arrays.asList(words)));
                }
            } catch (IOException ignored) {
            }

            wordCategories.values().forEach(Collections::shuffle);

    }

    public static List<String> getWords(String category) {
        return wordCategories.getOrDefault(category, new ArrayList<>());
    }
}
