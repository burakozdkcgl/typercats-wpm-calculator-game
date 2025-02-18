package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Score {

    public static String SCORES_FILE = "scores.txt";
    private final String date;
    private final String difficulty;
    private final String language;
    private final String timer_duration;
    private final int correctWordsCount;
    private final int incorrectWordsCount;
    private final int correctKeyStroke;
    private final int incorrectKeyStroke;
    private static ArrayList<Score> scoreList;

    public Score(
            String date,
            String difficulty,
            String language,
            String timer_duration,
            int correctWordsCount,
            int incorrectWordsCount,
            int correctKeyStroke,
            int incorrectKeyStroke
    ) {
        this.date = date;
        this.difficulty = difficulty;
        this.language = language;
        this.timer_duration = timer_duration;
        this.correctWordsCount = correctWordsCount;
        this.incorrectWordsCount = incorrectWordsCount;
        this.correctKeyStroke = correctKeyStroke;
        this.incorrectKeyStroke = incorrectKeyStroke;
    }

    public String getDate() { return date;}

    public String getDifficulty() { return difficulty;}

    public String getLanguage() { return language;}

    public String getTimerDuration() { return timer_duration;}

    public int getCorrectWordsCount() { return correctWordsCount;}

    public int getIncorrectWordsCount() { return incorrectWordsCount;}


    public double getWPM() {
        int secondsPassed = 0;
        if (Objects.equals(timer_duration, "20 seconds")) secondsPassed = 20;
        if (Objects.equals(timer_duration, "1 minute")) secondsPassed = 60;
        if (Objects.equals(timer_duration, "3 minutes")) secondsPassed = 180;
        return correctWordsCount / ((secondsPassed) / 60.0);
    }

    public int getCorrectKeyStroke() { return correctKeyStroke;}

    public int getIncorrectKeyStroke() { return incorrectKeyStroke;}

    public double getAccuracy() { return (((double) correctKeyStroke / (correctKeyStroke+incorrectKeyStroke)) * 100);}

    public static ArrayList<Score> getScoreList() {

        scoreList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) { // Ensure the line has exactly 8 fields
                    String date = parts[0];
                    String difficulty = parts[1];
                    String language = parts[2];
                    String timer_duration = parts[3];
                    int correctWordsCount = Integer.parseInt(parts[4]);
                    int incorrectWordsCount = Integer.parseInt(parts[5]);
                    int correctKeyStroke = Integer.parseInt(parts[6]);
                    int incorrectKeyStroke = Integer.parseInt(parts[7]);

                    scoreList.add(new Score(
                            date, difficulty, language, timer_duration,
                            correctWordsCount, incorrectWordsCount, correctKeyStroke, incorrectKeyStroke
                    ));
                }
            }
        } catch (IOException ignored) {
        }

        return scoreList;
    }


    // Method to add a score and save to file
    public static void addScore(Score score) {
        getScoreList().add(score);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) {
            for (Score i : scoreList) {
                writer.write(i.getDate() + "," + i.getDifficulty() + "," + i.getLanguage() + ","
                        + i.getTimerDuration() + "," + i.getCorrectWordsCount() + ","
                        + i.getIncorrectWordsCount() + "," + i.getCorrectKeyStroke()
                        + "," + i.getIncorrectKeyStroke() + "\n");
            }
        } catch (IOException ignored) {
        }
    }

    //method to reset scores
    public static void resetScores() {
        scoreList=null;
        try {
            new FileWriter("scores.txt", false).close(); // Clears the file by overwriting it with nothing
        } catch (IOException ignored) {
        }
    }

}
