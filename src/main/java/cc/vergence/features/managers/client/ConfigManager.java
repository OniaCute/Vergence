package cc.vergence.features.managers.client;

import cc.vergence.Vergence;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.EnumUtil;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import cc.vergence.modules.Module;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Scanner;

public class ConfigManager implements Wrapper {
    public static final String CONFIG_FOLDER_NAME = "Vergence";
    public static final File MAIN_FOLDER = new File(mc.runDirectory, CONFIG_FOLDER_NAME);
    public static final File CONFIG_FOLDER = new File(MAIN_FOLDER, "configs");
    public static final File SOUNDS_FOLDER = new File(MAIN_FOLDER, "sounds");
    public static final File THEMES_FOLDER = new File(MAIN_FOLDER, "themes");
    public static final File MISC_FOLDER = new File(MAIN_FOLDER, "misc");
    public static final File MISC_SPAMMER_FOLDER = new File(MISC_FOLDER, "spammer");
    public static final File MISC_ADVERTISER_FOLDER = new File(MISC_FOLDER, "advertiser");
    public static final File FONTS_FOLDER = new File(MAIN_FOLDER, "fonts");
    public File currentConfig = null;
    public String currentConfigName = "default";
    public static boolean firstUse = false;

    public ConfigManager() {
        if (MAIN_FOLDER.isFile() || !MAIN_FOLDER.exists()) {
            firstUse = true;
        }

        ensureDirectory(MAIN_FOLDER);
        ensureDirectory(CONFIG_FOLDER);
        ensureDirectory(SOUNDS_FOLDER);
        ensureDirectory(THEMES_FOLDER);
        ensureDirectory(MISC_FOLDER);
        ensureDirectory(MISC_SPAMMER_FOLDER);
        ensureDirectory(MISC_ADVERTISER_FOLDER);
        ensureDirectory(FONTS_FOLDER);

        File defaultSpammer = new File(MISC_SPAMMER_FOLDER + "default.txt");
        File defaultAdvertiser = new File(MISC_ADVERTISER_FOLDER + "default.txt");

        if (firstUse || !defaultSpammer.exists() || !defaultSpammer.isFile()) {
            ArrayList<String> defaultValue = new ArrayList<>();
            defaultValue.add("Get Vergence Get Good.");
            defaultValue.add("Get Vergence Get Unique Sense Of Minecraft.");
            defaultValue.add("Get Vergence In https://github.com/OniaCute/Vergence");
            createDefaultFile(MISC_SPAMMER_FOLDER, "default.txt", defaultValue);
        }
        if (firstUse || !defaultAdvertiser.exists() || !defaultAdvertiser.isFile()) {
            ArrayList<String> defaultValue = new ArrayList<>();
            defaultValue.add("Get Vergence Get Good.");
            defaultValue.add("Get Vergence Get Unique Sense Of Minecraft.");
            defaultValue.add("Get Vergence In https://github.com/OniaCute/Vergence");
            createDefaultFile(MISC_ADVERTISER_FOLDER, "default.txt", defaultValue);
        }

        File defaultThemeFile = new File(THEMES_FOLDER, "default.json");

        if (defaultThemeFile.exists()) {
            try {
                defaultThemeFile.delete();
            } catch (Exception _E) {
                _E.printStackTrace();
            }
        }

        try (InputStream in = getClass().getResourceAsStream("/assets/vergence/themes/default.json");
             OutputStream out = Files.newOutputStream(defaultThemeFile.toPath())) {
            if (in != null) {
                Files.createDirectories(defaultThemeFile.getParentFile().toPath());
                IOUtils.copy(in, out);
                Vergence.CONSOLE.logInfo("Copied default theme to " + defaultThemeFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Vergence.CONSOLE.logError("Failed to copy default theme");
            e.printStackTrace();
        }
    }

    private void ensureDirectory(File folder) {
        if (folder.exists()) {
            if (!folder.isDirectory()) {
                Vergence.CONSOLE.logWarn("Default directory is not available : " + folder.getName() + "  (already delete and remade)");
                folder.delete();
                folder.mkdirs();
            }
        } else {
            folder.mkdirs();
        }
    }

    private void createDefaultFile(File folder, String fileName, ArrayList<String> lines) {
        File file = new File(folder, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String s : lines) {
                    writer.write(s);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                Vergence.CONSOLE.logError("Failed to create default file: " + folder.getName() + "/" + fileName);
            }
        }
    }

    public boolean load(String name) {
        File file = new File(CONFIG_FOLDER, name + ".vgc");
        if (!file.exists()) {
            Vergence.CONSOLE.logError("File \"" + file.getName() + "\" doesn't found!");
            return false;
        }

        if (currentConfig != null) {
            save(currentConfig);
        }
        load(file);
        return true;
    }

    public void load(@NotNull File config) {
        if (!config.exists()) {
            save(config);
        }
        try {
            FileReader reader = new FileReader(config);
            JsonParser parser = new JsonParser();

            JsonArray array = null;
            try {
                array = (JsonArray) parser.parse(reader);
            } catch (ClassCastException e) {
                save(config);
            }

            JsonArray modules = null;
            JsonArray friends = null;
            JsonArray enemies = null;
            JsonArray client = null;
            JsonObject mainObject = array.get(0).getAsJsonObject();

            int moduleCounter = 0;

            if (mainObject.has("Modules")) {
                modules = mainObject.getAsJsonArray("Modules");
                for (JsonElement element : modules) {
                    moduleCounter ++;
                    try {
                        parseModule(element.getAsJsonObject());
                    } catch (NullPointerException e) {
                        System.err.println(" Parse Module Object Error: " + e.getMessage());
                    }
                }
                Vergence.CONSOLE.logInfo("[CONFIG] loaded all module options, total " + moduleCounter + " modules.");
            }
            if (mainObject.has("Friends")) {
                friends = mainObject.getAsJsonArray("Friends");
                Vergence.FRIEND.resetFriend();
                for (JsonElement element : friends) {
                    String s = element.getAsString();
                    Vergence.FRIEND.addFriend(s);
                }
                Vergence.CONSOLE.logInfo("[CONFIG] Friend list is loaded, your friends: " + String.join(", ", Vergence.FRIEND.friendList));
            }
            if (mainObject.has("Enemies")) {
                enemies = mainObject.getAsJsonArray("Enemies");
                Vergence.ENEMY.resetEnemy();
                for (JsonElement element : enemies) {
                    String s = element.getAsString();
                    Vergence.ENEMY.addEnemy(s);
                }
                Vergence.CONSOLE.logInfo("[CONFIG] Enemy list is loaded, your enemies: " + String.join(", ", Vergence.ENEMY.enemyList));
            }
            if (mainObject.has("Client")) {
                client = mainObject.getAsJsonArray("Client");
                try {
                    if (!client.get(0).getAsString().equals(Vergence.MOD_ID)) {
                        Vergence.CONSOLE.logWarn("[CONFIG] Unmatched Mod Id, the config file may be not a Vergence Client config file.");
                    } else {
                        Vergence.CONSOLE.logInfo("[CONFIG] Config file info \"Mod Id\" : " + client.get(0).getAsString());
                    }

                    if (!client.get(1).getAsString().equals(Vergence.VERSION)) {
                        Vergence.CONSOLE.logWarn("[CONFIG] Unmatched Mod Version, the config file may be out of the date.");
                    } else {
                        Vergence.CONSOLE.logInfo("[CONFIG] Config file info \"Client Version\" : " + client.get(1).getAsString());
                    }

                    if (!client.get(2).getAsString().equals(Vergence.CONFIG_TEMPLATE_VERSION)) {
                        Vergence.CONSOLE.logWarn("[CONFIG] Unmatched Config Template Version, config may not apply to current version.");
                    } else {
                        Vergence.CONSOLE.logInfo("[CONFIG] Config file info \"Config Template Version\" : " + client.get(2).getAsString());
                    }
                    Vergence.CONSOLE.logInfo("[CONFIG] Config file info \"UI Style Version\" : " + client.get(3).getAsString());
                    Vergence.CONSOLE.logInfo("[CONFIG] Config file info \"Save Date\" : " + client.get(4).getAsString());
                    currentConfigName = client.get(3).getAsString().length() <= 1 ? "Unknown" : client.get(5).getAsString();
                } catch (Exception e) {
                    Vergence.CONSOLE.logWarn("[CONFIG] Client Info incomplete or invalid.");
                    e.printStackTrace();
                }
            }

            Vergence.CONSOLE.logInfo("Loaded Config: " + config.getName(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentConfig = config;
        currentConfigName = config.getName().replace(".vgc", "");
        saveCurrentConfig();
    }

    public File getCurrentConfig() {
        File file = new File(CONFIG_FOLDER_NAME + "/config/" + currentConfigName + ".vgc");
        String name = currentConfigName;
        try {
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine())
                    name = reader.nextLine();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentConfig = new File(CONFIG_FOLDER, name + ".vgc");
        return currentConfig;
    }

    public void saveCurrentConfig() {
        File file = new File(CONFIG_FOLDER_NAME + "/config/" + currentConfigName + ".vgc");
        try {
            if (file.exists()) {
                FileWriter writer = new FileWriter(file);
                writer.write(currentConfig.getName().replace(".vgc", ""));
                writer.close();
            } else {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(currentConfig.getName().replace(".vgc", ""));
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Vergence.THEME.getTheme().getName().equals("default")) {
            Vergence.THEME.saveTheme(Vergence.THEME.getTheme().getName(), Vergence.THEME.dumpToJson());
        }
    }

    private void parseModule(JsonObject object) throws NullPointerException {
        Module module = ModuleManager.modules.stream()
                .filter(m -> object.getAsJsonObject(m.getName()) != null)
                .findFirst()
                .orElse(null);
        if (module == null || module.getName().equals("ThemeEditor")) {
            return ;
        }

        JsonObject MObject = object.getAsJsonObject(module.getName());
        for (Option<?> option : module.getOptions()) {
            try {
                switch (option.getType()) {
                    case "Boolean":
                        if (MObject != null) {
                            ((BooleanOption) option).setValue(MObject.getAsJsonPrimitive(option.getName()).getAsBoolean());
                        }
                        continue;
                    case "Double":
                        if (MObject != null) {
                            ((DoubleOption) option).setValue(MObject.getAsJsonPrimitive(option.getName()).getAsDouble());
                        }
                        continue;
                    case "Bind":
                        JsonArray array001 = null;
                        if (MObject != null) {
                            array001 = MObject.getAsJsonArray(option.getName());
                            ((BindOption)option).setValue(array001.get(0).getAsInt());
                            String value = array001.get(1).getAsString();
                            ((BindOption)option).setBindType(value.equals(BindOption.BindType.Click.name()) ? BindOption.BindType.Click : BindOption.BindType.Press);
                        }
                        continue;
                    case "Text":
                        if (MObject != null) {
                            ((TextOption)option).setValue(MObject.getAsJsonPrimitive(option.getName()).getAsString());
                        }
                        continue;
                    case "Color":
                        JsonArray array002 = null;
                        if (MObject != null) {
                            array002 = MObject.getAsJsonArray(option.getName());
                            ((ColorOption) option).setValue(ColorUtil.setAlpha(new Color(array002.get(0).getAsInt()), array002.get(1).getAsInt()));
                            ((ColorOption) option).setRainbow(array002.get(2).getAsBoolean());
                        }
                        continue;
                    case "Enum":
                        try {
                            if (MObject != null) {
                                Enum<?> value = EnumUtil.getValueByString(((EnumOption) option).getValue(), MObject.getAsJsonPrimitive(option.getName()).getAsString());
                                ((EnumOption) option).setValue(value != null ? value : ((EnumOption) option).getValue());
                            }
                        } catch (Exception ignored) {}
                        continue;
                    case "Multiple":
                        try {
                            if (MObject != null) {
                                JsonArray jsonArray = MObject.getAsJsonArray(option.getName());
                                MultipleOption<?> multipleOption = (MultipleOption<?>) option;
                                Class<? extends Enum<?>> enumClass = multipleOption.getEnumClass();
                                EnumSet enumSet = EnumSet.noneOf((Class) enumClass);
                                for (JsonElement elem : jsonArray) {
                                    String name = elem.getAsString();
                                    Enum<?> constant = EnumUtil.getEnumByName(enumClass.getEnumConstants()[0], name);
                                    if (constant != null) {
                                        enumSet.add(constant);
                                    }
                                }
                                multipleOption.setValue(enumSet);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        continue;

                }
            } catch (Exception _E) {
                _E.printStackTrace();
            }
        }
        try {
            if (MObject != null) {
                if (module.isAlwaysEnable()) {
                    return ;
                }
                module.setStatus(MObject.getAsJsonPrimitive("_STATUS").getAsBoolean());
            }
            if (module.getCategory().equals(Module.Category.HUD)) {
                if (MObject != null) {
                    module.setX(MObject.getAsJsonPrimitive("_X").getAsInt());
                }
                if (MObject != null) {
                    module.setY(MObject.getAsJsonPrimitive("_Y").getAsInt());
                }
                if (MObject != null) {
                    module.setWidth(MObject.getAsJsonPrimitive("_WIDTH").getAsInt());
                }
                if (MObject != null) {
                    module.setHeight(MObject.getAsJsonPrimitive("_HEIGHT").getAsInt());
                }
            }
        } catch (NullPointerException igrone) {
            module.setStatus(false);
            if (module.getCategory().equals(Module.Category.HUD)) {
                module.setX(0);
                module.setY(0);
                module.setWidth(0);
                module.setHeight(0);
            }
        }
    }

    public void save(@NotNull File config) {
        currentConfigName = config.getName().replace(".vgc", "");
        try {
            if (!config.exists()) {
                config.createNewFile();
            }
            JsonArray array = new JsonArray();

            JsonObject obj = new JsonObject();
            obj.add("Modules", getModuleArray());
            obj.add("Friends", getFriendArray());
            obj.add("Enemies", getEnemyArray());
            obj.add("Client", getClientInfoArray());
            array.add(obj);

            FileWriter writer = new FileWriter(config);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            gson.toJson(array, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private @NotNull JsonArray getClientInfoArray() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = now.format(formatter);

        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(Vergence.MOD_ID));
        array.add(new JsonPrimitive(Vergence.VERSION));
        array.add(new JsonPrimitive(Vergence.CONFIG_TEMPLATE_VERSION));
        array.add(new JsonPrimitive(Vergence.UI_STYLE_VERSION));
        array.add(new JsonPrimitive(formattedDate));
        array.add(new JsonPrimitive(currentConfigName));
        return array;
    }

    private @NotNull JsonArray getFriendArray() {
        JsonArray friendArray = new JsonArray();
        for (String str : Vergence.FRIEND.friendList) {
            friendArray.add(new JsonPrimitive(str));
        }
        return friendArray;
    }

    private @NotNull JsonArray getEnemyArray() {
        JsonArray enemyArray = new JsonArray();
        for (String str : Vergence.ENEMY.enemyList) {
            enemyArray.add(new JsonPrimitive(str));
        }
        return enemyArray;
    }

    private @NotNull JsonArray getModuleArray() {
        JsonArray modulesArray = new JsonArray();
        for (Module module : ModuleManager.modules) {
            modulesArray.add(getModuleObject(module));
        }
        return modulesArray;
    }

    public JsonObject getModuleObject(@NotNull Module module) {
        JsonObject json = new JsonObject();
        json.add("_STATUS", new JsonPrimitive(module.getStatus()));
        if (module.getCategory().equals(Module.Category.HUD)) {
            json.add("_X", new JsonPrimitive(module.getX()));
            json.add("_Y", new JsonPrimitive(module.getY()));
            json.add("_WIDTH", new JsonPrimitive(module.getWidth()));
            json.add("_HEIGHT", new JsonPrimitive(module.getHeight()));
        }

        for (Option<?> option : module.getOptions()) {
            if (option instanceof BooleanOption) {
                json.add(option.getName(), ((BooleanOption) option).getJsonValue());
            }
            else if (option instanceof ColorOption) {
                JsonArray array = new JsonArray();
                array.add(((ColorOption) option).getJsonValue());
                array.add(new JsonPrimitive(((ColorOption) option).getValue().getAlpha()));
                array.add(new JsonPrimitive(((ColorOption) option).isRainbow()));
                json.add(option.getName(), array);
            }
            else if (option instanceof DoubleOption) {
                json.add(option.getName(), ((DoubleOption) option).getJsonValue());
            }
            else if (option instanceof BindOption) {
                JsonArray array = new JsonArray();
                array.add(((BindOption) option).getJsonValue());
                array.add(new JsonPrimitive(((BindOption) option).getBindType().name()));
                json.add(option.getName(), array);
            }
            else if (option instanceof TextOption) {
                json.add(option.getName(), ((TextOption) option).getJsonValue());
            }
            else if (option instanceof EnumOption) {
                json.add(option.getName(), new JsonPrimitive(((EnumOption) option).getValue().name()));
            }
            else if (option instanceof MultipleOption<?>) {
                JsonArray array = new JsonArray();
                for (Enum<?> e : ((MultipleOption<?>) option).getValue()) {
                    array.add(new JsonPrimitive(e.name()));
                }
                json.add(option.getName(), array);
            }
        }

        JsonObject moduleObject = new JsonObject();
        moduleObject.add(module.getName(), json);
        return moduleObject;
    }
}
