package cc.vergence;

import cc.vergence.features.event.eventbus.EventBus;
import cc.vergence.features.managers.client.*;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.managers.feature.NetworkManager;
import cc.vergence.features.managers.feature.ServerManager;
import cc.vergence.features.managers.feature.TimerManager;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.other.TextManager;
import cc.vergence.features.managers.player.*;
import cc.vergence.features.managers.render.ShaderManager;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.util.other.Console;
import cc.vergence.util.player.InventoryUtil;
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
    public static final String CONFIG_TEMPLATE_VERSION = "vergence_1_0_vcg_json";
    public static final String UI_STYLE_VERSION = "vergence_1_0_ui_gird";
    public static final ArrayList<String> AUTHORS = new ArrayList<String>();
    public static String PREFIX = "$";
    public static boolean LOADED = false;
    public static boolean OUT_OF_DATE = false;
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
    public static EventManager EVENTS;
    public static NetworkManager NETWORK;
    public static TimerManager TIMER;
    public static ServerManager SERVER;
    public static ShaderManager SHADER;
    public static GuiManager GUI;
    public static HudManager HUD;
    public static InventoryManager INVENTORY;
    public static RotateManager ROTATE;
    public static MineManager MINE;
    public static ModuleManager MODULE;
    public static ConfigManager CONFIG;
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

        EVENTS = new EventManager();
        CONSOLE.logInfo("Event Manager was loaded");

        NETWORK = new NetworkManager();
        CONSOLE.logInfo("Network Manager was loaded");

        TIMER = new TimerManager();
        CONSOLE.logInfo("Timer Manager was loaded");

        SERVER = new ServerManager();
        CONSOLE.logInfo("Server Manager was loaded");

        SHADER = new ShaderManager();
        CONSOLE.logInfo("Shader Manager was loaded");

        GUI = new GuiManager();
        CONSOLE.logInfo("GUI Manager was loaded");

        HUD = new HudManager();
        CONSOLE.logInfo("HUD Manager was loaded");

        INVENTORY = new InventoryManager();
        CONSOLE.logInfo("Inventory Manager was loaded");

        ROTATE = new RotateManager();
        CONSOLE.logInfo("Rotate Manager was loaded");

        MINE = new MineManager();
        CONSOLE.logInfo("Mine Manager was loaded");

        MODULE = new ModuleManager();
        CONSOLE.logInfo("Module Manager was loaded");

        CONFIG = new ConfigManager();
        CONSOLE.logInfo("Config Manager was loaded");

        COMMAND = new CommandManager();
        CONSOLE.logInfo("Command Manager was loaded");

        NOTIFY = new NotifyManager();
        CONSOLE.logInfo("Notify Manager was loaded");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (LOADED) {
                save();
            }
        }));

        CONFIG.load(CONFIG.getCurrentConfig());

        OUT_OF_DATE = needUpdate();

        CONSOLE.logInfo("Vergence Client Loaded!");
        CONSOLE.logInfo("Vergence Loaded In " + (System.currentTimeMillis() - LOAD_TIME) + " ms.");

        LOADED = true;
    }

    public static boolean needUpdate() {
        return URL.getResponse("https://update.vergence.cc/", MOD_ID+"_"+VERSION);
    }

    public static void unload() {
        CONSOLE.logInfo("Vergence Client is unloading ...");
        EVENTBUS.listenerMap.clear();
        save();
        CONSOLE.logInfo("Vergence Client is was unloaded");
    }

    public static void save() {
        CONSOLE.logInfo("Vergence Client is shutting down ...");
        EVENTS.onShutDown();
        CONSOLE.logInfo("Vergence Client is saving ...");
        CONFIG.save(CONFIG.getCurrentConfig());
        CONSOLE.logInfo("Vergence Client was saved");
    }
}
