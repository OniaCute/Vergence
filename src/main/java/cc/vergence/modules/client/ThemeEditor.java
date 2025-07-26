package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.features.themes.Theme;
import cc.vergence.modules.Module;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThemeEditor extends Module {
    public static ThemeEditor INSTANCE;

    public ThemeEditor() {
        super("ThemeEditor", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<String> displayNameOption = addOption(new TextOption("DisplayName", "New Theme"));
    public Option<String> descriptionOption = addOption(new TextOption("Description", "A vergence client theme."));
    public Option<String> authorsOption = addOption(new TextOption("Author", "{player}|other"));
    public Option<Color> mainColorOption = addOption(new ColorOption("MainColor", Vergence.THEME.getTheme().getMainColor()));
    public Option<Color> mainPageBackgroundColorOption = addOption(new ColorOption("MainPageBackgroundColor", new Color(232, 232, 232, 245)));
    public Option<Color> mainPageSplitLineColorOption = addOption(new ColorOption("MainPageSplitLineColor", new Color(246, 246, 246)));
    public Option<Color> categoryBackgroundColorOption = addOption(new ColorOption("CategoryBackgroundColor", new Color(238, 238, 238, 26)));
    public Option<Color> categoryTextColorOption = addOption(new ColorOption("CategoryTextColor", new Color(0, 0, 0)));
    public Option<Color> categoryHoveredBackgroundColorOption = addOption(new ColorOption("CategoryHoveredBackgroundColor", new Color(238, 108, 255, 228)));
    public Option<Color> categoryHoveredTextColorOption = addOption(new ColorOption("CategoryHoveredTextColor", new Color(255, 255, 255)));
    public Option<Color> categoryCurrentBackgroundColorOption = addOption(new ColorOption("CategoryCurrentBackgroundColor", new Color(230, 118, 255, 247)));
    public Option<Color> categoryCurrentTextColorOption = addOption(new ColorOption("CategoryCurrentTextColor", new Color(255, 255, 255)));
    public Option<Color> moduleBackgroundColorOption = addOption(new ColorOption("ModuleBackgroundColor", new Color(238, 238, 238)));
    public Option<Color> moduleEnabledBackgroundColorOption = addOption(new ColorOption("ModuleEnabledBackgroundColor", new Color(248, 141, 255)));
    public Option<Color> moduleTextColorOption = addOption(new ColorOption("ModuleTextColor", new Color(21, 21, 21)));
    public Option<Color> moduleEnabledTextColorOption = addOption(new ColorOption("ModuleEnabledTextColor", new Color(255, 255, 255)));
    public Option<Color> moduleHoveredBackgroundColorOption = addOption(new ColorOption("ModuleHoveredBackgroundColor", new Color(236, 236, 236)));
    public Option<Color> moduleHoveredTextColorOption = addOption(new ColorOption("ModuleHoveredTextColor", new Color(0, 0, 0)));
    public Option<Color> moduleGearTextColorOption = addOption(new ColorOption("ModuleGearTextColor", new Color(40, 40, 40)));
    public Option<Color> moduleHoveredGearTextColorOption = addOption(new ColorOption("ModuleHoveredGearTextColor", new Color(28, 28, 28)));
    public Option<Color> moduleEnabledGearTextColorOption = addOption(new ColorOption("ModuleEnabledGearTextColor", new Color(255, 255, 255)));
    public Option<Color> notificationBackgroundColorOption = addOption(new ColorOption("NotificationBackgroundColor", new Color(227, 227, 227, 182)));
    public Option<Color> notificationTextColorOption = addOption(new ColorOption("NotificationTextColor", new Color(255, 255, 255)));
    public Option<Color> notificationHighlightTextColorOption = addOption(new ColorOption("NotificationHighlightTextColor", new Color(255, 127, 226)));
    public Option<Color> buttonCircleColorOption = addOption(new ColorOption("ButtonCircleColor", new Color(232, 232, 232)));
    public Option<Color> buttonInlineColorOption = addOption(new ColorOption("ButtonInlineColor", new Color(243, 243, 243)));
    public Option<Color> buttonEnabledCircleColorOption = addOption(new ColorOption("ButtonEnabledCircleColor", new Color(250, 142, 255)));
    public Option<Color> buttonEnabledInlineColorOption = addOption(new ColorOption("ButtonEnabledInlineColor", new Color(255, 130, 250)));
    public Option<Color> buttonBackgroundColorOption = addOption(new ColorOption("ButtonBackgroundColor", new Color(222, 222, 222)));
    public Option<Color> buttonHoveredBackgroundColorOption = addOption(new ColorOption("ButtonHoveredBackgroundColor", new Color(245, 195, 255, 255)));
    public Option<Color> buttonEnabledBackgroundColorOption = addOption(new ColorOption("ButtonEnabledBackgroundColor", new Color(244, 181, 255, 255)));
    public Option<Color> buttonHoveredInlineColorOption = addOption(new ColorOption("ButtonHoveredInlineColor", new Color(243, 147, 255, 255)));
    public Option<Color> buttonHoveredCircleColorOption = addOption(new ColorOption("ButtonHoveredCircleColor", new Color(241, 162, 255, 255)));
    public Option<Color> sliderBackgroundColorOption = addOption(new ColorOption("SliderBackgroundColor", new Color(222, 222, 222)));
    public Option<Color> sliderHoveredBackgroundColorOption = addOption(new ColorOption("SliderHoveredBackgroundColor", new Color(248, 209, 255, 255)));
    public Option<Color> sliderCircleColorOption = addOption(new ColorOption("SliderCircleColor", new Color(232, 232, 232)));
    public Option<Color> sliderInlineColorOption = addOption(new ColorOption("SliderInlineColor", new Color(243, 243, 243)));
    public Option<Color> sliderHoveredCircleColorOption = addOption(new ColorOption("SliderHoveredCircleColor", new Color(240, 147, 255, 255)));
    public Option<Color> sliderHoveredInlineColorOption = addOption(new ColorOption("SliderHoveredInlineColor", new Color(233, 141, 255, 255)));
    public Option<Color> sliderClickedCircleColorOption = addOption(new ColorOption("SliderClickedCircleColor", new Color(242, 156, 255, 255)));
    public Option<Color> sliderClickedInlineColorOption = addOption(new ColorOption("SliderClickedInlineColor", new Color(239, 135, 255, 255)));
    public Option<Color> sliderValuedBackgroundColorOption = addOption(new ColorOption("SliderValuedBackgroundColor", new Color(246, 142, 255)));
    public Option<Color> optionsTextColorOption = addOption(new ColorOption("OptionsTextColor", new Color(19, 19, 19)));
    public Option<Color> optionAreaBackgroundColorOption = addOption(new ColorOption("OptionAreaBackgroundColor", new Color(255, 255, 255)));
    public Option<Color> hudEditorTipsTextColorOption = addOption(new ColorOption("HudEditorTipsTextColor", new Color(232, 232, 232)));
    public Option<Color> hoverComponentBackgroundColorOption = addOption(new ColorOption("HoverComponentBackgroundColor", new Color(255, 255, 255, 255)));
    public Option<Color> hoverComponentTextColorOption = addOption(new ColorOption("HoverComponentTextColor", new Color(255, 255, 255)));
    public Option<Color> descriptionBackgroundColorOption = addOption(new ColorOption("DescriptionBackgroundColor", new Color(255, 255, 255, 242)));
    public Option<Color> descriptionTextColorOption = addOption(new ColorOption("DescriptionTextColor", new Color(16, 16, 16)));
    public Option<Color> chatPrefixColorOption = addOption(new ColorOption("ChatPrefixColor", new Color(237, 151, 255)));
    public Option<Color> topBarBackgroundColorOption = addOption(new ColorOption("TopBarBackgroundColor", new Color(239, 239, 239, 239)));
    public Option<Color> topBarResearchFrameBackgroundColorOption = addOption(new ColorOption("TopBarResearchFrameBackgroundColor", new Color(229, 229, 229, 255)));
    public Option<Color> topBarResearchFrameTextColorOption = addOption(new ColorOption("TopBarResearchFrameTextColor", new Color(49, 49, 49)));
    public Option<Color> topBarHoveredResearchFrameBackgroundColorOption = addOption(new ColorOption("TopBarHoveredResearchFrameBackgroundColor", new Color(231, 231, 231)));
    public Option<Color> topBarHoveredResearchFrameTextColorOption = addOption(new ColorOption("TopBarHoveredResearchFrameTextColor", new Color(21, 21, 21)));
    public Option<Color> topBarFocusedResearchFrameBackgroundColorOption = addOption(new ColorOption("TopBarFocusedResearchFrameBackgroundColor", new Color(241, 241, 241)));
    public Option<Color> topBarFocusedResearchFrameTextColorOption = addOption(new ColorOption("TopBarFocusedResearchFrameTextColor", new Color(9, 9, 9)));
    public Option<Color> topbarButtonBackgroundColorOption = addOption(new ColorOption("TopbarButtonBackgroundColor", new Color(236, 236, 236)));
    public Option<Color> topbarHoveredButtonBackgroundColorOption = addOption(new ColorOption("TopbarHoveredButtonBackgroundColor", new Color(255, 200, 246)));
    public Option<Color> topbarButtonIconColorOption = addOption(new ColorOption("TopbarButtonIconColor", new Color(31, 31, 31, 221)));
    public Option<Color> topbarHoveredButtonIconColorOption = addOption(new ColorOption("TopbarHoveredButtonIconColor", new Color(255, 119, 253)));
    public Option<Color> inputFrameBackgroundColorOption = addOption(new ColorOption("InputFrameBackgroundColor", new Color(241, 241, 241)));
    public Option<Color> inputFrameTextColorOption = addOption(new ColorOption("InputFrameTextColor", new Color(44, 44, 44)));
    public Option<Color> inputFrameHoveredBackgroundColorOption = addOption(new ColorOption("InputFrameHoveredBackgroundColor", new Color(246, 246, 246)));
    public Option<Color> inputFrameHoveredTextColorOption = addOption(new ColorOption("InputFrameHoveredTextColor", new Color(243, 111, 255)));
    public Option<Color> choicesBackgroundColorOption = addOption(new ColorOption("ChoicesBackgroundColor", new Color(241, 241, 241)));
    public Option<Color> choicesTextColorOption = addOption(new ColorOption("ChoicesTextColor", new Color(44, 44, 44)));
    public Option<Color> choicesHoveredBackgroundColorOption = addOption(new ColorOption("ChoicesHoveredBackgroundColor", new Color(253, 228, 255)));
    public Option<Color> choicesHoveredTextColorOption = addOption(new ColorOption("ChoicesHoveredTextColor", new Color(245, 140, 255)));
    public Option<Color> choicesDefaultBackgroundColorOption = addOption(new ColorOption("ChoicesDefaultBackgroundColor", new Color(238, 238, 238)));
    public Option<Color> choicesDefaultTextColorOption = addOption(new ColorOption("ChoicesDefaultTextColor", new Color(44, 44, 44)));
    public Option<Color> choicesEnabledBackgroundColorOption = addOption(new ColorOption("ChoicesEnabledBackgroundColor", new Color(255, 158, 250)));
    public Option<Color> choicesEnabledTextColorOption = addOption(new ColorOption("ChoicesEnabledTextColor", new Color(255, 255, 255)));
    public Option<Color> choicesAreaBackgroundColorOption = addOption(new ColorOption("ChoicesAreaBackgroundColor", new Color(241, 241, 241)));
    public Option<Color> colorPaletteBackgroundColorOption = addOption(new ColorOption("ColorPaletteBackgroundColor", new Color(243, 243, 243)));
    public Option<Color> colorPaletteOutlineColorOption = addOption(new ColorOption("ColorPaletteOutlineColor", new Color(201, 201, 201)));
    public Option<Color> themePageComponentChosenBackgroundColorOption = addOption(new ColorOption("ThemePageComponentChosenBackgroundColor", new Color(248, 141, 255)));
    public Option<Color> themePageComponentTextColorOption = addOption(new ColorOption("ThemePageComponentTextColor", new Color(21, 21, 21)));
    public Option<Color> themePageComponentAuthorsColorOption = addOption(new ColorOption("ThemePageComponentAuthorsColor", new Color(45, 45, 45)));
    public Option<Color> themePageComponentHoveredAuthorsColorOption = addOption(new ColorOption("ThemePageComponentHoveredAuthorsColor", new Color(16, 16, 16)));
    public Option<Color> themePageComponentChosenAuthorsColorOption = addOption(new ColorOption("ThemePageComponentChosenAuthorsColor", new Color(255, 215, 242)));
    public Option<Color> themePageComponentHoveredBackgroundColorOption = addOption(new ColorOption("ThemePageComponentHoveredBackgroundColor", new Color(255, 255, 255)));
    public Option<Color> themePageComponentBackgroundColorOption = addOption(new ColorOption("ThemePageComponentBackgroundColor", new Color(236, 236, 236)));
    public Option<Color> themePageComponentHoveredTextColorOption = addOption(new ColorOption("ThemePageComponentHoveredTextColor", new Color(0, 0, 0)));
    public Option<Color> themePageComponentChosenTextColorOption = addOption(new ColorOption("ThemePageComponentChosenTextColor", new Color(245, 245, 245)));


    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTickAlways() {
        Theme currentTheme = Vergence.THEME.getTheme();
        if (currentTheme != null && !currentTheme.getName().equals("default")) {
            displayNameOption.setValue(currentTheme.getDisplayName());
            descriptionOption.setValue(currentTheme.getDescription());
            authorsOption.setValue(String.join("|", currentTheme.getAuthors()));

            mainColorOption.setValue(currentTheme.getMainColor());
            mainPageBackgroundColorOption.setValue(currentTheme.getMainPageBackgroundColor());
            mainPageSplitLineColorOption.setValue(currentTheme.getMainPageSplitLineColor());
            categoryBackgroundColorOption.setValue(currentTheme.getCategoryBackgroundColor());
            categoryTextColorOption.setValue(currentTheme.getCategoryTextColor());
            categoryHoveredBackgroundColorOption.setValue(currentTheme.getCategoryHoveredBackgroundColor());
            categoryHoveredTextColorOption.setValue(currentTheme.getCategoryHoveredTextColor());
            categoryCurrentBackgroundColorOption.setValue(currentTheme.getCategoryCurrentBackgroundColor());
            categoryCurrentTextColorOption.setValue(currentTheme.getCategoryCurrentTextColor());
            moduleBackgroundColorOption.setValue(currentTheme.getModuleBackgroundColor());
            moduleEnabledBackgroundColorOption.setValue(currentTheme.getModuleEnabledBackgroundColor());
            moduleTextColorOption.setValue(currentTheme.getModuleTextColor());
            moduleEnabledTextColorOption.setValue(currentTheme.getModuleEnabledTextColor());
            moduleHoveredBackgroundColorOption.setValue(currentTheme.getModuleHoveredBackgroundColor());
            moduleHoveredTextColorOption.setValue(currentTheme.getModuleHoveredTextColor());
            moduleGearTextColorOption.setValue(currentTheme.getModuleGearTextColor());
            moduleHoveredGearTextColorOption.setValue(currentTheme.getModuleHoveredGearTextColor());
            moduleEnabledGearTextColorOption.setValue(currentTheme.getModuleEnabledGearTextColor());
            notificationBackgroundColorOption.setValue(currentTheme.getNotificationBackgroundColor());
            notificationTextColorOption.setValue(currentTheme.getNotificationTextColor());
            notificationHighlightTextColorOption.setValue(currentTheme.getNotificationHighlightTextColor());
            buttonCircleColorOption.setValue(currentTheme.getButtonCircleColor());
            buttonInlineColorOption.setValue(currentTheme.getButtonInlineColor());
            buttonEnabledCircleColorOption.setValue(currentTheme.getButtonEnabledCircleColor());
            buttonEnabledInlineColorOption.setValue(currentTheme.getButtonEnabledInlineColor());
            buttonBackgroundColorOption.setValue(currentTheme.getButtonBackgroundColor());
            buttonHoveredBackgroundColorOption.setValue(currentTheme.getButtonHoveredBackgroundColor());
            buttonEnabledBackgroundColorOption.setValue(currentTheme.getButtonEnabledBackgroundColor());
            buttonHoveredInlineColorOption.setValue(currentTheme.getButtonHoveredInlineColor());
            buttonHoveredCircleColorOption.setValue(currentTheme.getButtonHoveredCircleColor());
            sliderBackgroundColorOption.setValue(currentTheme.getSliderBackgroundColor());
            sliderHoveredBackgroundColorOption.setValue(currentTheme.getSliderHoveredBackgroundColor());
            sliderCircleColorOption.setValue(currentTheme.getSliderCircleColor());
            sliderInlineColorOption.setValue(currentTheme.getSliderInlineColor());
            sliderHoveredCircleColorOption.setValue(currentTheme.getSliderHoveredCircleColor());
            sliderHoveredInlineColorOption.setValue(currentTheme.getSliderHoveredInlineColor());
            sliderClickedCircleColorOption.setValue(currentTheme.getSliderClickedCircleColor());
            sliderClickedInlineColorOption.setValue(currentTheme.getSliderClickedInlineColor());
            sliderValuedBackgroundColorOption.setValue(currentTheme.getSliderValuedBackgroundColor());
            optionsTextColorOption.setValue(currentTheme.getOptionsTextColor());
            optionAreaBackgroundColorOption.setValue(currentTheme.getOptionAreaBackgroundColor());
            hudEditorTipsTextColorOption.setValue(currentTheme.getHudEditorTipsTextColor());
            hoverComponentBackgroundColorOption.setValue(currentTheme.getHoverComponentBackgroundColor());
            hoverComponentTextColorOption.setValue(currentTheme.getHoverComponentTextColor());
            descriptionBackgroundColorOption.setValue(currentTheme.getDescriptionBackgroundColor());
            descriptionTextColorOption.setValue(currentTheme.getDescriptionTextColor());
            chatPrefixColorOption.setValue(currentTheme.getChatPrefixColor());
            topBarBackgroundColorOption.setValue(currentTheme.getTopBarBackgroundColor());
            topBarResearchFrameBackgroundColorOption.setValue(currentTheme.getTopBarResearchFrameBackgroundColor());
            topBarResearchFrameTextColorOption.setValue(currentTheme.getTopBarResearchFrameTextColor());
            topBarHoveredResearchFrameBackgroundColorOption.setValue(currentTheme.getTopBarHoveredResearchFrameBackgroundColor());
            topBarHoveredResearchFrameTextColorOption.setValue(currentTheme.getTopBarHoveredResearchFrameTextColor());
            topBarFocusedResearchFrameBackgroundColorOption.setValue(currentTheme.getTopBarFocusedResearchFrameBackgroundColor());
            topBarFocusedResearchFrameTextColorOption.setValue(currentTheme.getTopBarFocusedResearchFrameTextColor());
            topbarButtonBackgroundColorOption.setValue(currentTheme.getTopbarButtonBackgroundColor());
            topbarHoveredButtonBackgroundColorOption.setValue(currentTheme.getTopbarHoveredButtonBackgroundColor());
            topbarButtonIconColorOption.setValue(currentTheme.getTopbarButtonIconColor());
            topbarHoveredButtonIconColorOption.setValue(currentTheme.getTopbarHoveredButtonIconColor());
            inputFrameBackgroundColorOption.setValue(currentTheme.getInputFrameBackgroundColor());
            inputFrameTextColorOption.setValue(currentTheme.getInputFrameTextColor());
            inputFrameHoveredBackgroundColorOption.setValue(currentTheme.getInputFrameHoveredBackgroundColor());
            inputFrameHoveredTextColorOption.setValue(currentTheme.getInputFrameHoveredTextColor());
            choicesBackgroundColorOption.setValue(currentTheme.getChoicesBackgroundColor());
            choicesTextColorOption.setValue(currentTheme.getChoicesTextColor());
            choicesHoveredBackgroundColorOption.setValue(currentTheme.getChoicesHoveredBackgroundColor());
            choicesHoveredTextColorOption.setValue(currentTheme.getChoicesHoveredTextColor());
            choicesDefaultBackgroundColorOption.setValue(currentTheme.getChoicesDefaultBackgroundColor());
            choicesDefaultTextColorOption.setValue(currentTheme.getChoicesDefaultTextColor());
            choicesEnabledBackgroundColorOption.setValue(currentTheme.getChoicesEnabledBackgroundColor());
            choicesEnabledTextColorOption.setValue(currentTheme.getChoicesEnabledTextColor());
            choicesAreaBackgroundColorOption.setValue(currentTheme.getChoicesAreaBackgroundColor());
            colorPaletteBackgroundColorOption.setValue(currentTheme.getColorPaletteBackgroundColor());
            colorPaletteOutlineColorOption.setValue(currentTheme.getColorPaletteOutlineColor());
            themePageComponentChosenBackgroundColorOption.setValue(currentTheme.getThemePageComponentChosenBackgroundColor());
            themePageComponentTextColorOption.setValue(currentTheme.getThemePageComponentTextColor());
            themePageComponentAuthorsColorOption.setValue(currentTheme.getThemePageComponentAuthorsColor());
            themePageComponentHoveredAuthorsColorOption.setValue(currentTheme.getThemePageComponentHoveredAuthorsColor());
            themePageComponentChosenAuthorsColorOption.setValue(currentTheme.getThemePageComponentChosenAuthorsColor());
            themePageComponentHoveredBackgroundColorOption.setValue(currentTheme.getThemePageComponentHoveredBackgroundColor());
            themePageComponentBackgroundColorOption.setValue(currentTheme.getThemePageComponentBackgroundColor());
            themePageComponentHoveredTextColorOption.setValue(currentTheme.getThemePageComponentHoveredTextColor());
            themePageComponentChosenTextColorOption.setValue(currentTheme.getThemePageComponentChosenTextColor());
        }
    }

    @Override
    public void onOptionValueUpdate() {
        Theme currentTheme = Vergence.THEME.getTheme();
        if (currentTheme != null && !currentTheme.getName().equals("default")) {
            ArrayList<String> arr = new ArrayList<>();

            // Info
            currentTheme.setDisplayName(displayNameOption.getValue());
            currentTheme.setDescription(descriptionOption.getValue());
            if (authorsOption.getValue().isEmpty() || authorsOption.getValue().trim().equals("|")) {
                arr.add("");
            } else {
                arr.addAll(Arrays.asList(authorsOption.getValue().split("\\|")));
            }
            currentTheme.setAuthors(arr);

            currentTheme.setMainColor(mainColorOption.getValue());
            currentTheme.setMainPageBackgroundColor(mainPageBackgroundColorOption.getValue());
            currentTheme.setMainPageSplitLineColor(mainPageSplitLineColorOption.getValue());
            currentTheme.setCategoryBackgroundColor(categoryBackgroundColorOption.getValue());
            currentTheme.setCategoryTextColor(categoryTextColorOption.getValue());
            currentTheme.setCategoryHoveredBackgroundColor(categoryHoveredBackgroundColorOption.getValue());
            currentTheme.setCategoryHoveredTextColor(categoryHoveredTextColorOption.getValue());
            currentTheme.setCategoryCurrentBackgroundColor(categoryCurrentBackgroundColorOption.getValue());
            currentTheme.setCategoryCurrentTextColor(categoryCurrentTextColorOption.getValue());
            currentTheme.setModuleBackgroundColor(moduleBackgroundColorOption.getValue());
            currentTheme.setModuleEnabledBackgroundColor(moduleEnabledBackgroundColorOption.getValue());
            currentTheme.setModuleTextColor(moduleTextColorOption.getValue());
            currentTheme.setModuleEnabledTextColor(moduleEnabledTextColorOption.getValue());
            currentTheme.setModuleHoveredBackgroundColor(moduleHoveredBackgroundColorOption.getValue());
            currentTheme.setModuleHoveredTextColor(moduleHoveredTextColorOption.getValue());
            currentTheme.setModuleGearTextColor(moduleGearTextColorOption.getValue());
            currentTheme.setModuleHoveredGearTextColor(moduleHoveredGearTextColorOption.getValue());
            currentTheme.setModuleEnabledGearTextColor(moduleEnabledGearTextColorOption.getValue());
            currentTheme.setNotificationBackgroundColor(notificationBackgroundColorOption.getValue());
            currentTheme.setNotificationTextColor(notificationTextColorOption.getValue());
            currentTheme.setNotificationHighlightTextColor(notificationHighlightTextColorOption.getValue());
            currentTheme.setButtonCircleColor(buttonCircleColorOption.getValue());
            currentTheme.setButtonInlineColor(buttonInlineColorOption.getValue());
            currentTheme.setButtonEnabledCircleColor(buttonEnabledCircleColorOption.getValue());
            currentTheme.setButtonEnabledInlineColor(buttonEnabledInlineColorOption.getValue());
            currentTheme.setButtonBackgroundColor(buttonBackgroundColorOption.getValue());
            currentTheme.setButtonHoveredBackgroundColor(buttonHoveredBackgroundColorOption.getValue());
            currentTheme.setButtonEnabledBackgroundColor(buttonEnabledBackgroundColorOption.getValue());
            currentTheme.setButtonHoveredInlineColor(buttonHoveredInlineColorOption.getValue());
            currentTheme.setButtonHoveredCircleColor(buttonHoveredCircleColorOption.getValue());
            currentTheme.setSliderBackgroundColor(sliderBackgroundColorOption.getValue());
            currentTheme.setSliderHoveredBackgroundColor(sliderHoveredBackgroundColorOption.getValue());
            currentTheme.setSliderCircleColor(sliderCircleColorOption.getValue());
            currentTheme.setSliderInlineColor(sliderInlineColorOption.getValue());
            currentTheme.setSliderHoveredCircleColor(sliderHoveredCircleColorOption.getValue());
            currentTheme.setSliderHoveredInlineColor(sliderHoveredInlineColorOption.getValue());
            currentTheme.setSliderClickedCircleColor(sliderClickedCircleColorOption.getValue());
            currentTheme.setSliderClickedInlineColor(sliderClickedInlineColorOption.getValue());
            currentTheme.setSliderValuedBackgroundColor(sliderValuedBackgroundColorOption.getValue());
            currentTheme.setOptionsTextColor(optionsTextColorOption.getValue());
            currentTheme.setOptionAreaBackgroundColor(optionAreaBackgroundColorOption.getValue());
            currentTheme.setHudEditorTipsTextColor(hudEditorTipsTextColorOption.getValue());
            currentTheme.setHoverComponentBackgroundColor(hoverComponentBackgroundColorOption.getValue());
            currentTheme.setHoverComponentTextColor(hoverComponentTextColorOption.getValue());
            currentTheme.setDescriptionBackgroundColor(descriptionBackgroundColorOption.getValue());
            currentTheme.setDescriptionTextColor(descriptionTextColorOption.getValue());
            currentTheme.setChatPrefixColor(chatPrefixColorOption.getValue());
            currentTheme.setTopBarBackgroundColor(topBarBackgroundColorOption.getValue());
            currentTheme.setTopBarResearchFrameBackgroundColor(topBarResearchFrameBackgroundColorOption.getValue());
            currentTheme.setTopBarResearchFrameTextColor(topBarResearchFrameTextColorOption.getValue());
            currentTheme.setTopBarHoveredResearchFrameBackgroundColor(topBarHoveredResearchFrameBackgroundColorOption.getValue());
            currentTheme.setTopBarHoveredResearchFrameTextColor(topBarHoveredResearchFrameTextColorOption.getValue());
            currentTheme.setTopBarFocusedResearchFrameBackgroundColor(topBarFocusedResearchFrameBackgroundColorOption.getValue());
            currentTheme.setTopBarFocusedResearchFrameTextColor(topBarFocusedResearchFrameTextColorOption.getValue());
            currentTheme.setTopbarButtonBackgroundColor(topbarButtonBackgroundColorOption.getValue());
            currentTheme.setTopbarHoveredButtonBackgroundColor(topbarHoveredButtonBackgroundColorOption.getValue());
            currentTheme.setTopbarButtonIconColor(topbarButtonIconColorOption.getValue());
            currentTheme.setTopbarHoveredButtonIconColor(topbarHoveredButtonIconColorOption.getValue());
            currentTheme.setInputFrameBackgroundColor(inputFrameBackgroundColorOption.getValue());
            currentTheme.setInputFrameTextColor(inputFrameTextColorOption.getValue());
            currentTheme.setInputFrameHoveredBackgroundColor(inputFrameHoveredBackgroundColorOption.getValue());
            currentTheme.setInputFrameHoveredTextColor(inputFrameHoveredTextColorOption.getValue());
            currentTheme.setChoicesBackgroundColor(choicesBackgroundColorOption.getValue());
            currentTheme.setChoicesTextColor(choicesTextColorOption.getValue());
            currentTheme.setChoicesHoveredBackgroundColor(choicesHoveredBackgroundColorOption.getValue());
            currentTheme.setChoicesHoveredTextColor(choicesHoveredTextColorOption.getValue());
            currentTheme.setChoicesDefaultBackgroundColor(choicesDefaultBackgroundColorOption.getValue());
            currentTheme.setChoicesDefaultTextColor(choicesDefaultTextColorOption.getValue());
            currentTheme.setChoicesEnabledBackgroundColor(choicesEnabledBackgroundColorOption.getValue());
            currentTheme.setChoicesEnabledTextColor(choicesEnabledTextColorOption.getValue());
            currentTheme.setChoicesAreaBackgroundColor(choicesAreaBackgroundColorOption.getValue());
            currentTheme.setColorPaletteBackgroundColor(colorPaletteBackgroundColorOption.getValue());
            currentTheme.setColorPaletteOutlineColor(colorPaletteOutlineColorOption.getValue());
            currentTheme.setThemePageComponentChosenBackgroundColor(themePageComponentChosenBackgroundColorOption.getValue());
            currentTheme.setThemePageComponentTextColor(themePageComponentTextColorOption.getValue());
            currentTheme.setThemePageComponentAuthorsColor(themePageComponentAuthorsColorOption.getValue());
            currentTheme.setThemePageComponentHoveredAuthorsColor(themePageComponentHoveredAuthorsColorOption.getValue());
            currentTheme.setThemePageComponentChosenAuthorsColor(themePageComponentChosenAuthorsColorOption.getValue());
            currentTheme.setThemePageComponentHoveredBackgroundColor(themePageComponentHoveredBackgroundColorOption.getValue());
            currentTheme.setThemePageComponentBackgroundColor(themePageComponentBackgroundColorOption.getValue());
            currentTheme.setThemePageComponentHoveredTextColor(themePageComponentHoveredTextColorOption.getValue());
            currentTheme.setThemePageComponentChosenTextColor(themePageComponentChosenTextColorOption.getValue());
        }
    }
}
