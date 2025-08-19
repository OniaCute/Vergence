package cc.vergence.modules.hud;

import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class InventoryHud extends Module {
    public static InventoryHud INSTANCE;

    public InventoryHud() {
        super("InventoryHud",Category.HUD);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Normal));
    public Option<String> title = addOption(new TextOption("Title", "Inventory Items"));
    public Option<Enum<?>> titleAlign = addOption(new EnumOption("TitleAlign", Aligns.Center));
    public Option<Color> titleColor = addOption(new ColorOption("TitleColor", new Color(241, 137, 255)));
    public Option<Boolean> icon = addOption(new BooleanOption("Icon", true));
    public Option<Boolean> count = addOption(new BooleanOption("Count", true));
    public Option<Color> countColor = addOption(new ColorOption("CountColor", new Color(0, 0, 0), v -> count.getValue()));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", false, v -> background.getValue()));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(246, 246, 246, 242), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {
        if (!blur.getValue() || !background.getValue()) {
            return ;
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        String iconText = icon.getValue() ? "\uF1B2 " : "";
    }

    private cc.vergence.features.enums.other.Aligns translateAlign(Aligns aligns) {
        switch (aligns) {
            case Left -> {
                return cc.vergence.features.enums.other.Aligns.LEFT;
            }
            case Center -> {
                return cc.vergence.features.enums.other.Aligns.CENTER;
            }
            default -> {
                return cc.vergence.features.enums.other.Aligns.RIGHT;
            }
        }
    }

    private FontSize getTitleFontSize() {
        return mode.getValue().equals(Modes.Normal) ? FontSize.MEDIUM : FontSize.SMALL;
    }

    private FontSize getFontSize() {
        return mode.getValue().equals(Modes.Normal) ? FontSize.SMALL : FontSize.SMALLEST;
    }

    private enum Aligns {
        Left,
        Center,
        Right
    }

    private enum Modes {
        Normal,
        Mini
    }
}
