package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.render.utils.Render2DUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class HitParticles extends Module {
    public static HitParticles INSTANCE;
    private final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();

    public HitParticles() {
        super("HitParticles", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Hearts));
    public Option<Enum<?>> physics = addOption(new EnumOption("Physics", Physics.Fall));
    public Option<Boolean> selfOnly = addOption(new BooleanOption("SelfOnly", false));
    public Option<Color> color = addOption(new ColorOption("Color", new Color(242, 146, 255, 195)));
    public Option<Double> amount = addOption(new DoubleOption("Amount", 0, 5, 1));
    public Option<Double> lifeTime = addOption(new DoubleOption("LifeTime", 1, 5, 2));
    public Option<Double> speed = addOption(new DoubleOption("Speed", 1, 20, 2));
    public Option<Double> scale = addOption(new DoubleOption("Scale", 1, 10, 3, v -> mode.getValue().equals(Modes.Orbiz)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        particles.removeIf(Particle::update);

        if (isNull()) {
            return ;
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (selfOnly.getValue() && player != mc.player)
                continue;
            if (player.hurtTime > 0) {
                for (int i = 0; i < amount.getValue(); i++) {
                    particles.add(new Particle((float) player.getX(), RANDOM.nextFloat((float) (player.getY() + player.getHeight()), (float) player.getY()), (float) player.getZ(), color.getValue(), RANDOM.nextInt(0, 180), RANDOM.nextFloat(10f, 60f), 0));
                }
            }
        }
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        RenderSystem.disableDepthTest();
        if (!isNull()) {
            for (Particle particle : particles) {
                particle.render(matrixStack);
            }
        }
        RenderSystem.enableDepthTest();
    }

    public class Particle {
        float x;
        float y;
        float z;

        float px;
        float py;
        float pz;

        float motionX;
        float motionY;
        float motionZ;

        float rotationAngle;
        float rotationSpeed;
        float health;

        long time;
        Color color;

        public Particle(float x, float y, float z, Color color, float rotationAngle, float rotationSpeed, float health) {
            this.x = x;
            this.y = y;
            this.z = z;
            px = x;
            py = y;
            pz = z;
            motionX = RANDOM.nextFloat(-(float) speed.getValue().floatValue() / 50f, (float) speed.getValue().floatValue() / 50f);
            motionY = RANDOM.nextFloat(-(float) speed.getValue().floatValue() / 50f, (float) speed.getValue().floatValue() / 50f);
            motionZ = RANDOM.nextFloat(-(float) speed.getValue().floatValue() / 50f, (float) speed.getValue().floatValue() / 50f);
            time = System.currentTimeMillis();
            this.color = color;
            this.rotationAngle = rotationAngle;
            this.rotationSpeed = rotationSpeed;
            this.health = health;
        }

        public long getTime() {
            return time;
        }

        public boolean update() {
            double sp = Math.sqrt(motionX * motionX + motionZ * motionZ);
            px = x;
            py = y;
            pz = z;

            x += motionX;
            y += motionY;
            z += motionZ;

            if (posBlock(x, y - scale.getValue() / 10f, z)) {
                motionY = -motionY / 1.1f;
                motionX = motionX / 1.1f;
                motionZ = motionZ / 1.1f;
            } else {
                if (posBlock(x - sp, y, z - sp)
                        || posBlock(x + sp, y, z + sp)
                        || posBlock(x + sp, y, z - sp)
                        || posBlock(x - sp, y, z + sp)
                        || posBlock(x + sp, y, z)
                        || posBlock(x - sp, y, z)
                        || posBlock(x, y, z + sp)
                        || posBlock(x, y, z - sp)
                ) {
                    motionX = -motionX;
                    motionZ = -motionZ;
                }
            }

            if (physics.getValue() == Physics.Fall)
                motionY -= 0.035f;

            motionX /= 1.005f;
            motionZ /= 1.005f;
            motionY /= 1.005f;

            return System.currentTimeMillis() - getTime() > lifeTime.getValue() * 1000;
        }

        public void render(MatrixStack matrixStack) {
            float size = scale.getValue().floatValue();

            final double posX = Render2DUtil.interpolate(px, x, mc.getRenderTickCounter().getTickDelta(true)) - mc.getEntityRenderDispatcher().camera.getPos().getX();
            final double posY = Render2DUtil.interpolate(py, y, mc.getRenderTickCounter().getTickDelta(true)) + 0.1 - mc.getEntityRenderDispatcher().camera.getPos().getY();
            final double posZ = Render2DUtil.interpolate(pz, z, mc.getRenderTickCounter().getTickDelta(true)) - mc.getEntityRenderDispatcher().camera.getPos().getZ();

            matrixStack.push();
            matrixStack.translate(posX, posY, posZ);

            matrixStack.scale(0.07f, 0.07f, 0.07f);
            matrixStack.translate(size / 2, size / 2, size / 2);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mc.gameRenderer.getCamera().getYaw()));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationAngle += (float) (0.016f * rotationSpeed)));
            matrixStack.translate(-size / 2, -size / 2, -size / 2);

            switch (((Modes) mode.getValue())) {
                case Orbiz -> {
                    Render2DUtil.drawOrbiz(matrixStack, 0.0f, 0.3, color);
                    Render2DUtil.drawOrbiz(matrixStack, -0.1f, 0.5, color);
                    Render2DUtil.drawOrbiz(matrixStack, -0.2f, 0.7, color);
                }
                case Stars -> {
                    Render2DUtil.drawStar(matrixStack, color, size);
                }
                case Hearts -> {
                    Render2DUtil.drawHeart(matrixStack, color, size);
                }
                case Bloom -> {
                    Render2DUtil.drawBloom(matrixStack, color, size);
                }
            }

            matrixStack.scale(0.8f, 0.8f, 0.8f);
            matrixStack.pop();
        }

        private boolean posBlock(double x, double y, double z) {
            Block b = mc.world.getBlockState(BlockPos.ofFloored(x, y, z)).getBlock();
            return (!(b instanceof AirBlock) && b != Blocks.WATER && b != Blocks.LAVA);
        }
    }

    public enum Physics {
        Fall,
        Fly
    }

    private enum Modes {
        Orbiz,
        Stars,
        Hearts,
        Bloom
    }
}
