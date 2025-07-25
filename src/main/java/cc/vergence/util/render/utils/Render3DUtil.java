package cc.vergence.util.render.utils;

import cc.vergence.injections.accessors.render.WorldRendererAccessor;
import cc.vergence.util.interfaces.Wrapper;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Render3DUtil implements Wrapper {
    public static boolean RELOADED = false;

    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();
    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

    public static List<VertexCollection> QUADS = new ArrayList<>();
    public static List<VertexCollection> DEBUG_LINES = new ArrayList<>();
    public static List<VertexCollection> SHINE_QUADS = new ArrayList<>();
    public static List<VertexCollection> SHINE_DEBUG_LINES = new ArrayList<>();

    public static void reload() {
        QUADS = new ArrayList<>();
        DEBUG_LINES = new ArrayList<>();

        SHINE_QUADS = new ArrayList<>();
        SHINE_DEBUG_LINES = new ArrayList<>();

        RELOADED = true;
    }

    public static @NotNull Vec3d worldSpaceToScreenSpace(@NotNull Vec3d pos) {
        Camera camera = mc.getEntityRenderDispatcher().camera;
        int displayHeight = mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f target = new Vector3f();

        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.f).mul(lastWorldSpaceMatrix);
        Matrix4f matrixProj = new Matrix4f(lastProjMat);
        Matrix4f matrixModel = new Matrix4f(lastModMat);
        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
        return new Vec3d(target.x / mc.getWindow().getScaleFactor(), (displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color) {
        draw3DBox(matrixStack, box, color, true, color, true);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color, Color outlineColor) {
        draw3DBox(matrixStack, box, color, true, outlineColor, true);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color, boolean fill, Color outlineColor, boolean outline) {
        box = box.offset(mc.gameRenderer.getCamera().getPos().negate());
        RenderSystem.enableBlend();
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

        if (outline) {
            RenderSystem.setShaderColor(outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);
            RenderSystem.setShader(ShaderProgramKeys.POSITION);
            tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);

            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderSystem.disableBlend();

        RenderSystem.enableBlend();
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        matrix = matrixStack.peek().getPositionMatrix();
        tessellator = RenderSystem.renderThreadTesselator();
        bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        if (fill) {
            RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
            RenderSystem.setShader(ShaderProgramKeys.POSITION);
            tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);

            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);

            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);

            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderSystem.disableBlend();
    }

    public static void draw(java.util.List<VertexCollection> quads, List<VertexCollection> debugLines, boolean shine) {
        RenderSystem.enableBlend();
        if (shine) RenderSystem.blendFunc(770, 32772);
        else RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.disableDepthTest();

        if (!quads.isEmpty()) {
            BufferBuilder buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            for (VertexCollection collection : quads) collection.vertex(buffer);

            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            RenderSystem.disableCull();

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.enableCull();
        }

        if (!debugLines.isEmpty()) {
            BufferBuilder buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            for (VertexCollection collection : debugLines) collection.vertex(buffer);

            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static boolean isFrustumVisible(Box box) {
        return ((WorldRendererAccessor) mc.worldRenderer).getFrustum().isVisible(box);
    }

    private static Vec3d cameraTransform(Vec3d vec3d) {
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        return new Vec3d(vec3d.x - camera.getX(), vec3d.y - camera.getY(), vec3d.z - camera.getZ());
    }

    private static Box cameraTransform(Box box) {
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        return new Box(box.minX - camera.getX(), box.minY - camera.getY(), box.minZ - camera.getZ(), box.maxX - camera.getX(), box.maxY - camera.getY(), box.maxZ - camera.getZ());
    }

    public record VertexCollection(Vertex... vertices) {
        public void vertex(BufferBuilder buffer) {
            for (Vertex vertex : vertices) {
                buffer.vertex(vertex.matrix, vertex.x, vertex.y, vertex.z).color(vertex.color.getRed(), vertex.color.getGreen(), vertex.color.getBlue(), vertex.color.getAlpha());
            }
        }
    }

    public record Vertex(Matrix4f matrix, float x, float y, float z, Color color) {}
}
