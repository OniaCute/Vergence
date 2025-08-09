package cc.vergence.modules.visual;

import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.other.ModelRenderer;
import cc.vergence.util.render.utils.Render2DUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4d;

import java.awt.*;

public class ItemESP extends Module {
    public static ItemESP INSTANCE;

    public ItemESP() {
        super("ItemESP", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Circle));
//    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(255, 255, 255, 255)));
    public Option<Color> espColor = addOption(new ColorOption("EspColor", new Color(0, 0, 0, 240)));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0.1, 5, 0.6, v -> mode.getValue().equals(Modes.Circle)));
    public Option<Double> points = addOption(new DoubleOption("Points", 3, 42, 6, v -> mode.getValue().equals(Modes.Circle)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw2D() {
//        for (Entity ent : mc.world.getEntities()) {
//            if (!(ent instanceof ItemEntity)) {
//                continue;
//            }
//            Vec3d[] vectors = getPoints(ent, mc.getRenderTickCounter().getTickDelta(true));
//            Vector4d position = null;
//            for (Vec3d vector : vectors) {
//                vector = Render3DUtil.worldSpaceToScreenSpace(new Vec3d(vector.x, vector.y, vector.z));
//                if (vector.z > 0 && vector.z < 1) {
//                    if (position == null) {
//                        position = new Vector4d(vector.x, vector.y, vector.z, 0);
//                    }
//                    position.x = Math.min(vector.x, position.x);
//                    position.y = Math.min(vector.y, position.y);
//                    position.z = Math.max(vector.x, position.z);
//                    position.w = Math.max(vector.y, position.w);
//                }
//            }
//
//            if (position != null) {
//                float posX = (float) position.x;
//                float posY = (float) position.y;
//                float endPosX = (float) position.z;
//                float diff = (endPosX - posX) / 2f;
//                double textWidth = (FontUtil.getWidth(FontSize.SMALL, ent.getName().getString()));
//                double tagX = (posX + diff - textWidth / 2f);
//                FontUtil.drawText(
//                        context,
//                        ent.getDisplayName().getString(),
//                        tagX,
//                        (float) posY - 10,
//                        textColor.getValue(),
//                        FontSize.SMALL
//                );
//            }
//        }
//
//        if (mode.getValue().equals(Modes.Rect)) {
//            boolean any = false;
//            for (Entity ent : mc.world.getEntities()) {
//                if (!(ent instanceof ItemEntity)) {
//                    continue;
//                }
//                Vec3d[] vectors = getPoints(ent, tickDelta);
//
//                Vector4d position = null;
//                for (Vec3d vector : vectors) {
//                    vector = Render3DUtil.worldSpaceToScreenSpace(new Vec3d(vector.x, vector.y, vector.z));
//                    if (vector.z > 0 && vector.z < 1) {
//                        if (position == null) {
//                            position = new Vector4d(vector.x, vector.y, vector.z, 0);
//                        }
//                        position.x = Math.min(vector.x, position.x);
//                        position.y = Math.min(vector.y, position.y);
//                        position.z = Math.max(vector.x, position.z);
//                        position.w = Math.max(vector.y, position.w);
//                    }
//                }
//
//                if (position != null) {
//                    any = true;
//                }
//            }
//            if (!any) {
//                return;
//            }
//            Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
//            Render2DUtil.enableRender();
//            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
//
//            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//
//            for (Entity ent : mc.world.getEntities()) {
//                if (!(ent instanceof ItemEntity)) {
//                    continue;
//                }
//                Vec3d[] vectors = getPoints(ent, tickDelta);
//
//                Vector4d position = null;
//                for (Vec3d vector : vectors) {
//                    vector = Render3DUtil.worldSpaceToScreenSpace(new Vec3d(vector.x, vector.y, vector.z));
//                    if (vector.z > 0 && vector.z < 1) {
//                        if (position == null)
//                            position = new Vector4d(vector.x, vector.y, vector.z, 0);
//                        position.x = Math.min(vector.x, position.x);
//                        position.y = Math.min(vector.y, position.y);
//                        position.z = Math.max(vector.x, position.z);
//                        position.w = Math.max(vector.y, position.w);
//                    }
//                }
//
//                if (position != null) {
//                    float posX = (float) position.x;
//                    float posY = (float) position.y;
//                    float endPosX = (float) position.z;
//                    float endPosY = (float) position.w;
//
//                    drawRect(bufferBuilder, matrix, posX, posY, endPosX, endPosY);
//                }
//            }
//            Render2DUtil.endBuilding(bufferBuilder);
//            Render2DUtil.disableRender();
//        }
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (mode.getValue().equals(Modes.Circle)) {
            for (Entity ent : mc.world.getEntities()) {
                if (ent instanceof ItemEntity) {
                    Render3DUtil.drawCircle3D(matrixStack, tickDelta, ent, radius.getValue(), espColor.getValue().getRGB(), points.getValue().intValue());
                }
            }
        }
    }

    private void drawRect(BufferBuilder bufferBuilder, Matrix4f stack, float posX, float posY, float endPosX, float endPosY) {
        Color black = Color.BLACK;
        Render2DUtil.setRectPoints(bufferBuilder, stack, posX - 1F, posY, (posX + 0.5f), endPosY + 0.5f, black, black, black, black);
        Render2DUtil.setRectPoints(bufferBuilder, stack, posX - 1F, (posY - 0.5f), endPosX + 0.5f, posY + 1f, black, black, black, black);
        Render2DUtil.setRectPoints(bufferBuilder, stack, endPosX - 1f, posY, endPosX + 0.5f, endPosY + 0.5f, black, black, black, black);
        Render2DUtil.setRectPoints(bufferBuilder, stack, posX - 1, endPosY - 1f, endPosX + 0.5f, endPosY + 0.5f, black, black, black, black);
        Render2DUtil.setRectPoints(bufferBuilder, stack, posX - 0.5f, posY, posX, endPosY, espColor.getValue(), espColor.getValue(), espColor.getValue(), espColor.getValue());
        Render2DUtil.setRectPoints(bufferBuilder, stack, posX, endPosY - 0.5f, endPosX, endPosY, espColor.getValue(), espColor.getValue(), espColor.getValue(), espColor.getValue());
        Render2DUtil.setRectPoints(bufferBuilder, stack, posX - 0.5f, posY, endPosX, (posY + 0.5f), espColor.getValue(), espColor.getValue(), espColor.getValue(), espColor.getValue());
        Render2DUtil.setRectPoints(bufferBuilder, stack, endPosX - 0.5f, posY, endPosX, endPosY, espColor.getValue(), espColor.getValue(), espColor.getValue(), espColor.getValue());
    }

    @NotNull
    private static Vec3d[] getPoints(Entity ent, float tickDelta) {
        Box axisAlignedBB = getBox(ent, tickDelta);
        Vec3d[] vectors = new Vec3d[]{new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
        return vectors;
    }

    @NotNull
    private static Box getBox(Entity ent, float tickDelta) {
        double x = ent.prevX + (ent.getX() - ent.prevX) * tickDelta;
        double y = ent.prevY + (ent.getY() - ent.prevY) * tickDelta;
        double z = ent.prevZ + (ent.getZ() - ent.prevZ) * tickDelta;
        Box axisAlignedBB2 = ent.getBoundingBox();
        Box axisAlignedBB = new Box(axisAlignedBB2.minX - ent.getX() + x - 0.05, axisAlignedBB2.minY - ent.getY() + y, axisAlignedBB2.minZ - ent.getZ() + z - 0.05, axisAlignedBB2.maxX - ent.getX() + x + 0.05, axisAlignedBB2.maxY - ent.getY() + y + 0.15, axisAlignedBB2.maxZ - ent.getZ() + z + 0.05);
        return axisAlignedBB;
    }
    
    public enum Modes {
        Circle,
        Rect,
        None
    }
}
