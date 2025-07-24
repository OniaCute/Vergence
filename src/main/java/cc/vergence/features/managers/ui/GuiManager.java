package cc.vergence.features.managers.ui;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.*;
import cc.vergence.ui.gui.impl.impl.button.BooleanButtonComponent;
import cc.vergence.ui.gui.impl.impl.choice.BindChoicesComponent;
import cc.vergence.ui.gui.impl.impl.choice.EnumChoicesComponent;
import cc.vergence.ui.gui.impl.impl.choice.MultipleChoicesComponent;
import cc.vergence.ui.gui.impl.impl.hovered.*;
import cc.vergence.ui.gui.impl.impl.input.*;
import cc.vergence.ui.gui.impl.impl.color.ColorPalette;
import cc.vergence.ui.gui.impl.impl.color.ColorPreviewer;
import cc.vergence.ui.gui.impl.impl.slider.DoubleSlider;
import cc.vergence.util.animations.ScrollAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.animations.GuiAnimation;
import cc.vergence.util.other.EnumUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import oshi.util.tuples.Pair;

import java.util.ArrayList;

public class GuiManager implements Wrapper {
    private static GuiComponent currentComponent = null;
    public static ArrayList<GuiComponent> rootComponents = new ArrayList<>();
    public static ArrayList<GuiComponent> hoveredComponents = new ArrayList<>();
    public static ArrayList<GuiComponent> inputComponents = new ArrayList<>();
    public static ArrayList<GuiComponent> positionComponents = new ArrayList<>();
    public static ArrayList<GuiComponent> allModuleComponent = new ArrayList<>();
    public static ArrayList<GuiComponent> searchModuleComponent = new ArrayList<>();
    public static Pair<Double, Double> latestCategoryComponentPosition = new Pair<>(0.00, 0.00);
    public static Pair<Double, Double> latestModuleComponentPosition = new Pair<>(0.00, 0.00);
    public static Pair<Double, Double> latestOptionComponentPosition = new Pair<>(0.00, 0.00);
    public static double mouseScrolledOffset = 0;
    public static ScrollAnimation scrollAnimation = new ScrollAnimation(0, 0);
    public static ClickGuiScreen CLICK_GUI_SCREEN = new ClickGuiScreen();
    public static Module.Category currentCategory = null;
    public static double MOUSE_X = 0;
    public static double MOUSE_Y = 0;
    public static boolean CLICKED_LEFT = false;
    public static boolean CLICKED_RIGHT = false;
    public static double MAIN_PAGE_X = 0;
    public static double MAIN_PAGE_Y = 0;
    public static double MAIN_PAGE_X_OFFSET = 0;
    public static double MAIN_PAGE_Y_OFFSET = 0;
    public static double MAIN_PAGE_WIDTH = 400;
    public static double MAIN_PAGE_HEIGHT = 280;
    public static boolean isClickGuiInited = false;
    public static boolean hoverComponentDrawing = false;
    public static GuiComponent hoveredDrawingComponent = null;
    public static GuiAnimation ENTRY_ANIMATION = new GuiAnimation(0);
    private static double lastMouseX = -1;
    private static double lastMouseY = -1;
    private static boolean isDragging;
    public static boolean isTyping = false;
    public static SearchFrameComponent SEARCH = new SearchFrameComponent();


    public GuiManager() {
    }

    public boolean isAvailable() {
        return currentComponent == null;
    }

    public boolean isAvailable(GuiComponent component) {
        return currentComponent == component;
    }

    public static void setCurrentComponent(GuiComponent currentComponent) {
        GuiManager.currentComponent = currentComponent;
    }

    public void onRenderClickGui(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (!ClickGUI.INSTANCE.getStatus()) {
            return ;
        }
        MOUSE_X = mouseX;
        MOUSE_Y = mouseY;
        if (isClickGuiInited) {
            updateClickGui();
            drawClickGui(context, partialTicks);
        }
    }

    public void onMouseMoveInClickGuiScreen(DrawContext context, double mouseX, double mouseY) {
        MOUSE_X = mouseX;
        MOUSE_Y = mouseY;
    }

    public void onTick() {
        if (!(mc.currentScreen instanceof ClickGuiScreen) || ClickGUI.INSTANCE == null || !ClickGUI.INSTANCE.getStatus()) {
            isTyping = false;
            for (GuiComponent component : inputComponents) {
                if (component instanceof BindFrameComponent component1) {
                    component1.setListening(false);
                }
                else if (component instanceof ColorFrameComponent component1) {
                    component1.setListening(false);
                }
                else if (component instanceof DoubleFrameComponent component1) {
                    component1.setListening(false);
                }
                else if (component instanceof SearchFrameComponent component1) {
                    component1.setListening(false);
                }
                else if (component instanceof TextFrameComponent component1) {
                    component1.setListening(false);
                }
            }
        }
        boolean temp = false;
        for (GuiComponent component : inputComponents) {
            if (component instanceof BindFrameComponent component1 && component1.isListening()) {
                temp = true;
                break;
            } else if (component instanceof ColorFrameComponent component1 && component1.isListening()) {
                temp = true;
                break;
            } else if (component instanceof DoubleFrameComponent component1 && component1.isListening()) {
                temp = true;
                break;
            } else if (component instanceof SearchFrameComponent component1 && component1.isListening()) {
                temp = true;
                break;
            } else if (component instanceof TextFrameComponent component1 && component1.isListening()) {
                temp = true;
                break;
            }
        }
        GuiManager.isTyping = temp;
    }

