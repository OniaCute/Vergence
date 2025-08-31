package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.other.TextureStorage;
import cc.vergence.util.render.utils.Render2DUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JumpCircle extends Module {
    public static JumpCircle INSTANCE;
    private final List<Circle> circles = new ArrayList<>();
    private final List<PlayerEntity> cache = new CopyOnWriteArrayList<>();

    public JumpCircle() {
        super("JumpCircle", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Boolean> selfOnly = addOption(new BooleanOption("SelfOnly", true));
    public Option<Boolean> easeOut = addOption(new BooleanOption("EaseOut", true));
    public Option<Double> scale = addOption(new DoubleOption("Scale", 0.2, 5, 1));
    public Option<Double> speed = addOption(new DoubleOption("Speed", 0.5, 8, 2));
    public Option<Color> color = addOption(new ColorOption("Color", new Color(255, 147, 252)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        for (PlayerEntity pl : mc.world.getPlayers()) {
            if (!cache.contains(pl) && pl.isOnGround() && (mc.player == pl || !selfOnly.getValue())) {
                cache.add(pl);
            }
        }

        cache.forEach(pl -> {
            if (pl != null && !pl.isOnGround()) {
                circles.add(new Circle(new Vec3d(pl.getX(), (int) Math.floor(pl.getY()) + 0.001f, pl.getZ()), new FastTimerUtil()));
                cache.remove(pl);
            }
        });

        circles.removeIf(c -> c.timer.passedMs(easeOut.getValue() ? 5000 : 6000));
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        Collections.reverse(circles);

        for (Circle c : circles) {
            float colorAnim = (float) (c.timer.getGapMs()) / 6000f;
            float sizeAnim = (float) (scale.getValue() - (float) Math.pow(1 - ((c.timer.getGapMs() * (easeOut.getValue() ? 2f : 1f)) / 5000f), 4));
            float alpha = 1f - colorAnim;

            Render2DUtil.drawCircle(matrixStack, c.pos(), sizeAnim * speed.getValue().floatValue() * 1000f, color.getValue(), alpha, 0);
        }

        Collections.reverse(circles);
    }

    public record Circle(Vec3d pos, FastTimerUtil timer) {}
}
