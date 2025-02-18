package logic;

import java.io.*;

public class Settings {

    private static String difficulty;
    private static String language;
    private static String timerDuration;
    private static final String SETTINGS_FILE = "settings.txt";


    public static void getInstance(){
        // Load settings from file when initialized
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "difficulty" -> difficulty = parts[1];
                        case "language" -> language = parts[1];
                        case "timerDuration" -> timerDuration = parts[1];
                    }
                }
            }
        } catch (IOException ignored) {

        }
    }

    public static String getDifficulty() {
        String currentDifficulty;
        if (difficulty == null) currentDifficulty = "medium";
        else if (difficulty.equals("null"))   currentDifficulty = "medium";
        else currentDifficulty = difficulty;
        return currentDifficulty; // Default to "medium" if not set
    }

    public static void setDifficulty(String difficulty) {
        Settings.difficulty = difficulty;
        saveSettingsToFile();
    }

    public static String getLanguage() {
        String currentLanguage;
        if (language == null) currentLanguage = "english";
        else if (language.equals("null"))   currentLanguage = "english";
        else currentLanguage = language;
        return currentLanguage; // Default to "english" if not set
    }

    public static void setLanguage(String language) {
        Settings.language = language;
        saveSettingsToFile();
    }

    public static String getTimerDuration() {
        String currentTimer;
        if (timerDuration == null) currentTimer = "20 seconds";
        else if (timerDuration.equals("null"))   currentTimer = "20 seconds";
        else currentTimer = timerDuration;
        return currentTimer; // Default to "english" if not set
    }

    public static void setTimerDuration(String timerDuration) {
        Settings.timerDuration = timerDuration;
        saveSettingsToFile();
    }

    // Save settings to a file
    private static void saveSettingsToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_FILE))) {
            writer.write("difficulty=" + difficulty + "\n");
            writer.write("language=" + language + "\n");
            writer.write("timerDuration=" + timerDuration + "\n");
        } catch (IOException ignored) {
        }
    }


}
