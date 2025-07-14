package cc.vergence.util.render;

import cc.vergence.util.interfaces.Wrapper;
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

public class Render3DUtil implements Wrapper {
    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();
    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

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

    // 世界坐标 → 屏幕坐标 (-1~1)
    public static Vec3d toScreen(Vec3d pos) {
        Camera camera = mc.gameRenderer.getCamera();
        Matrix4f proj  = new Matrix4f(RenderSystem.getProjectionMatrix());
        Matrix4f model = new Matrix4f(RenderSystem.getModelViewMatrix());
        Matrix4f matrix = new Matrix4f();
        proj.mul(model, matrix);

        Vector4f vec = new Vector4f((float) pos.x, (float) pos.y, (float) pos.z, 1.0F);
        vec.mul(matrix);

        if (vec.w() == 0) return new Vec3d(0, 0, -1);

        float wInv = 1F / vec.w();
        float x = (vec.x() * wInv * 0.5F + 0.5F) * mc.getWindow().getFramebufferWidth();
        float y = (1.0F - vec.y() * wInv * 0.5F - 0.5F) * mc.getWindow().getFramebufferHeight();
        float z = vec.z() * wInv;

        return new Vec3d(x, y, z);
    }
}
