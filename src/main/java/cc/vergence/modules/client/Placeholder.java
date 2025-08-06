package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;

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
    public Option<String> placeholder_player_speed_km = addOption(new TextOption("Placeholder_Speed_Km", "Blank").setEditable(false));
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
            placeholder_player_speed.setValue(String.valueOf(mc.player.speed));
            placeholder_player_speed_km.setValue(String.valueOf(String.format("%.2f", mc.player.speed * 3.6)));
        }
    }
}
