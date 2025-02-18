package logic;

public class Achievement {

    String title;
    String description;
    int value;
    int number;
    public Achievement(int i) {
        String[] Labels = new String[]{
                Language.getText("at1"),
                Language.getText("at2"),
                Language.getText("at3"),
                Language.getText("at4"),
                Language.getText("at5"),
                Language.getText("at6"),
                Language.getText("at7"),
                Language.getText("at8"),
                Language.getText("at9"),
                Language.getText("at10"),
                Language.getText("at11"),
                Language.getText("at12"),
                Language.getText("ad1"),
                Language.getText("ad2"),
                Language.getText("ad3"),
                Language.getText("ad4"),
                Language.getText("ad5"),
                Language.getText("ad6"),
                Language.getText("ad7"),
                Language.getText("ad8"),
                Language.getText("ad9"),
                Language.getText("ad10"),
                Language.getText("ad11"),
                Language.getText("ad12"),
        };
        this.title = Labels[i];
        this.description = Labels[i+12];
        number = i;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress(){
        int[] values = new int[]{a1(), a2(), a3(), a4(), a5(), a6(), a7(), a8(), a9(), a10(), a11(), a12()};
        value = values[number];
        return value;
    }

    private int a1() {
        // "One Mistake Only" - Finish a test with only one incorrect keystroke
        return (int) Score.getScoreList().stream()
                .filter(score -> score.getIncorrectKeyStroke() == 1)
                .count() > 0 ? 100 : 0; // 100% if any test matches, 0% otherwise
    }

    private int a2() {
        // "Lightning Mcqueen" - Reach 50+ WPM in five different tests
        long count = Score.getScoreList().stream()
                .filter(score -> score.getWPM() >= 50)
                .count();
        return Math.min((int) (count * 20), 100); // Progress based on the count, max 100%
    }

    private int a3() {
        // "Key Conqueror" - Hit 5,000 correct keystrokes across all tests
        int totalKeystrokes = Score.getScoreList().stream()
                .mapToInt(Score::getCorrectKeyStroke)
                .sum();
        return Math.min((totalKeystrokes * 100) / 5000, 100); // Progress percentage
    }

    private int a4() {
        // "Challenge Accepted" - Finish three tests on Hard mode without a single mistake
        long count = Score.getScoreList().stream()
                .filter(score -> score.getDifficulty().equalsIgnoreCase("hard") && score.getIncorrectWordsCount() == 0)
                .count();
        return Math.min((int) (count * 34), 100); // Progress percentage
    }

    private int a5() {
        // "Determined" - Play the game on 10 different days
        long uniqueDays = Score.getScoreList().stream()
                .map(Score::getDate)
                .distinct()
                .count();
        return Math.min((int) (uniqueDays * 10), 100); // Progress percentage
    }

    private int a6() {
        // "Explorer" - Play at least one test in three different difficulties
        long uniqueDifficulties = Score.getScoreList().stream()
                .map(Score::getDifficulty)
                .distinct()
                .count();
        return Math.min((int) (uniqueDifficulties * 34), 100); // Progress percentage
    }

    private int a7() {
        // "Addicted" - Spend one hour testing
        int totalTime = Score.getScoreList().stream()
                .mapToInt(score -> switch (score.getTimerDuration()) {
                    case "20 seconds" -> 20;
                    case "1 minute" -> 60;
                    case "3 minutes" -> 180;
                    default -> 0;
                })
                .sum();
        return Math.min((totalTime * 100) / 3600, 100); // Progress percentage
    }

    private int a8() {
        // "Polyglot" - Complete at least one test in two different languages
        long uniqueLanguages = Score.getScoreList().stream()
                .map(Score::getLanguage)
                .distinct()
                .count();
        return Math.min((int) (uniqueLanguages * 50), 100); // Progress percentage
    }

    private int a9() {
        // "No Room for Error" - Complete 10 tests with 100% accuracy
        long count = Score.getScoreList().stream()
                .filter(score -> score.getAccuracy() == 100)
                .count();
        return Math.min((int) (count * 10), 100); // Progress percentage
    }

    private int a10() {
        // "Sharp Shooter" - Achieve 100% accuracy in three consecutive tests
        int consecutiveCount = 0;
        int maxConsecutive = 0;
        for (Score score : Score.getScoreList()) {
            if (score.getAccuracy() == 100) {
                consecutiveCount++;
                maxConsecutive = Math.max(maxConsecutive, consecutiveCount);
            } else {
                consecutiveCount = 0;
            }
        }
        return Math.min((maxConsecutive * 34), 100); // Progress percentage
    }

    private int a11() {
        // "Pro" - Achieve 50+ WPM in three consecutive tests
        int consecutiveCount = 0;
        int maxConsecutive = 0;
        for (Score score : Score.getScoreList()) {
            if (score.getWPM() >= 50) {
                consecutiveCount++;
                maxConsecutive = Math.max(maxConsecutive, consecutiveCount);
            } else {
                consecutiveCount = 0;
            }
        }
        return Math.min((maxConsecutive * 34), 100); // Progress percentage
    }

    private int a12() {
        // "Tester" - Finish 50 tests
        int totalTests = Score.getScoreList().size();
        return Math.min((totalTests * 2), 100); // Progress percentage
    }



}
