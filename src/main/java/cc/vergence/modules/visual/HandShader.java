package cc.vergence.modules.visual;

import cc.vergence.features.event.events.HeldItemRendererEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.awt.*;

public class HandShader extends Module {
    public static HandShader INSTANCE;

    public HandShader() {
        super("HandShader", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Fill));
    public Option<Color> color = addOption(new ColorOption("Color", new Color(234, 142, 255, 250), v -> mode.getValue().equals(Modes.Fill)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onHeldItemRendererEvent(HeldItemRendererEvent event, Hand hand, ItemStack item, float equipProgress, MatrixStack stack) {
        switch (((Modes) mode.getValue())) {
            case Fill -> {
                RenderSystem.setShaderColor(color.getValue().getRed() / 255f, color.getValue().getGreen() / 255f, color.getValue().getBlue() / 255f, color.getValue().getAlpha() / 255f);
            }
            default -> {
                // NO SHADERS QAQ
            }
        }
    }

    public enum Modes {
        Fill,
        Outline,
        Smoke,
        Rainbow,
        Snow,
        Shine
    }
}
