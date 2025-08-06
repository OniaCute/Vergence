package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.maths.Easing;
import cc.vergence.util.maths.MathUtil;
import cc.vergence.util.other.WorldUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.awt.*;
import java.util.EnumSet;

public class BlockHighlight extends Module {
    public static BlockHighlight INSTANCE;
    private BlockPos prevPosition = null;
    private Vec3d renderPosition = null;
    private long animationStart = 0;

    public BlockHighlight() {
        super("BlockHighlight", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<EnumSet<Modes>> mode = addOption(new MultipleOption<Modes>("Mode", EnumSet.of(Modes.Outline)));
    public Option<Color> fillColor = addOption(new ColorOption("FillColor", new Color(255, 255, 255, 89), v -> mode.getValue().contains(Modes.Fill)));
    public Option<Color> outlineColor = addOption(new ColorOption("Outline", new Color(255, 255, 255, 242), v -> mode.getValue().contains(Modes.Fill)));
    public Option<Enum<?>> animationMode = addOption(new EnumOption("AnimationMode", AnimationModes.Static));
    public Option<Double> animationSmooth = addOption(new DoubleOption("AnimationSmooth", 0, 20, 1));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (!(mc.crosshairTarget instanceof BlockHitResult hitResult)) {
            return;
        }

        BlockPos position = hitResult.getBlockPos();

        if (animationMode.getValue().equals("Slide") && position != null) {
            if (renderPosition == null) renderPosition = MathUtil.getVec(position);

            if (!WorldUtil.equals(position, prevPosition)) {
                animationStart = System.currentTimeMillis();
                prevPosition = position;
            }
        }
        Vec3d offset = MathUtil.getVec(position);
        if (animationMode.getValue().equals(AnimationModes.Slide) && renderPosition != null) {
            float easing = Easing.ease(Easing.toDelta(animationStart, (int) (Math.pow(animationSmooth.getValue(), 1.4d) * 1000)), Easing.Method.EASE_OUT_QUART);
            renderPosition = renderPosition.add(MathUtil.getVec(position).subtract(renderPosition).multiply(easing));
            offset = renderPosition;
        }
        BlockState state = mc.world.getBlockState(position);
        if (state.isAir() || !mc.world.getWorldBorder().contains(position)) {
            return;
        }
        VoxelShape shape = state.getOutlineShape(mc.world, position);
        if (shape.isEmpty()) {
            return;
        }
        Render3DUtil.draw3DBox(matrixStack, shape.getBoundingBox().offset(offset), fillColor.getValue(), mode.getValue().contains(Modes.Fill), outlineColor.getValue(), mode.getValue().contains(Modes.Outline));
    }

    public enum Modes {
        Outline,
        Fill
    }

    public enum AnimationModes {
        Static,
        Slide
    }
}
