package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.modules.Module;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.player.InventoryUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Trajectories extends Module {
    public static Trajectories INSTANCE;
    private List<Entity> hitEntities = new ArrayList<>();

    public Trajectories() {
        super("Trajectories", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Color> lineColor = addOption(new ColorOption("LineColor", new Color(255, 179, 251, 200)));
    public Option<Color> entityFillColor = addOption(new ColorOption("EntityFillColor", new Color(255, 255, 255, 200)));
    public Option<Color> entityOutlineColor = addOption(new ColorOption("EntityOutlineColor", new Color(255, 255, 255, 255)));
    public Option<Color> blockFillColor = addOption(new ColorOption("BlockFillColor", new Color(255, 255, 255, 200)));
    public Option<Color> blockOutlineColor = addOption(new ColorOption("BlockOutlineColor", new Color(255, 255, 255, 255)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (isNull() || mc.options.hudHidden) {
            return;
        }

        Hand activeHand;

        if (mc.player.getMainHandStack().getItem() instanceof BowItem || mc.player.getMainHandStack().getItem() instanceof CrossbowItem || InventoryUtil.isThrowable(mc.player.getMainHandStack().getItem())) {
            activeHand = Hand.MAIN_HAND;
        }
        else if (mc.player.getOffHandStack().getItem() instanceof BowItem || mc.player.getOffHandStack().getItem() instanceof CrossbowItem || InventoryUtil.isThrowable(mc.player.getOffHandStack().getItem())) {
            activeHand = Hand.OFF_HAND;
        } else {
            return;
        }

        boolean prevBobView = mc.options.getBobView().getValue();
        mc.options.getBobView().setValue(false);
        hitEntities.clear();

        if ((mc.player.getOffHandStack().getItem() instanceof CrossbowItem && EnchantmentHelper.getLevel(mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.MULTISHOT), mc.player.getOffHandStack()) != 0) || (mc.player.getMainHandStack().getItem() instanceof CrossbowItem && EnchantmentHelper.getLevel(mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.MULTISHOT), mc.player.getMainHandStack()) != 0)) {
            project(matrixStack, activeHand == Hand.OFF_HAND ? mc.player.getOffHandStack().getItem() : mc.player.getMainHandStack().getItem(), mc.player.getYaw() - 10, tickDelta);
            project(matrixStack, activeHand == Hand.OFF_HAND ? mc.player.getOffHandStack().getItem() : mc.player.getMainHandStack().getItem(), mc.player.getYaw(), tickDelta);
            project(matrixStack, activeHand == Hand.OFF_HAND ? mc.player.getOffHandStack().getItem() : mc.player.getMainHandStack().getItem(), mc.player.getYaw() + 10, tickDelta);
        } else {
            project(matrixStack, activeHand == Hand.OFF_HAND ? mc.player.getOffHandStack().getItem() : mc.player.getMainHandStack().getItem(), mc.player.getYaw(), tickDelta);
        }

        mc.options.getBobView().setValue(prevBobView);
    }

    private void project(MatrixStack matrices, Item item, float yaw, float tickDelta) {
        double x = MathHelper.lerp(tickDelta, mc.player.prevX, mc.player.getX());
        double y = MathHelper.lerp(tickDelta, mc.player.prevY, mc.player.getY());
        double z = MathHelper.lerp(tickDelta, mc.player.prevZ, mc.player.getZ());

        y = y + mc.player.getEyeHeight(mc.player.getPose()) - 0.1000000014901161;

        if (item == mc.player.getMainHandStack().getItem()) {
            x = x - MathHelper.cos(yaw / 180.0f * 3.1415927f) * 0.16f;
            z = z - MathHelper.sin(yaw / 180.0f * 3.1415927f) * 0.16f;
        } else {
            x = x + MathHelper.cos(yaw / 180.0f * 3.1415927f) * 0.16f;
            z = z + MathHelper.sin(yaw / 180.0f * 3.1415927f) * 0.16f;
        }

        float maxDistance = item instanceof BowItem ? 1.0f : 0.4f;

        double motionX = -MathHelper.sin(yaw / 180.0f * 3.1415927f) * MathHelper.cos(mc.player.getPitch() / 180.0f * 3.1415927f) * maxDistance;
        double motionY = -MathHelper.sin((mc.player.getPitch() - getThrowPitch(item)) / 180.0f * 3.141593f) * maxDistance;
        double motionZ = MathHelper.cos(yaw / 180.0f * 3.1415927f) * MathHelper.cos(mc.player.getPitch() / 180.0f * 3.1415927f) * maxDistance;

        float power = mc.player.getItemUseTime() / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f || power == 0) power = 1.0f;

        float distance = MathHelper.sqrt((float) (motionX * motionX + motionY * motionY + motionZ * motionZ));
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;

        float pow = (item instanceof BowItem ? (power * 2.0f) : item instanceof CrossbowItem ? (2.2f) : 1.0f) * getThrowVelocity(item);
        motionX *= pow;
        motionY *= pow;
        motionZ *= pow;

        if (!mc.player.isOnGround()) motionY += mc.player.getVelocity().getY();

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        Vec3d lastPosition;
        boolean landed = false;
        HitResult result = null;
        Entity entity = null;

        while(!landed && y > -65) {
            lastPosition = new Vec3d(x, y, z);
            x += motionX;
            y += motionY;
            z += motionZ;

            if (mc.world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() == Blocks.WATER) {
                motionX *= 0.8;
                motionY *= 0.8;
                motionZ *= 0.8;
            } else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }

            if (item instanceof BowItem) {
                motionY -= 0.05000000074505806;
            }
            else if (mc.player.getMainHandStack().getItem() instanceof CrossbowItem) {
                motionY -= 0.05000000074505806;
            } else {
                motionY -= 0.03f;
            }

            Vec3d position = new Vec3d(x, y, z);

            Entity hitEntity = getHitEntity(new Box(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));
            HitResult possibleResult = mc.world.raycast(new RaycastContext(lastPosition, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));

            if (hitEntity != null) {
                entity = hitEntity;
                landed = true;
            } else {
                if(possibleResult != null && possibleResult.getType() != HitResult.Type.MISS) {
                    result = possibleResult;
                    landed = true;
                }
            }

            Render3DUtil.DEBUG_LINES.add(new Render3DUtil.VertexCollection(new Render3DUtil.Vertex(matrix4f, (float) (lastPosition.getX() - mc.gameRenderer.getCamera().getPos().getX()), (float) (lastPosition.getY() - mc.gameRenderer.getCamera().getPos().getY()), (float) (lastPosition.getZ() - mc.gameRenderer.getCamera().getPos().getZ()), lineColor.getValue()), new Render3DUtil.Vertex(matrix4f, (float) (position.getX() - mc.gameRenderer.getCamera().getPos().getX()), (float) (position.getY() - mc.gameRenderer.getCamera().getPos().getY()), (float) (position.getZ() - mc.gameRenderer.getCamera().getPos().getZ()), lineColor.getValue())));
        }

        if (result != null && result.getType() == HitResult.Type.BLOCK) {
            Box box = new Box(result.getPos().getX() - 0.15, result.getPos().getY() - 0.15, result.getPos().getZ() - 0.15, result.getPos().getX() + 0.15, result.getPos().getY() + 0.15, result.getPos().getZ() + 0.15);
            Render3DUtil.draw3DBox(matrices, box, blockFillColor.getValue(), true, blockOutlineColor.getValue(), true);
        }

        if (entity != null && !hitEntities.contains(entity)) {
            Render3DUtil.draw3DBox(matrices, entity.getBoundingBox(), entityFillColor.getValue(), true, entityOutlineColor.getValue(), true);
            hitEntities.add(entity);
        }
    }

    private Entity getHitEntity(Box box) {
        for(Entity entity : mc.world.getEntities()) {
            if(entity == mc.player || entity instanceof ArrowEntity) continue;
            if (entity.getBoundingBox().intersects(box)) return entity;
        }
        return null;
    }

    private float getThrowVelocity(Item item) {
        if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem) return 0.5f;
        if (item instanceof ExperienceBottleItem) return 0.59f;
        if (item instanceof TridentItem) return 2f;
        return 1.5f;
    }

    private int getThrowPitch(Item item) {
        if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem || item instanceof ExperienceBottleItem)
            return 20;
        return 0;
    }
}
