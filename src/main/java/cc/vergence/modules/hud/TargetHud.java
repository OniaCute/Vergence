package cc.vergence.modules.hud;

import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.event.events.AttackEvent;
import cc.vergence.features.event.events.HurtEvent;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.lwjgl.opengl.GL40C;

import java.awt.*;

public class TargetHud extends Module {
    public static TargetHud INSTANCE;
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;
    private FastTimerUtil timer = new FastTimerUtil();
    private LivingEntity target;

    public TargetHud() {
        super("TargetHud", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
    }

    public Option<Double> lifeTime = addOption(new DoubleOption("LifeTime", 100, 3000, 1200).setUnit("ms"));
    public Option<Boolean> doNick = addOption(new BooleanOption("NickPlayer", false));
    public Option<String> nickName = addOption(new TextOption("NickName", "Minecraft Player", v -> doNick.getValue()));
    public Option<Color> playerNameColor = addOption(new ColorOption("PlayerNameColor", new Color(0, 0, 0, 255)));
    public Option<Color> playerHealthColor = addOption(new ColorOption("PlayerHealthColor", new Color(255, 143, 241, 255)));
    public Option<Double> headRadius = addOption(new DoubleOption("HeadRadius", 0, 6, 2));
    public Option<Double> healthRadius = addOption(new DoubleOption("HealthRadius", 0, 4, 2));
    public Option<Boolean> items = addOption(new BooleanOption("Items", true));
    public Option<Boolean> background = addOption(new BooleanOption("Background"));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(255, 255, 255, 247)));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true, v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 4, v -> background.getValue() && rounded.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (screen instanceof HudEditorScreen && button.equals(MouseButtons.LEFT)) {
            this.lastMouseStatus = false;
            HudManager.currentHud = null;
        }
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onTick() {
        if (timer.passedMs(lifeTime.getValue())) {
            target = null;
        }
    }

    @Override
    public void onAttack(AttackEvent event, LivingEntity entity) {
        if (target == null || target.isDead()) {
            return ;
        }
        target = entity;
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {

    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        if (isNull()) {
            return;
        }
        if (target == null && !(mc.currentScreen instanceof HudEditorScreen)) {
            return ;
        }
        if (target != null && !(target instanceof PlayerEntity)) {
            return ;
        }

        setWidth(
                5 + // left paddin
                30 + // player head image,
                5 + // middle padding
                Math.max(
                        FontUtil.getWidth(FontSize.MEDIUM, doNick.getValue() ? nickName.getValue() : (target != null ? target.getName().getString() : mc.player.getName().getString())), // playerName
                        items.getValue() ? ((16 + 3) * 6 - 3) : -1 // items
                ) +
                5 // right padding
        );
        setHeight(30 + 3 * 2);

        if (background.getValue()) {
            if (rounded.getValue()) {
                Render2DUtil.drawRoundedRect(
                        context.getMatrices(),
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        radius.getValue(),
                        backgroundColor.getValue()
                );
            } else {
                Render2DUtil.drawRect(
                        context,
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        backgroundColor.getValue()
                );
            }
        }

        float hurtPercent = ((target != null ? target : mc.player).hurtTime) / 6f;
        RenderSystem.setShaderTexture(0, ((AbstractClientPlayerEntity) (target != null ? target : mc.player)).getSkinTextures().texture());
        context.getMatrices().push();
        context.getMatrices().translate(getX() + 5, getY() + 3, 0);
        context.getMatrices().scale(1 - hurtPercent / 20f, 1 - hurtPercent / 20f, 1f);
        context.getMatrices().translate(-(getX() + 5), -(getY() + 3), 0);
        RenderSystem.enableBlend();
        RenderSystem.colorMask(false, false, false, true);
        RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
        RenderSystem.clear(GL40C.GL_COLOR_BUFFER_BIT);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Render2DUtil.renderRoundedQuadInternal(context.getMatrices().peek().getPositionMatrix(), 1, 1, 1, 1, getX() + 3, getY() + 3, getX() + 3 + 30, getY() + 3 + 30, 5, 10);
        RenderSystem.blendFunc(GL40C.GL_DST_ALPHA, GL40C.GL_ONE_MINUS_DST_ALPHA);
        RenderSystem.setShaderColor(1, 1 - hurtPercent / 2, 1 - hurtPercent / 2, 1);
        Render2DUtil.renderTexture(context.getMatrices(), getX() + 3, getY() + 3, 30, 30, 8, 8, 8, 8, 64, 64);
        Render2DUtil.renderTexture(context.getMatrices(), getX() + 3, getY() + 3, 30, 30, 40, 8, 8, 8, 64, 64);
        RenderSystem.defaultBlendFunc();
        context.getMatrices().pop();

        // Draw player name
        String playerName = doNick.getValue() ? nickName.getValue() : (target != null ? target : mc.player).getName().getString();
        FontUtil.drawText(context, playerName, getX() + 35, getY() + 5, playerNameColor.getValue(), FontSize.SMALL);

        // Draw health bar
        int health = (int) (target != null ? target : mc.player).getHealth();
        int maxHealth = (int) (target != null ? target : mc.player).getMaxHealth();
        int healthWidth = (int) ((health / (float) maxHealth) * (getWidth() - 5 * 3 - 30));
        Render2DUtil.drawRoundedRect(context.getMatrices(), getX() + 35, getY() + FontUtil.getHeight(FontSize.SMALL) + 2, healthWidth, 4, healthRadius.getValue(), playerHealthColor.getValue());

        // Draw items
        if (items.getValue()) {
            DefaultedList<ItemStack> inventory = ((PlayerEntity) (target != null ? target : mc.player)).getInventory().armor;
            for (int i = 0; i < 6; i++) {
                ItemStack itemStack = null;
                if (i == 0) {
                    itemStack = ((PlayerEntity) (target != null ? target : mc.player)).getMainHandStack();
                }
                else if (i == 1) {
                    itemStack = ((PlayerEntity) (target != null ? target : mc.player)).getOffHandStack();
                } else {
                    itemStack = inventory.get(3 - (i - 2));
                }
                if (!itemStack.isEmpty()) {
                    context.drawItem(itemStack, (int) (getX() + 35 + i * 19), (int) (getY() + FontUtil.getHeight(FontSize.SMALL) + 8));
                }
            }
        }

        if (HudManager.CLICKED_LEFT) {
            if (HudManager.MOUSE_X > getX() && HudManager.MOUSE_X < getX() + getWidth() &&
                    HudManager.MOUSE_Y > getY() && HudManager.MOUSE_Y < getY() + getHeight() && HudManager.currentHud == null || HudManager.currentHud == this) {
                HudManager.currentHud = this;
                if (!lastMouseStatus) {
                    lastMouseStatus = true;
                } else {
                    setX(getX() + HudManager.MOUSE_X - lastMouseX);
                    setY(getY() + HudManager.MOUSE_Y - lastMouseY);
                }
                lastMouseX = HudManager.MOUSE_X;
                lastMouseY = HudManager.MOUSE_Y;
            }
        }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
        timer.reset();
    }
}
