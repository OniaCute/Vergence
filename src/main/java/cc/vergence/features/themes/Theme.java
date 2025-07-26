package cc.vergence.features.themes;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;

public class Theme {
    private String name;
    private String displayName;
    private String description;
    private ArrayList<String> authors = new ArrayList<>();
    private Color mainColor;
    private Color mainPageBackgroundColor;
    private Color categoryBackgroundColor;
    private Color categoryTextColor;
    private Color categoryHoveredBackgroundColor;
    private Color categoryHoveredTextColor;
    private Color categoryCurrentBackgroundColor;
    private Color categoryCurrentTextColor;
    private Color moduleBackgroundColor;
    private Color moduleEnabledBackgroundColor;
    private Color moduleTextColor;
    private Color moduleEnabledTextColor;
    private Color moduleHoveredTextColor;
    private Color moduleHoveredBackgroundColor;
    private Color buttonBackgroundColor;
    private Color buttonHoveredBackgroundColor;
    private Color buttonCircleColor;
    private Color buttonInlineColor;
    private Color buttonHoveredCircleColor;
    private Color buttonHoveredInlineColor;
    private Color buttonEnabledBackgroundColor;
    private Color buttonEnabledCircleColor;
    private Color buttonEnabledInlineColor;
    private Color optionsTextColor;
    private Color hudEditorTipsTextColor;
    private Color hoverComponentBackgroundColor;
    private Color hoverComponentTextColor;
    private Color mainPageSplitLineColor;
    private Color topBarBackgroundColor;
    private Color moduleGearTextColor;
    private Color moduleHoveredGearTextColor;
    private Color moduleEnabledGearTextColor;
    private Color optionAreaBackgroundColor;
    private Color inputFrameBackgroundColor;
    private Color inputFrameTextColor;
    private Color inputFrameHoveredBackgroundColor;
    private Color inputFrameHoveredTextColor;
    private Color choicesBackgroundColor;
    private Color choicesTextColor;
    private Color choicesHoveredBackgroundColor;
    private Color choicesHoveredTextColor;
    private Color choicesDefaultBackgroundColor;
    private Color choicesDefaultTextColor;
    private Color choicesEnabledBackgroundColor;
    private Color choicesEnabledTextColor;
    private Color choicesAreaBackgroundColor;
    private Color sliderBackgroundColor;
    private Color sliderHoveredBackgroundColor;
    private Color sliderCircleColor;
    private Color sliderInlineColor;
    private Color sliderHoveredCircleColor;
    private Color sliderHoveredInlineColor;
    private Color sliderClickedCircleColor;
    private Color sliderClickedInlineColor;
    private Color sliderValuedBackgroundColor;
    private Color colorPaletteBackgroundColor;
    private Color colorPaletteOutlineColor;
    private Color descriptionBackgroundColor;
    private Color descriptionTextColor;
    private Color topbarButtonBackgroundColor;
    private Color topbarButtonIconColor;
    private Color topbarHoveredButtonBackgroundColor;
    private Color topbarHoveredButtonIconColor;
    private Color themePageComponentBackgroundColor;
    private Color themePageComponentTextColor;
    private Color themePageComponentAuthorsColor;
    private Color themePageComponentHoveredBackgroundColor;
    private Color themePageComponentHoveredTextColor;
    private Color themePageComponentHoveredAuthorsColor;
    private Color themePageComponentChosenBackgroundColor;
    private Color themePageComponentChosenTextColor;
    private Color themePageComponentChosenAuthorsColor;
    private Color configPageComponentBackgroundColor;
    private Color configPageComponentTextColor;
    private Color configPageComponentDateColor;
    private Color configPageComponentHoveredBackgroundColor;
    private Color configPageComponentHoveredTextColor;
    private Color configPageComponentHoveredDateColor;
    private Color configPageComponentChosenBackgroundColor;
    private Color configPageComponentChosenTextColor;
    private Color configPageComponentChosenDateColor;

    public Theme(String name, String displayName, String description, ArrayList<String> authors) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.authors = authors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void addAuthors(String name) {
        this.authors.add(name);
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }

    public Color getMainColor() {
        return mainColor;
    }

    public void setCategoryBackgroundColor(Color categoryBackgroundColor) {
        this.categoryBackgroundColor = categoryBackgroundColor;
    }

