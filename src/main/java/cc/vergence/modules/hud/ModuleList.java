package cc.vergence.modules.hud;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.animations.SimpleAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
            HudManager.currentHud = null;
        }
    }

    @Override
    public void onDraw2D() {
        Aligns aligns = (Aligns) align.getValue();
        FontSize size = (FontSize) fontSize.getValue();
        double pad = padding.getValue();
        boolean animEnabled = applyAnimation.getValue();

        Set<Module> allModules = ModuleManager.modules.stream()
                .filter(Module::shouldDraw)
                .collect(Collectors.toSet());

        for (Module module : allModules) {
            simpleAnimations.computeIfAbsent(module, m -> new SimpleAnimation(0, animationTime.getValue().longValue()));
            simpleAnimations.get(module).setDuration(animationTime.getValue().longValue());
            if (animEnabled) {
                simpleAnimations.get(module).to(module.getStatus() ? 1 : 0);
            } else {
                simpleAnimations.get(module).set(module.getStatus() ? 1 : 0);
            }
        }

        List<Module> modulesToDraw = allModules.stream()
                .filter(m -> simpleAnimations.get(m).get() > 0.001)
                .sorted((a, b) -> {
                    double wa = FontUtil.getWidth(size, buildModuleText(a));
                    double wb = FontUtil.getWidth(size, buildModuleText(b));
                    boolean bottom = aligns.name().contains("BOTTOM");
                    return bottom ? Double.compare(wa, wb) : Double.compare(wb, wa);
                })
                .toList();

        double lineHeight = FontUtil.getHeight(size) + pad;
        double baseX = getX();
        double baseY = getY();

        double maxWidth = modulesToDraw.stream()
                .mapToDouble(m -> FontUtil.getWidth(size, buildModuleText(m)) * simpleAnimations.get(m).get())
                .max().orElse(0);

        double y = baseY;

        for (Module module : modulesToDraw) {
            double alpha = simpleAnimations.get(module).get();
            if (alpha < 0.01) continue;

            String display = buildModuleText(module);
            double fullWidth = FontUtil.getWidth(size, display);
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

            Color animatedTextColor = alphaColor(textColor.getValue(), alpha);
            Color animatedDetailsColor = alphaColor(detailsColor.getValue(), alpha);

            if (background.getValue()) {
                Color bg = alphaColor(backgroundColor.getValue(), alpha);
                if (rounded.getValue()) {
                    Render2DUtil.drawRoundedRect(bgX, bgY, bgWidth, bgHeight, radius.getValue() * alpha, bg);
                } else {
                    Render2DUtil.drawRect(bgX, bgY, bgWidth, bgHeight, bg);
                }
            }

            if (rect.getValue()) {
                double rectX = switch (aligns) {
                    case RIGHT, RIGHT_TOP, RIGHT_BOTTOM -> drawX + animWidth + pad;
                    default -> drawX - pad - rectWidth.getValue();
                };

                Color animatedRectColor = alphaColor(rectColor.getValue(), alpha);
                if (roundedRect.getValue()) {
                    Render2DUtil.drawRoundedRect(rectX, y, rectWidth.getValue(), FontUtil.getHeight(size), radiusRect.getValue(), animatedRectColor);
                } else {
                    Render2DUtil.drawRect(rectX, y, rectWidth.getValue(), FontUtil.getHeight(size), animatedRectColor);
                }
            }

            FontUtil.drawText(module.getDisplayName(), drawX, drawY, animatedTextColor, size);
            if (drawDetails.getValue() && !module.getDetails().isEmpty()) {
                double moduleNameWidth = FontUtil.getWidth(size, module.getDisplayName());
                FontUtil.drawText(" [" + module.getDetails() + "]", drawX + moduleNameWidth, drawY, animatedDetailsColor, size);
            }

            y += lineHeight * (animEnabled ? alpha : 1);
        }

        // calc
        setWidth(maxWidth + (rect.getValue() ? pad + rectWidth.getValue() : 0));

        double totalHeight = 0;
        for (Module m : modulesToDraw) {
            double alpha = simpleAnimations.get(m).get();
            if (alpha < 0.01) {
                continue;
            }
            totalHeight += lineHeight * (animEnabled ? alpha : 1);
        }
        setHeight(totalHeight);

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

    private String buildModuleText(Module module) {
        if (drawDetails.getValue() && !module.getDetails().isEmpty()) {
            return module.getDisplayName() + " [" + module.getDetails() + "]";
        }
        return module.getDisplayName();
    }

    public static Color alphaColor(Color color, double alpha) {
        int a = (int) (color.getAlpha() * alpha);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, Math.max(0, a)));
    }
}
