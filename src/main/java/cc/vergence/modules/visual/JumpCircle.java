package cc.vergence.modules.visual;

import cc.vergence.features.event.events.DisconnectEvent;
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
    public Option<Double> time = addOption(new DoubleOption("Time", 0.1, 10, 2.5).setUnit("s"));
    public Option<Double> size = addOption(new DoubleOption("Scale", 0.2, 5, 1));
    public Option<Color> color = addOption(new ColorOption("Color", new Color(255, 147, 252)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDisable() {
        cache.clear();
        circles.clear();
    }

    @Override
    public void onLogout() {
        cache.clear();
        circles.clear();
    }

    @Override
    public void onDisconnect(DisconnectEvent event, String reason) {
        cache.clear();
        circles.clear();
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

        circles.removeIf(c -> c.timer.passedMs(easeOut.getValue() ? getTime() : getTime() + 1000));
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.setShaderTexture(0, TextureStorage.circle);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (Circle c : circles) {
            float colorAnim = (float) (c.timer.getGapMs()) / (getTime() + 1000);
            float sizeAnim = (float) (size.getValue() - (float) Math.pow(1 - ((c.timer.getGapMs() * (easeOut.getValue() ? 2f : 1f)) / getTime()), 4));

            matrixStack.push();
            matrixStack.translate(c.pos().x - mc.getEntityRenderDispatcher().camera.getPos().getX(), c.pos().y - mc.getEntityRenderDispatcher().camera.getPos().getY(), c.pos().z - mc.getEntityRenderDispatcher().camera.getPos().getZ());
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(sizeAnim * 1 * 1000f));
            Matrix4f matrix = matrixStack.peek().getPositionMatrix();

            float scale = sizeAnim * 2f;

            buffer.vertex(matrix, -sizeAnim, -sizeAnim + scale, 0).texture(0, 1).color(ColorUtil.applyOpacity(color.getValue(), 1f - colorAnim).getRGB());
            buffer.vertex(matrix, -sizeAnim + scale, -sizeAnim + scale, 0).texture(1, 1).color(ColorUtil.applyOpacity(color.getValue(), 1f - colorAnim).getRGB());
            buffer.vertex(matrix, -sizeAnim + scale, -sizeAnim, 0).texture(1, 0).color(ColorUtil.applyOpacity(color.getValue(), 1f - colorAnim).getRGB());
            buffer.vertex(matrix, -sizeAnim, -sizeAnim, 0).texture(0, 0).color(ColorUtil.applyOpacity(color.getValue(), 1f - colorAnim).getRGB());

            matrixStack.pop();
        }

        Render2DUtil.endBuilding(buffer);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
    }

    private float getTime() {
        return time.getValue().floatValue() * 1000f;
    }

    public record Circle(Vec3d pos, FastTimerUtil timer) {}
}