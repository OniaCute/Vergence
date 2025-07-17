package cc.vergence.modules.hud;

import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.managers.HudManager;
import cc.vergence.features.managers.ModuleManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.animations.SimpleAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleList extends Module {
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    private final ConcurrentHashMap<Module, SimpleAnimation> simpleAnimations = new ConcurrentHashMap<>();

    public ModuleList() {
        super("ModuleList", Category.HUD);
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Enum<?>> align = addOption(new EnumOption("Align", Aligns.RIGHT_TOP));
    public Option<Enum<?>> fontSize = addOption(new EnumOption("FontSize", FontSize.SMALL));
    public Option<Boolean> drawDetails = addOption(new BooleanOption("Details", true));
    public Option<Color> detailsColor = addOption(new ColorOption("DetailsColor", new Color(21, 21, 21, 232), v -> drawDetails.getValue()));
    public Option<Enum<?>> detailsType = addOption(new EnumOption("DetailsType", DetailsType.Bracket,v -> drawDetails.getValue()));
    public Option<Double> padding = addOption(new DoubleOption("Padding", -2, 6, 2).setUnit("px"));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(21, 21, 21, 232)));
    public Option<Boolean> background = addOption(new BooleanOption("Background", false));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(241, 241, 241, 232), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", false, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 4, v -> background.getValue() && rounded.getValue()));
    public Option<Boolean> rect = addOption(new BooleanOption("Rect", false));
    public Option<Double> rectWidth = addOption(new DoubleOption("RectWidth", 2, 6, 3, v -> rect.getValue()).setUnit("px"));
    public Option<Color> rectColor = addOption(new ColorOption("RectColor", new Color(251, 163, 255, 232), v -> rect.getValue()));
    public Option<Boolean> roundedRect = addOption(new BooleanOption("RoundedRect", false, v -> rect.getValue()));
    public Option<Double> radiusRect = addOption(new DoubleOption("RadiusRect", 0, 4, 2, v -> rect.getValue() && roundedRect.getValue()));
    public Option<Boolean> applyAnimation = addOption(new BooleanOption("Animation", false));
    public Option<Double> animationTime = addOption(new DoubleOption("AnimationTime", 20, 1200, 300, v -> applyAnimation.getValue()).setUnit("ms"));

    @Override
    public String getDetails() {
        return align.getValue().name();
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (screen instanceof HudEditorScreen && button.equals(MouseButtons.LEFT)) {
            this.lastMouseStatus = false;
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        Aligns aligns = (Aligns) align.getValue();
        FontSize size = (FontSize) fontSize.getValue();
        double pad = padding.getValue();
        boolean animEnabled = applyAnimation.getValue();

        Set<Module> allModules = new LinkedHashSet<>(ModuleManager.modules);

        for (Module module : allModules) {
            simpleAnimations.computeIfAbsent(module, m -> new SimpleAnimation(0, animationTime.getValue().longValue()));
            if (applyAnimation.getValue()) {
                simpleAnimations.get(module).to(module.getStatus() && module.shouldDraw() ? 1 : 0);
            } else {
                simpleAnimations.get(module).set(module.getStatus() && module.shouldDraw() ? 1 : 0);
            }
        }


        List<Module> tempModules = allModules.stream()
                .filter(m -> simpleAnimations.get(m).get() > 0.001)
                .sorted(Comparator.comparingDouble(m -> -FontUtil.getWidth(size, m.getDisplayName() + (drawDetails.getValue() ? m.getDetails() : 0))))
                .toList();

        ArrayList<Module> modulesToDraw = new ArrayList<>(tempModules);

        double lineHeight = FontUtil.getHeight(size) + pad;
        double baseX = getX();
        double baseY = getY();

        double maxWidth = modulesToDraw.stream()
                .mapToDouble(m -> FontUtil.getWidth(size, m.getDisplayName()) * simpleAnimations.get(m).get())
                .max().orElse(0);

        double actualMaxWidth = allModules.stream()
                .filter(Module::shouldDraw)
                .mapToDouble(m -> FontUtil.getWidth(size, m.getDisplayName() + (
                        drawDetails.getValue() && !m.getDetails().isEmpty()
                                ? (detailsType.getValue().equals(DetailsType.Bracket) ? " [" + m.getDetails() + "]"
                                : " | " + m.getDetails())
                                : ""
                )))
                .max().orElse(0);

        modulesToDraw.sort((a, b) -> {
            double wa = FontUtil.getWidth(size, a.getDisplayName() + (drawDetails.getValue() ? a.getDetails() : 0));
            double wb = FontUtil.getWidth(size, b.getDisplayName() + (drawDetails.getValue() ? b.getDetails() : 0));
            boolean bottom = aligns.name().contains("BOTTOM");
            return bottom ? Double.compare(wa, wb) : Double.compare(wb, wa);
        });

        double y = baseY;
        for (Module module : modulesToDraw) {
            String moduleName = module.getDisplayName();
            String detailsText = "";
            if (drawDetails.getValue() && !module.getDetails().isEmpty()) {
                if (detailsType.getValue().equals(DetailsType.Bracket)) {
                    detailsText = " [" + module.getDetails() + "]";
                } else if (detailsType.getValue().equals(DetailsType.Line)) {
                    detailsText = " | " + module.getDetails();
                }
            }

            double alpha = simpleAnimations.get(module).get();
            if (alpha < 0.01) continue;

            double moduleNameWidth = FontUtil.getWidth(size, moduleName);
            double detailsWidth = FontUtil.getWidth(size, detailsText);
            double fullWidth = moduleNameWidth + detailsWidth;
            double animWidth = fullWidth * alpha;
            double drawX = switch (aligns) {
                case RIGHT, RIGHT_TOP, RIGHT_BOTTOM -> baseX + maxWidth - animWidth;
                case CENTER, TOP, BOTTOM -> baseX + (maxWidth - animWidth) / 2;
                default -> baseX;
            };
            double bgX = drawX - 1;
            double bgY = y;
            double bgWidth = animWidth + 2;
            double bgHeight = FontUtil.getHeight(size);
            double drawY = y + 1;
            Color animatedTextColor = alphaColor(textColor.getValue(), Math.min(alpha, 1));
            Color animatedDetailsColor = alphaColor(detailsColor.getValue(), Math.min(alpha, 1));

            if (alpha > 0.001 && background.getValue()) {
                Color bg = alphaColor(backgroundColor.getValue(), alpha);
                if (rounded.getValue()) {
                    double dynamicRadius = radius.getValue() * alpha;
                    Render2DUtil.drawRoundedRect(context.getMatrices(), bgX, bgY, bgWidth, bgHeight, dynamicRadius, bg);
                } else {
                    Render2DUtil.drawRect(context, bgX, bgY, bgWidth, bgHeight, bg);
                }
            }
            if (alpha > 0.001 && rect.getValue()) {
                double rectX = switch (aligns) {
                    case RIGHT, RIGHT_TOP, RIGHT_BOTTOM -> drawX + animWidth + pad;
                    default -> drawX - pad - rectWidth.getValue();
                };

                Color animatedRectColor = alphaColor(rectColor.getValue(), alpha);

                if (roundedRect.getValue()) {
                    Render2DUtil.drawRoundedRect(context.getMatrices(), rectX, y, rectWidth.getValue(), FontUtil.getHeight(size), radiusRect.getValue(), animatedRectColor);
                } else {
                    Render2DUtil.drawRect(context, rectX, y, rectWidth.getValue(), FontUtil.getHeight(size), animatedRectColor);
                }
            }

            switch (aligns) {
                case RIGHT:
                case RIGHT_TOP:
                case RIGHT_BOTTOM:
                    double detailsX = drawX + animWidth - detailsWidth;
                    if (alpha > 0.001) {
                        FontUtil.drawText(context, moduleName, drawX, drawY, animatedTextColor, size);
                        FontUtil.drawText(context, detailsText, detailsX, drawY, animatedDetailsColor, size);
                    }
                    break;
                case CENTER:
                case TOP:
                case BOTTOM:
                    double moduleNameX = drawX + animWidth - fullWidth;
                    if (alpha > 0.001) {
                        FontUtil.drawText(context, moduleName, moduleNameX, drawY, animatedTextColor, size);
                        FontUtil.drawText(context, detailsText, moduleNameX + moduleNameWidth, drawY, animatedDetailsColor, size);
                    }
                    break;
                default:
                    if (alpha > 0.001) {
                        FontUtil.drawText(context, moduleName, drawX, drawY, animatedTextColor, size);
                        FontUtil.drawText(context, detailsText, drawX + moduleNameWidth, drawY, animatedDetailsColor, size);
                    }
                    break;
            }

            y += lineHeight * (animEnabled ? alpha : 1);
        }

        setWidth(actualMaxWidth + pad + (rect.getValue() ? rectWidth.getValue() : 0));
        setHeight(modulesToDraw.stream().mapToDouble(m -> lineHeight * (animEnabled ? simpleAnimations.get(m).get() : 1)).sum());

        if (HudManager.CLICKED_LEFT) {
            if (HudManager.MOUSE_X > getX() && HudManager.MOUSE_X < getX() + getWidth() &&
                    HudManager.MOUSE_Y > getY() && HudManager.MOUSE_Y < getY() + getHeight()) {
                HudManager.currentHud = this;
                if (!lastMouseStatus) {
                    lastMouseStatus = true;
                } else {
                    setX(getX() + HudManager.MOUSE_X - lastMouseX);
                    setY(getY() + HudManager.MOUSE_Y - lastMouseY);
                }
                lastMouseX = HudManager.MOUSE_X;
                lastMouseY = HudManager.MOUSE_Y;
            } else {
                HudManager.currentHud = null;
            }
        }
    }

    public static Color alphaColor(Color color, double alpha) {
        int a = (int) (color.getAlpha() * alpha);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, Math.max(0, a)));
    }

    public enum DetailsType {
        Bracket,
        Line
    }
}