    public void onMouseClick(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (button.equals(MouseButtons.LEFT)) {
            CLICKED_LEFT = true;
        }
        else if (button.equals(MouseButtons.RIGHT)) {
            CLICKED_RIGHT = true;
        }
        for (GuiComponent component : GuiManager.inputComponents) {
            if (component instanceof BindFrameComponent bindFrame && bindFrame.isListening()) {
                int keyCode = switch (button) {
                    case LEFT -> -100; // LEFT_MOUSE
                    case RIGHT -> -101; // RIGHT_MOUSE
                    case CENTER -> -102; // MIDDLE_MOUSE
                    case FLANK_FRONT -> -103; // FLANK_FRONT
                    case FLANK_BACK -> -104; // FLANK_BACK
                    default -> -1;
                };
                if (keyCode != -1) {
                    bindFrame.setListening(false);
                    bindFrame.getOption().setValue(keyCode);
                    return;
                }
            }
        }
    }

    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (button.equals(MouseButtons.LEFT)) {
            CLICKED_LEFT = false;
        }
        else if (button.equals(MouseButtons.RIGHT)) {
            CLICKED_RIGHT = false;
        }
        isDragging = false;
        for (GuiComponent component : positionComponents) {
            ((DoubleSlider) component).onMouseRelease(mouseX, mouseY, screen, button);
        }
    }

    /*
     * Main Page Size: 400x280
     * Absolute Padding : 3px * scale
     */
    public static void updateClickGui() {
        scrollAnimation.setDuration(ClickGUI.INSTANCE.scrollAnimationTime.getValue().intValue());
        ENTRY_ANIMATION.setDuration(ClickGUI.INSTANCE.mainPageAnimationTime.getValue().intValue());

        mouseScrolledOffset = scrollAnimation.get();

        MAIN_PAGE_X = Render2DUtil.getAlignPosition(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), MAIN_PAGE_WIDTH, MAIN_PAGE_HEIGHT, Aligns.CENTER)[0];
        MAIN_PAGE_Y = Render2DUtil.getAlignPosition(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), MAIN_PAGE_WIDTH, MAIN_PAGE_HEIGHT, Aligns.CENTER)[1];
        MAIN_PAGE_X += MAIN_PAGE_X_OFFSET;
        MAIN_PAGE_Y += MAIN_PAGE_Y_OFFSET;
        MAIN_PAGE_X *= Render2DUtil.getScaleFactor();
        MAIN_PAGE_Y *= Render2DUtil.getScaleFactor();
        MAIN_PAGE_WIDTH *= Render2DUtil.getScaleFactor();
        MAIN_PAGE_HEIGHT *= Render2DUtil.getScaleFactor();
        latestCategoryComponentPosition = new Pair<>(MAIN_PAGE_X, MAIN_PAGE_Y + 50);

