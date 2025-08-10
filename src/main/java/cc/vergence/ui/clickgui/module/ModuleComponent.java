package cc.vergence.ui.clickgui.module;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.animations.GuiAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import oshi.util.tuples.Pair;

import java.awt.*;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class ModuleComponent extends GuiComponent {
    private Module module;
    private boolean isSpread;
    public boolean isActuallySpread = false;
    private final GuiAnimation animation = new GuiAnimation(ClickGUI.INSTANCE.moduleSpreadAnimationTime.getValue().intValue());
    public float animationProgress = 1f;
    
    public ModuleComponent(Module module) {
        this.module = module;
        this.setDisplayName(Vergence.TEXT.get("Module.Modules." + module.getName() + ".name"));
    }
    

    public void setModule(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public void spread() {
        this.isSpread = true;
    }

    public boolean isSpread() {
        return this.isSpread;
    }

    public void fold() {
        this.isSpread = false;
    }

    public double getBaseHeight() {
        return FontUtil.getHeight(FontSize.MEDIUM)
                + FontUtil.getHeight(FontSize.SMALL)
                + 2;
    }

    public double getFullHeight() {
        double height = getBaseHeight();
        if (isActuallySpread || animation.getProgress() > 0.001f) {
            for (GuiComponent c : getSubComponents()) {
                height += c.getHeight() + 2;
            }
        }
        return height;
    }

    @Override
    public double getHeight() {
        return Math.max(super.getHeight() * animationProgress, getBaseHeight());
    }


    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        animation.setDuration(ClickGUI.INSTANCE.moduleSpreadAnimationTime.getValue().intValue());
        if (isHovered(this.x, this.y, this.width, (FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL)), mouseX, mouseY)) {
            GuiManager.setCurrentComponent(this);
            if (clickLeft) {
                this.module.toggle();
                GuiManager.CLICKED_LEFT = false;
            } else if (clickRight) {
                GuiManager.CLICKED_RIGHT = false;
                if (!animation.isRunning()) {
                    if (!this.isSpread) {
                        animation.restart();
                        this.isSpread = true;
                        this.isActuallySpread = true;
                    } else {
                        animation.reverse();
                        this.isSpread = false;
                    }
                }
            }
        } else {
            GuiManager.setCurrentComponent(null);
        }

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                4,
                isHovered(mouseX, mouseY) ? (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledBackgroundColor() : Vergence.THEME.getTheme().getModuleHoveredBackgroundColor()) : (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledBackgroundColor() : Vergence.THEME.getTheme().getModuleBackgroundColor())
        );

        FontUtil.drawText(
                context,
                this.getModule().getDisplayName(),
                this.getX() + 3,
                this.getY() + 2,
                isHovered(mouseX, mouseY) ? (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledTextColor() : Vergence.THEME.getTheme().getModuleHoveredTextColor()) : (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledTextColor() : Vergence.THEME.getTheme().getModuleTextColor()),
                FontSize.MEDIUM
        );

        if (this.module.getDescription() == null) {
            Vergence.CONSOLE.logError("Module " + this.module.getName() + " has null description!");
            return;
        }

        FontUtil.drawText(
                context,
                this.module.getDescription(),
                this.getX() + 3,
                this.getY() + FontUtil.getHeight(FontSize.MEDIUM) + 3,
                isHovered(mouseX, mouseY) ? (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledTextColor() : Vergence.THEME.getTheme().getModuleHoveredTextColor()) : (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledTextColor() : Vergence.THEME.getTheme().getModuleTextColor()),
                FontSize.SMALLEST
        );

        Pair<Double, Double> GearComponentPosition = Render2DUtil.drawRoundedRectWithAlign(
                context.getMatrices(),
                this.x,
                this.y,
                this.x + this.width - ((FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL)) / 3),
                this.y + FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL) + 2 + (this.isActuallySpread ? 2 : 0),
                (FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL)) / 2,
                (FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL)) / 2,
                1,
                new Color(0, 0, 0, 0),
                Aligns.RIGHT
        );

        FontUtil.drawTextWithAlign(
                context,
                isActuallySpread ? "-" : "+",
                GearComponentPosition.getA(),
                GearComponentPosition.getB(),
                GearComponentPosition.getA() + (FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL)) / 2,
                GearComponentPosition.getB() + (FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL)) / 2,
                Aligns.CENTER,
                isHovered(mouseX, mouseY) ? (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledGearTextColor() : Vergence.THEME.getTheme().getModuleHoveredGearTextColor()) : (this.module.getStatus() ? Vergence.THEME.getTheme().getModuleEnabledGearTextColor() : Vergence.THEME.getTheme().getModuleGearTextColor()),
                FontSize.LARGEST
        );

        animationProgress = animation.getProgress();

        if (isSpread() || (animationProgress > 0.001f && animationProgress != 1f)) {
            MatrixStack matrices = context.getMatrices();
            matrices.push();

            Render2DUtil.pushDisplayArea(
                    context.getMatrices(),
                    (float) getX(),
                    (float) getY(),
                    (float) (getX() + getWidth()),
                    (float) (getY() + getHeight()),
                    1f
            );

            double baseY = getY() + FontUtil.getHeight(FontSize.MEDIUM)
                    + FontUtil.getHeight(FontSize.SMALL)
                    + 2;

            matrices.translate(getX(), baseY, 0);
            matrices.scale(1f, animationProgress, 1f);
            matrices.translate(-getX(), -(baseY), 0);

            for (GuiComponent component : getSubComponents()) {
                component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
            }

            matrices.pop();
            Render2DUtil.popDisplayArea();
        }
        if (!isSpread && !animation.isRunning() && animationProgress < 0.001f) {
            isActuallySpread = false;
        }
    }
}