    public Color getCategoryBackgroundColor() {
        return categoryBackgroundColor;
    }

    public void setCategoryTextColor(Color categoryTextColor) {
        this.categoryTextColor = categoryTextColor;
    }

    public Color getCategoryTextColor() {
        return categoryTextColor;
    }

    public void setModuleBackgroundColor(Color moduleBackgroundColor) {
        this.moduleBackgroundColor = moduleBackgroundColor;
    }

    public Color getModuleBackgroundColor() {
        return moduleBackgroundColor;
    }

    public void setModuleEnabledBackgroundColor(Color moduleEnabledBackgroundColor) {
        this.moduleEnabledBackgroundColor = moduleEnabledBackgroundColor;
    }

    public Color getModuleEnabledBackgroundColor() {
        return moduleEnabledBackgroundColor;
    }

    public void setModuleEnabledTextColor(Color moduleEnabledTextColor) {
        this.moduleEnabledTextColor = moduleEnabledTextColor;
    }

    public Color getModuleEnabledTextColor() {
        return moduleEnabledTextColor;
    }

    public void setModuleTextColor(Color moduleTextColor) {
        this.moduleTextColor = moduleTextColor;
    }

    public Color getModuleTextColor() {
        return moduleTextColor;
    }

    public void setButtonCircleColor(Color buttonCircleColor) {
        this.buttonCircleColor = buttonCircleColor;
    }

    public Color getButtonCircleColor() {
        return buttonCircleColor;
    }

    public void setButtonEnabledCircleColor(Color buttonEnabledCircleColor) {
        this.buttonEnabledCircleColor = buttonEnabledCircleColor;
    }

    public Color getButtonEnabledCircleColor() {
        return buttonEnabledCircleColor;
    }

    public void setButtonEnabledInlineColor(Color buttonEnabledInlineColor) {
        this.buttonEnabledInlineColor = buttonEnabledInlineColor;
    }

    public Color getButtonEnabledInlineColor() {
        return buttonEnabledInlineColor;
    }

    public void setButtonInlineColor(Color buttonInlineColor) {
        this.buttonInlineColor = buttonInlineColor;
    }

    public Color getButtonInlineColor() {
        return buttonInlineColor;
    }

    public void setButtonBackgroundColor(Color buttonBackgroundColor) {
        this.buttonBackgroundColor = buttonBackgroundColor;
    }

    public Color getButtonBackgroundColor() {
        return buttonBackgroundColor;
    }

    public void setButtonEnabledBackgroundColor(Color buttonEnabledBackgroundColor) {
        this.buttonEnabledBackgroundColor = buttonEnabledBackgroundColor;
    }

    public Color getButtonEnabledBackgroundColor() {
        return buttonEnabledBackgroundColor;
    }

    public void setOptionsTextColor(Color optionsTextColor) {
        this.optionsTextColor = optionsTextColor;
    }

    public Color getOptionsTextColor() {
        return optionsTextColor;
    }

    public void setHudEditorTipsTextColor(Color hudEditorTipsTextColor) {
        this.hudEditorTipsTextColor = hudEditorTipsTextColor;
    }

    public Color getHudEditorTipsTextColor() {
        return hudEditorTipsTextColor;
    }

    public void setHoverComponentBackgroundColor(Color hoverComponentBackgroundColor) {
        this.hoverComponentBackgroundColor = hoverComponentBackgroundColor;
    }

    public Color getHoverComponentBackgroundColor() {
        return hoverComponentBackgroundColor;
    }

    public void setHoverComponentTextColor(Color hoverComponentTextColor) {
        this.hoverComponentTextColor = hoverComponentTextColor;
    }

    public Color getHoverComponentTextColor() {
        return hoverComponentTextColor;
    }

    public void setCategoryHoveredBackgroundColor(Color categoryHoveredBackgroundColor) {
        this.categoryHoveredBackgroundColor = categoryHoveredBackgroundColor;
    }

    public void setCategoryHoveredTextColor(Color categoryHoveredTextColor) {
        this.categoryHoveredTextColor = categoryHoveredTextColor;
    }

    public void setMainPageBackgroundColor(Color mainPageBackgroundColor) {
        this.mainPageBackgroundColor = mainPageBackgroundColor;
    }