//        if (SEARCH.isListening() || !SEARCH.searchText.isEmpty()) {
//            String query = SEARCH.searchText.toLowerCase();
//            latestModuleComponentPosition = new Pair<>(MAIN_PAGE_X, MAIN_PAGE_Y + 37 + (mouseScrolledOffset * 8));
//            for (GuiComponent categoryComponent : rootComponents) {
//                for (GuiComponent moduleComponent : categoryComponent.getSubComponents()) {
//                    Module module = ((ModuleComponent) moduleComponent).getModule();
//                    String name = ClickGUI.INSTANCE.searchIgnoreCase.getValue() ? module.getName().toLowerCase() : module.getName();
//                    String description = (module.getDescription() != null) ? module.getDescription().toLowerCase() : "";
//                    if (name.contains(query) || (description.contains(query) && ClickGUI.INSTANCE.searchForDescription.getValue())) {
//                        moduleComponent.setX(MAIN_PAGE_X + 111);
//                        moduleComponent.setY(latestModuleComponentPosition.getB());
//                        moduleComponent.setWidth((400 - 114));
//                        moduleComponent.setHeight(FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL) + 2);
//                        latestModuleComponentPosition = new Pair<>(moduleComponent.getX(), moduleComponent.getY() + moduleComponent.getHeight() + 2);
//                        if (((ModuleComponent) moduleComponent).isActuallySpread) {
//                            layoutModuleAreaComponent(((ModuleComponent) moduleComponent));
//                        }
//                    }
//                }
//            }
//            latestModuleComponentPosition = new Pair<>(0.00, 0.00);
//            return;
//        }

        for (GuiComponent categoryComponent : rootComponents) {
            latestModuleComponentPosition = new Pair<>(MAIN_PAGE_X, MAIN_PAGE_Y + 37 + (mouseScrolledOffset * 8));
            categoryComponent.setX(MAIN_PAGE_X + 3);
            categoryComponent.setY(latestCategoryComponentPosition.getB());
            categoryComponent.setWidth(100);
            categoryComponent.setHeight((MAIN_PAGE_HEIGHT - 50 - (4 * Module.getCategories().size())) / Module.getCategories().size());
            latestCategoryComponentPosition = new Pair<>(categoryComponent.getX() + categoryComponent.getWidth(), categoryComponent.getY() + categoryComponent.getHeight() + 4);

            if (((CategoryComponent) categoryComponent).getCategory().equals(currentCategory)) {
                for (GuiComponent moduleComponent : categoryComponent.getSubComponents()) {
                    moduleComponent.setX(MAIN_PAGE_X + 111);
                    moduleComponent.setY(latestModuleComponentPosition.getB());
                    moduleComponent.setWidth((400 - 114));
                    moduleComponent.setHeight(FontUtil.getHeight(FontSize.MEDIUM) + FontUtil.getHeight(FontSize.SMALL) + 2);
                    latestModuleComponentPosition = new Pair<>(moduleComponent.getX(), moduleComponent.getY() + moduleComponent.getHeight() + 2);

                    if (((ModuleComponent) moduleComponent).isActuallySpread) {
                        layoutModuleAreaComponent(((ModuleComponent) moduleComponent));
                        searchModuleComponent.add(moduleComponent);
                    }
                }
            }
            latestModuleComponentPosition = new Pair<>(0.00, 0.00);
        }
    }

    public static boolean shouldDisplayOptionComponent(GuiComponent component) {
        if (component instanceof BooleanComponent) {
            return ((BooleanComponent) component).getOption().isVisible();
        }
        else if (component instanceof EnumComponent) {
            return ((EnumComponent) component).getOption().isVisible();
        }
        else if (component instanceof MultipleComponent) {
            return ((MultipleComponent) component).getOption().isVisible();
        }
        else if (component instanceof TextComponent) {
            return ((TextComponent) component).getOption().isVisible();
        }
        else if (component instanceof DoubleComponent) {
            return ((DoubleComponent) component).getOption().isVisible();
        }
        else if (component instanceof ColorComponent) {
            return ((ColorComponent) component).getOption().isVisible();
        }
        else if (component instanceof BindComponent) {
            return ((BindComponent) component).getOption().isVisible();
        }
        return true;
    }

    public static void initClickGui() {
        scrollAnimation = new ScrollAnimation(0, ClickGUI.INSTANCE.scrollAnimationTime.getValue().intValue());
        ENTRY_ANIMATION = new GuiAnimation(ClickGUI.INSTANCE.mainPageAnimationTime.getValue().intValue());
        for (Module.Category category : Module.getCategories()) {
            CategoryComponent categoryComponent = new CategoryComponent(category);
            for (Module module : ModuleManager.categoryModuleHashMap.get(category)) {
                ModuleComponent moduleComponent = new ModuleComponent(module);
                ModuleAreaComponent moduleAreaComponent = new ModuleAreaComponent(moduleComponent);
                for (Option<?> option : module.getOptions()) {
                    if (option instanceof BooleanOption) {
                        BooleanComponent booleanComponent = new BooleanComponent((BooleanOption) option);
                        BooleanButtonComponent button = new BooleanButtonComponent((BooleanOption) option);

                        HoverTextComponent description = new HoverTextComponent(booleanComponent);
                        description.setParentComponent(booleanComponent);
                        booleanComponent.addSub(description);
                        button.setParentComponent(booleanComponent);
                        booleanComponent.addSub(button);
                        booleanComponent.setParentComponent(moduleAreaComponent);
                        moduleAreaComponent.addSub(booleanComponent);
                    }
                    else if (option instanceof EnumOption) {
                        EnumChoicesComponent enumChoicesComponent = new EnumChoicesComponent((EnumOption) option);
                        HoverEnumChoicesComponent hoverEnumChoicesComponent = new HoverEnumChoicesComponent(enumChoicesComponent);
                        for (Enum<?> clazz : EnumUtil.getAllEnumValuesReflect(option.getValue().getClass())) {
                            EnumChoiceComponent component = new EnumChoiceComponent(hoverEnumChoicesComponent, clazz);
                            component.setParentComponent(hoverEnumChoicesComponent);
                            hoverEnumChoicesComponent.addSub(component);
                        }
                        hoverEnumChoicesComponent.setParentComponent(enumChoicesComponent);
                        enumChoicesComponent.addSub(hoverEnumChoicesComponent);
                        EnumComponent enumComponent = new EnumComponent((EnumOption) option, enumChoicesComponent);
                        HoverTextComponent description = new HoverTextComponent(enumComponent);
                        description.setParentComponent(enumComponent);
                        enumComponent.addSub(description);
                        enumChoicesComponent.setParentComponent(enumComponent);
                        enumComponent.addSub(enumChoicesComponent);
                        enumComponent.setParentComponent(moduleAreaComponent);
                        moduleAreaComponent.addSub(enumComponent);
                        hoveredComponents.add(hoverEnumChoicesComponent);
                    }
                    else if (option instanceof MultipleOption) {
                        MultipleChoicesComponent multipleChoicesComponent = new MultipleChoicesComponent((MultipleOption<?>) option);
                        HoverMultipleChoicesComponent hoverMultipleChoicesComponent = new HoverMultipleChoicesComponent(multipleChoicesComponent);
                        Class<? extends Enum<?>> enumClass = ((MultipleOption<?>) option).getEnumClass();
                        if (enumClass != null) {
                            for (Enum<?> enumConstant : EnumUtil.getAllEnumValuesReflect(enumClass)) {
                                MultipleChoiceComponent component = new MultipleChoiceComponent(hoverMultipleChoicesComponent, enumConstant);
                                component.setParentComponent(hoverMultipleChoicesComponent);
                                hoverMultipleChoicesComponent.addSub(component);
                            }
                        }
                        hoverMultipleChoicesComponent.setParentComponent(multipleChoicesComponent);
                        multipleChoicesComponent.addSub(hoverMultipleChoicesComponent);
                        MultipleComponent multipleComponent = new MultipleComponent((MultipleOption<?>) option, multipleChoicesComponent);
                        HoverTextComponent description = new HoverTextComponent(multipleComponent);
                        description.setParentComponent(multipleComponent);
                        multipleComponent.addSub(description);
                        multipleChoicesComponent.setParentComponent(multipleComponent);
                        multipleComponent.addSub(multipleChoicesComponent);
                        multipleComponent.setParentComponent(moduleAreaComponent);
                        moduleAreaComponent.addSub(multipleComponent);
                        hoveredComponents.add(hoverMultipleChoicesComponent);
                    }
                    else if (option instanceof TextOption) {
                        TextComponent textComponent = new TextComponent((TextOption) option);
                        TextFrameComponent inputFrame = new TextFrameComponent((TextOption) option);
                        HoverTextComponent description = new HoverTextComponent(textComponent);
                        description.setParentComponent(textComponent);
                        textComponent.addSub(description);
                        inputFrame.setParentComponent(textComponent);
                        textComponent.addSub(inputFrame);
                        textComponent.setParentComponent(moduleAreaComponent);
                        moduleAreaComponent.addSub(textComponent);
                    }
                    else if(option instanceof DoubleOption) {
                        DoubleComponent doubleComponent = new DoubleComponent((DoubleOption) option);
                        DoubleFrameComponent doubleFrameComponent = new DoubleFrameComponent((DoubleOption) option);
                        DoubleSlider doubleSlider = new DoubleSlider((DoubleOption) option);
                        HoverTextComponent description = new HoverTextComponent(doubleComponent);
                        description.setParentComponent(doubleComponent);
                        doubleComponent.addSub(description);

                        doubleSlider.setParentComponent(doubleFrameComponent);
                        doubleFrameComponent.addSub(doubleSlider);
                        doubleFrameComponent.setParentComponent(doubleComponent);
                        doubleComponent.addSub(doubleFrameComponent);
                        doubleComponent.setParentComponent(moduleAreaComponent);
                        moduleAreaComponent.addSub(doubleComponent);
                        positionComponents.add(doubleSlider);
                    }
                    else if (option instanceof ColorOption) {
                        ColorComponent colorComponent = new ColorComponent((ColorOption) option);
                        ColorPalette colorPalette = new ColorPalette((ColorOption) option);
                        ColorFrameComponent colorFrameComponent = new ColorFrameComponent((ColorOption) option);
                        ColorPreviewer colorPreviewer = new ColorPreviewer(colorPalette);
                        HoverTextComponent description = new HoverTextComponent(colorComponent);
                        description.setParentComponent(colorComponent);
                        colorComponent.addSub(description);

                        colorPalette.setParentComponent(colorPreviewer);
                        colorPreviewer.addSub(colorPalette);
                        colorPreviewer.setParentComponent(colorComponent);
                        colorComponent.addSub(colorPreviewer);
                        colorFrameComponent.setParentComponent(colorComponent);
                        colorComponent.addSub(colorFrameComponent);
                        colorComponent.setParentComponent(moduleAreaComponent);
                        moduleAreaComponent.addSub(colorComponent);
                        hoveredComponents.add(colorPalette);
                    }
                    else if (option instanceof BindOption) {
                        BindComponent bindComponent = new BindComponent((BindOption) option);
                        BindFrameComponent inputFrame = new BindFrameComponent((BindOption) option);
                        HoverTextComponent description = new HoverTextComponent(bindComponent);
                        description.setParentComponent(bindComponent);
                        bindComponent.addSub(description);
                        inputFrame.setParentComponent(bindComponent);
                        bindComponent.addSub(inputFrame);
                        BindChoicesComponent bindChoicesComponent = new BindChoicesComponent((BindOption) option);
                        HoverBindChoicesComponent hoverBindChoicesComponent = new HoverBindChoicesComponent(bindChoicesComponent);
                        for (BindOption.BindType clazz : BindOption.BindType.values()) {
                            BindChoiceComponent component = new BindChoiceComponent(hoverBindChoicesComponent, clazz);
                            component.setParentComponent(hoverBindChoicesComponent);
                            hoverBindChoicesComponent.addSub(component);
                        }
                        hoverBindChoicesComponent.setParentComponent(bindChoicesComponent);
                        bindChoicesComponent.addSub(hoverBindChoicesComponent);
                        bindChoicesComponent.setParentComponent(bindComponent);
                        bindComponent.addSub(bindChoicesComponent);
                        bindComponent.setParentComponent(moduleAreaComponent);
                        moduleAreaComponent.addSub(bindComponent);
                        hoveredComponents.add(hoverBindChoicesComponent);
                    }
                }
                moduleAreaComponent.setParentComponent(moduleComponent);
                moduleComponent.addSub(moduleAreaComponent);
                moduleComponent.setParentComponent(categoryComponent);
                categoryComponent.addSub(moduleComponent);
                allModuleComponent.add(moduleComponent);
            }
            rootComponents.add(categoryComponent);
        }
    }

    public void drawClickGui(DrawContext context, float tickDelta) {
        double centerX = MAIN_PAGE_X + MAIN_PAGE_WIDTH / 2.0;
        double centerY = MAIN_PAGE_Y + MAIN_PAGE_HEIGHT / 2.0;
        float progress = (float) ENTRY_ANIMATION.getProgress();
        MatrixStack matrices = context.getMatrices();

        matrices.push();

        matrices.translate(centerX, centerY, 0);
        matrices.scale(progress, progress, 1f);
        matrices.translate(-centerX, -centerY, 0);
        Render2DUtil.pushDisplayArea(
                context.getMatrices(),
                (float) MAIN_PAGE_X,
                (float) MAIN_PAGE_Y,
                (float) (MAIN_PAGE_X + MAIN_PAGE_WIDTH),
                (float) (MAIN_PAGE_Y + MAIN_PAGE_HEIGHT),
                1d
        );

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                MAIN_PAGE_X,
                MAIN_PAGE_Y,
                MAIN_PAGE_WIDTH,
                MAIN_PAGE_HEIGHT,
                6,
                Vergence.THEME.getTheme().getMainPageBackgroundColor()
        );

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                MAIN_PAGE_X + 106,
                MAIN_PAGE_Y,
                MAIN_PAGE_WIDTH - 105,
                30 + 2,
                6,
                Vergence.THEME.getTheme().getTopBarBackgroundColor()
        );

        Render2DUtil.drawRect(
                context,
                MAIN_PAGE_X + 106,
                MAIN_PAGE_Y,
                2,
                MAIN_PAGE_HEIGHT - 0.65,
                Vergence.THEME.getTheme().getMainPageSplitLineColor()
        );

        Render2DUtil.drawRect(
                context,
                MAIN_PAGE_X + 108,
                MAIN_PAGE_Y + 30,
                MAIN_PAGE_WIDTH - 108,
                2,
                Vergence.THEME.getTheme().getMainPageSplitLineColor()
        );

        FontUtil.drawTextWithAlign(
                context,
                ClickGUI.INSTANCE.title.getValue(),
                MAIN_PAGE_X,
                MAIN_PAGE_Y,
                MAIN_PAGE_X + 106,
                MAIN_PAGE_Y + 50,
                Aligns.CENTER,
                Vergence.THEME.getTheme().getMainColor(),
                FontSize.LARGEST,
                true
        );

        hoverComponentDrawing = false;

        for (GuiComponent component : hoveredComponents) {
            if (component instanceof HoverEnumChoicesComponent) {
                if (((EnumChoicesComponent) component.getParentComponent()).isActuallySpread && ((ModuleComponent) component.getParentComponent().getParentComponent().getParentComponent().getParentComponent()).isSpread()) {
                    hoverComponentDrawing = true;
                }
            } else if (component instanceof HoverMultipleChoicesComponent) {
                if (((MultipleChoicesComponent) component.getParentComponent()).isActuallySpread && ((ModuleComponent) component.getParentComponent().getParentComponent().getParentComponent().getParentComponent()).isSpread()) {
                    hoverComponentDrawing = true;
                }
            } else if (component instanceof HoverBindChoicesComponent) {
                if (((BindChoicesComponent) component.getParentComponent()).isActuallySpread && ((ModuleComponent) component.getParentComponent().getParentComponent().getParentComponent().getParentComponent()).isSpread()) {
                    hoverComponentDrawing = true;
                }
            } else if (component instanceof ColorPalette) {
                if (((ColorPreviewer) component.getParentComponent()).isSpread) {
                    hoverComponentDrawing = true;
                }
            }
        }

        for (GuiComponent component : rootComponents) {
            component.onDraw(context, MOUSE_X, MOUSE_Y, CLICKED_LEFT && !hoverComponentDrawing && !isDragging && notInTopBarChecking(), CLICKED_RIGHT && !hoverComponentDrawing && !isDragging && notInTopBarChecking());
        }

