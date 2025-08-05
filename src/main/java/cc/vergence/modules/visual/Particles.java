package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.other.TextureStorage;
import cc.vergence.util.render.utils.Render2DUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Particles extends Module {
    public static Particles INSTANCE;
    private final ArrayList<ParticleBase> fireFlies = new ArrayList<>();
    private final ArrayList<ParticleBase> particles = new ArrayList<>();

    public Particles() {
        super("Particles", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Off));
    public Option<Enum<?>> physics = addOption(new EnumOption("Physics", Physics.Drop));
    public Option<Double> count = addOption(new DoubleOption("Count", 20, 800, 100));
    public Option<Double> size = addOption(new DoubleOption("Size", 0.1, 3, 1));
    public Option<Color> color = addOption(new ColorOption("Color", new Color(243, 142, 255)));
    public Option<Boolean> forFireFiles = addOption(new BooleanOption("FireFiles", false));
    public Option<Double> fireFilesCount = addOption(new DoubleOption("FireFilesCount", 20, 200, 30, v -> forFireFiles.getValue()));
    public Option<Double> fireFilesSize = addOption(new DoubleOption("FireFilesSize", 0.1, 3, 1, v -> forFireFiles.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        fireFlies.removeIf(ParticleBase::tick);
        particles.removeIf(ParticleBase::tick);

        for (int i = fireFlies.size(); i < fireFilesCount.getValue(); i++) {
            if (forFireFiles.getValue()) {
                fireFlies.add(new FireFly(
                        (float) (mc.player.getX() + RANDOM.nextFloat(-25f, 25f)),
                        (float) (mc.player.getY() + RANDOM.nextFloat(2f, 15f)),
                        (float) (mc.player.getZ() + RANDOM.nextFloat(-25f, 25f)),
                        RANDOM.nextFloat(-0.2f, 0.2f),
                        RANDOM.nextFloat(-0.1f, 0.1f),
                        RANDOM.nextFloat(-0.2f, 0.2f)));
            }
        }

        for (int j = particles.size(); j < count.getValue(); j++) {
            boolean drop = physics.getValue() == Physics.Drop;
            if (!mode.getValue().equals(Modes.Off)) {
                particles.add(new ParticleBase(
                        (float) (mc.player.getX() + RANDOM.nextFloat(-48f, 48f)),
                        (float) (mc.player.getY() + RANDOM.nextFloat(2, 48f)),
                        (float) (mc.player.getZ() + RANDOM.nextFloat(-48f, 48f)),
                        drop ? 0 : RANDOM.nextFloat(-0.4f, 0.4f),
                        drop ? RANDOM.nextFloat(-0.2f, -0.05f) : RANDOM.nextFloat(-0.1f, 0.1f),
                        drop ? 0 : RANDOM.nextFloat(-0.4f, 0.4f)));
            }
        }
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (forFireFiles.getValue()) {
            matrixStack.push();
            RenderSystem.setShaderTexture(0, TextureStorage.firefly);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            fireFlies.forEach(p -> p.render(bufferBuilder));
            Render2DUtil.endBuilding(bufferBuilder);
            RenderSystem.depthMask(true);
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
            matrixStack.pop();
        }

        if (!mode.getValue().equals(Modes.Off)) {
            matrixStack.push();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            particles.forEach(p -> p.render(bufferBuilder));
            Render2DUtil.endBuilding(bufferBuilder);
            RenderSystem.depthMask(true);
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
            matrixStack.pop();
        }
    }

    public class FireFly extends ParticleBase {
        private final List<Trail> trails = new ArrayList<>();

        public FireFly(float posX, float posY, float posZ, float motionX, float motionY, float motionZ) {
            super(posX, posY, posZ, motionX, motionY, motionZ);
        }

        @Override
        public boolean tick() {

            if (mc.player.squaredDistanceTo(posX, posY, posZ) > 100) {
                age -= 4;
            }
            else if (!mc.world.getBlockState(new BlockPos((int) posX, (int) posY, (int) posZ)).isAir()) {
                age -= 8;
            } else {
                age--;
            }
            if (age < 0) {
                return true;
            }

            trails.removeIf(Trail::update);

            prevposX = posX;
            prevposY = posY;
            prevposZ = posZ;

            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            trails.add(new Trail(new Vec3d(prevposX, prevposY, prevposZ), new Vec3d(posX, posY, posZ), color.getValue()));

            motionX *= 0.99f;
            motionY *= 0.99f;
            motionZ *= 0.99f;

            return false;
        }

        @Override
        public void render(BufferBuilder bufferBuilder) {
            RenderSystem.setShaderTexture(0, TextureStorage.firefly);
            if (!trails.isEmpty()) {
                Camera camera = mc.gameRenderer.getCamera();
                for (Trail ctx : trails) {
                    Vec3d pos = ctx.interpolate(1f);
                    MatrixStack matrices = new MatrixStack();
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
                    matrices.translate(pos.x, pos.y, pos.z);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                    Matrix4f matrix = matrices.peek().getPositionMatrix();

                    bufferBuilder.vertex(matrix, 0, -fireFilesSize.getValue().floatValue(), 0).texture(0f, 1f).color(ColorUtil.setAlpha(ctx.color(), (int) (255 * ((float) age / (float) maxAge) * ctx.animation(mc.getRenderTickCounter().getTickDelta(true)))).getRGB());
                    bufferBuilder.vertex(matrix, -fireFilesSize.getValue().floatValue(), -fireFilesSize.getValue().floatValue(), 0).texture(1f, 1f).color(ColorUtil.setAlpha(ctx.color(), (int) (255 * ((float) age / (float) maxAge) * ctx.animation(mc.getRenderTickCounter().getTickDelta(true)))).getRGB());
                    bufferBuilder.vertex(matrix, -fireFilesSize.getValue().floatValue(), 0, 0).texture(1f, 0).color(ColorUtil.setAlpha(ctx.color(), (int) (255 * ((float) age / (float) maxAge) * ctx.animation(mc.getRenderTickCounter().getTickDelta(true)))).getRGB());
                    bufferBuilder.vertex(matrix, 0, 0, 0).texture(0, 0).color(ColorUtil.setAlpha(ctx.color(), (int) (255 * ((float) age / (float) maxAge) * ctx.animation(mc.getRenderTickCounter().getTickDelta(true)))).getRGB());
                }
            }
        }
    }

    public static class Trail {
        private final Vec3d from;
        private final Vec3d to;
        private final Color color;
        private int ticks, prevTicks;

        public Trail(Vec3d from, Vec3d to, Color color) {
            this.from = from;
            this.to = to;
            this.ticks = 10;
            this.color = color;
        }

        public Vec3d interpolate(float pt) {
            double x = from.x + ((to.x - from.x) * pt) - mc.getEntityRenderDispatcher().camera.getPos().getX();
            double y = from.y + ((to.y - from.y) * pt) - mc.getEntityRenderDispatcher().camera.getPos().getY();
            double z = from.z + ((to.z - from.z) * pt) - mc.getEntityRenderDispatcher().camera.getPos().getZ();
            return new Vec3d(x, y, z);
        }

        public double animation(float pt) {
            return (this.prevTicks + (this.ticks - this.prevTicks) * pt) / 10.;
        }

        public boolean update() {
            this.prevTicks = this.ticks;
            return this.ticks-- <= 0;
        }

        public Color color() {
            return color;
        }
    }

    public class ParticleBase {
        protected float prevposX, prevposY, prevposZ, posX, posY, posZ, motionX, motionY, motionZ;
        protected int age, maxAge;

        public ParticleBase(float posX, float posY, float posZ, float motionX, float motionY, float motionZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            prevposX = posX;
            prevposY = posY;
            prevposZ = posZ;
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            age = (int) RANDOM.nextInt(100, 300);
            maxAge = age;
        }

        public boolean tick() {
            if (mc.player.squaredDistanceTo(posX, posY, posZ) > 4096) {
                age -= 8;
            } else {
                age--;
            }

            if (age < 0) {
                return true;
            }

            prevposX = posX;
            prevposY = posY;
            prevposZ = posZ;

            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            motionX *= 0.9f;
            if (physics.getValue() == Physics.Fly) {
                motionY *= 0.9f;
            }
            motionZ *= 0.9f;
            motionY -= 0.001f;
            return false;
        }

        public void render(BufferBuilder bufferBuilder) {
            switch (((Modes) mode.getValue())) {
                case Bloom -> RenderSystem.setShaderTexture(0, TextureStorage.firefly);
                case SnowFlake -> RenderSystem.setShaderTexture(0, TextureStorage.snowflake);
                case Dollars -> RenderSystem.setShaderTexture(0, TextureStorage.dollar);
                case Hearts -> RenderSystem.setShaderTexture(0, TextureStorage.heart);
                case Stars -> RenderSystem.setShaderTexture(0, TextureStorage.star);
            }

            Camera camera = mc.gameRenderer.getCamera();
            Vec3d pos = Render3DUtil.interpolatePos(prevposX, prevposY, prevposZ, posX, posY, posZ);
            MatrixStack matrices = new MatrixStack();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

            Matrix4f matrix = matrices.peek().getPositionMatrix();

            bufferBuilder.vertex(matrix, 0, -size.getValue().floatValue(), 0).texture(0f, 1f).color(ColorUtil.setAlpha(color.getValue(), (int) (255 * ((float) age / (float) maxAge))).getRGB());
            bufferBuilder.vertex(matrix, -size.getValue().floatValue(), -size.getValue().floatValue(), 0).texture(1f, 1f).color(ColorUtil.setAlpha(color.getValue(), (int) (255 * ((float) age / (float) maxAge))).getRGB());
            bufferBuilder.vertex(matrix, -size.getValue().floatValue(), 0, 0).texture(1f, 0).color(ColorUtil.setAlpha(color.getValue(), (int) (255 * ((float) age / (float) maxAge))).getRGB());
            bufferBuilder.vertex(matrix, 0, 0, 0).texture(0, 0).color(ColorUtil.setAlpha(color.getValue(), (int) (255 * ((float) age / (float) maxAge))).getRGB());
        }
    }

    public enum Modes {
        SnowFlake,
        Stars,
        Hearts,
        Dollars,
        Bloom,
        Off
    }

    public enum Physics {
        Drop,
        Fly
    }
}
