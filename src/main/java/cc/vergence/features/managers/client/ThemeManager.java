package cc.vergence.features.managers.client;

import cc.vergence.Vergence;
import cc.vergence.features.themes.DefaultTheme;
import cc.vergence.features.themes.Theme;
import cc.vergence.util.color.HexColor;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import static cc.vergence.features.managers.client.ConfigManager.THEMES_FOLDER;

public class ThemeManager {
    private static Theme currentTheme = null;
    public Theme defaultTheme;

    public ThemeManager() {
        defaultTheme = new DefaultTheme();
        currentTheme = defaultTheme;
    }

    public void loadTheme(Theme theme) {
        if (theme == defaultTheme) {
            Vergence.CONSOLE.logWarn("Theme unload failed. It's default theme.");
        }
        currentTheme = theme;
    }

    public void unloadTheme() {
        Vergence.CONSOLE.logInfo("unloading theme ...");
        currentTheme = new DefaultTheme();
        Vergence.CONSOLE.logInfo("Theme unloaded, now the theme is default theme.");
    }

    public Theme getTheme() {
        return currentTheme;
    }

    public JsonObject dumpToJson() {
        JsonObject root = new JsonObject();
        JsonObject info = new JsonObject();
        info.addProperty("id", currentTheme.getName());
        info.addProperty("name", currentTheme.getDisplayName());
        info.addProperty("description", currentTheme.getDescription());
        JsonArray authors = new JsonArray();
        currentTheme.getAuthors().forEach(authors::add);
        info.add("authors", authors);
        root.add("info", info);
        JsonObject colors = new JsonObject();
        for (Method method : Theme.class.getDeclaredMethods()) {
            if (method.getName().startsWith("get") && method.getReturnType() == Color.class) {
                try {
                    String fieldName = decapitalize(method.getName().substring(3));
                    Color color = (Color) method.invoke(currentTheme);
                    colors.addProperty(fieldName, new HexColor(color).getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        root.add("colors", colors);
        return root;
    }

    public Theme loadFromJson(JsonObject root) {
        try {
            JsonObject info = root.getAsJsonObject("info");
            String id = info.get("id").getAsString();
            String name = info.get("name").getAsString();
            String desc = info.get("description").getAsString();
            JsonArray authorsArr = info.getAsJsonArray("authors");
            ArrayList<String> authors = new ArrayList<>();
            authorsArr.forEach(e -> authors.add(e.getAsString()));
            Theme theme = new Theme(id, name, desc, authors);
            JsonObject colors = root.getAsJsonObject("colors");
            for (Map.Entry<String, JsonElement> entry : colors.entrySet()) {
                String fieldName = entry.getKey();
                String hex = entry.getValue().getAsString();
                Color color = new HexColor(hex).getValueAsColor();
                try {
                    String setterName = "set" + capitalize(fieldName);
                    Method setter = Theme.class.getDeclaredMethod(setterName, Color.class);
                    setter.invoke(theme, color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return theme;
        } catch (Exception ex) {
            Vergence.CONSOLE.logError("Failed to load theme from JSON: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public void saveTheme(String fileName, JsonObject json) {
        File file = new File(THEMES_FOLDER, fileName + ".json");
        try (Writer w = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(json, w);
        } catch (IOException e) {
            Vergence.CONSOLE.logError("Failed to save theme: " + fileName);
        }
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static String decapitalize(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
