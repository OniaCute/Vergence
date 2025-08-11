package cc.vergence.util.render.utils;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.modules.client.Client;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.maths.MathUtil;
import cc.vergence.util.other.TextureStorage;
import cc.vergence.util.render.other.AlphaOverride;
import cc.vergence.util.render.other.SkiaContext;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.Path;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import io.github.humbleui.skija.Canvas;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.Stack;
import java.util.function.Function;

public class Render2DUtil implements Wrapper {
    final static Stack<Rectangle> clipStack = new Stack<>();

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

    public static void drawRect(DrawContext context, double x, double y, double width, double height, Color color) {
        drawRect(context, (x * getScaleFactor()), (y * getScaleFactor()), ((width) * getScaleFactor()), ((height) * getScaleFactor()), color.getRGB());
    }

    public static void drawRect(DrawContext context, double x, double y, double width, double height, int color) {
        drawRect(context, (float) (x * getScaleFactor()), (float) (y * getScaleFactor()), (float) (width * getScaleFactor()), (float) (height * getScaleFactor()), color);
    }

    public static void drawRect(DrawContext context, float x, float y, float width, float height, int color) {
        if (width == 0 || height == 0) {
            return;
        }

        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        float alpha = transformColor((float) (color >> 24 & 255) / 255.0F);
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x, y + height, 0.0F).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, x + width, y + height, 0.0F).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, x + width, y, 0.0F).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(red, green, blue, alpha);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        disableRender();
    }

    public static void drawRectOutline(DrawContext context, double x, double y, double width, double height, double outlineWidth, Color outlineColor) {
        float scale = getScaleFactor();
        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;
        outlineWidth *= scale;

        drawRect(context, x - outlineWidth, y - outlineWidth, width + 2 * outlineWidth, outlineWidth, outlineColor);
        drawRect(context, x - outlineWidth, y + height, width + 2 * outlineWidth, outlineWidth, outlineColor);
        drawRect(context, x - outlineWidth, y, outlineWidth, height, outlineColor);
        drawRect(context, x + width, y, outlineWidth, height, outlineColor);
    }

    public static void drawRectWithOutline(DrawContext context, double x, double y, double width, double height, double outlineWidth, Color fillColor, Color outlineColor) {
        float scale = getScaleFactor();
        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;
        outlineWidth *= scale;

        drawRect(context, x - outlineWidth, y - outlineWidth, width + 2 * outlineWidth, outlineWidth, outlineColor);
        drawRect(context, x - outlineWidth, y + height, width + 2 * outlineWidth, outlineWidth, outlineColor);
        drawRect(context, x - outlineWidth, y, outlineWidth, height, outlineColor);
        drawRect(context, x + width, y, outlineWidth, height, outlineColor);
        drawRect(context, x, y, width, height, fillColor);
    }


    public static void drawRectHorizontal(MatrixStack matrices, float x, float y, float width, float height, Color startColor, Color endColor) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(startColor.getRGB());
        bufferBuilder.vertex(matrix, x, y + height, 0.0F).color(startColor.getRGB());
        bufferBuilder.vertex(matrix, x + width, y + height, 0.0F).color(endColor.getRGB());
        bufferBuilder.vertex(matrix, x + width, y, 0.0F).color(endColor.getRGB());
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        disableRender();
    }
    public static void drawRectVertical(MatrixStack matrices, float x, float y, float width, float height, Color startColor, Color endColor) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(startColor.getRGB());
        bufferBuilder.vertex(matrix, x, y + height, 0.0F).color(endColor.getRGB());
        bufferBuilder.vertex(matrix, x + width, y + height, 0.0F).color(endColor.getRGB());
        bufferBuilder.vertex(matrix, x + width, y, 0.0F).color(startColor.getRGB());
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        disableRender();
    }
    public static void verticalGradient(MatrixStack matrices, float left, float top, float right, float bottom, Color startColor, Color endColor) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, left, top, 0.0F).color(startColor.getRGB());
        bufferBuilder.vertex(matrix, left, bottom, 0.0F).color(endColor.getRGB());
        bufferBuilder.vertex(matrix, right, bottom, 0.0F).color(endColor.getRGB());
        bufferBuilder.vertex(matrix, right, top, 0.0F).color(startColor.getRGB());
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        disableRender();
    }
    public static void setRectPoints(BufferBuilder bufferBuilder, Matrix4f matrix, float x, float y, float x1, float y1, Color c1, Color c2, Color c3, Color c4) {
        bufferBuilder.vertex(matrix, x, y1, 0.0F).color(c1.getRGB());
        bufferBuilder.vertex(matrix, x1, y1, 0.0F).color(c2.getRGB());
        bufferBuilder.vertex(matrix, x1, y, 0.0F).color(c3.getRGB());
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(c4.getRGB());
    }

    public static void drawRect(DrawContext context, float x, float y, float width, float height, int color, float outlineWidth, int outlineColor) {
        x = (x * getScaleFactor());
        y = (y * getScaleFactor());
        width = ((width + x) * getScaleFactor());
        height = ((height + y) * getScaleFactor());

        context.fill((int) x, (int) y, (int) (x + width), (int) (y + height), color);
        context.fill((int) x, (int) y, (int) (x + width), (int) (y + outlineWidth), outlineColor);
        context.fill((int) x, (int) (y + height - outlineWidth), (int) (x + width), (int) (y + height), outlineColor);
        context.fill((int) x, (int) y, (int) (x + outlineWidth), (int) (y + height), outlineColor);
        context.fill((int) (x + width - outlineWidth), (int) y, (int) (x + width), (int) (y + height), outlineColor);
    }

    public static void drawRoundedRect(MatrixStack matrices, float x, float y, float width, float height, float radius, int color) {
        renderRounded(matrices, new Color(color), x, y, width + x, height + y, radius, 4);
    }

    public static void drawRoundedRect(MatrixStack matrices, float x, float y, float width, float height, float radius, Color color) {
        renderRounded(matrices, color, (x * getScaleFactor()), (y * getScaleFactor()), ((width + x) * getScaleFactor()), ((height + y) * getScaleFactor()), ((radius * getScaleFactor())), 4);
    }

    public static void drawRoundedRect(MatrixStack matrices, double x, double y, double width, double height, double radius, Color color) {
        drawRoundedRect(matrices, (float) x, (float) y, (float) width, (float) height, (float) radius, color);
    }

    public static void drawRoundedRectWithOutline(MatrixStack matrices, double x, double y, double width, double height, double radius, double outlineWidth, Color fillColor, Color outlineColor) {
        drawRoundedRectWithOutline(matrices, (float) x, (float) y, (float) width, (float) height, (float) radius, (float) outlineWidth, fillColor, outlineColor);
    }

    public static void drawRoundedRectWithOutline(MatrixStack matrices, float x, float y, float width, float height, float radius, float outlineWidth, Color fillColor, Color outlineColor) {
        float scale = Render2DUtil.getScaleFactor();
        double scaledRadius = radius * scale;
        double scaledOutline = outlineWidth * scale;
        double innerX1 = x;
        double innerY1 = y;
        double innerX2 = x + width;
        double innerY2 = y + height;
        double outerX1 = x - scaledOutline;
        double outerY1 = y - scaledOutline;
        double outerX2 = x + width + scaledOutline;
        double outerY2 = y + height + scaledOutline;
        double outerRadius = scaledRadius + scaledOutline;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Render2DUtil.renderRounded(matrices, outlineColor, outerX1, outerY1, outerX2, outerY2, outerRadius, 4);
        Render2DUtil.renderRounded(matrices,
                new Color(0, 0, 0, 0),
                innerX1, innerY1, innerX2, innerY2, scaledRadius, 4);
        Render2DUtil.renderRounded(matrices, fillColor, innerX1, innerY1, innerX2, innerY2, scaledRadius, 4);
        RenderSystem.disableBlend();
    }



    public static void drawCircle(MatrixStack matrices, Color c, double originX, double originY, double radius, int segments) {
        originX *= getScaleFactor();
        originY *= getScaleFactor();
        radius *= getScaleFactor();
        int segments1 = MathHelper.clamp(segments, 4, 360);
        int color = c.getRGB();

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = transformColor((float) (color >> 24 & 255) / 255.0F);
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float k = (float) (color & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 360; i += Math.min(360 / segments1, 360 - i)) {
            double radians = Math.toRadians(i);
            double sin = Math.sin(radians) * radius;
            double cos = Math.cos(radians) * radius;
            bufferBuilder.vertex(matrix, (float) (originX + sin), (float) (originY + cos), 0).color(g, h, k, f);
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        disableRender();
    }

    public static void drawCircleWithInline(MatrixStack matrices, Color baseColor, Color inlineColor,
                                            double originX, double originY, double radius,
                                            float inlineDistance, float inlineWidth, int segments) {
        inlineDistance *= getScaleFactor();
        inlineWidth *= getScaleFactor();

        drawCircle(matrices, baseColor, originX, originY, radius, segments);

        originX *= getScaleFactor();
        originY *= getScaleFactor();
        radius *= getScaleFactor();
        int clampedSegments = MathHelper.clamp(segments, 8, 360);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float br = baseColor.getRed() / 255f;
        float bg = baseColor.getGreen() / 255f;
        float bb = baseColor.getBlue() / 255f;
        float ba = transformColor(baseColor.getAlpha() / 255f);

        float ir = inlineColor.getRed() / 255f;
        float ig = inlineColor.getGreen() / 255f;
        float ib = inlineColor.getBlue() / 255f;
        float ia = transformColor(inlineColor.getAlpha() / 255f);

        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        float innerR = (float) (radius - inlineDistance - inlineWidth);
        float outerR = (float) (radius - inlineDistance);
        if (innerR > 0 && inlineWidth > 0) {
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
            for (int i = 0; i <= clampedSegments; i++) {
                double angle = 2 * Math.PI * i / clampedSegments;
                float cos = (float) Math.cos(angle);
                float sin = (float) Math.sin(angle);

                buffer.vertex(matrix, (float) (originX + cos * outerR), (float) (originY + sin * outerR), 0).color(ir, ig, ib, ia);
                buffer.vertex(matrix, (float) (originX + cos * innerR), (float) (originY + sin * innerR), 0).color(ir, ig, ib, ia);
            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }

        if (innerR > 0) {
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
            buffer.vertex(matrix, (float) originX, (float) originY, 0).color(br, bg, bb, ba);
            for (int i = 0; i <= clampedSegments; i++) {
                double angle = 2 * Math.PI * i / clampedSegments;
                float x = (float) (originX + Math.cos(angle) * innerR);
                float y = (float) (originY + Math.sin(angle) * innerR);
                buffer.vertex(matrix, x, y, 0).color(br, bg, bb, ba);
            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }

        disableRender();
    }

    public static Pair<Double, Double> drawRectWithAlign(DrawContext context, double x, double y, double x2, double y2, double width, double height, Color color, Aligns align) {
        double[] pos = getAlignPosition(x, y, x2, y2, width, height, align);
        drawRect(context, pos[0], pos[1], width, height, color);
        return new Pair<>(pos[0], pos[1]);
    }

    public static void drawRectWithAlign(DrawContext context, float x, float y, float x2, float y2, float width, float height, int color, Aligns align) {
        double[] pos = getAlignPosition(x, y, x2, y2, width, height, align);
        drawRect(context, (float) pos[0], (float) pos[1], width, height, color);
    }

    public static Pair<Double, Double> drawRoundedRectWithAlign(MatrixStack matrices, double x, double y, double x2, double y2, double width, double height, double radius, Color color, Aligns align) {
        double[] pos = getAlignPosition(x, y, x2, y2, width, height, align);
        drawRoundedRect(matrices, (float) pos[0], (float) pos[1], width, height, radius, color);
        return new Pair<>(pos[0], pos[1]);
    }

    public static void drawRoundedRectWithAlign(MatrixStack matrices, float x, float y, float x2, float y2, float width, float height, double radius, Color color, Aligns align) {
        double[] pos = getAlignPosition(x, y, x2, y2, width, height, align);
        drawRoundedRect(matrices, (float) pos[0], (float) pos[1], width, height, radius, color);
    }

    public static void drawCircleWithAlign(MatrixStack matrices, Color c, double originX, double originY, double radius, int segments, Aligns align) {
        double[] pos = getAlignPosition(originX, originY, originX + radius * 2, originY + radius * 2, radius * 2, radius * 2, align);
        drawCircle(matrices, c, pos[0], pos[1], radius, segments);
    }

    public static void drawCircleWithInlineWithAlign(MatrixStack matrices, Color baseColor, Color inlineColor,
                                                     double originX, double originY, double radius,
                                                     float inlineDistance, float inlineWidth, int segments, Aligns align) {
        double[] pos = getAlignPosition(originX, originY, originX + radius * 2, originY + radius * 2, radius * 2, radius * 2, align);
        drawCircleWithInline(matrices, baseColor, inlineColor, pos[0], pos[1], radius, inlineDistance, inlineWidth, segments);
    }

    public static void drawCircleWithInlineWithAlign(MatrixStack matrices, Color baseColor, Color inlineColor,
                                                     double x, double y, double x2, double y2, double radius,
                                                     float inlineDistance, float inlineWidth, int segments, Aligns align) {
        double[] pos = getAlignPosition(x, y, x2 + radius * 2, y2 + radius * 2, radius * 2, radius * 2, align);
        drawCircleWithInline(matrices, baseColor, inlineColor, pos[0], pos[1], radius, inlineDistance, inlineWidth, segments);
    }



    public static void renderRounded(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double radius, double samples) {
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        renderRoundedQuadInternal(matrices.peek().getPositionMatrix(), c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f, fromX, fromY, toX, toY, radius, samples);
        disableRender();
    }

    public static void renderRounded2(MatrixStack matrices, Color c, Color c2, Color c3, Color c4, double fromX, double fromY, double toX, double toY, double radius) {
        enableRender();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        renderRoundedQuadInternal2(matrices.peek().getPositionMatrix(), c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f, c2.getRed() / 255f, c2.getGreen() / 255f, c2.getBlue() / 255f, c2.getAlpha() / 255f, c3.getRed() / 255f, c3.getGreen() / 255f, c3.getBlue() / 255f, c3.getAlpha() / 255f, c4.getRed() / 255f, c4.getGreen() / 255f, c4.getBlue() / 255f, c4.getAlpha() / 255f, fromX, fromY, toX, toY, radius);
        disableRender();
    }

    public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double radius, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double[][] map = new double[][]{new double[]{toX - radius, toY - radius, radius}, new double[]{toX - radius, fromY + radius, radius}, new double[]{fromX + radius, fromY + radius, radius}, new double[]{fromX + radius, toY - radius, radius}};
        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            double rad = current[2];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca);
            }
            float rad1 = (float) Math.toRadians((360 / 4d + i * 90d));
            float sin = (float) (Math.sin(rad1) * rad);
            float cos = (float) (Math.cos(rad1) * rad);
            bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca);
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void renderRoundedQuadInternal2(Matrix4f matrix, float cr, float cg, float cb, float ca, float cr1, float cg1, float cb1, float ca1, float cr2, float cg2, float cb2, float ca2, float cr3, float cg3, float cb3, float ca3, double fromX, double fromY, double toX, double toY, double radC1) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double[][] map = new double[][]{new double[]{toX - radC1, toY - radC1, radC1}, new double[]{toX - radC1, fromY + radC1, radC1}, new double[]{fromX + radC1, fromY + radC1, radC1}, new double[]{fromX + radC1, toY - radC1, radC1}};

        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            double rad = current[2];
            for (double r = i * 90; r < (90 + i * 90); r += 10) {
                float rad1 = (float) java.lang.Math.toRadians(r);
                float sin = (float) (java.lang.Math.sin(rad1) * rad);
                float cos = (float) (java.lang.Math.cos(rad1) * rad);
                switch (i) {
                    case 0 ->
                            bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr1, cg1, cb1, ca1);
                    case 1 ->
                            bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca);
                    case 2 ->
                            bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr2, cg2, cb2, ca2);
                    default ->
                            bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr3, cg3, cb3, ca3);
                }
            }
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void pushDisplayArea(MatrixStack stack, Rectangle r1) {
        Matrix4f matrix = stack.peek().getPositionMatrix();
        Vector4f coord = new Vector4f(r1.x, r1.y, 0, 1);
        Vector4f end = new Vector4f(r1.x1, r1.y1, 0, 1);
        coord.mulTranspose(matrix);
        end.mulTranspose(matrix);
        float x = coord.x();
        float y = coord.y();
        float endX = end.x();
        float endY = end.y();
        Rectangle r = new Rectangle(x, y, endX, endY);
        if (clipStack.empty()) {
            clipStack.push(r);
            beginScissor(r.x, r.y, r.x1, r.y1);
        } else {
            Rectangle lastClip = clipStack.peek();
            float lsx = lastClip.x;
            float lsy = lastClip.y;
            float lstx = lastClip.x1;
            float lsty = lastClip.y1;
            float nsx = MathHelper.clamp(r.x, lsx, lstx);
            float nsy = MathHelper.clamp(r.y, lsy, lsty);
            float nstx = MathHelper.clamp(r.x1, nsx, lstx);
            float nsty = MathHelper.clamp(r.y1, nsy, lsty);
            clipStack.push(new Rectangle(nsx, nsy, nstx, nsty));
            beginScissor(nsx, nsy, nstx, nsty);
        }
    }

    public static void pushDisplayArea(MatrixStack stack, float x, float y, float x1, float y1, double animation_factor) {
        float h = y + y1;
        float h2 = (float) (h * (1d - MathUtil.clamp(animation_factor, 0, 1.0025f)));

        float x3 = x;
        float y3 = y + h2;
        float x4 = x1;
        float y4 = y1 - h2;

        if (x4 < x3) x4 = x3;
        if (y4 < y3) y4 = y3;
        pushDisplayArea(stack, new Rectangle(x3, y3, x4, y4));
    }

    public static void popDisplayArea() {
        clipStack.pop();
        if (clipStack.empty()) {
            endScissor();
        } else {
            Rectangle r = clipStack.peek();
            beginScissor(r.x, r.y, r.x1, r.y1);
        }
    }

    public static void insertDisplayArea(MatrixStack stack, float x, float y, float x1, float y1, double animationFactor, Runnable renderAction) {
        float h = y + y1;
        float h2 = (float) (h * (1d - MathUtil.clamp(animationFactor, 0, 1.0025f)));

        float x3 = x;
        float y3 = y + h2;
        float x4 = x1;
        float y4 = y1 - h2;

        if (x4 < x3) x4 = x3;
        if (y4 < y3) y4 = y3;
        Matrix4f matrix = stack.peek().getPositionMatrix();
        Vector4f coord = new Vector4f(x3, y3, 0, 1);
        Vector4f end = new Vector4f(x4, y4, 0, 1);
        coord.mulTranspose(matrix);
        end.mulTranspose(matrix);

        float dx = coord.x();
        float dy = coord.y();
        float dx1 = end.x();
        float dy1 = end.y();

        float d = (float) mc.getWindow().getScaleFactor();
        double width = java.lang.Math.max(0, dx1 - dx);
        double height = java.lang.Math.max(0, dy1 - dy);
        int ay = (int) ((mc.getWindow().getScaledHeight() - (dy + height)) * d);
        RenderSystem.enableScissor((int) (dx * d), ay, (int) (width * d), (int) (height * d));
        try {
            renderAction.run();
        } finally {
            if (!clipStack.isEmpty()) {
                Rectangle r = clipStack.peek();
                beginScissor(r.x, r.y, r.x1, r.y1);
            } else {
                endScissor();
            }
        }
    }

    public static void beginScissor(double x, double y, double endX, double endY) {
        double width = endX - x;
        double height = endY - y;
        width = java.lang.Math.max(0, width);
        height = java.lang.Math.max(0, height);
        float d = (float) mc.getWindow().getScaleFactor();
        int ay = (int) ((mc.getWindow().getScaledHeight() - (y + height)) * d);
        RenderSystem.enableScissor((int) (x * d), ay, (int) (width * d), (int) (height * d));
    }

    public static void endScissor() {
        RenderSystem.disableScissor();
    }

    public record Rectangle(float x, float y, float x1, float y1) {
        public boolean contains(double x, double y) {
            return x >= this.x && x <= x1 && y >= this.y && y <= y1;
        }
    }

    public static double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float transformColor(float f) {
        return AlphaOverride.compute((int) (f * 255)) / 255f;
    }

    public static float getScaleFactor() {
        if (Client.INSTANCE == null || Client.INSTANCE.UIScale == null) {
            return 1.0f;
        }
        return switch ((Client.UIScales) Client.INSTANCE.UIScale.getValue()) {
            case X50 -> 0.5f;
            case X150 -> 1.5f;
            case X200 -> 2.0f;
            default -> 1.0f;
        };
    }

    public static double[] getAlignPosition(double x1, double y1, double x2, double y2, double width, double height, Aligns align) {
        float scale = getScaleFactor();
        width *= scale;
        height *= scale;
        x1 *= scale;
        y1 *= scale;
        x2 *= scale;
        y2 *= scale;

        double startX = x1, startY = y1;

        switch (align) {
            case LEFT -> {
                startX = x1;
                startY = (y1 + y2 - height) / 2;
            }
            case RIGHT -> {
                startX = x2 - width;
                startY = (y1 + y2 - height) / 2;
            }
            case CENTER -> {
                startX = (x1 + x2 - width) / 2;
                startY = (y1 + y2 - height) / 2;
            }
            case TOP -> {
                startX = (x1 + x2 - width) / 2;
                startY = y1;
            }
            case BOTTOM -> {
                startX = (x1 + x2 - width) / 2;
                startY = y2 - height;
            }
            case LEFT_TOP -> {
                startX = x1;
                startY = y1;
            }
            case LEFT_BOTTOM -> {
                startX = x1;
                startY = y2 - height;
            }
            case RIGHT_TOP -> {
                startX = x2 - width;
                startY = y1;
            }
            case RIGHT_BOTTOM -> {
                startX = x2 - width;
                startY = y2 - height;
            }
        }

        return new double[]{startX, startY};
    }

    public static void drawTextureCustom(
          DrawContext context,
          Function<Identifier, RenderLayer> renderLayers,
          Identifier sprite,
          int x,
          int y,
          float u,
          float v,
          int width,
          int height,
          int textureWidth,
          int textureHeight,
          int color
    ) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, sprite);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        float u2 = u + (float)width / textureWidth;
        float v2 = v + (float)height / textureHeight;

        float alpha = (float)((color >> 24) & 0xFF) / 255.0F;
        float red = (float)((color >> 16) & 0xFF) / 255.0F;
        float green = (float)((color >> 8) & 0xFF) / 255.0F;
        float blue = (float)(color & 0xFF) / 255.0F;

        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        bufferBuilder.vertex(matrix, x, y, 0).texture(u, v).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, x, y + height, 0).texture(u, v2).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, x + width, y + height, 0).texture(u2, v2).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, x + width, y, 0).texture(u2, v).color(red, green, blue, alpha);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void renderTexture(MatrixStack matrices, double x0, double y0, double width, double height, float u, float v, double regionWidth, double regionHeight, double textureWidth, double textureHeight) {
        double x1 = x0 + width;
        double y1 = y0 + height;
        double z = 0;
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrix, (float) x0, (float) y1, (float) z).texture((u) / (float) textureWidth, (v + (float) regionHeight) / (float) textureHeight);
        buffer.vertex(matrix, (float) x1, (float) y1, (float) z).texture((u + (float) regionWidth) / (float) textureWidth, (v + (float) regionHeight) / (float) textureHeight);
        buffer.vertex(matrix, (float) x1, (float) y0, (float) z).texture((u + (float) regionWidth) / (float) textureWidth, (v) / (float) textureHeight);
        buffer.vertex(matrix, (float) x0, (float) y0, (float) z).texture((u) / (float) textureWidth, (v + 0.0F) / (float) textureHeight);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
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
            final float x2 = (float) (java.lang.Math.sin(((i * 56.548656f) / 180f)) * r);
            final float y2 = (float) (java.lang.Math.cos(((i * 56.548656f) / 180f)) * r);
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
}
