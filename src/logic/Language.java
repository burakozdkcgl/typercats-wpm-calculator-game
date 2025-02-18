package logic;

import java.util.HashMap;
import java.util.Map;

public class Language {

    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static{

        //English Translations
        Map<String, String> english = new HashMap<>();
        english.put("title", "Typing Analysis Tool");
        english.put("start_test", "Start Typing Test");
        english.put("view_scores", "View Scores");
        english.put("settings", "Settings");
        english.put("exit", "Exit");
        english.put("difficulty", "Difficulty");
        english.put("language", "Language");
        english.put("timer_duration", "Timer Duration");
        english.put("back_to_menu", "Back to Menu");
        english.put("typing_test", "Typing Test");
        english.put("results", "Typing Test Results");
        english.put("easy", "Easy");
        english.put("medium", "Medium");
        english.put("hard", "Hard");
        english.put("20 seconds", "20 seconds");
        english.put("1 minute", "1 minute");
        english.put("3 minutes", "3 minutes");
        english.put("timer_left","Time left:");
        english.put("wpm","WPM");
        english.put("accuracy","Accuracy (%)");
        english.put("date","Date");
        english.put("timer","Timer");
        english.put("wordCount","Word Count");
        english.put("keyStrokeCount","Keystrokes");
        english.put("english","English");
        english.put("turkish","Türkçe");
        english.put("reset_scores", "Reset Scores");
        english.put("reset_scores_confirmation", "Are you sure you want to reset all scores?");
        english.put("reset_scores_success", "Scores reset successfully!");
        english.put("confirmation", "Confirmation");
        english.put("success", "Success");
        english.put("yes", "Yes");
        english.put("no", "No");
        english.put("ok", "OK");
        english.put("restart_test", "Restart Test");
        english.put("total_words", "Total Words");
        english.put("correct_words", "Correct Words");
        english.put("incorrect_words", "Incorrect Words");
        english.put("correct_keystrokes", "Correct Keystrokes");
        english.put("incorrect_keystrokes", "Incorrect Keystrokes");
        english.put("test_results", "Test Results");
        english.put("achievements", "Achievements");
        english.put("at1","One Mistake Only");
        english.put("at2","Lightning Mcqueen");
        english.put("at3","I Lost My Keys!");
        english.put("at4","Challenge Accepted");
        english.put("at5","Determined");
        english.put("at6","Explorer");
        english.put("at7","Addicted");
        english.put("at8","Polyglot");
        english.put("at9","No Room For Error");
        english.put("at10","Sharp Shooter");
        english.put("at11","Pro");
        english.put("at12","Enthusiast");
        english.put("ad1","Finish a test with only one incorrect keystroke");
        english.put("ad2","Reach 50+ WPM in five different test");
        english.put("ad3","Hit 5,000 correct keystrokes across all tests");
        english.put("ad4","Finish three tests on Hard mode with all words correct");
        english.put("ad5","Play the game on 10 different days");
        english.put("ad6","Play at least one test in three different difficulties");
        english.put("ad7","Spend one hour testing");
        english.put("ad8","Complete at least one test in two different languages");
        english.put("ad9","Complete 10 tests with %100 accuracy");
        english.put("ad10","Achieve 100% accuracy in three consecutive tests.");
        english.put("ad11","Achieve +50 WPM in three consecutive tests.");
        english.put("ad12","Finish 50 tests.");
        english.put("rats_caught","Rats caught");
        english.put("customs","Custom Game Modes (Work In Progress)");
        english.put("dead_mode","Dead Mode!");
        english.put("dead_mode_description","No Timer! The test ends as soon as you make your first incorrect keystroke! A challenging mode for ultimate accuracy.");
        english.put("rat_mode","Rat Minigame!");
        english.put("rat_mode_description","Defend your cheese! Rats come from the left and right sides of the screen with text above them. Type the text quickly to chase them away before they reach the center. You start with 5 pieces of cheese, and the test ends when you run out of cheese!");




        // Turkish Translations
        Map<String, String> turkish = new HashMap<>();
        turkish.put("title", "Yazma Analizi Aracı");
        turkish.put("start_test", "Yazma Testine Başla");
        turkish.put("view_scores", "Puanları Gör");
        turkish.put("settings", "Ayarlar");
        turkish.put("exit", "Çıkış");
        turkish.put("difficulty", "Zorluk");
        turkish.put("language", "Dil");
        turkish.put("timer_duration", "Zamanlayıcı Süresi");
        turkish.put("back_to_menu", "Menüye Dön");
        turkish.put("typing_test", "Yazma Testi");
        turkish.put("results", "Yazma Testi Sonuçları");
        turkish.put("easy", "Kolay");
        turkish.put("medium", "Orta");
        turkish.put("hard", "Zor");
        turkish.put("20 seconds", "20 saniye");
        turkish.put("1 minute", "1 dakika");
        turkish.put("3 minutes", "3 dakika");
        turkish.put("timer_left","Kalan zaman:");
        turkish.put("wpm","DBK");
        turkish.put("accuracy","Doğruluk (%)");
        turkish.put("date","Tarih");
        turkish.put("timer","Zamanlayıcı");
        turkish.put("wordCount","Kelime Sayısı");
        turkish.put("keyStrokeCount","Tuş Vuruşu");
        turkish.put("english","English");
        turkish.put("turkish","Türkçe");
        turkish.put("reset_scores", "Puanları Sıfırla");
        turkish.put("reset_scores_confirmation", "Tüm puanları sıfırlamak istediğinizden emin misiniz?");
        turkish.put("reset_scores_success", "Puanlar başarıyla sıfırlandı!");
        turkish.put("confirmation", "Onay");
        turkish.put("success", "Başarılı");
        turkish.put("yes", "Evet");
        turkish.put("no", "Hayır");
        turkish.put("ok", "Tamam");
        turkish.put("restart_test", "Yeniden Başlat");
        turkish.put("total_words", "Toplam Kelime Sayısı");
        turkish.put("correct_words", "Doğru Kelime Sayısı");
        turkish.put("incorrect_words", "Yanlış Kelime Sayısı");
        turkish.put("correct_keystrokes", "Doğru Tuş Vuruşu");
        turkish.put("incorrect_keystrokes", "Yanlış Tuş Vuruşu");
        turkish.put("test_results", "Test Sonuçları");
        turkish.put("achievements", "Başarımlar");
        turkish.put("at1","Nazar Boncuğu");
        turkish.put("at2","Şimşek Mcqueen");
        turkish.put("at3","Klavye Delikanlısı");
        turkish.put("at4","Usta");
        turkish.put("at5","Azimli");
        turkish.put("at6","Meraklı");
        turkish.put("at7","Bağımlı");
        turkish.put("at8","Poliglot");
        turkish.put("at9","Kusursuz");
        turkish.put("at10","Keskin Nişancı");
        turkish.put("at11","Şovmen");
        turkish.put("at12","Hevesli");
        turkish.put("ad1","Tek bir vuruş hatasıyla testi bitir");
        turkish.put("ad2","Beş farklı teste 50+ DBK'ye ulaş");
        turkish.put("ad3","5000 doğru tuş vuruşuna ulaş");
        turkish.put("ad4","3 zor mod testini kelime hatasız bitir");
        turkish.put("ad5","10 farklı gün teste gir");
        turkish.put("ad6","Üç farklı zorluk modunda en az bir test bitir");
        turkish.put("ad7","Test çözerken bir saat geçir");
        turkish.put("ad8","İki farklı dil modunda en az bir test bitir");
        turkish.put("ad9","%100 doğrulukla 10 test bitir");
        turkish.put("ad10","Art arda üç kez %100 doğruluğa ulaş");
        turkish.put("ad11","Art arda üç kez +50 DBK'ye ulaş");
        turkish.put("ad12","50 test bitir.");
        turkish.put("rats_caught","Yakalanan fareler");
        turkish.put("customs","Özel Oyun Modları (Yapım Aşamasında)");
        turkish.put("dead_mode", "Ölüm Modu!");
        turkish.put("dead_mode_description", "Zamanlayıcı yok! İlk yanlış tuş vuruşunuzu yaptığınız anda test sona erer! Maksimum doğruluk isteyenler için zorlayıcı bir mod.");
        turkish.put("rat_mode","Fare Minioyunu!");
        turkish.put("rat_mode_description","Peyniri koru! Ekranın sağından ve solundan gelen fareleri kov. Onları kovalamak için üzerlerindeki metinleri hızlıca yaz! 5 adet peynirle başlarsınız ve peyniriniz bittiğinde test sona erer!");

        translations.put("english", english);
        translations.put("turkish", turkish);
        
    }

    public static String getText(String key) {
        Map<String, String> langMap = translations.get(Settings.getLanguage().toLowerCase());
        return langMap != null ? langMap.getOrDefault(key, key) : key;
    }

}