    public void setModuleHoveredBackgroundColor(Color moduleHoveredBackgroundColor) {
        this.moduleHoveredBackgroundColor = moduleHoveredBackgroundColor;
    }

    public void setModuleHoveredTextColor(Color moduleHoveredTextColor) {
        this.moduleHoveredTextColor = moduleHoveredTextColor;
    }

    public Color getCategoryHoveredBackgroundColor() {
        return categoryHoveredBackgroundColor;
    }

    public Color getCategoryHoveredTextColor() {
        return categoryHoveredTextColor;
    }

    public Color getMainPageBackgroundColor() {
        return mainPageBackgroundColor;
    }

    public Color getModuleHoveredBackgroundColor() {
        return moduleHoveredBackgroundColor;
    }

    public Color getModuleHoveredTextColor() {
        return moduleHoveredTextColor;
    }

    public void setCategoryCurrentBackgroundColor(Color categoryCurrentBackgroundColor) {
        this.categoryCurrentBackgroundColor = categoryCurrentBackgroundColor;
    }

    public void setCategoryCurrentTextColor(Color categoryCurrentTextColor) {
        this.categoryCurrentTextColor = categoryCurrentTextColor;
    }

    public Color getCategoryCurrentBackgroundColor() {
        return categoryCurrentBackgroundColor;
    }

    public Color getCategoryCurrentTextColor() {
        return categoryCurrentTextColor;
    }

    public void setMainPageSplitLineColor(Color mainPageSplitLineColor) {
        this.mainPageSplitLineColor = mainPageSplitLineColor;
    }

    public Color getMainPageSplitLineColor() {
        return mainPageSplitLineColor;
    }

    public void setTopBarBackgroundColor(Color topBarBackgroundColor) {
        this.topBarBackgroundColor = topBarBackgroundColor;
    }

    public Color getTopBarBackgroundColor() {
        return topBarBackgroundColor;
    }

    public void setModuleGearTextColor(Color moduleGearTextColor) {
        this.moduleGearTextColor = moduleGearTextColor;
    }

    public Color getModuleGearTextColor() {
        return moduleGearTextColor;
    }

    public void setModuleEnabledGearTextColor(Color moduleEnabledGearTextColor) {
        this.moduleEnabledGearTextColor = moduleEnabledGearTextColor;
    }

    public void setModuleHoveredGearTextColor(Color moduleHoveredGearTextColor) {
        this.moduleHoveredGearTextColor = moduleHoveredGearTextColor;
    }

    public Color getModuleEnabledGearTextColor() {
        return moduleEnabledGearTextColor;
    }

    public Color getModuleHoveredGearTextColor() {
        return moduleHoveredGearTextColor;
    }

    public void setOptionAreaBackgroundColor(Color optionAreaBackgroundColor) {
        this.optionAreaBackgroundColor = optionAreaBackgroundColor;
    }

    public Color getOptionAreaBackgroundColor() {
        return optionAreaBackgroundColor;
    }

    public void setButtonHoveredBackgroundColor(Color buttonHoveredBackgroundColor) {
        this.buttonHoveredBackgroundColor = buttonHoveredBackgroundColor;
    }

    public Color getButtonHoveredBackgroundColor() {
        return buttonHoveredBackgroundColor;
    }

    public void setButtonHoveredCircleColor(Color buttonHoveredCircleColor) {
        this.buttonHoveredCircleColor = buttonHoveredCircleColor;
    }

    public void setButtonHoveredInlineColor(Color buttonHoveredInlineColor) {
        this.buttonHoveredInlineColor = buttonHoveredInlineColor;
    }

    public Color getButtonHoveredCircleColor() {
        return buttonHoveredCircleColor;
    }

    public Color getButtonHoveredInlineColor() {
        return buttonHoveredInlineColor;
    }

    public void setInputFrameBackgroundColor(Color inputFrameBackgroundColor) {
        this.inputFrameBackgroundColor = inputFrameBackgroundColor;
    }

    public void setInputFrameHoveredBackgroundColor(Color inputFrameHoveredBackgroundColor) {
        this.inputFrameHoveredBackgroundColor = inputFrameHoveredBackgroundColor;
    }

    public void setInputFrameHoveredTextColor(Color inputFrameHoveredTextColor) {
        this.inputFrameHoveredTextColor = inputFrameHoveredTextColor;
    }

