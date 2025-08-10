package cc.vergence.util.render.utils;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.TextureStorage;
import cc.vergence.util.render.other.AlphaOverride;
import cc.vergence.util.render.other.SkiaContext;
import cc.vergence.util.render.utils.blur.KawaseBlur;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.awt.Color;
import java.util.Stack;

public class NewRender2DUtil implements Wrapper {
    private static final ImageHelper imageHelper = new ImageHelper();
    final static Stack<Rectangle> clipStack = new Stack<>();

    public record Rectangle(float x, float y, float x1, float y1) {
        public boolean contains(double x, double y) {
            return x >= this.x && x <= x1 && y >= this.y && y <= y1;
        }
    }

    public static Canvas getCanvas() {
        return SkiaContext.getCanvas();
    }

    public static void enableRender() {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
    public static void disableRender() {
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    public static void endBuilding(BufferBuilder bb) {
        BuiltBuffer builtBuffer = bb.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram(builtBuffer);
        }
    }

    public static double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float transformColor(float f) {
        return AlphaOverride.compute((int) (f * 255)) / 255f;
    }

    public static io.github.humbleui.skija.Paint getPaint(Color color) {
        io.github.humbleui.skija.Paint paint = new io.github.humbleui.skija.Paint();
        paint.setARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
        return paint;
    }

    public static void save() {
        getCanvas().save();
    }
    public static void restore() {
        getCanvas().restore();
    }
    public static void scale(float s) {
        getCanvas().scale(s, s);
    }

    public static void translate(float x, float y) {
        getCanvas().translate(x, y);
    }

    public static void clip(float x, float y, float w, float h) {
        getCanvas().clipRect(Rect.makeXYWH(x * getScaleFactor(), y * getScaleFactor(), w * getScaleFactor(), h * getScaleFactor()), ClipMode.INTERSECT);
    }

    public static void clip(float x, float y, float width, float height, float radius, ClipMode mode) {

        Path path = new Path();

        path.addRRect(RRect.makeXYWH(x, y, width, height, radius));
        clipPath(path, mode, true);
    }

    public static void clipPath(Path path, ClipMode mode, boolean arg) {
        getCanvas().clipPath(path, mode, arg);
    }

    public static float getScaleFactor() {
        return 1;
    }

    public static void drawRect(double x, double y, double w, double h, Color c) {
        drawRect((float) x, (float) y, (float) w, (float) h, c);
    }

    public static void drawRect(float x, float y, float w, float h, Color c) {
        getCanvas().drawRect(Rect.makeXYWH(x * getScaleFactor(), y * getScaleFactor(), w * getScaleFactor(), h * getScaleFactor()), getPaint(c));
    }

    public static void drawRectOutline(double x, double y, double w, double h, double ow, Color c) {
        drawRoundedRect((float) x, (float) y, (float) w, (float) h, (float) ow, c);
    }

    public static void drawRectOutline(float x, float y, float w, float h, float ow, Color c) {
        float s = getScaleFactor();
        float sx = x * s, sy = y * s, sw = w * s, sh = h * s, sos = ow * s;
        drawRect(sx - sos, sy - sos, sw + 2 * sos, sos, c);
        drawRect(sx - sos, sy + sh, sw + 2 * sos, sos, c);
        drawRect(sx - sos, sy, sos, sh, c);
        drawRect(sx + sw, sy, sos, sh, c);
    }

    public static void drawRoundedRect(double x, double y, double w, double h, double r, Color c) {
        drawRoundedRect((float) x, (float) y, (float) w, (float) h, (float) r, c);
    }

    public static void drawRoundedRect(float x, float y, float w, float h, float r, Color c) {
        float s = getScaleFactor();
        getCanvas().drawRRect(RRect.makeXYWH(x * s, y * s, w * s, h * s, r * s), getPaint(c));
    }

    public static void drawCircle(float x, float y, float r, Color c) {
        getCanvas().drawCircle(x * getScaleFactor(), y * getScaleFactor(), r * getScaleFactor(), getPaint(c));
    }

    public static void drawCircle(double x, double y, double r, Color c) {
        drawCircle((float) x, (float) y, (float) r, c);
    }

    public static void drawCircleWithInline(double x, double y, double r, double id, double iw, Color base, Color inline) {
        drawCircleWithInline((float) x, (float) y, (float) r, (float) id, (float) iw, base, inline);
    }

    public static void drawCircleWithInline(float x, float y, float r, float id, float iw, Color base, Color inline) {
        float s = getScaleFactor();
        drawCircle(x, y, r + id + iw, inline);
        drawCircle(x, y, r + id, inline);
        drawCircle(x, y, r, base);
    }

    public static void drawRectWithOutline(double x, double y, double w, double h, double ow, Color fill, Color outline) {
        drawRectWithOutline((float) x, (float) y, (float) w, (float) h, (float) ow, fill, outline);
    }

    public static void drawRectWithOutline(float x, float y, float w, float h, float ow, Color fill, Color outline) {
        drawRect(x, y, w, h, fill);
        drawRectOutline(x, y, w, h, ow, outline);
    }

    public static void drawRoundedRectWithOutline(double x, double y, double w, double h, double r, double ow, Color fill, Color outline) {
        drawRoundedRectWithOutline((float) x, (float) y, (float) w, (float) h, (float) r, (float) ow, fill, outline);
    }

    public static void drawRoundedRectWithOutline(float x, float y, float w, float h, float r, float ow, Color fill, Color outline) {
        drawRoundedRect(x, y, w, h, r, fill);
        drawRectOutline(x - ow, y - ow, w + 2 * ow, h + 2 * ow, ow, outline);
    }

    public static void drawRectWithAlign(float x, float y, float w, float h, Color c, Aligns align) {
        double[] p = getAlignPosition(x, y, x + w, y + h, w, h, align);
        drawRect((float) p[0], (float) p[1], w, h, c);
    }

    public static void drawRoundedRectWithAlign(double x, double y, double ex, double ey, double w, double h, double r, Color c, Aligns align) {
        drawRoundedRectWithAlign((float) x, (float) y, (float) ex, (float) ey, (float) w, (float) h, (float) r, c, align);
    }

    public static void drawRoundedRectWithAlign(float x, float y, float ex, float ey, float w, float h, float r, Color c, Aligns align) {
        double[] p = getAlignPosition(x, y, ex, ey, w, h, align);
        drawRoundedRect((float) p[0], (float) p[1], w, h, r, c);
    }

    public static void drawCircleWithAlign(float x, float y, float r, Color c, Aligns align) {
        double[] p = getAlignPosition(x, y, x + r * 2, y + r * 2, r * 2, r * 2, align);
        drawCircle((float) p[0], (float) p[1], r, c);
    }

    public static void drawCircleWithInlineWithAlign(double x, double y, double ex, double ey, double r, double id, double iw, Color base, Color inline, Aligns align) {
        double[] p = getAlignPosition(x, y, ex, ey, r * 2, r * 2, align);
        drawCircleWithInline((float) p[0], (float) p[1], (float) r, (float) id, (float) iw, base, inline);
    }

    public static Pair<Double, Double> getAlignPositionAsPair(double x1, double y1, double x2, double y2, double w, double h, Aligns align) {
        double[] pos = getAlignPosition(x1, y1, x2, y2, w, h, align);
        return new Pair<Double, Double>(pos[0], pos[1]);
    }

    public static double[] getAlignPosition(double x1, double y1, double x2, double y2, double w, double h, Aligns align) {
        float s = getScaleFactor();
        w *= s; h *= s; x1 *= s; y1 *= s; x2 *= s; y2 *= s;
        switch (align) {
            case CENTER -> { return new double[] {(x1 + x2 - w) / 2, (y1 + y2 - h) / 2}; }
            case RIGHT -> { return new double[] {x2 - w, (y1 + y2 - h) / 2}; }
            case LEFT -> { return new double[] {x1, (y1 + y2 - h) / 2}; }
            case TOP -> { return new double[] {(x1 + x2 - w) / 2, y1}; }
            case BOTTOM -> { return new double[] {(x1 + x2 - w) / 2, y2 - h}; }
            case LEFT_BOTTOM -> { return new double[] {x1, y2 - h}; }
            case RIGHT_TOP -> { return new double[] {x2 - w, y1}; }
            case RIGHT_BOTTOM -> { return new double[] {x2 - w, y2 - h}; }
            default -> { return new double[] {x1, y1}; }
        }
    }

    public static void pushDisplayArea(Rectangle r) {
        float x = r.x;
        float y = r.y;
        float endX = r.x1;
        float endY = r.y1;

        if (clipStack.isEmpty()) {
            clipStack.push(new Rectangle(x, y, endX, endY));
            getCanvas().clipRect(Rect.makeLTRB(x, y, endX, endY), ClipMode.INTERSECT);
        } else {
            Rectangle lastClip = clipStack.peek();
            float nsx = MathHelper.clamp(x, lastClip.x, lastClip.x1);
            float nsy = MathHelper.clamp(y, lastClip.y, lastClip.y1);
            float nstx = MathHelper.clamp(endX, nsx, lastClip.x1);
            float nsty = MathHelper.clamp(endY, nsy, lastClip.y1);

            clipStack.push(new Rectangle(nsx, nsy, nstx, nsty));
            getCanvas().clipRect(Rect.makeLTRB(nsx, nsy, nstx, nsty), ClipMode.INTERSECT);
        }
    }

    public static void pushDisplayArea(double x, double y, double ex, double ey) {
        pushDisplayArea(new Rectangle((float) x, (float) y, (float) (ex), (float) (ey)));
    }

    public static void popDisplayArea() {
        clipStack.pop();
        if (!clipStack.isEmpty()) {
            Rectangle r = clipStack.peek();
            getCanvas().clipRect(Rect.makeLTRB(r.x, r.y, r.x1, r.y1), ClipMode.INTERSECT);
        } else {
            getCanvas().restore();
        }
    }

    public static void insertDisplayArea(float x, float y, float x1, float y1, Runnable renderAction) {
        pushDisplayArea(new Rectangle(x, y, x1, y1));
        try {
            renderAction.run();
        } finally {
            popDisplayArea();
        }
    }

    public static void verticalGradient(float x, float y, float w, float h, Color top, Color bottom) {
        float sf = getScaleFactor();
        x *= sf;
        y *= sf;
        w *= sf;
        h *= sf;

        io.github.humbleui.skija.Paint paint = new io.github.humbleui.skija.Paint();
        paint.setShader(Shader.makeLinearGradient(0, 0, 0, h, new int[]{top.getRGB(), bottom.getRGB()}, null));
        getCanvas().drawRect(Rect.makeXYWH(x, y, w, h), paint);
    }

    public static void renderGradientTexture(MatrixStack matrices, double x0, double y0, double width, double height, float u, float v, double regionWidth, double regionHeight, double textureWidth, double textureHeight, Color c1, Color c2, Color c3, Color c4) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        renderGradientTextureInternal(buffer, matrices, x0, y0, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight, c1, c2, c3, c4);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    public static void renderGradientTextureInternal(BufferBuilder buff, MatrixStack matrices, double x0, double y0, double width, double height, float u, float v, double regionWidth, double regionHeight, double textureWidth, double textureHeight, Color c1, Color c2, Color c3, Color c4) {
        double x1 = x0 + width;
        double y1 = y0 + height;
        double z = 0;
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buff.vertex(matrix, (float) x0, (float) y1, (float) z).texture((u) / (float) textureWidth, (v + (float) regionHeight) / (float) textureHeight).color(c1.getRGB());
        buff.vertex(matrix, (float) x1, (float) y1, (float) z).texture((u + (float) regionWidth) / (float) textureWidth, (v + (float) regionHeight) / (float) textureHeight).color(c2.getRGB());
        buff.vertex(matrix, (float) x1, (float) y0, (float) z).texture((u + (float) regionWidth) / (float) textureWidth, (v) / (float) textureHeight).color(c3.getRGB());
        buff.vertex(matrix, (float) x0, (float) y0, (float) z).texture((u) / (float) textureWidth, (v + 0.0F) / (float) textureHeight).color(c4.getRGB());
    }

    public static void drawOrbiz(MatrixStack matrices, float z, final double r, Color c) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 20; i++) {
            final float x2 = (float) (Math.sin(((i * 56.548656f) / 180f)) * r);
            final float y2 = (float) (Math.cos(((i * 56.548656f) / 180f)) * r);
            bufferBuilder.vertex(matrix, x2, y2, z).color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 0.4f);
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        disableRender();
    }

    public static void drawStar(MatrixStack matrices, Color c, float scale) {
        enableRender();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.setShaderTexture(0, TextureStorage.star);
        RenderSystem.setShaderColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        renderGradientTexture(matrices, 0, 0, scale, scale, 0, 0, 128, 128, 128, 128, c, c, c, c);
        disableRender();
    }

    public static void drawHeart(MatrixStack matrices, Color c, float scale) {
        enableRender();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.setShaderTexture(0, TextureStorage.heart);
        RenderSystem.setShaderColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        renderGradientTexture(matrices, 0, 0, scale, scale, 0, 0, 128, 128, 128, 128, c, c, c, c);
        disableRender();
    }

    public static void drawBloom(MatrixStack matrices, Color c, float scale) {
        enableRender();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.setShaderTexture(0, TextureStorage.firefly);
        RenderSystem.setShaderColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        renderGradientTexture(matrices, 0, 0, scale, scale, 0, 0, 128, 128, 128, 128, c, c, c, c);
        disableRender();
    }

    public static void setRectPoints(BufferBuilder bufferBuilder, Matrix4f matrix, float x, float y, float x1, float y1, Color c1, Color c2, Color c3, Color c4) {
        bufferBuilder.vertex(matrix, x, y1, 0.0F).color(c1.getRGB());
        bufferBuilder.vertex(matrix, x1, y1, 0.0F).color(c2.getRGB());
        bufferBuilder.vertex(matrix, x1, y, 0.0F).color(c3.getRGB());
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(c4.getRGB());
    }

    public static ImageHelper getImageHelper() {
        return imageHelper;
    }

    public static void drawImage(int textureId, float x, float y, float width, float height, float alpha, SurfaceOrigin origin) {
        if (imageHelper.load(textureId, width, height, origin)) {
            io.github.humbleui.skija.Paint paint = new io.github.humbleui.skija.Paint();
            paint.setAlpha((int) (255 * alpha));
            getCanvas().drawImageRect(imageHelper.get(textureId), Rect.makeXYWH(x, y, width, height), paint);
        }
    }

    public static void drawBlur(double x, double y, double width, double height) {
        drawBlur((float) x, (float) y, (float) width, (float) height);
    }

    public static void drawBlur(float x, float y, float width, float height) {
        if (ClickGUI.INSTANCE.blurBackground.getValue()) {
            Window window = mc.getWindow();
            Path path = new Path();
            path.addRect(Rect.makeXYWH(x * getScaleFactor(), y * getScaleFactor(), width * getScaleFactor(), height * getScaleFactor()));

            save();
            getCanvas().clipPath(path, ClipMode.INTERSECT, true);
            drawImage(KawaseBlur.INGAME_BLUR.getTexture(), 0, 0, window.getScaledWidth(), window.getScaledHeight(), 1F, SurfaceOrigin.BOTTOM_LEFT);
            restore();
        }
    }

    public static void drawRoundedBlur(double x, double y, double width, double height, double radius) {
        drawRoundedBlur((float) x, (float) y, (float) width, (float) height, (float) radius);
    }

    public static void drawRoundedBlur(float x, float y, float width, float height, float radius) {
        if (ClickGUI.INSTANCE != null && ClickGUI.INSTANCE.blurBackground.getValue()) {
            Window window = MinecraftClient.getInstance().getWindow();
            Path path = new Path();
            path.addRRect(RRect.makeXYWH(x * getScaleFactor(), y * getScaleFactor(), width * getScaleFactor(), height *  getScaleFactor(), radius));

            save();
            getCanvas().clipPath(path, ClipMode.INTERSECT, true);
            drawImage(KawaseBlur.INGAME_BLUR.getTexture(), 0, 0, window.getScaledWidth(), window.getScaledHeight(), 1F, SurfaceOrigin.BOTTOM_LEFT);
            restore();
        }
    }

    public static void drawShadow(float x, float y, float width, float height, float radius) {

        io.github.humbleui.skija.Paint paint = getPaint(new Color(0, 0, 0, 120));

        paint.setImageFilter(ImageFilter.makeBlur(2.5F, 2.5F, FilterTileMode.DECAL));
        save();
        clip(x, y, width, height, radius, ClipMode.DIFFERENCE);
        getCanvas().drawRRect(RRect.makeXYWH(x, y, width, height, radius), paint);
        restore();
    }
}
