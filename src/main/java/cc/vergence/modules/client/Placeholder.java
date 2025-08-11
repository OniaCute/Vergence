package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.movement.TickShift;

public class Placeholder extends Module {
    public static Placeholder INSTANCE;
    public Placeholder() {
        super("Placeholder", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<String> placeholder_client_id = addOption(new TextOption("Placeholder_Client_ID", Vergence.MOD_ID).setEditable(false));
    public Option<String> placeholder_client_name = addOption(new TextOption("Placeholder_Client_Name", Vergence.NAME).setEditable(false));
    public Option<String> placeholder_client_full_name = addOption(new TextOption("Placeholder_Client_Full_Name", Vergence.NAME + " Client").setEditable(false));
    public Option<String> placeholder_client_version = addOption(new TextOption("Placeholder_Client_Version", Vergence.VERSION).setEditable(false));
    public Option<String> placeholder_player_name = addOption(new TextOption("Placeholder_Player", "Blank").setEditable(false));
    public Option<String> placeholder_player_HP = addOption(new TextOption("Placeholder_HP", "Blank").setEditable(false));
    public Option<String> placeholder_player_max_HP = addOption(new TextOption("Placeholder_Max_HP", "Blank").setEditable(false));
    public Option<String> placeholder_player_armor = addOption(new TextOption("Placeholder_Armor", "Blank").setEditable(false));
    public Option<String> placeholder_player_speed = addOption(new TextOption("Placeholder_Speed", "Blank").setEditable(false));
    public Option<String> placeholder_player_speed_km = addOption(new TextOption("Placeholder_Speeds_Km", "Blank").setEditable(false));
    public Option<String> placeholder_player_fps = addOption(new TextOption("Placeholder_Fps", "0").setEditable(false));
    public Option<String> placeholder_player_memory = addOption(new TextOption("Placeholder_Memory", "0").setEditable(false));
    public Option<String> placeholder_player_memory_max = addOption(new TextOption("Placeholder_Memory_Max", "0").setEditable(false));
    public Option<String> placeholder_player_ping = addOption(new TextOption("Placeholder_Ping", "-1").setEditable(false));
    public Option<String> placeholder_player_combo = addOption(new TextOption("Placeholder_Combo", "0").setEditable(false));
    public Option<String> placeholder_player_cps = addOption(new TextOption("Placeholder_CPS", "0").setEditable(false));
    public Option<String> placeholder_player_right_cps = addOption(new TextOption("Placeholder_Right_CPS", "0").setEditable(false));
    public Option<String> placeholder_player_gametime = addOption(new TextOption("Placeholder_GameTime", "0").setEditable(false));
    public Option<String> placeholder_player_gametime_formatted = addOption(new TextOption("Placeholder_GameTime_Formatted", "0").setEditable(false));
    public Option<String> placeholder_player_server = addOption(new TextOption("Placeholder_Server", "0").setEditable(false));
    public Option<String> placeholder_player_position_x = addOption(new TextOption("Placeholder_Position_X", "Blank").setEditable(false));
    public Option<String> placeholder_player_position_y = addOption(new TextOption("Placeholder_Position_Y", "Blank").setEditable(false));
    public Option<String> placeholder_player_position_z = addOption(new TextOption("Placeholder_Position_Z", "Blank").setEditable(false));
    public Option<String> placeholder_player_tickshift_used = addOption(new TextOption("Placeholder_TickShift_Used", "Blank").setEditable(false));
    public Option<String> placeholder_player_tickshift_saved = addOption(new TextOption("Placeholder_TickShift_Saved", "Blank").setEditable(false));
    public Option<String> placeholder_player_tickshift_max = addOption(new TextOption("Placeholder_TickShift_Max", "Blank").setEditable(false));
    public Option<String> placeholder_player_combat_distance = addOption(new TextOption("Placeholder_Combat_Distance", "0").setEditable(false));
    public Option<String> placeholder_world_overworld = addOption(new TextOption("Placeholder_OverWorld", "OverWorld").setEditable(true));
    public Option<String> placeholder_world_nether = addOption(new TextOption("Placeholder_Nether", "Nether").setEditable(true));
    public Option<String> placeholder_world_the_end = addOption(new TextOption("Placeholder_TheEnd", "TheEnd").setEditable(true));

    @Override
    public String getDetails() {
        return String.valueOf(getOptions().size() - 2);
    }

    @Override
    public void onRegister() {
        this.enable();
    }

    @Override
    public void onTick() {
        if (mc.player != null) {
            placeholder_player_name.setValue(mc.player.getName().getString());
            placeholder_player_HP.setValue(String.valueOf((int) mc.player.getHealth()));
            placeholder_player_max_HP.setValue(String.valueOf((int) mc.player.getMaxHealth()));
            placeholder_player_armor.setValue(String.valueOf((int) mc.player.getArmor()));
            placeholder_player_speed.setValue(String.format("%.2f", Vergence.INFO.getSpeedPerS()));
            placeholder_player_speed_km.setValue(String.format("%.2f", Vergence.INFO.getSpeed()));
            placeholder_player_fps.setValue(String.valueOf(Vergence.INFO.getCurrentFPS()));
            placeholder_player_memory.setValue(String.valueOf(Vergence.INFO.getSpentMemory()));
            placeholder_player_memory_max.setValue(String.valueOf(Vergence.INFO.getMaxMemory()));
            placeholder_player_ping.setValue(String.valueOf(Vergence.INFO.getPing()));
            placeholder_player_combo.setValue(String.valueOf(Vergence.INFO.getCombo()));
            placeholder_player_position_x.setValue(String.format("%.2f", mc.player.getPos().x));
            placeholder_player_position_y.setValue(String.format("%.2f", mc.player.getPos().y));
            placeholder_player_position_z.setValue(String.format("%.2f", mc.player.getPos().z));
            placeholder_player_cps.setValue(String.valueOf(Vergence.INFO.getLeftClicks()));
            placeholder_player_right_cps.setValue(String.valueOf(Vergence.INFO.getLeftClicks()));
            placeholder_player_gametime.setValue(String.valueOf(Vergence.INFO.getGameTime()));
            placeholder_player_gametime_formatted.setValue(String.valueOf(Vergence.INFO.getGameTimeFormatted()));
            placeholder_player_server.setValue(Vergence.SERVER.getChachServer());
            placeholder_player_tickshift_used.setValue(String.valueOf(TickShift.INSTANCE != null ? TickShift.INSTANCE.getUsed() : "0%"));
            placeholder_player_tickshift_saved.setValue(String.valueOf(TickShift.INSTANCE != null ? TickShift.INSTANCE.getTicks() : "0"));
            placeholder_player_tickshift_max.setValue(String.valueOf(TickShift.INSTANCE != null ? TickShift.INSTANCE.maxTicks.getValue().intValue() : "0"));
            placeholder_player_combat_distance.setValue(String.format("%.2f", Vergence.INFO.getCombatDistance()));
        }
    }
}
