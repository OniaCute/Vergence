package cc.vergence.ui;

import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.screens.ClickGuiScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public abstract class GuiComponent {
    protected int id;
    protected String name;
    protected String displayName;
    protected double x = -100;
    protected double y = - 100;
    protected double width = 0;
    protected double height = 0;
    protected GuiComponent parentComponent;
    protected ArrayList<GuiComponent> subComponents = new ArrayList<GuiComponent>();
    protected boolean pinned = false;

    public boolean isHovered(double mouseX, double mouseY) {
        return x < mouseX && x + width > mouseX && y < mouseY && y + height > mouseY;
    }

    public boolean isHovered(double x, double y, double width, double height, double mouseX, double mouseY) {
        return x < mouseX && x + width > mouseX && y < mouseY && y + height > mouseY;
    }

    public static boolean isHovering(double x, double y, double width, double height, double mouseX, double mouseY) {
        return (
                mouseX >= x &&
                        mouseX <= x + width &&
                        mouseY >= y &&
                        mouseY <= y + height
        );
    }

    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
    }

    public abstract void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight);

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void addSub(GuiComponent component) {
        this.subComponents.add(component);
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public ArrayList<GuiComponent> getSubComponents() {
        return subComponents;
    }

    public void setParentComponent(GuiComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public GuiComponent getParentComponent() {
        return parentComponent;
    }

    public void resetParentComponent() {
        this.parentComponent = null;
    }
}
