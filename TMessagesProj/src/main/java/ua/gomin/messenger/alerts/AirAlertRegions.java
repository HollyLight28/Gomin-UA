package ua.gomin.messenger.alerts;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Єдине джерело правди для списку регіонів України.
 * <p>
 * Всі інші класи (AirAlertController, AirAlertStatsActivity, GominSettingsEntry)
 * використовують цей клас замість того, щоб дублювати список вручну.
 */
public class AirAlertRegions {

    /** (id, назва) — id як String, назва як String */
    private static final List<Pair<String, String>> REGIONS_STRING = Arrays.asList(
        new Pair<>("1", "Вінницька"), new Pair<>("2", "Волинська"), new Pair<>("3", "Дніпропетровська"), new Pair<>("4", "Донецька"),
        new Pair<>("5", "Житомирська"), new Pair<>("6", "Закарпатська"), new Pair<>("7", "Запорізька"), new Pair<>("8", "Івано-Франківська"),
        new Pair<>("9", "Київська"), new Pair<>("10", "Кіровоградська"), new Pair<>("11", "Луганська"), new Pair<>("12", "Львівська"),
        new Pair<>("13", "Миколаївська"), new Pair<>("14", "Одеська"), new Pair<>("15", "Полтавська"), new Pair<>("16", "Рівненська"),
        new Pair<>("17", "Сумська"), new Pair<>("18", "Тернопільська"), new Pair<>("19", "Харківська"), new Pair<>("20", "Херсонська"),
        new Pair<>("21", "Хмельницька"), new Pair<>("22", "Черкаська"), new Pair<>("23", "Чернівецька"), new Pair<>("24", "Чернігівська"),
        new Pair<>("25", "м. Київ"), new Pair<>("26", "АР Крим")
    );

    /** (id, назва) — id як Integer, назва як String (для AirAlertStatsActivity) */
    private static final List<Pair<Integer, String>> REGIONS_INT = new ArrayList<>();
    static {
        for (Pair<String, String> r : REGIONS_STRING) {
            REGIONS_INT.add(new Pair<>(Integer.parseInt(r.first), r.second));
        }
    }

    /** Повертає список (id:String, name) — для AirAlertController.fetchRegions() */
    public static List<Pair<String, String>> getAllAsString() {
        return REGIONS_STRING;
    }

    /** Повертає список (id:Integer, name) — для AirAlertStatsActivity */
    public static List<Pair<Integer, String>> getAllAsInt() {
        return REGIONS_INT;
    }

    /** Повертає масив назв регіонів — для PopupHelper (GominSettingsEntry) */
    public static String[] getNames() {
        String[] names = new String[REGIONS_STRING.size()];
        for (int i = 0; i < REGIONS_STRING.size(); i++) {
            names[i] = REGIONS_STRING.get(i).second;
        }
        return names;
    }

    /** Повертає масив ID регіонів як String — для PopupHelper (GominSettingsEntry) */
    public static String[] getIds() {
        String[] ids = new String[REGIONS_STRING.size()];
        for (int i = 0; i < REGIONS_STRING.size(); i++) {
            ids[i] = REGIONS_STRING.get(i).first;
        }
        return ids;
    }

    /** Отримати назву регіону за його ID (String) */
    public static String getNameById(String id) {
        for (Pair<String, String> r : REGIONS_STRING) {
            if (r.first.equals(id)) return r.second;
        }
        return "";
    }

    /** Отримати назву регіону за його ID (Integer) */
    public static String getNameById(int id) {
        return getNameById(String.valueOf(id));
    }
}