//        SEARCH.setX(MAIN_PAGE_X + 106 + 3);
//        SEARCH.setY(MAIN_PAGE_Y);
//        SEARCH.setWidth(MAIN_PAGE_WIDTH - 106 - 6);
//        SEARCH.setHeight(FontUtil.getHeight(FontSize.MEDIUM) + 2);
//        SEARCH.onDraw(context, MOUSE_X, MOUSE_Y, CLICKED_LEFT, CLICKED_RIGHT);
//
//        if (SEARCH.isListening() || !SEARCH.searchText.isEmpty()) {
//            for (GuiComponent component : allModuleComponent) {
//                if (searchModuleComponent.contains(component)) {
//                    component.onDraw(context, MOUSE_X, MOUSE_Y, CLICKED_LEFT && !hoverComponentDrawing && !isDragging && notInTopBarChecking(), CLICKED_RIGHT && !hoverComponentDrawing && !isDragging && !notInTopBarChecking());
//                }
//            }
//            searchModuleComponent.clear();
//        }

        Render2DUtil.popDisplayArea();

        for (GuiComponent component : hoveredComponents) {
            if (component instanceof HoverEnumChoicesComponent) {
                if (((EnumChoicesComponent) component.getParentComponent()).isActuallySpread && ((ModuleComponent) component.getParentComponent().getParentComponent().getParentComponent().getParentComponent()).isSpread()) {
                    hoveredDrawingComponent = component;
                    component.onDraw(context, MOUSE_X, MOUSE_Y, CLICKED_LEFT, CLICKED_RIGHT);
                }
            }
            else if (component instanceof HoverMultipleChoicesComponent) {
                if (((MultipleChoicesComponent) component.getParentComponent()).isActuallySpread && ((ModuleComponent) component.getParentComponent().getParentComponent().getParentComponent().getParentComponent()).isSpread()) {
                    hoveredDrawingComponent = component;
                    component.onDraw(context, MOUSE_X, MOUSE_Y, CLICKED_LEFT, CLICKED_RIGHT);
                }
            }
            else if (component instanceof HoverBindChoicesComponent) {
                if (((BindChoicesComponent) component.getParentComponent()).isActuallySpread && ((ModuleComponent) component.getParentComponent().getParentComponent().getParentComponent().getParentComponent()).isSpread()) {
                    hoveredDrawingComponent = component;
                    component.onDraw(context, MOUSE_X, MOUSE_Y, CLICKED_LEFT, CLICKED_RIGHT);
                }
            }
            else if (component instanceof ColorPalette) {
                if (((ColorPreviewer) component.getParentComponent()).isSpread) {
                    hoveredDrawingComponent = component;
                    component.onDraw(context, MOUSE_X, MOUSE_Y, CLICKED_LEFT, CLICKED_RIGHT);
                }
            }
        }

        if (CLICKED_LEFT && !hoverComponentDrawing) {
            if (MOUSE_X > MAIN_PAGE_X && MOUSE_X < MAIN_PAGE_X + MAIN_PAGE_WIDTH && MOUSE_Y > MAIN_PAGE_Y && MOUSE_Y < MAIN_PAGE_Y + MAIN_PAGE_HEIGHT) {
                if (!isDragging) {
                    isDragging = true;
                } else {
                    MAIN_PAGE_X_OFFSET += MOUSE_X - lastMouseX;
                    MAIN_PAGE_Y_OFFSET += MOUSE_Y - lastMouseY;
                }
                lastMouseX = MOUSE_X;
                lastMouseY = MOUSE_Y;
            }
        }

        matrices.pop();
    }

    private static boolean notInTopBarChecking() {
        return !((MOUSE_X >= MAIN_PAGE_X + 106) && (MOUSE_X <= MAIN_PAGE_X + MAIN_PAGE_WIDTH) && (MOUSE_Y >= MAIN_PAGE_Y) && (MOUSE_Y <= MAIN_PAGE_Y + 37));
    }

    private static void layoutModuleAreaComponent(ModuleComponent moduleComponent) {
        for (GuiComponent tempComponent : moduleComponent.getSubComponents()) {
            ModuleAreaComponent moduleAreaComponent = (ModuleAreaComponent) tempComponent;
            moduleAreaComponent.setX(latestModuleComponentPosition.getA() + 3);
            moduleAreaComponent.setY(latestModuleComponentPosition.getB() - 2);
            moduleAreaComponent.setWidth(moduleComponent.getWidth() - 6);
            moduleAreaComponent.setHeight(0);
            latestOptionComponentPosition = new Pair<>(moduleAreaComponent.getX(), moduleAreaComponent.getY() + moduleAreaComponent.getHeight() + 2);

            for (GuiComponent optionComponent : moduleAreaComponent.getSubComponents()) {
                if (!shouldDisplayOptionComponent(optionComponent)) {
                    continue;
                }
                optionComponent.setX(latestOptionComponentPosition.getA());
                optionComponent.setY(latestOptionComponentPosition.getB());
                optionComponent.setWidth(moduleAreaComponent.getWidth());
                optionComponent.setHeight(15);
                moduleAreaComponent.setHeight(moduleAreaComponent.getHeight() + optionComponent.getHeight() + 1);
                latestOptionComponentPosition = new Pair<>(moduleAreaComponent.getX(), optionComponent.getY() + optionComponent.getHeight() + 1);

                if (optionComponent instanceof BooleanComponent comp) {
                    layoutBooleanComponent(comp);
                }
                else if (optionComponent instanceof EnumComponent comp) {
                    layoutEnumComponent(comp);
                }
                else if (optionComponent instanceof DoubleComponent comp) {
                    layoutDoubleComponent(comp);
                }
                else if (optionComponent instanceof BindComponent comp) {
                    layoutBindComponent(comp);
                }
                else if (optionComponent instanceof TextComponent comp) {
                    layoutTextComponent(comp);
                }
                else if (optionComponent instanceof MultipleComponent comp) {
                    layoutMultipleComponent(comp);
                }
                else if (optionComponent instanceof ColorComponent comp) {
                    layoutColorComponent(comp);
                }
            }
            moduleAreaComponent.setHeight(moduleAreaComponent.getHeight() + 3);
            moduleComponent.setHeight(moduleComponent.getHeight() + moduleAreaComponent.getHeight() + 2);
            latestModuleComponentPosition = new Pair<>(moduleComponent.getX(), moduleComponent.getY() + moduleComponent.getHeight() + 2);
        }
    }

    private static void layoutBooleanComponent(BooleanComponent optionComponent) {
        for (GuiComponent subComponent : optionComponent.getSubComponents()) {
            if (subComponent instanceof HoverTextComponent hoverTextComponent) {
                hoverTextComponent.setX(optionComponent.getX() + optionComponent.getWidth() + 8);
                hoverTextComponent.setY(optionComponent.getY());
                hoverTextComponent.setWidth(FontUtil.getWidth(FontSize.SMALLEST, optionComponent.getOption().getDescription()) + 4);
                hoverTextComponent.setHeight(optionComponent.getHeight() - 2);
            } else if (subComponent instanceof BooleanButtonComponent button) {
                button.setX(optionComponent.getX() + optionComponent.getWidth() - optionComponent.getWidth() / 13);
                button.setY(optionComponent.getY());
                button.setWidth(optionComponent.getWidth() / 13);
                button.setHeight(optionComponent.getHeight());
            }
        }
    }

    private static void layoutEnumComponent(EnumComponent optionComponent) {
        for (GuiComponent subComponent : optionComponent.getSubComponents()) {
            if (subComponent instanceof HoverTextComponent hoverTextComponent) {
                hoverTextComponent.setX(optionComponent.getX() + optionComponent.getWidth() + 8);
                hoverTextComponent.setY(optionComponent.getY());
                hoverTextComponent.setWidth(FontUtil.getWidth(FontSize.SMALLEST, optionComponent.getOption().getDescription()) + 4);
                hoverTextComponent.setHeight(optionComponent.getHeight() - 2);
            } else if (subComponent instanceof EnumChoicesComponent enumChoicesComponent) {
                double labelWidth = FontUtil.getWidth(FontSize.SMALL, optionComponent.getOption().getDisplayName());
                enumChoicesComponent.setX(optionComponent.getX() + labelWidth + 4);
                enumChoicesComponent.setY(optionComponent.getY() + 1);
                enumChoicesComponent.setWidth(optionComponent.getWidth() - labelWidth - 6);
                enumChoicesComponent.setHeight(optionComponent.getHeight() - 2);
                for (GuiComponent subComponent2 : enumChoicesComponent.getSubComponents()) {
                    HoverEnumChoicesComponent hoverEnumChoicesComponent = (HoverEnumChoicesComponent) subComponent2;
                    hoverEnumChoicesComponent.setX(enumChoicesComponent.getX());
                    hoverEnumChoicesComponent.setY(enumChoicesComponent.getY() + enumChoicesComponent.getHeight() - 1);
                    hoverEnumChoicesComponent.setWidth(enumChoicesComponent.getWidth());
                    hoverEnumChoicesComponent.setHeight(0);
                    Pair<Double, Double> latestChoicePosition = new Pair<>(hoverEnumChoicesComponent.getX(), hoverEnumChoicesComponent.getY() + 1);
                    for (GuiComponent subComponent3 : hoverEnumChoicesComponent.getSubComponents()) {
                        EnumChoiceComponent choice = (EnumChoiceComponent) subComponent3;
                        choice.setX(latestChoicePosition.getA());
                        choice.setY(latestChoicePosition.getB());
                        choice.setWidth(hoverEnumChoicesComponent.getWidth());
                        choice.setHeight(14);
                        hoverEnumChoicesComponent.setHeight(hoverEnumChoicesComponent.getHeight() + choice.getHeight());
                        latestChoicePosition = new Pair<>(choice.getX(), choice.getY() + choice.getHeight());
                    }
                    hoverEnumChoicesComponent.setHeight(hoverEnumChoicesComponent.getHeight() + 2);
                }
            }
        }
    }

    private static void layoutMultipleComponent(MultipleComponent optionComponent) {
        for (GuiComponent subComponent : optionComponent.getSubComponents()) {
            if (subComponent instanceof HoverTextComponent hoverTextComponent) {
                hoverTextComponent.setX(optionComponent.getX() + optionComponent.getWidth() + 8);
                hoverTextComponent.setY(optionComponent.getY());
                hoverTextComponent.setWidth(FontUtil.getWidth(FontSize.SMALLEST, optionComponent.getOption().getDescription()) + 4);
                hoverTextComponent.setHeight(optionComponent.getHeight() - 2);
            } else if (subComponent instanceof MultipleChoicesComponent multipleChoicesComponent) {
                double labelWidth = FontUtil.getWidth(FontSize.SMALL, optionComponent.getOption().getDisplayName());
                multipleChoicesComponent.setX(optionComponent.getX() + labelWidth + 4);
                multipleChoicesComponent.setY(optionComponent.getY() + 1);
                multipleChoicesComponent.setWidth(optionComponent.getWidth() - labelWidth - 6);
                multipleChoicesComponent.setHeight(optionComponent.getHeight() - 2);
                for (GuiComponent subComponent2 : multipleChoicesComponent.getSubComponents()) {
                    HoverMultipleChoicesComponent hover = (HoverMultipleChoicesComponent) subComponent2;
                    hover.setX(multipleChoicesComponent.getX());
                    hover.setY(multipleChoicesComponent.getY() + multipleChoicesComponent.getHeight() - 1);
                    hover.setWidth(multipleChoicesComponent.getWidth());
                    hover.setHeight(0);
                    Pair<Double, Double> latestChoicePosition = new Pair<>(hover.getX(), hover.getY() + 1);
                    for (GuiComponent subComponent3 : hover.getSubComponents()) {
                        MultipleChoiceComponent choice = (MultipleChoiceComponent) subComponent3;
                        choice.setX(latestChoicePosition.getA());
                        choice.setY(latestChoicePosition.getB());
                        choice.setWidth(hover.getWidth());
                        choice.setHeight(14);
                        hover.setHeight(hover.getHeight() + choice.getHeight());
                        latestChoicePosition = new Pair<>(choice.getX(), choice.getY() + choice.getHeight());
                    }
                    hover.setHeight(hover.getHeight() + 2);
                }
            }
        }
    }

    private static void layoutTextComponent(TextComponent optionComponent) {
        for (GuiComponent subComponent : optionComponent.getSubComponents()) {
            if (subComponent instanceof HoverTextComponent hoverTextComponent) {
                hoverTextComponent.setX(optionComponent.getX() + optionComponent.getWidth() + 8);
                hoverTextComponent.setY(optionComponent.getY());
                hoverTextComponent.setWidth(FontUtil.getWidth(FontSize.SMALLEST, optionComponent.getOption().getDescription()) + 4);
                hoverTextComponent.setHeight(optionComponent.getHeight() - 2);
            } else if (subComponent instanceof TextFrameComponent frame) {
                double labelWidth = FontUtil.getWidth(FontSize.SMALL, optionComponent.getOption().getDisplayName());
                frame.setX(optionComponent.getX() + labelWidth + 4);
                frame.setY(optionComponent.getY() + 1);
                frame.setWidth(optionComponent.getWidth() - labelWidth - 6);
                frame.setHeight(optionComponent.getHeight() - 2);
            }
        }
    }

    private static void layoutDoubleComponent(DoubleComponent optionComponent) {
        double frameWidth = FontUtil.getWidth(FontSize.SMALLEST, "10000.00_") + 6;
        double sliderWidth = 110;
        double spacing = 4;
        double totalWidth = frameWidth + spacing + sliderWidth;
        double rightStartX = optionComponent.getX() + optionComponent.getWidth() - totalWidth;
        double sliderY = optionComponent.getY() + (optionComponent.getHeight() - 3) / 2.0;
        double frameY = optionComponent.getY() + 1;
        double frameHeight = optionComponent.getHeight() - 2;

        for (GuiComponent subComponent : optionComponent.getSubComponents()) {
            if (subComponent instanceof HoverTextComponent hoverTextComponent) {
                hoverTextComponent.setX(optionComponent.getX() + optionComponent.getWidth() + 8);
                hoverTextComponent.setY(optionComponent.getY());
                hoverTextComponent.setWidth(FontUtil.getWidth(FontSize.SMALLEST, optionComponent.getOption().getDescription()) + 4);
                hoverTextComponent.setHeight(optionComponent.getHeight() - 2);
            } else if (subComponent instanceof DoubleFrameComponent frame) {
                frame.setX(rightStartX + sliderWidth + spacing - 2);
                frame.setY(frameY);
                frame.setWidth(frameWidth);
                frame.setHeight(frameHeight);
                for (GuiComponent subComponent2 : frame.getSubComponents()) {
                    if (subComponent2 instanceof DoubleSlider slider) {
                        slider.setX(rightStartX - 4);
                        slider.setY(sliderY);
                        slider.setWidth(sliderWidth);
                        slider.setHeight(3);
                    }
                }
            }
        }
    }

    private static void layoutColorComponent(ColorComponent optionComponent) {
        double frameWidth = FontUtil.getWidth(FontSize.SMALLEST, "#FFFFFFFF_") + 6;
        double previewSize = 10;
        double spacing = 2;

        double frameX = optionComponent.getX() + optionComponent.getWidth() - frameWidth;
        double previewX = frameX - spacing - previewSize;

        for (GuiComponent subComponent : optionComponent.getSubComponents()) {
            if (subComponent instanceof HoverTextComponent hoverTextComponent) {
                hoverTextComponent.setX(optionComponent.getX() + optionComponent.getWidth() + 8);
                hoverTextComponent.setY(optionComponent.getY());
                hoverTextComponent.setWidth(FontUtil.getWidth(FontSize.SMALLEST, optionComponent.getOption().getDescription()) + 4);
                hoverTextComponent.setHeight(optionComponent.getHeight() - 2);
            } else if (subComponent instanceof ColorPreviewer previewer) {
                previewer.setX(previewX - spacing);
                previewer.setY(optionComponent.getY() + (optionComponent.getHeight() - previewSize) / 2.0);
                previewer.setWidth(previewSize);
                previewer.setHeight(previewSize);
                for (GuiComponent popup : subComponent.getSubComponents()) {
                    popup.setX(previewX - spacing * 2 - frameWidth);
                    popup.setY(optionComponent.getY() + optionComponent.getHeight() + 2);
                    popup.setWidth(130);
                    popup.setHeight(90);
                }
            } else if (subComponent instanceof ColorFrameComponent frame) {
                frame.setX(frameX - spacing);
                frame.setY(optionComponent.getY() + 1);
                frame.setWidth(frameWidth);
                frame.setHeight(optionComponent.getHeight());
            }
        }
    }

    private static void layoutBindComponent(BindComponent optionComponent) {
        double frameWidth = FontUtil.getWidth(FontSize.SMALLEST, "SHIFT_RIGHT_SHIFT_") + 2;
        double selectWidth = FontUtil.getWidth(FontSize.SMALLEST, "PRESS AND CLICK OPTION") + 8;
        double spacing = 4;

        double frameX = optionComponent.getX() + optionComponent.getWidth() - selectWidth - spacing - frameWidth;
        double selectX = optionComponent.getX() + optionComponent.getWidth() - selectWidth - 2;
        double frameY = optionComponent.getY() + 1;
        double frameHeight = optionComponent.getHeight() - 2;

        for (GuiComponent subComponent : optionComponent.getSubComponents()) {
            if (subComponent instanceof HoverTextComponent hoverTextComponent) {
                hoverTextComponent.setX(optionComponent.getX() + optionComponent.getWidth() + 8);
                hoverTextComponent.setY(optionComponent.getY());
                hoverTextComponent.setWidth(FontUtil.getWidth(FontSize.SMALLEST, optionComponent.getOption().getDescription()) + 4);
                hoverTextComponent.setHeight(optionComponent.getHeight() - 2);
            } else if (subComponent instanceof BindFrameComponent inputFrame) {
                inputFrame.setX(frameX - 2);
                inputFrame.setY(frameY);
                inputFrame.setWidth(frameWidth);
                inputFrame.setHeight(frameHeight);
            } else if (subComponent instanceof BindChoicesComponent bindChoicesComponent) {
                bindChoicesComponent.setX(selectX);
                bindChoicesComponent.setY(frameY);
                bindChoicesComponent.setWidth(selectWidth);
                bindChoicesComponent.setHeight(frameHeight);
                for (GuiComponent subComponent2 : bindChoicesComponent.getSubComponents()) {
                    if (subComponent2 instanceof HoverBindChoicesComponent hover) {
                        hover.setX(selectX);
                        hover.setY(bindChoicesComponent.getY() + bindChoicesComponent.getHeight() - 1);
                        hover.setWidth(selectWidth);
                        hover.setHeight(0);
                        Pair<Double, Double> pos = new Pair<>(selectX, hover.getY() + 1);
                        for (GuiComponent choice : hover.getSubComponents()) {
                            BindChoiceComponent c = (BindChoiceComponent) choice;
                            c.setX(pos.getA());
                            c.setY(pos.getB());
                            c.setWidth(selectWidth);
                            c.setHeight(14);
                            hover.setHeight(hover.getHeight() + c.getHeight());
                            pos = new Pair<>(selectX, c.getY() + c.getHeight());
                        }
                        hover.setHeight(hover.getHeight() + 2);
                    }
                }
            }
        }
    }
}