    public void setInputFrameTextColor(Color inputFrameTextColor) {
        this.inputFrameTextColor = inputFrameTextColor;
    }

    public Color getInputFrameBackgroundColor() {
        return inputFrameBackgroundColor;
    }

    public Color getInputFrameHoveredBackgroundColor() {
        return inputFrameHoveredBackgroundColor;
    }

    public Color getInputFrameTextColor() {
        return inputFrameTextColor;
    }

    public void setChoicesBackgroundColor(Color choicesBackgroundColor) {
        this.choicesBackgroundColor = choicesBackgroundColor;
    }

    public void setChoicesHoveredBackgroundColor(Color choicesHoveredBackgroundColor) {
        this.choicesHoveredBackgroundColor = choicesHoveredBackgroundColor;
    }

    public void setChoicesHoveredTextColor(Color choicesHoveredTextColor) {
        this.choicesHoveredTextColor = choicesHoveredTextColor;
    }

    public void setChoicesTextColor(Color choicesTextColor) {
        this.choicesTextColor = choicesTextColor;
    }

    public Color getChoicesBackgroundColor() {
        return choicesBackgroundColor;
    }

    public Color getChoicesHoveredBackgroundColor() {
        return choicesHoveredBackgroundColor;
    }

    public Color getChoicesHoveredTextColor() {
        return choicesHoveredTextColor;
    }

    public Color getChoicesTextColor() {
        return choicesTextColor;
    }

    public Color getInputFrameHoveredTextColor() {
        return inputFrameHoveredTextColor;
    }

    public void setChoicesDefaultBackgroundColor(Color choicesDefaultBackgroundColor) {
        this.choicesDefaultBackgroundColor = choicesDefaultBackgroundColor;
    }

    public void setChoicesDefaultTextColor(Color choicesDefaultTextColor) {
        this.choicesDefaultTextColor = choicesDefaultTextColor;
    }

    public void setChoicesEnabledBackgroundColor(Color choicesEnabledBackgroundColor) {
        this.choicesEnabledBackgroundColor = choicesEnabledBackgroundColor;
    }

    public void setChoicesEnabledTextColor(Color choicesEnabledTextColor) {
        this.choicesEnabledTextColor = choicesEnabledTextColor;
    }

    public Color getChoicesDefaultBackgroundColor() {
        return choicesDefaultBackgroundColor;
    }

    public Color getChoicesDefaultTextColor() {
        return choicesDefaultTextColor;
    }

    public Color getChoicesEnabledBackgroundColor() {
        return choicesEnabledBackgroundColor;
    }

    public Color getChoicesEnabledTextColor() {
        return choicesEnabledTextColor;
    }

    public void setChoicesAreaBackgroundColor(Color choicesAreaBackgroundColor) {
        this.choicesAreaBackgroundColor = choicesAreaBackgroundColor;
    }

    public Color getChoicesAreaBackgroundColor() {
        return choicesAreaBackgroundColor;
    }

    public void setSliderBackgroundColor(Color sliderBackgroundColor) {
        this.sliderBackgroundColor = sliderBackgroundColor;
    }

    public void setSliderCircleColor(Color sliderCircleColor) {
        this.sliderCircleColor = sliderCircleColor;
    }

    public void setSliderClickedCircleColor(Color sliderClickedCircleColor) {
        this.sliderClickedCircleColor = sliderClickedCircleColor;
    }

    public void setSliderClickedInlineColor(Color sliderClickedInlineColor) {
        this.sliderClickedInlineColor = sliderClickedInlineColor;
    }

    public void setSliderHoveredBackgroundColor(Color sliderHoveredBackgroundColor) {
        this.sliderHoveredBackgroundColor = sliderHoveredBackgroundColor;
    }

    public void setSliderHoveredCircleColor(Color sliderHoveredCircleColor) {
        this.sliderHoveredCircleColor = sliderHoveredCircleColor;
    }

    public void setSliderHoveredInlineColor(Color sliderHoveredInlineColor) {
        this.sliderHoveredInlineColor = sliderHoveredInlineColor;
    }

    public void setSliderInlineColor(Color sliderInlineColor) {
        this.sliderInlineColor = sliderInlineColor;
    }

    public Color getSliderBackgroundColor() {
        return sliderBackgroundColor;
    }

