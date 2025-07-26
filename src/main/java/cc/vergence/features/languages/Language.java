package cc.vergence.features.languages;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class Language {
    protected Map<String, String> languageBase = new HashMap<>();

    public abstract void load();

    @Nullable
    public String get(String key) {
        return languageBase.get(key);
    }
}
