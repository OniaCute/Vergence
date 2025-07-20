package cc.vergence.ui.gui.impl.impl.choice;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.animations.GuiAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.other.EnumUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

public class EnumChoicesComponent extends GuiComponent {
    private EnumOption option;
    private boolean isSpread;
    public boolean isActuallySpread = false;
    public final GuiAnimation animation = new GuiAnimation(ClickGUI.INSTANCE.optionsSpreadAnimationTime.getValue().intValue());
    public float animationProgress = 1f;

    public EnumChoicesComponent(EnumOption option) {
        this.option = option;
    }

    public void spread() {
        this.isSpread = true;
    }

    public boolean isSpread() {
        return isSpread;
    }

    public void fold() {
        animation.reverse();
        this.isSpread = false;
    }

    public void setOption(EnumOption option) {
        this.option = option;
    }

    public EnumOption getOption() {
        return option;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        animation.setDuration(ClickGUI.INSTANCE.optionsSpreadAnimationTime.getValue().intValue());
        if (isHovered(mouseX, mouseY)) {
            GuiManager.setCurrentComponent(this);
            if (Vergence.GUI.isAvailable(this) || GuiManager.hoverComponentDrawing) {
                if (clickLeft || (GuiManager.CLICKED_LEFT && GuiManager.hoveredDrawingComponent.equals(getSubComponents().get(0)))) {
                    Enum<?> current = option.getValue();
                    Enum<?> next = EnumUtil.getNextEnumValueReflect(current);
                    if (next != null) {
                        option.setValue(next);
                    }
                    GuiManager.CLICKED_LEFT = false;
                }
                else if (clickRight || (GuiManager.CLICKED_RIGHT && GuiManager.hoveredDrawingComponent.equals(getSubComponents().get(0)))) {
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
                    GuiManager.CLICKED_RIGHT = false;
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
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getChoicesHoveredBackgroundColor() : Vergence.THEME.getTheme().getChoicesBackgroundColor()
        );

        FontUtil.drawTextWithAlign(
                context,
                option.getValue().name(),
                this.getX(),
                this.getY() + 4,
                this.getX() + this.getWidth() - 4,
                this.getY() + this.getHeight(),
                Aligns.CENTER,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getChoicesHoveredTextColor() : Vergence.THEME.getTheme().getChoicesTextColor(),
                FontSize.SMALLEST
        );

        FontUtil.drawTextWithAlign(
                context,
                isActuallySpread ? "-" : "+",
                this.getX(),
                this.getY() + 4 + (isActuallySpread ? -2 : 2),
                this.getX() + this.getWidth() - 6,
                this.getY() + this.getHeight(),
                Aligns.RIGHT,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getChoicesHoveredTextColor() : Vergence.THEME.getTheme().getChoicesTextColor(),
                FontSize.SMALL
        );
        animationProgress = animation.getProgress();
        if (!isSpread && !animation.isRunning() && animationProgress < 0.001f) {
            isActuallySpread = false;
        }

        // Use "Hover Component Renderer"
//        if (isSpread()) {
//            for (GuiComponent component : this.getSubComponents()) {
//                component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
//            }
//        }
    }
}