    public Color getSliderCircleColor() {
        return sliderCircleColor;
    }

    public Color getSliderClickedCircleColor() {
        return sliderClickedCircleColor;
    }

    public Color getSliderClickedInlineColor() {
        return sliderClickedInlineColor;
    }

    public Color getSliderHoveredBackgroundColor() {
        return sliderHoveredBackgroundColor;
    }

    public Color getSliderHoveredCircleColor() {
        return sliderHoveredCircleColor;
    }

    public Color getSliderHoveredInlineColor() {
        return sliderHoveredInlineColor;
    }

    public Color getSliderInlineColor() {
        return sliderInlineColor;
    }

    public void setSliderValuedBackgroundColor(Color sliderValuedBackgroundColor) {
        this.sliderValuedBackgroundColor = sliderValuedBackgroundColor;
    }

    public Color getSliderValuedBackgroundColor() {
        return sliderValuedBackgroundColor;
    }

    public void setColorPaletteBackgroundColor(Color colorPaletteBackgroundColor) {
        this.colorPaletteBackgroundColor = colorPaletteBackgroundColor;
    }

    public Color getColorPaletteBackgroundColor() {
        return colorPaletteBackgroundColor;
    }

    public Color getColorPaletteOutlineColor() {
        return colorPaletteOutlineColor;
    }

    public void setColorPaletteOutlineColor(Color colorPaletteOutlineColor) {
        this.colorPaletteOutlineColor = colorPaletteOutlineColor;
    }

    public void setDescriptionBackgroundColor(Color descriptionBackgroundColor) {
        this.descriptionBackgroundColor = descriptionBackgroundColor;
    }

    public void setDescriptionTextColor(Color descriptionTextColor) {
        this.descriptionTextColor = descriptionTextColor;
    }

    public Color getDescriptionBackgroundColor() {
        return descriptionBackgroundColor;
    }

    public Color getDescriptionTextColor() {
        return descriptionTextColor;
    }

    public void setTopbarButtonBackgroundColor(Color topbarButtonBackgroundColor) {
        this.topbarButtonBackgroundColor = topbarButtonBackgroundColor;
    }

    public void setTopbarButtonIconColor(Color topbarButtonIconColor) {
        this.topbarButtonIconColor = topbarButtonIconColor;
    }

    public void setTopbarHoveredButtonBackgroundColor(Color topbarHoveredButtonBackgroundColor) {
        this.topbarHoveredButtonBackgroundColor = topbarHoveredButtonBackgroundColor;
    }

    public Color getTopbarButtonBackgroundColor() {
        return topbarButtonBackgroundColor;
    }

    public Color getTopbarButtonIconColor() {
        return topbarButtonIconColor;
    }

    public Color getTopbarHoveredButtonBackgroundColor() {
        return topbarHoveredButtonBackgroundColor;
    }

    public void setTopbarHoveredButtonIconColor(Color topbarHoveredButtonIconColor) {
        this.topbarHoveredButtonIconColor = topbarHoveredButtonIconColor;
    }

    public Color getTopbarHoveredButtonIconColor() {
        return topbarHoveredButtonIconColor;
    }

    public void setThemePageComponentBackgroundColor(Color themePageComponentBackgroundColor) {
        this.themePageComponentBackgroundColor = themePageComponentBackgroundColor;
    }

    public void setThemePageComponentChosenBackgroundColor(Color themePageComponentChosenBackgroundColor) {
        this.themePageComponentChosenBackgroundColor = themePageComponentChosenBackgroundColor;
    }

    public void setThemePageComponentChosenTextColor(Color themePageComponentChosenTextColor) {
        this.themePageComponentChosenTextColor = themePageComponentChosenTextColor;
    }

    public void setThemePageComponentHoveredBackgroundColor(Color themePageComponentHoveredBackgroundColor) {
        this.themePageComponentHoveredBackgroundColor = themePageComponentHoveredBackgroundColor;
    }

    public void setThemePageComponentHoveredTextColor(Color themePageComponentHoveredTextColor) {
        this.themePageComponentHoveredTextColor = themePageComponentHoveredTextColor;
    }

    public void setThemePageComponentTextColor(Color themePageComponentTextColor) {
        this.themePageComponentTextColor = themePageComponentTextColor;
    }

