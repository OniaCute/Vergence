package cc.vergence;

import cc.vergence.features.event.eventbus.EventBus;
import cc.vergence.features.managers.*;
import cc.vergence.util.other.Console;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import static cc.vergence.util.interfaces.Wrapper.mc;

public class Vergence implements ModInitializer {
    // Information & Mod status
    public static final ModMetadata MOD_INFO;
    public static final String MOD_ID = "vergence";
    public static final String NAME = "Vergence";
    public static final String VERSION = "1.0.0";
    public static final ArrayList<String> AUTHORS = new ArrayList<String>();
    public static String PREFIX = "$";
    public static String HWID = "";
    public static String AUTHID = "0X0000FF";
    public static boolean AUTHED = false;
    public static boolean LOADED = false;
    public static long LOAD_TIME;

    // Manager & Preload
    public static Console CONSOLE;
    public static EventBus EVENTBUS;
    public static URLManager URL;
    public static PopManager POP;
    public static TextManager TEXT;
    public static ThemeManager THEME;
    public static MessageManager MESSAGE;
    public static FriendManager FRIEND;
    public static EnemyManager ENEMY;
    public static TotemManager TOTEM;
    public static EventManager EVENTS;
    public static NetworkManager NETWORK;
    public static GuiManager GUI;
    public static HudManager HUD;
    public static RotateManager ROTATE;
    public static ModuleManager MODULE;
    public static CommandManager COMMAND;
    public static NotifyManager NOTIFY;

    // Mod Info Load
    static {
        MOD_INFO = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();
    }

    @Override
    public void onInitialize() {
        CONSOLE = new Console();
        CONSOLE.logInfo("Vergence Client | Preloading ...", true);
        CONSOLE.logInfo("VERSION: " + VERSION, true);
        CONSOLE.logInfo("Authors: Voury, Onia", true);

        if (!MOD_INFO.getId().equals(MOD_ID)) {
            CONSOLE.logWarn("Fabric mod value check failed!");
            CONSOLE.logWarn("Vergence Client was exited!");
            CONSOLE.logWarn("This version may have been acquired from informal sources or created without permission!");
            mc.stop();
        } else {
//            if (!AuthSystem.doAuth()) {
//                CONSOLE.logAuth("Auth failed, please check your Network status and computer status.");
//                CONSOLE.logAuth("Vergence will not provide services until you have successfully auth.");
//                mc.stop();
//            }
            load();
        }
    }

    public static void load() {
        // Pre load
        CONSOLE.logInfo("Event Bus is loading...");
        EVENTBUS = new EventBus();
        CONSOLE.logInfo("Event Bus is loaded.");
        CONSOLE.logInfo("Registering Event bus ...");
        EVENTBUS.registerLambdaFactory("cc.vergence", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        CONSOLE.logInfo("Lambda was loaded.");
        CONSOLE.logInfo("Vergence Client was preloaded.");
        // Information define & Intellectual property declaration
        AUTHORS.add("Voury");
        AUTHORS.add("Onia");
        // Real load
        LOAD_TIME = System.currentTimeMillis();

        URL = new URLManager();
        CONSOLE.logInfo("URL Manager was loaded");

        POP = new PopManager();
        CONSOLE.logInfo("Pop Manager was loaded");

        TEXT = new TextManager();
        CONSOLE.logInfo("Text Manager was loaded");

        MESSAGE = new MessageManager();
        CONSOLE.logInfo("Message Manager was loaded");

        THEME = new ThemeManager();
        CONSOLE.logInfo("Theme Manager was loaded");

        FRIEND = new FriendManager();
        CONSOLE.logInfo("Friend Manager was loaded");

        ENEMY = new EnemyManager();
        CONSOLE.logInfo("Enemy Manager was loaded");

        TOTEM = new TotemManager();
        CONSOLE.logInfo("Totem Manager was loaded");

        EVENTS = new EventManager();
        CONSOLE.logInfo("Event Manager was loaded");

        NETWORK = new NetworkManager();
        CONSOLE.logInfo("Network Manager was loaded");

        GUI = new GuiManager();
        CONSOLE.logInfo("GUI Manager was loaded");

        HUD = new HudManager();
        CONSOLE.logInfo("HUD Manager was loaded");

        ROTATE = new RotateManager();
        CONSOLE.logInfo("Rotate Manager was loaded");

        MODULE = new ModuleManager();
        CONSOLE.logInfo("Module Manager was loaded");

        COMMAND = new CommandManager();
        CONSOLE.logInfo("Command Manager was loaded");

        NOTIFY = new NotifyManager();
        CONSOLE.logInfo("Notify Manager was loaded");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (LOADED) {
                save();
            }
        }));

        CONSOLE.logInfo("Vergence Client Loaded!");
        CONSOLE.logInfo("Vergence Loaded In " + (System.currentTimeMillis() - LOAD_TIME) + " ms.");

        LOADED = true;
    }

    public static void unload() {
        CONSOLE.logInfo("Vergence Client was unloaded.");
        EVENTBUS.listenerMap.clear();
    }

    public static void save() {
        CONSOLE.logInfo("Vergence Client config is saving...");
    }

}
