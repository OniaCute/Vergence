package cc.vergence.features.managers.feature;

import cc.vergence.Vergence;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.*;
import cc.vergence.modules.combat.*;
import cc.vergence.modules.exploit.*;
import cc.vergence.modules.hud.*;
import cc.vergence.modules.misc.*;
import cc.vergence.modules.movement.*;
import cc.vergence.modules.player.*;
import cc.vergence.modules.player.Timer;
import cc.vergence.modules.visual.*;

import java.util.*;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<Module>();
    public static HashMap<Module.Category, ArrayList<Module>> categoryModuleHashMap = new HashMap<>();

    public ModuleManager() {
        for (Module.Category category : Module.getCategories()) {
            ArrayList<Module> categoryModules = new ArrayList<>();
            categoryModuleHashMap.put(category, categoryModules);
        }

        // Pre
        registerModule(new Client());
        registerModule(new ClickGUI());
        registerModule(new Notify());
        registerModule(new AntiCheat());

        // Normal
        registerModule(new BetterChat());
        registerModule(new HudEditor());
        registerModule(new Watermark());
        registerModule(new Spammer());
        registerModule(new AutoSprint());
        registerModule(new FastUse());
        registerModule(new StorageESP());
        registerModule(new AntiHungry());
        registerModule(new KillAura());
        registerModule(new Scaffold());
        registerModule(new FakePlayer());
        registerModule(new SafeWalk());
        registerModule(new Reach());
        registerModule(new FOVModifier());
        registerModule(new InventoryMove());
        registerModule(new Disabler());
        registerModule(new AutoWalk());
        registerModule(new NoCooldown());
        registerModule(new PortalGod());
        registerModule(new NoFall());
        registerModule(new BetterTab());
        registerModule(new Title());
        registerModule(new Placeholder());
        registerModule(new ModuleList());
        registerModule(new Advertiser());
        registerModule(new Hotkeys());
        registerModule(new MainMenu());
        registerModule(new AirPearl());
        registerModule(new AutoSword());
        registerModule(new AutoTool());
        registerModule(new Chams());
        registerModule(new NoBacktrack());
        registerModule(new NoRender());
        registerModule(new FreeCamera());
        registerModule(new CameraClip());
        registerModule(new FullBright());
        registerModule(new SwingModifier());
        registerModule(new HandModifier());
        registerModule(new BlockHighlight());
        registerModule(new MiddleClickExpansion());
        registerModule(new SaveBreak());
        registerModule(new Timer());
        registerModule(new MovementLagSpoof());
        registerModule(new AntiLevitation());
        registerModule(new Parkour());
        registerModule(new TickShift());
        registerModule(new NameProtect());
        registerModule(new AutoLogin());
        registerModule(new VisualRange());
        registerModule(new CarryPro());
        registerModule(new SilentDisconnect());
        registerModule(new AutoRespawn());
        registerModule(new PacketsLimit());
        registerModule(new NameTags());
        registerModule(new ResourceESP());
        registerModule(new HitboxDesync());
        registerModule(new MurdererCatcher());
        registerModule(new ColorMatcher());
        registerModule(new FakeAnimalCatcher());
        registerModule(new AutoLogout());
        registerModule(new AutoRepairArmor());
        registerModule(new Scoreboard());
        registerModule(new ThemeEditor());
        registerModule(new AutoBowAim());
        registerModule(new FastEat());
        registerModule(new MultipleTask());
        registerModule(new NoEntityTrace());
        registerModule(new NoInteract());
        registerModule(new PingLagSpoof());
        registerModule(new LagNotify());
        registerModule(new FastLatencyCalc());
        registerModule(new UnfocusedFPS());
        registerModule(new FastDrop());
        registerModule(new FastFall());
        registerModule(new AutoReplenish());
        registerModule(new CrystalAura());
        registerModule(new SmartOffhand());
        registerModule(new AutoBowRelease());
        registerModule(new Velocity());
        registerModule(new Surround());
        registerModule(new Defender());
        registerModule(new Xray());

        // special module
        registerModule(new SafeMode());

        // sort
        Vergence.CONSOLE.logInfo("[UI] Sorting modules ...");
        modules.sort(Comparator
                .<Module>comparingInt(m -> m.isAlwaysEnable() ? 0 : 1)
                .thenComparing(Module::getDisplayName)
        );
        for (ArrayList<Module> categoryList : categoryModuleHashMap.values()) {
            categoryList.sort(Comparator
                    .<Module>comparingInt(m -> m.isAlwaysEnable() ? 0 : 1)
                    .thenComparing(Module::getDisplayName)
            );
        }
        Vergence.CONSOLE.logInfo("[UI] All modules are sorted!");

        // init UI
        Vergence.CONSOLE.logInfo("[UI] Init UI ...");
        GuiManager.initClickGui();
        Vergence.CONSOLE.logInfo("[UI] UI was loaded!");
    }

    public void registerModule(Module module) {
        Vergence.CONSOLE.logInfo("[MODULE] Registering module \"" + module.getName() + "\" ...");
        for (String s : module.getOptionHashMap().keySet()) {
            settingOptionInfo(module, module.getOptionHashMap().get(s));
        }
        modules.add(module);
        categoryModuleHashMap.get(module.getCategory()).add(module);
        if (module.getCategory().equals(Module.Category.HUD)) {
            HudManager.hudList.add(module);
        }
        module.onRegister();
        Vergence.CONSOLE.logInfo("[MODULE] Module \"" + module.getName() + "\" is loaded.");
    }

    public void settingOptionInfo(Module module, Option<?> option) {
        option.setModule(module);

        if (option instanceof BindOption) {
            if (option.getName().equals("_BIND_")) {
                option.setDisplayName(Vergence.TEXT.get("Module.Special.ModuleBind.name"));
                option.setDescription(Vergence.TEXT.get("Module.Special.ModuleBind.description"));
                return ;
            }
            option.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.BindOption." + option.getName() + ".name"));
            option.setDescription(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.BindOption." + option.getName() + ".description"));
        }
        else if (option instanceof BooleanOption) {
            if (option.getName().equals("_DRAW_")) {
                option.setDisplayName(Vergence.TEXT.get("Module.Special.ModuleDraw.name"));
                option.setDescription(Vergence.TEXT.get("Module.Special.ModuleDraw.description"));
                return ;
            }
            option.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.BooleanOption." + option.getName() + ".name"));
            option.setDescription(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.BooleanOption." + option.getName() + ".description"));
        }
        else if (option instanceof ColorOption) {
            option.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.ColorOption." + option.getName() + ".name"));
            option.setDescription(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.ColorOption." + option.getName() + ".description"));
        }
        else if (option instanceof DoubleOption) {
            if (option.getName().equals("_PRIORITY_")) {
                option.setDisplayName(Vergence.TEXT.get("Module.Special.ModulePriority.name"));
                option.setDescription(Vergence.TEXT.get("Module.Special.ModulePriority.description"));
                return ;
            }
            option.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.DoubleOption." + option.getName() + ".name"));
            option.setDescription(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.DoubleOption." + option.getName() + ".description"));
        }
        else if (option instanceof EnumOption) {
            option.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.EnumOption." + option.getName() + ".name"));
            option.setDescription(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.EnumOption." + option.getName() + ".description"));
        }
        else if (option instanceof MultipleOption) {
            option.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.MultipleOption." + option.getName() + ".name"));
            option.setDescription(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.MultipleOption." + option.getName() + ".description"));
        }
        else if (option instanceof TextOption) {
            option.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.TextOption." + option.getName() + ".name"));
            option.setDescription(Vergence.TEXT.get("Module.Modules." + module.getName() + ".Options.TextOption." + option.getName() + ".description"));
        }
    }

    public Module getModuleByName(String string) {
        for(Module module : modules) {
            if(module.getName().equalsIgnoreCase(string)) {
                return module;
            }
        }
        return null;
    }
}
