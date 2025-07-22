package cc.vergence.modules.visual;

import cc.vergence.Vergence;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.EnumSet;

public class NameTags extends Module {
    public static NameTags INSTANCE;

    public NameTags() {
        super("NameTags", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Enum<?>> fontSize = addOption(new EnumOption("FonsSize", FontSize.SMALL));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(14, 14, 14)));
    public Option<EnumSet<Modes>> mode = addOption(new MultipleOption<Modes>("Modes", EnumSet.of(Modes.Fill, Modes.Outline)));
    public Option<Color> fillColor = addOption(new ColorOption("FillColor", new Color(234, 234, 234), v -> mode.getValue().contains(Modes.Fill)));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(246, 246, 246), v -> mode.getValue().contains(Modes.Outline)));
    public Option<Double> outlineWidth = addOption(new DoubleOption("OutlineWidth", 0, 4, 1, v -> mode.getValue().contains(Modes.Outline)));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true, v -> !mode.getValue().isEmpty()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 3, v -> rounded.getValue() && !mode.getValue().isEmpty()));
    public Option<Boolean> withPing = addOption(new BooleanOption("Ping", true));
    public Option<Boolean> withHealth = addOption(new BooleanOption("Health", true));
    public Option<Boolean> withDistance = addOption(new BooleanOption("Distance", true));
    public Option<Boolean> withGameMode = addOption(new BooleanOption("GameMode", true));
    public Option<Boolean> withPop = addOption(new BooleanOption("Popped", true));
    public Option<Double> scale = addOption(new DoubleOption("Scale", 10, 100, 30));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (isNull()) {
            return;
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) {
                continue ;
            }
            if (!Render3DUtil.isFrustumVisible(player.getBoundingBox())) {
                continue ;
            }

            double x = MathHelper.lerp(tickDelta, player.lastRenderX, player.getX());
            double y = MathHelper.lerp(tickDelta, player.lastRenderY, player.getY()) + (player.isSneaking() ? 1.9f : 2.1f);
            double z = MathHelper.lerp(tickDelta, player.lastRenderZ, player.getZ());

            Vec3d vec3d = new Vec3d(x - mc.getEntityRenderDispatcher().camera.getPos().x, y - mc.getEntityRenderDispatcher().camera.getPos().y, z - mc.getEntityRenderDispatcher().camera.getPos().z);
            float distance = (float) Math.sqrt(mc.getEntityRenderDispatcher().camera.getPos().squaredDistanceTo(x, y, z));
            float scaling = 0.0018f + (scale.getValue().intValue() / 10000.0f) * distance;
            if (distance <= 8.0) {
                scaling = 0.0245f;
            }

            matrixStack.push();
            matrixStack.translate(vec3d.x, vec3d.y, vec3d.z);
            matrixStack.multiply(mc.getEntityRenderDispatcher().getRotation());
            matrixStack.scale(scaling, -scaling, scaling);

            String displayString = player.getName().getString();
            if (withHealth.getValue()) {
                displayString = ColorUtil.getHealthColor(player.getHealth() + player.getAbsorptionAmount()) + new DecimalFormat("0.0").format(player.getHealth() + player.getAbsorptionAmount()) + Formatting.RESET + " " + displayString;
            }
            if (withGameMode.getValue()) {
                displayString = "[" + EntityUtil.getGameModeText(EntityUtil.getGameMode(player)) + "] " + displayString;
            }
            if (withPing.getValue()) {
                displayString += " " + EntityUtil.getLatency(player) + "ms";
            }
            if (withDistance.getValue()) {
                displayString += " " + new DecimalFormat("0.0").format(EntityUtil.getDistance(player)) + "m";
            }
            if (withPop.getValue()) {
                displayString += Vergence.POP.getPop(player.getName().getString()) != 0 ? Vergence.POP.getPop(player.getName().getString()) + "POP" : "";
            }
            double fontWidth = FontUtil.getWidth((FontSize) fontSize.getValue(), displayString);
            float pad = outlineWidth.getValue().floatValue();
            float left = -(float) (fontWidth / 2.0f) - pad;
            float top = (float) (-FontUtil.getHeight((FontSize) fontSize.getValue()) - pad);
            float right = (float) (fontWidth / 2.0f) + pad;

            if (mode.getValue().contains(Modes.Fill) || mode.getValue().contains(Modes.Outline)) {
                float radiusF = radius.getValue().floatValue();
                float outline = mode.getValue().contains(Modes.Outline) ? outlineWidth.getValue().floatValue() : 0.0f;
                Render2DUtil.drawRoundedRectWithOutline(
                        matrixStack,
                        left, top,
                        right - left, pad - top,
                        rounded.getValue() ? radiusF : 0.0f,
                        outline,
                        mode.getValue().contains(Modes.Fill) ? fillColor.getValue() : new Color(0, 0, 0, 0),
                        mode.getValue().contains(Modes.Outline) ? outlineColor.getValue() : new Color(0, 0, 0, 0)
                );
            }

            FontUtil.drawText(
                    matrixStack,
                    displayString,
                    -fontWidth / 2,
                    -FontUtil.getHeight((FontSize) fontSize.getValue()) + 2,
                    textColor.getValue(),
                    (FontSize) fontSize.getValue()
            );

            matrixStack.pop();
        }
    }

    public enum Modes {
        Fill,
        Outline
    }
}
