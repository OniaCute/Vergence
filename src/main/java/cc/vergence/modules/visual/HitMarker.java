package cc.vergence.modules.visual;

import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.other.FastTimerUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.function.Function;

public class HitMarker extends Module {
    public static HitMarker INSTANCE;
    public static FastTimerUtil timer = new FastTimerUtil();
    private final Identifier marker = Identifier.of("vergence", "textures/hud/elements/hitmarker.png");

    public HitMarker() {
        super("HitMarker", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Double> lifeTime = addOption(new DoubleOption("LifeTime", 0, 1200, 400).setUnit("ms"));
    public Option<Color> hitColor = addOption(new ColorOption("Color", new Color(255, 255, 255, 255)));
    public Option<Double> offsetX = addOption(new DoubleOption("OffsetX", -10, 10, 0));
    public Option<Double> offsetY = addOption(new DoubleOption("OffsetY", -10, 10, 0));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        if (isNull() || timer.passedMs(lifeTime.getValue())) {
            return;
        }

        float life = 1.0F - (float) timer.getGapMs() / lifeTime.getValue().floatValue();
        int alpha = (int) (life * hitColor.getValue().getAlpha()) & 0xFF;
        int color = ColorUtil.setAlpha(hitColor.getValue().getRGB(), alpha);
        int w = mc.getWindow().getScaledWidth();
        int h = mc.getWindow().getScaledHeight();
        int x = w / 2 - 8 + offsetX.getValue().intValue();
        int y = h / 2 - 8 + offsetY.getValue().intValue();

        RenderSystem.setShaderTexture(0, marker);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        MatrixStack matrices = context.getMatrices();
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = ( color & 0xFF) / 255F;
        float a = ((color >> 24) & 0xFF) / 255F;
        buffer.vertex(matrix, x, y + 16, 0).texture(0, 1).color(r, g, b, a);
        buffer.vertex(matrix, x + 16, y + 16, 0).texture(1, 1).color(r, g, b, a);
        buffer.vertex(matrix, x + 16, y, 0).texture(1, 0).color(r, g, b, a);
        buffer.vertex(matrix, x, y, 0).texture(0, 0).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (packet instanceof PlayerInteractEntityC2SPacket) {
            timer.reset();
        }
    }
}
