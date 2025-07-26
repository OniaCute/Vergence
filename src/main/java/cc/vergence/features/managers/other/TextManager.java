package cc.vergence.features.managers.other;

import cc.vergence.Vergence;
import cc.vergence.features.enums.client.Languages;
import cc.vergence.features.languages.impl.ChineseLanguage;
import cc.vergence.features.languages.impl.EnglishLanguage;
import cc.vergence.features.languages.Language;
import cc.vergence.features.languages.impl.RussianLanguage;
import cc.vergence.modules.client.Client;

import java.util.ArrayList;
import java.util.HashMap;

public class TextManager {
    public static HashMap<Languages, Language> languageMap = new HashMap<>();

    public TextManager() {
        addLanguage(Languages.English, new EnglishLanguage());
        addLanguage(Languages.Chinese, new ChineseLanguage());
        addLanguage(Languages.Russian, new RussianLanguage());
    }

    public void addLanguage(Languages languages, Language language) {
        languageMap.put(languages, language);
    }

    public String get(String key) {
        String result = languageMap.get(Client.INSTANCE == null ? Languages.English : (Languages) Client.INSTANCE.language.getValue()).get(key);
        if (result == null) {
            Vergence.CONSOLE.logWarn("[Text Manager] Language Text not found, text key: " + key);
            return key;
        }
        return result;
    }
}
