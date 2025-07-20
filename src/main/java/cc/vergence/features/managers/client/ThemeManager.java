package cc.vergence.features.managers.client;

import cc.vergence.Vergence;
import cc.vergence.features.themes.DefaultTheme;
import cc.vergence.features.themes.Theme;

public class ThemeManager {
    private static Theme currentTheme = null;
    public Theme defaultTheme;

    public ThemeManager() {
        defaultTheme = new DefaultTheme();
        currentTheme = defaultTheme;
    }

    public void loadTheme(Theme theme) {
        if (theme == defaultTheme) {
            Vergence.CONSOLE.logWarn("Theme unload failed. It's default theme.");
        }
        currentTheme = theme;
    }

    public void unloadTheme() {
        Vergence.CONSOLE.logInfo("unloading theme ...");
        currentTheme = new DefaultTheme();
        Vergence.CONSOLE.logInfo("Theme unloaded, now the theme is default theme.");
    }

    public Theme getTheme() {
        return currentTheme;
    }
}
