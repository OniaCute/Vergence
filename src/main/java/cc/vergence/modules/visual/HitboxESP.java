package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.injections.accessors.player.EntityAccessor;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class HitboxESP extends Module {
    public static HitboxESP INSTANCE;

    public HitboxESP() {
        super("HitboxESP", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Boolean> self = addOption(new BooleanOption("Self", false));
    public Option<Boolean> invisibleOnly = addOption(new BooleanOption("InvisibleOnly", false));
    public Option<Double> ticks = addOption(new DoubleOption("Ticks", 0, 40, 5));
    public Option<Color> fillColor = addOption(new ColorOption("FillColor", new Color(229, 229, 229, 36)));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(255, 255, 255, 113)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        for (Entity player : mc.world.getPlayers()) {
            if (invisibleOnly.getValue() && !player.isInvisible()) {
                return ;
            }
            if (player != mc.player || self.getValue()) {
                Render3DUtil.draw3DBox(matrixStack, ((EntityAccessor) player).getDimensions().getBoxAt(new Vec3d(interpolate(player.lastRenderX, player.getX(), tickDelta), interpolate(player.lastRenderY, player.getY(), tickDelta), interpolate(player.lastRenderZ, player.getZ(), tickDelta)).add(EntityUtil.getMotionVec(player, ticks.getValue().intValue(), true))), fillColor.getValue(), true, outlineColor.getValue(), true);
            }
        }
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }
}
