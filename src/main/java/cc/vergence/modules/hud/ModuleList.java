package cc.vergence.modules.hud;

import cc.vergence.features.enums.Aligns;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import net.minecraft.client.gui.DrawContext;

public class ModuleList extends Module {
    public ModuleList() {
        super("ModuleList", Category.HUD);
    }

    public Option<Enum<?>> align = addOption(new EnumOption("Align", Aligns.RIGHT_TOP));

    @Override
    public String getDetails() {
        return align.getValue().name();
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {

    }
}
