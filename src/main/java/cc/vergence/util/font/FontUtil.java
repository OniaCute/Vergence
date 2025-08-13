package cc.vergence.util.font;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontScales;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.font.Fonts;
import cc.vergence.modules.client.Client;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class FontUtil implements Wrapper {
    public static boolean LOADED;

    public static FontScales asFontScales(Client.UIScales uiScales, FontSize size, Fonts fonts) {
        if (fonts.equals(Fonts.Sans)) {
            switch (uiScales) {
                case X50 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SANS_7F;
                        }
                        case LARGE -> {
                            return FontScales.SANS_6F;
                        }
                        case MEDIUM -> {
                            return FontScales.SANS_5F;
                        }
                        case SMALL -> {
                            return FontScales.SANS_4F;
                        }
                        case SMALLEST -> {
                            return FontScales.SANS_3F;
                        }
                    }
                }
                case X100 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SANS_16F;
                        }
                        case LARGE -> {
                            return FontScales.SANS_12F;
                        }
                        case MEDIUM -> {
                            return FontScales.SANS_10F;
                        }
                        case SMALL -> {
                            return FontScales.SANS_8F;
                        }
                        case SMALLEST -> {
                            return FontScales.SANS_6F;
                        }
                    }
                }
                case X150 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SANS_24F;
                        }
                        case LARGE -> {
                            return FontScales.SANS_18F;
                        }
                        case MEDIUM -> {
                            return FontScales.SANS_15F;
                        }
                        case SMALL -> {
                            return FontScales.SANS_12F;
                        }
                        case SMALLEST -> {
                            return FontScales.SANS_9F;
                        }
                    }
                }
                case X200 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SANS_32F;
                        }
                        case LARGE -> {
                            return FontScales.SANS_24F;
                        }
                        case MEDIUM -> {
                            return FontScales.SANS_20F;
                        }
                        case SMALL -> {
                            return FontScales.SANS_16F;
                        }
                        case SMALLEST -> {
                            return FontScales.SANS_12F;
                        }
                    }
                }
            }

            return FontScales.SANS_10F;
        }
        if (fonts.equals(Fonts.RHR)) {
            switch (uiScales) {
                case X50 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.RHR_7F;
                        }
                        case LARGE -> {
                            return FontScales.RHR_6F;
                        }
                        case MEDIUM -> {
                            return FontScales.RHR_5F;
                        }
                        case SMALL -> {
                            return FontScales.RHR_4F;
                        }
                        case SMALLEST -> {
                            return FontScales.RHR_3F;
                        }
                    }
                }
                case X100 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.RHR_16F;
                        }
                        case LARGE -> {
                            return FontScales.RHR_12F;
                        }
                        case MEDIUM -> {
                            return FontScales.RHR_10F;
                        }
                        case SMALL -> {
                            return FontScales.RHR_8F;
                        }
                        case SMALLEST -> {
                            return FontScales.RHR_6F;
                        }
                    }
                }
                case X150 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.RHR_24F;
                        }
                        case LARGE -> {
                            return FontScales.RHR_18F;
                        }
                        case MEDIUM -> {
                            return FontScales.RHR_15F;
                        }
                        case SMALL -> {
                            return FontScales.RHR_12F;
                        }
                        case SMALLEST -> {
                            return FontScales.RHR_9F;
                        }
                    }
                }
                case X200 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.RHR_32F;
                        }
                        case LARGE -> {
                            return FontScales.RHR_24F;
                        }
                        case MEDIUM -> {
                            return FontScales.RHR_20F;
                        }
                        case SMALL -> {
                            return FontScales.RHR_16F;
                        }
                        case SMALLEST -> {
                            return FontScales.RHR_12F;
                        }
                    }
                }
            }

            return FontScales.RHR_10F;
        }
        if (fonts.equals(Fonts.Icon)) {
            switch (uiScales) {
                case X50 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.ICON_7F;
                        }
                        case LARGE -> {
                            return FontScales.ICON_6F;
                        }
                        case MEDIUM -> {
                            return FontScales.ICON_5F;
                        }
                        case SMALL -> {
                            return FontScales.ICON_4F;
                        }
                        case SMALLEST -> {
                            return FontScales.ICON_3F;
                        }
                    }
                }
                case X100 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.ICON_16F;
                        }
                        case LARGE -> {
                            return FontScales.ICON_12F;
                        }
                        case MEDIUM -> {
                            return FontScales.ICON_10F;
                        }
                        case SMALL -> {
                            return FontScales.ICON_8F;
                        }
                        case SMALLEST -> {
                            return FontScales.ICON_6F;
                        }
                    }
                }
                case X150 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.ICON_24F;
                        }
                        case LARGE -> {
                            return FontScales.ICON_18F;
                        }
                        case MEDIUM -> {
                            return FontScales.ICON_15F;
                        }
                        case SMALL -> {
                            return FontScales.ICON_12F;
                        }
                        case SMALLEST -> {
                            return FontScales.ICON_9F;
                        }
                    }
                }
                case X200 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.ICON_32F;
                        }
                        case LARGE -> {
                            return FontScales.ICON_24F;
                        }
                        case MEDIUM -> {
                            return FontScales.ICON_20F;
                        }
                        case SMALL -> {
                            return FontScales.ICON_16F;
                        }
                        case SMALLEST -> {
                            return FontScales.ICON_12F;
                        }
                    }
                }
            }

            return FontScales.ICON_10F;
        } else {
            switch (uiScales) {
                case X50 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SMOOTH_7F;
                        }
                        case LARGE -> {
                            return FontScales.SMOOTH_6F;
                        }
                        case MEDIUM -> {
                            return FontScales.SMOOTH_5F;
                        }
                        case SMALL -> {
                            return FontScales.SMOOTH_4F;
                        }
                        case SMALLEST -> {
                            return FontScales.SMOOTH_3F;
                        }
                    }
                }
                case X100 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SMOOTH_16F;
                        }
                        case LARGE -> {
                            return FontScales.SMOOTH_12F;
                        }
                        case MEDIUM -> {
                            return FontScales.SMOOTH_10F;
                        }
                        case SMALL -> {
                            return FontScales.SMOOTH_8F;
                        }
                        case SMALLEST -> {
                            return FontScales.SMOOTH_6F;
                        }
                    }
                }
                case X150 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SMOOTH_24F;
                        }
                        case LARGE -> {
                            return FontScales.SMOOTH_18F;
                        }
                        case MEDIUM -> {
                            return FontScales.SMOOTH_15F;
                        }
                        case SMALL -> {
                            return FontScales.SMOOTH_12F;
                        }
                        case SMALLEST -> {
                            return FontScales.SMOOTH_9F;
                        }
                    }
                }
                case X200 -> {
                    switch (size) {
                        case LARGEST -> {
                            return FontScales.SMOOTH_32F;
                        }
                        case LARGE -> {
                            return FontScales.SMOOTH_24F;
                        }
                        case MEDIUM -> {
                            return FontScales.SMOOTH_20F;
                        }
                        case SMALL -> {
                            return FontScales.SMOOTH_16F;
                        }
                        case SMALLEST -> {
                            return FontScales.SMOOTH_12F;
                        }
                    }
                }
            }

            return FontScales.SMOOTH_10F;
        }
    }

    public static double getWidth(FontSize size, String text) {
        Fonts fontType = (Fonts) Client.INSTANCE.font.getValue();
        return getWidth(size, fontType, text);
    }

    public static double getWidth(FontSize size, Fonts fontType, String text) {
        if (Client.INSTANCE == null || !LOADED) {return 0;}

        FontScales fontScales = asFontScales((Client.UIScales) Client.INSTANCE.UIScale.getValue(), size, fontType);
        if (fontType.equals(Fonts.Sans)) {
            switch (fontScales) {
                case SANS_3F -> {
                    return FontRenderers.SANS_3F.getWidth(text);
                }
                case SANS_4F -> {
                    return FontRenderers.SANS_4F.getWidth(text);
                }
                case SANS_5F -> {
                    return FontRenderers.SANS_5F.getWidth(text);
                }
                case SANS_6F -> {
                    return FontRenderers.SANS_6F.getWidth(text);
                }
                case SANS_7F -> {
                    return FontRenderers.SANS_7F.getWidth(text);
                }
                case SANS_8F -> {
                    return FontRenderers.SANS_8F.getWidth(text);
                }
                case SANS_9F -> {
                    return FontRenderers.SANS_9F.getWidth(text);
                }
                case SANS_10F -> {
                    return FontRenderers.SANS_10F.getWidth(text);
                }
                case SANS_12F -> {
                    return FontRenderers.SANS_12F.getWidth(text);
                }
                case SANS_14F -> {
                    return FontRenderers.SANS_14F.getWidth(text);
                }
                case SANS_15F -> {
                    return FontRenderers.SANS_15F.getWidth(text);
                }
                case SANS_16F -> {
                    return FontRenderers.SANS_16F.getWidth(text);
                }
                case SANS_18F -> {
                    return FontRenderers.SANS_18F.getWidth(text);
                }
                case SANS_20F -> {
                    return FontRenderers.SANS_20F.getWidth(text);
                }
                case SANS_21F -> {
                    return FontRenderers.SANS_21F.getWidth(text);
                }
                case SANS_24F -> {
                    return FontRenderers.SANS_24F.getWidth(text);
                }
                case SANS_28F -> {
                    return FontRenderers.SANS_28F.getWidth(text);
                }
                case SANS_32F -> {
                    return FontRenderers.SANS_32F.getWidth(text);
                }
            }
            return FontRenderers.SANS_10F.getWidth(text);
        }
        else if (fontType.equals(Fonts.RHR)) {
            switch (fontScales) {
                case RHR_3F -> {
                    return FontRenderers.RHR_3F.getWidth(text);
                }
                case RHR_4F -> {
                    return FontRenderers.RHR_4F.getWidth(text);
                }
                case RHR_5F -> {
                    return FontRenderers.RHR_5F.getWidth(text);
                }
                case RHR_6F -> {
                    return FontRenderers.RHR_6F.getWidth(text);
                }
                case RHR_7F -> {
                    return FontRenderers.RHR_7F.getWidth(text);
                }
                case RHR_8F -> {
                    return FontRenderers.RHR_8F.getWidth(text);
                }
                case RHR_9F -> {
                    return FontRenderers.RHR_9F.getWidth(text);
                }
                case RHR_10F -> {
                    return FontRenderers.RHR_10F.getWidth(text);
                }
                case RHR_12F -> {
                    return FontRenderers.RHR_12F.getWidth(text);
                }
                case RHR_14F -> {
                    return FontRenderers.RHR_14F.getWidth(text);
                }
                case RHR_15F -> {
                    return FontRenderers.RHR_15F.getWidth(text);
                }
                case RHR_16F -> {
                    return FontRenderers.RHR_16F.getWidth(text);
                }
                case RHR_18F -> {
                    return FontRenderers.RHR_18F.getWidth(text);
                }
                case RHR_20F -> {
                    return FontRenderers.RHR_20F.getWidth(text);
                }
                case RHR_21F -> {
                    return FontRenderers.RHR_21F.getWidth(text);
                }
                case RHR_24F -> {
                    return FontRenderers.RHR_24F.getWidth(text);
                }
                case RHR_28F -> {
                    return FontRenderers.RHR_28F.getWidth(text);
                }
                case RHR_32F -> {
                    return FontRenderers.RHR_32F.getWidth(text);
                }
            }
            return FontRenderers.RHR_10F.getWidth(text);
        }
        else if (fontType.equals(Fonts.Icon)) {
            switch (fontScales) {
                case ICON_3F -> {
                    return FontRenderers.ICON_3F.getWidth(text);
                }
                case ICON_4F -> {
                    return FontRenderers.ICON_4F.getWidth(text);
                }
                case ICON_5F -> {
                    return FontRenderers.ICON_5F.getWidth(text);
                }
                case ICON_6F -> {
                    return FontRenderers.ICON_6F.getWidth(text);
                }
                case ICON_7F -> {
                    return FontRenderers.ICON_7F.getWidth(text);
                }
                case ICON_8F -> {
                    return FontRenderers.ICON_8F.getWidth(text);
                }
                case ICON_9F -> {
                    return FontRenderers.ICON_9F.getWidth(text);
                }
                case ICON_10F -> {
                    return FontRenderers.ICON_10F.getWidth(text);
                }
                case ICON_12F -> {
                    return FontRenderers.ICON_12F.getWidth(text);
                }
                case ICON_14F -> {
                    return FontRenderers.ICON_14F.getWidth(text);
                }
                case ICON_15F -> {
                    return FontRenderers.ICON_15F.getWidth(text);
                }
                case ICON_16F -> {
                    return FontRenderers.ICON_16F.getWidth(text);
                }
                case ICON_18F -> {
                    return FontRenderers.ICON_18F.getWidth(text);
                }
                case ICON_20F -> {
                    return FontRenderers.ICON_20F.getWidth(text);
                }
                case ICON_21F -> {
                    return FontRenderers.ICON_21F.getWidth(text);
                }
                case ICON_24F -> {
                    return FontRenderers.ICON_24F.getWidth(text);
                }
                case ICON_28F -> {
                    return FontRenderers.ICON_28F.getWidth(text);
                }
                case ICON_32F -> {
                    return FontRenderers.ICON_32F.getWidth(text);
                }
            }
            return FontRenderers.ICON_10F.getWidth(text);
        } else {
            switch (fontScales) {
                case SMOOTH_3F -> {
                    return FontRenderers.SMOOTH_3F.getWidth(text);
                }
                case SMOOTH_4F -> {
                    return FontRenderers.SMOOTH_4F.getWidth(text);
                }
                case SMOOTH_5F -> {
                    return FontRenderers.SMOOTH_5F.getWidth(text);
                }
                case SMOOTH_6F -> {
                    return FontRenderers.SMOOTH_6F.getWidth(text);
                }
                case SMOOTH_7F -> {
                    return FontRenderers.SMOOTH_7F.getWidth(text);
                }
                case SMOOTH_8F -> {
                    return FontRenderers.SMOOTH_8F.getWidth(text);
                }
                case SMOOTH_9F -> {
                    return FontRenderers.SMOOTH_9F.getWidth(text);
                }
                case SMOOTH_10F -> {
                    return FontRenderers.SMOOTH_10F.getWidth(text);
                }
                case SMOOTH_12F -> {
                    return FontRenderers.SMOOTH_12F.getWidth(text);
                }
                case SMOOTH_14F -> {
                    return FontRenderers.SMOOTH_14F.getWidth(text);
                }
                case SMOOTH_15F -> {
                    return FontRenderers.SMOOTH_15F.getWidth(text);
                }
                case SMOOTH_16F -> {
                    return FontRenderers.SMOOTH_16F.getWidth(text);
                }
                case SMOOTH_18F -> {
                    return FontRenderers.SMOOTH_18F.getWidth(text);
                }
                case SMOOTH_20F -> {
                    return FontRenderers.SMOOTH_20F.getWidth(text);
                }
                case SMOOTH_21F -> {
                    return FontRenderers.SMOOTH_21F.getWidth(text);
                }
                case SMOOTH_24F -> {
                    return FontRenderers.SMOOTH_24F.getWidth(text);
                }
                case SMOOTH_28F -> {
                    return FontRenderers.SMOOTH_28F.getWidth(text);
                }
                case SMOOTH_32F -> {
                    return FontRenderers.SMOOTH_32F.getWidth(text);
                }
            }
            return FontRenderers.SMOOTH_10F.getWidth(text);
        }
    }

    public static double getHeight(FontSize size) {
        Fonts fontType = (Fonts) Client.INSTANCE.font.getValue();
        return getHeight(size, fontType, "Get Vergence Get Good");
    }

    public static double getHeight(FontSize size, Fonts fonts) {
        return getHeight(size, fonts, "Get Vergence Get Good");
    }

    public static double getHeight(FontSize size, Fonts fontType, String text) {
        if (Client.INSTANCE == null || !LOADED) {return 0;}

        FontScales fontScales = asFontScales((Client.UIScales) Client.INSTANCE.UIScale.getValue(), size, fontType);
        if (fontType.equals(Fonts.Sans)) {
            switch (fontScales) {
                case SANS_3F -> {
                    return FontRenderers.SANS_3F.getFontHeight(text);
                }
                case SANS_4F -> {
                    return FontRenderers.SANS_4F.getFontHeight(text);
                }
                case SANS_5F -> {
                    return FontRenderers.SANS_5F.getFontHeight(text);
                }
                case SANS_6F -> {
                    return FontRenderers.SANS_6F.getFontHeight(text);
                }
                case SANS_7F -> {
                    return FontRenderers.SANS_7F.getFontHeight(text);
                }
                case SANS_8F -> {
                    return FontRenderers.SANS_8F.getFontHeight(text);
                }
                case SANS_9F -> {
                    return FontRenderers.SANS_9F.getFontHeight(text);
                }
                case SANS_10F -> {
                    return FontRenderers.SANS_10F.getFontHeight(text);
                }
                case SANS_12F -> {
                    return FontRenderers.SANS_12F.getFontHeight(text);
                }
                case SANS_14F -> {
                    return FontRenderers.SANS_14F.getFontHeight(text);
                }
                case SANS_15F -> {
                    return FontRenderers.SANS_15F.getFontHeight(text);
                }
                case SANS_16F -> {
                    return FontRenderers.SANS_16F.getFontHeight(text);
                }
                case SANS_18F -> {
                    return FontRenderers.SANS_18F.getFontHeight(text);
                }
                case SANS_20F -> {
                    return FontRenderers.SANS_20F.getFontHeight(text);
                }
                case SANS_21F -> {
                    return FontRenderers.SANS_21F.getFontHeight(text);
                }
                case SANS_24F -> {
                    return FontRenderers.SANS_24F.getFontHeight(text);
                }
                case SANS_28F -> {
                    return FontRenderers.SANS_28F.getFontHeight(text);
                }
                case SANS_32F -> {
                    return FontRenderers.SANS_32F.getFontHeight(text);
                }
            }
            return FontRenderers.SANS_10F.getFontHeight(text);
        }
        else if (fontType.equals(Fonts.RHR)) {
            switch (fontScales) {
                case RHR_3F -> {
                    return FontRenderers.RHR_3F.getFontHeight(text);
                }
                case RHR_4F -> {
                    return FontRenderers.RHR_4F.getFontHeight(text);
                }
                case RHR_5F -> {
                    return FontRenderers.RHR_5F.getFontHeight(text);
                }
                case RHR_6F -> {
                    return FontRenderers.RHR_6F.getFontHeight(text);
                }
                case RHR_7F -> {
                    return FontRenderers.RHR_7F.getFontHeight(text);
                }
                case RHR_8F -> {
                    return FontRenderers.RHR_8F.getFontHeight(text);
                }
                case RHR_9F -> {
                    return FontRenderers.RHR_9F.getFontHeight(text);
                }
                case RHR_10F -> {
                    return FontRenderers.RHR_10F.getFontHeight(text);
                }
                case RHR_12F -> {
                    return FontRenderers.RHR_12F.getFontHeight(text);
                }
                case RHR_14F -> {
                    return FontRenderers.RHR_14F.getFontHeight(text);
                }
                case RHR_15F -> {
                    return FontRenderers.RHR_15F.getFontHeight(text);
                }
                case RHR_16F -> {
                    return FontRenderers.RHR_16F.getFontHeight(text);
                }
                case RHR_18F -> {
                    return FontRenderers.RHR_18F.getFontHeight(text);
                }
                case RHR_20F -> {
                    return FontRenderers.RHR_20F.getFontHeight(text);
                }
                case RHR_21F -> {
                    return FontRenderers.RHR_21F.getFontHeight(text);
                }
                case RHR_24F -> {
                    return FontRenderers.RHR_24F.getFontHeight(text);
                }
                case RHR_28F -> {
                    return FontRenderers.RHR_28F.getFontHeight(text);
                }
                case RHR_32F -> {
                    return FontRenderers.RHR_32F.getFontHeight(text);
                }
            }
            return FontRenderers.RHR_10F.getFontHeight(text);
        }
        else if (fontType.equals(Fonts.Icon)) {
            switch (fontScales) {
                case ICON_3F -> {
                    return FontRenderers.ICON_3F.getFontHeight(text);
                }
                case ICON_4F -> {
                    return FontRenderers.ICON_4F.getFontHeight(text);
                }
                case ICON_5F -> {
                    return FontRenderers.ICON_5F.getFontHeight(text);
                }
                case ICON_6F -> {
                    return FontRenderers.ICON_6F.getFontHeight(text);
                }
                case ICON_7F -> {
                    return FontRenderers.ICON_7F.getFontHeight(text);
                }
                case ICON_8F -> {
                    return FontRenderers.ICON_8F.getFontHeight(text);
                }
                case ICON_9F -> {
                    return FontRenderers.ICON_9F.getFontHeight(text);
                }
                case ICON_10F -> {
                    return FontRenderers.ICON_10F.getFontHeight(text);
                }
                case ICON_12F -> {
                    return FontRenderers.ICON_12F.getFontHeight(text);
                }
                case ICON_14F -> {
                    return FontRenderers.ICON_14F.getFontHeight(text);
                }
                case ICON_15F -> {
                    return FontRenderers.ICON_15F.getFontHeight(text);
                }
                case ICON_16F -> {
                    return FontRenderers.ICON_16F.getFontHeight(text);
                }
                case ICON_18F -> {
                    return FontRenderers.ICON_18F.getFontHeight(text);
                }
                case ICON_20F -> {
                    return FontRenderers.ICON_20F.getFontHeight(text);
                }
                case ICON_21F -> {
                    return FontRenderers.ICON_21F.getFontHeight(text);
                }
                case ICON_24F -> {
                    return FontRenderers.ICON_24F.getFontHeight(text);
                }
                case ICON_28F -> {
                    return FontRenderers.ICON_28F.getFontHeight(text);
                }
                case ICON_32F -> {
                    return FontRenderers.ICON_32F.getFontHeight(text);
                }
            }
            return FontRenderers.ICON_10F.getFontHeight(text);
        } else {
            switch (fontScales) {
                case SMOOTH_3F -> {
                    return FontRenderers.SMOOTH_3F.getFontHeight(text);
                }
                case SMOOTH_4F -> {
                    return FontRenderers.SMOOTH_4F.getFontHeight(text);
                }
                case SMOOTH_5F -> {
                    return FontRenderers.SMOOTH_5F.getFontHeight(text);
                }
                case SMOOTH_6F -> {
                    return FontRenderers.SMOOTH_6F.getFontHeight(text);
                }
                case SMOOTH_7F -> {
                    return FontRenderers.SMOOTH_7F.getFontHeight(text);
                }
                case SMOOTH_8F -> {
                    return FontRenderers.SMOOTH_8F.getFontHeight(text);
                }
                case SMOOTH_9F -> {
                    return FontRenderers.SMOOTH_9F.getFontHeight(text);
                }
                case SMOOTH_10F -> {
                    return FontRenderers.SMOOTH_10F.getFontHeight(text);
                }
                case SMOOTH_12F -> {
                    return FontRenderers.SMOOTH_12F.getFontHeight(text);
                }
                case SMOOTH_14F -> {
                    return FontRenderers.SMOOTH_14F.getFontHeight(text);
                }
                case SMOOTH_15F -> {
                    return FontRenderers.SMOOTH_15F.getFontHeight(text);
                }
                case SMOOTH_16F -> {
                    return FontRenderers.SMOOTH_16F.getFontHeight(text);
                }
                case SMOOTH_18F -> {
                    return FontRenderers.SMOOTH_18F.getFontHeight(text);
                }
                case SMOOTH_20F -> {
                    return FontRenderers.SMOOTH_20F.getFontHeight(text);
                }
                case SMOOTH_21F -> {
                    return FontRenderers.SMOOTH_21F.getFontHeight(text);
                }
                case SMOOTH_24F -> {
                    return FontRenderers.SMOOTH_24F.getFontHeight(text);
                }
                case SMOOTH_28F -> {
                    return FontRenderers.SMOOTH_28F.getFontHeight(text);
                }
                case SMOOTH_32F -> {
                    return FontRenderers.SMOOTH_32F.getFontHeight(text);
                }
            }
            return FontRenderers.SMOOTH_10F.getFontHeight(text);
        }
    }

    public static void drawText(DrawContext context, String text, int x, int y, Color color, FontSize size, boolean shadow) {
        drawText(context, text, x, y, color.getRGB(), size, shadow);
    }

    public static void drawText(DrawContext context, String text, double x, double y, Color color, FontSize size, boolean shadow) {
        drawText(context, text, x, y, color.getRGB(), size, shadow);
    }

    public static void drawText(DrawContext context, String text, double x, double y, Color color, FontSize size) {
        drawText(context, text, x, y, color.getRGB(), size, false);
    }

    public static void drawText(MatrixStack matrixStack, String text, double x, double y, Color color, FontSize size) {
        drawText(matrixStack, text, x, y, color, size, false);
    }

    public static void drawText(MatrixStack matrixStack, String text, double x, double y, Color color, FontSize size, boolean shadow) {
        Fonts fontType = Fonts.Sans;
        if (Client.INSTANCE != null) {
            fontType = (Fonts) Client.INSTANCE.font.getValue();
        }
        drawText(matrixStack, text, x, y, color.getRGB(), size, fontType, shadow);
    }

    public static void drawText(DrawContext context, String text, double x, double y, int color, FontSize size) {
        drawText(context, text, x, y, color, size, false);
    }

    public static void drawText(DrawContext context, String text, double x, double y, int color, FontSize size, boolean shadow) {
        Fonts fontType = Fonts.Sans;
        if (Client.INSTANCE != null) {
            fontType = (Fonts) Client.INSTANCE.font.getValue();
        }
        drawText(context.getMatrices(), text, x, y, color, size, fontType, shadow);
    }

    public static void drawIcon(DrawContext context, String text, double x, double y, Color color, FontSize size) {
        drawText(context.getMatrices(), text, x, y, color.getRGB(), size, Fonts.Icon, false);
    }

    public static void drawIcon(DrawContext context, String text, double x, double y, int color, FontSize size) {
        drawText(context.getMatrices(), text, x, y, color, size, Fonts.Icon, false);
    }

    public static void drawIcon(MatrixStack matrixStack, String text, double x, double y, Color color, FontSize size) {
        drawText(matrixStack, text, x, y, color.getRGB(), size, Fonts.Icon, false);
    }

    public static void drawIcon(MatrixStack matrixStack, String text, double x, double y, int color, FontSize size) {
        drawText(matrixStack, text, x, y, color, size, Fonts.Icon, false);
    }

    public static void drawText(MatrixStack matrixStack, String text, double x, double y, int color, FontSize size, Fonts fontType, boolean shadow) {
        if (Client.INSTANCE == null || !LOADED) {return ;}

        FontScales fontScales = asFontScales((Client.UIScales) Client.INSTANCE.UIScale.getValue(), size, fontType);
        if (fontType.equals(Fonts.Sans)) {
            switch (fontScales) {
                case SANS_3F -> FontRenderers.SANS_3F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_4F -> FontRenderers.SANS_4F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_5F -> FontRenderers.SANS_5F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_6F -> FontRenderers.SANS_6F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_7F -> FontRenderers.SANS_7F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_8F -> FontRenderers.SANS_8F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_9F -> FontRenderers.SANS_9F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_10F -> FontRenderers.SANS_10F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_12F -> FontRenderers.SANS_12F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_14F -> FontRenderers.SANS_14F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_15F -> FontRenderers.SANS_15F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_16F -> FontRenderers.SANS_16F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_18F -> FontRenderers.SANS_18F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_20F -> FontRenderers.SANS_20F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_21F -> FontRenderers.SANS_21F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_24F -> FontRenderers.SANS_24F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_28F -> FontRenderers.SANS_28F.drawString(matrixStack, text, x, y, color, shadow);
                case SANS_32F -> FontRenderers.SANS_32F.drawString(matrixStack, text, x, y, color, shadow);
            }
        }
        else if (fontType.equals(Fonts.RHR)) {
            switch (fontScales) {
                case RHR_3F -> FontRenderers.RHR_3F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_4F -> FontRenderers.RHR_4F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_5F -> FontRenderers.RHR_5F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_6F -> FontRenderers.RHR_6F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_7F -> FontRenderers.RHR_7F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_8F -> FontRenderers.RHR_8F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_9F -> FontRenderers.RHR_9F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_10F -> FontRenderers.RHR_10F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_12F -> FontRenderers.RHR_12F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_14F -> FontRenderers.RHR_14F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_15F -> FontRenderers.RHR_15F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_16F -> FontRenderers.RHR_16F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_18F -> FontRenderers.RHR_18F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_20F -> FontRenderers.RHR_20F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_21F -> FontRenderers.RHR_21F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_24F -> FontRenderers.RHR_24F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_28F -> FontRenderers.RHR_28F.drawString(matrixStack, text, x, y, color, shadow);
                case RHR_32F -> FontRenderers.RHR_32F.drawString(matrixStack, text, x, y, color, shadow);
            }
        }
        else if (fontType.equals(Fonts.Icon)) {
            switch (fontScales) {
                case ICON_3F -> FontRenderers.ICON_3F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_4F -> FontRenderers.ICON_4F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_5F -> FontRenderers.ICON_5F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_6F -> FontRenderers.ICON_6F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_7F -> FontRenderers.ICON_7F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_8F -> FontRenderers.ICON_8F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_9F -> FontRenderers.ICON_9F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_10F -> FontRenderers.ICON_10F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_12F -> FontRenderers.ICON_12F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_14F -> FontRenderers.ICON_14F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_15F -> FontRenderers.ICON_15F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_16F -> FontRenderers.ICON_16F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_18F -> FontRenderers.ICON_18F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_20F -> FontRenderers.ICON_20F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_21F -> FontRenderers.ICON_21F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_24F -> FontRenderers.ICON_24F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_28F -> FontRenderers.ICON_28F.drawString(matrixStack, text, x, y, color, shadow);
                case ICON_32F -> FontRenderers.ICON_32F.drawString(matrixStack, text, x, y, color, shadow);
            }
        } else {
            switch (fontScales) {
                case SMOOTH_3F -> FontRenderers.SMOOTH_3F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_4F -> FontRenderers.SMOOTH_4F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_5F -> FontRenderers.SMOOTH_5F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_6F -> FontRenderers.SMOOTH_6F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_7F -> FontRenderers.SMOOTH_7F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_8F -> FontRenderers.SMOOTH_8F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_9F -> FontRenderers.SMOOTH_9F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_10F -> FontRenderers.SMOOTH_10F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_12F -> FontRenderers.SMOOTH_12F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_14F -> FontRenderers.SMOOTH_14F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_15F -> FontRenderers.SMOOTH_15F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_16F -> FontRenderers.SMOOTH_16F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_18F -> FontRenderers.SMOOTH_18F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_20F -> FontRenderers.SMOOTH_20F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_21F -> FontRenderers.SMOOTH_21F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_24F -> FontRenderers.SMOOTH_24F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_28F -> FontRenderers.SMOOTH_28F.drawString(matrixStack, text, x, y, color, shadow);
                case SMOOTH_32F -> FontRenderers.SMOOTH_32F.drawString(matrixStack, text, x, y, color, shadow);
            }
        }
    }

    public static void drawTextWithAlign(DrawContext context, String text, double x, double y, double ex, double ey, Aligns align, Color color, FontSize size) {
        Fonts fontType = Fonts.Sans;
        if (Client.INSTANCE != null) {
            fontType = (Fonts) Client.INSTANCE.font.getValue();
        }
        drawTextWithAlign(context, text, x, y, ex, ey, align, color.getRGB(), size, fontType, false);
    }

    public static void drawTextWithAlign(DrawContext context, String text, double x, double y, double ex, double ey, Aligns align, int color, FontSize size) {
        Fonts fontType = Fonts.Sans;
        if (Client.INSTANCE != null) {
            fontType = (Fonts) Client.INSTANCE.font.getValue();
        }
        drawTextWithAlign(context, text, x, y, ex, ey, align, color, size, fontType, false);
    }

    public static void drawTextWithAlign(DrawContext context, String text, double x, double y, double ex, double ey, Aligns align, Color color, FontSize size, boolean shadow) {
        Fonts fontType = Fonts.Sans;
        if (Client.INSTANCE != null) {
            fontType = (Fonts) Client.INSTANCE.font.getValue();
        }
        drawTextWithAlign(context, text, x, y, ex, ey, align, color.getRGB(), size, fontType, shadow);
    }

    public static void drawIconWithAlign(DrawContext context, String text, double originalX, double originalY, double originalEx, double originalEy, Aligns align, Color color, FontSize size) {
        drawIconWithAlign(context, text, originalX, originalY, originalEx, originalEy, align, color.getRGB(), size);
    }

    public static void drawIconWithAlign(DrawContext context, String text, double originalX, double originalY, double originalEx, double originalEy, Aligns align, int color, FontSize size) {
        drawTextWithAlign(context, text, originalX, originalY, originalEx, originalEy, align, color, size, Fonts.Icon, false);
    }

    public static void drawTextWithAlign(DrawContext context, String text, double originalX, double originalY, double originalEx, double originalEy, Aligns align, int color, FontSize size, Fonts fontType, boolean shadow) {
        if (Client.INSTANCE == null || !LOADED) {
            return;
        }
        double textWidth = getWidth(size, fontType, text) * getScaleFactor();
        double textHeight = getHeight(size, fontType, text) * getScaleFactor();

        double scaleFactor = getScaleFactor();
        double startX = originalX * scaleFactor;
        double startY = originalY * scaleFactor;
        switch (align) {
            case CENTER:
                startX = ((originalX + originalEx) / 2 - textWidth / 2) * scaleFactor;
                startY = ((originalY + originalEy) / 2 - textHeight / 2) * scaleFactor;
                break;

            case RIGHT:
                startX = (originalEx - textWidth) * scaleFactor;
                startY = ((originalY + originalEy) / 2 - textHeight / 2) * scaleFactor;
                break;

            case LEFT:
                startX = originalX * scaleFactor;
                startY = ((originalY + originalEy) / 2 - textHeight / 2) * scaleFactor;
                break;

            case TOP:
                startX = ((originalX + originalEx) / 2 - textWidth / 2) * scaleFactor;
                startY = originalY * scaleFactor;
                break;
            case BOTTOM:
                startX = ((originalX + originalEx) / 2 - textWidth / 2) * scaleFactor;
                startY = (originalEy - textHeight) * scaleFactor;
                break;
            case LEFT_TOP:
                startX = originalX * scaleFactor;
                startY = originalY * scaleFactor;
                break;
            case LEFT_BOTTOM:
                startX = originalX * scaleFactor;
                startY = (originalEy - textHeight) * scaleFactor;
                break;
            case RIGHT_TOP:
                startX = (originalEx - textWidth) * scaleFactor;
                startY = originalY * scaleFactor;
                break;
            case RIGHT_BOTTOM:
                startX = (originalEx - textWidth) * scaleFactor;
                startY = (originalEy - textHeight) * scaleFactor;
                break;
        }
        if (fontType.equals(Fonts.Icon)) {
            drawIcon(context, text, startX, startY, color, size);
        } else {
            drawText(context, text, startX, startY, color, size, shadow);
        }
    }

    public static void drawTextWithAlign(MatrixStack matrixStack, String text, double originalX, double originalY, double originalEx, double originalEy, Aligns align, Color color, FontSize size) {
        Fonts fontType = Fonts.Sans;
        if (Client.INSTANCE != null) {
            fontType = (Fonts) Client.INSTANCE.font.getValue();
        }
        drawTextWithAlign(matrixStack, text, originalX, originalY, originalEx, originalEy, align, color, size, fontType, false);
    }

    public static void drawTextWithAlign(MatrixStack matrixStack, String text, double originalX, double originalY, double originalEx, double originalEy, Aligns align, Color color, FontSize size, Fonts fontType, boolean shadow) {
        if (Client.INSTANCE == null || !LOADED) {
            return;
        }
        double textWidth = getWidth(size, fontType, text) * getScaleFactor();
        double textHeight = getHeight(size, fontType, text) * getScaleFactor();

        double scaleFactor = getScaleFactor();
        double startX = originalX * scaleFactor;
        double startY = originalY * scaleFactor;
        switch (align) {
            case CENTER:
                startX = ((originalX + originalEx) / 2 - textWidth / 2) * scaleFactor;
                startY = ((originalY + originalEy) / 2 - textHeight / 2) * scaleFactor;
                break;

            case RIGHT:
                startX = (originalEx - textWidth) * scaleFactor;
                startY = ((originalY + originalEy) / 2 - textHeight / 2) * scaleFactor;
                break;

            case LEFT:
                startX = originalX * scaleFactor;
                startY = ((originalY + originalEy) / 2 - textHeight / 2) * scaleFactor;
                break;

            case TOP:
                startX = ((originalX + originalEx) / 2 - textWidth / 2) * scaleFactor;
                startY = originalY * scaleFactor;
                break;
            case BOTTOM:
                startX = ((originalX + originalEx) / 2 - textWidth / 2) * scaleFactor;
                startY = (originalEy - textHeight) * scaleFactor;
                break;
            case LEFT_TOP:
                startX = originalX * scaleFactor;
                startY = originalY * scaleFactor;
                break;
            case LEFT_BOTTOM:
                startX = originalX * scaleFactor;
                startY = (originalEy - textHeight) * scaleFactor;
                break;
            case RIGHT_TOP:
                startX = (originalEx - textWidth) * scaleFactor;
                startY = originalY * scaleFactor;
                break;
            case RIGHT_BOTTOM:
                startX = (originalEx - textWidth) * scaleFactor;
                startY = (originalEy - textHeight) * scaleFactor;
                break;
        }
        if (fontType.equals(Fonts.Icon)) {
            drawIcon(matrixStack, text, startX, startY, color, size);
        } else {
            drawText(matrixStack, text, startX, startY, color, size, shadow);
        }
    }

    public static double getScaleFactor() {
        if (Client.INSTANCE == null || Client.INSTANCE.UIScale == null) {
            return 1.0;
        }
        return switch ((Client.UIScales) Client.INSTANCE.UIScale.getValue()) {
            case X50 -> 0.5;
            case X150 -> 1.5;
            case X200 -> 2.0;
            default -> 1.0;
        };
    }
}