    public Color getThemePageComponentBackgroundColor() {
        return themePageComponentBackgroundColor;
    }

    public Color getThemePageComponentChosenBackgroundColor() {
        return themePageComponentChosenBackgroundColor;
    }

    public Color getThemePageComponentChosenTextColor() {
        return themePageComponentChosenTextColor;
    }

    public Color getThemePageComponentHoveredBackgroundColor() {
        return themePageComponentHoveredBackgroundColor;
    }

    public Color getThemePageComponentHoveredTextColor() {
        return themePageComponentHoveredTextColor;
    }

    public Color getThemePageComponentTextColor() {
        return themePageComponentTextColor;
    }

    public void setThemePageComponentChosenAuthorsColor(Color themePageComponentChosenAuthorsColor) {
        this.themePageComponentChosenAuthorsColor = themePageComponentChosenAuthorsColor;
    }

    public Color getThemePageComponentChosenAuthorsColor() {
        return themePageComponentChosenAuthorsColor;
    }

    public void setThemePageComponentHoveredAuthorsColor(Color themePageComponentHoveredAuthorsColor) {
        this.themePageComponentHoveredAuthorsColor = themePageComponentHoveredAuthorsColor;
    }

    public Color getThemePageComponentHoveredAuthorsColor() {
        return themePageComponentHoveredAuthorsColor;
    }

    public void setThemePageComponentAuthorsColor(Color themePageComponentAuthorsColor) {
        this.themePageComponentAuthorsColor = themePageComponentAuthorsColor;
    }

    public Color getThemePageComponentAuthorsColor() {
        return themePageComponentAuthorsColor;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setConfigPageComponentDateColor(Color configPageComponentDateColor) {
        this.configPageComponentDateColor = configPageComponentDateColor;
    }

    public void setConfigPageComponentBackgroundColor(Color configPageComponentBackgroundColor) {
        this.configPageComponentBackgroundColor = configPageComponentBackgroundColor;
    }

    public void setConfigPageComponentChosenDateColor(Color configPageComponentChosenDateColor) {
        this.configPageComponentChosenDateColor = configPageComponentChosenDateColor;
    }

    public void setConfigPageComponentChosenBackgroundColor(Color configPageComponentChosenBackgroundColor) {
        this.configPageComponentChosenBackgroundColor = configPageComponentChosenBackgroundColor;
    }

    public void setConfigPageComponentChosenTextColor(Color configPageComponentChosenTextColor) {
        this.configPageComponentChosenTextColor = configPageComponentChosenTextColor;
    }

    public void setConfigPageComponentHoveredDateColor(Color configPageComponentHoveredDateColor) {
        this.configPageComponentHoveredDateColor = configPageComponentHoveredDateColor;
    }

    public void setConfigPageComponentHoveredBackgroundColor(Color configPageComponentHoveredBackgroundColor) {
        this.configPageComponentHoveredBackgroundColor = configPageComponentHoveredBackgroundColor;
    }

    public void setConfigPageComponentHoveredTextColor(Color configPageComponentHoveredTextColor) {
        this.configPageComponentHoveredTextColor = configPageComponentHoveredTextColor;
    }

    public void setConfigPageComponentTextColor(Color configPageComponentTextColor) {
        this.configPageComponentTextColor = configPageComponentTextColor;
    }

    public Color getConfigPageComponentDateColor() {
        return configPageComponentDateColor;
    }

    public Color getConfigPageComponentBackgroundColor() {
        return configPageComponentBackgroundColor;
    }

    public Color getConfigPageComponentChosenDateColor() {
        return configPageComponentChosenDateColor;
    }

    public Color getConfigPageComponentChosenBackgroundColor() {
        return configPageComponentChosenBackgroundColor;
    }

    public Color getConfigPageComponentChosenTextColor() {
        return configPageComponentChosenTextColor;
    }

    public Color getConfigPageComponentHoveredDateColor() {
        return configPageComponentHoveredDateColor;
    }

    public Color getConfigPageComponentHoveredBackgroundColor() {
        return configPageComponentHoveredBackgroundColor;
    }

    public Color getConfigPageComponentHoveredTextColor() {
        return configPageComponentHoveredTextColor;
    }

    public Color getConfigPageComponentTextColor() {
        return configPageComponentTextColor;
    }
}
