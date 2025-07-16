package cc.vergence.modules.hud;

import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.managers.HudManager;
import cc.vergence.features.managers.ModuleManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
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

        allModules.forEach(module -> {
            simpleAnimations.computeIfAbsent(module, m -> new SimpleAnimation(0, animationTime.getValue().longValue()));
            if (module.getStatus() && module.shouldDraw()) {
                simpleAnimations.get(module).to(animEnabled ? 1 : 1);
            } else {
                simpleAnimations.get(module).to(animEnabled ? 0 : 0);
            }
        });

        List<Module> modulesToDraw = allModules.stream()
                .filter(m -> simpleAnimations.get(m).get() > 0.001)
                .sorted(Comparator.comparingDouble(m -> -FontUtil.getWidth(size, m.getDisplayName())))
                .toList();

        double lineHeight = FontUtil.getHeight(size) + pad;
        double baseX = getX();
        double baseY = getY();

        double maxWidth = modulesToDraw.stream()
                .mapToDouble(m -> FontUtil.getWidth(size, m.getDisplayName()) * simpleAnimations.get(m).get())
                .max().orElse(0);

        double y = baseY;
        for (Module module : modulesToDraw) {
            String name = module.getDisplayName();
            double fullWidth = FontUtil.getWidth(size, name);
            double alpha = simpleAnimations.get(module).get();
            if (alpha < 0.01) continue;

            double animWidth = fullWidth * alpha;
            double drawX = switch (aligns) {
                case RIGHT, RIGHT_TOP, RIGHT_BOTTOM -> baseX + maxWidth - animWidth;
                case CENTER, TOP, BOTTOM -> baseX + (maxWidth - animWidth) / 2;
                default -> baseX;
            };

            double bgX = drawX - 1;
            double bgY = y - 1;
            double bgWidth = animWidth + 2;
            double bgHeight = FontUtil.getHeight(size) + 2;

            if (background.getValue()) {
                if (rounded.getValue()) {
                    Render2DUtil.drawRoundedRect(context.getMatrices(), bgX, bgY, bgWidth, bgHeight, radius.getValue() * alpha, backgroundColor.getValue()); // alpha is like animation progress
                } else {
                    Render2DUtil.drawRect(context, bgX, bgY, bgWidth, bgHeight, backgroundColor.getValue());
                }
            }

            if (rect.getValue()) {
                double rectX = switch (aligns) {
                    case RIGHT, RIGHT_TOP, RIGHT_BOTTOM -> baseX + maxWidth + pad;
                    default -> baseX - pad - 2;
                };

                if (roundedRect.getValue()) {
                    Render2DUtil.drawRoundedRect(context.getMatrices(), rectX, y, rectWidth.getValue(), FontUtil.getHeight(size), radiusRect.getValue(), rectColor.getValue());
                } else {
                    Render2DUtil.drawRect(context, rectX, y, rectWidth.getValue(), FontUtil.getHeight(size), rectColor.getValue());
                }
            }

            Color finalColor = new Color(
                    textColor.getValue().getRed(),
                    textColor.getValue().getGreen(),
                    textColor.getValue().getBlue(),
                    (int) (textColor.getValue().getAlpha() * alpha)
            );

            FontUtil.drawText(context, name, drawX, y + 1, finalColor, size);
            y += lineHeight * (animEnabled ? alpha : 1);
        }

        setWidth(maxWidth);
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
}
