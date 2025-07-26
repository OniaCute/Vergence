package cc.vergence.features.themes;

import cc.vergence.Vergence;

import java.awt.*;
import java.util.ArrayList;

public class DefaultTheme extends Theme {
    public static DefaultTheme INSTANCE;

    public DefaultTheme() {
        super("default", Vergence.TEXT.get("Theme.DefaultTheme.name"), Vergence.TEXT.get("Theme.DefaultTheme.description"), new ArrayList<>());
        this.addAuthors("Voury");
        this.addAuthors("Onia");

        // ===== CLIENT =====
        setMainColor(new Color(216, 119, 255));

        // ===== Main Page =====
        setMainPageBackgroundColor(new Color(232, 232, 232, 245));
        setMainPageSplitLineColor(new Color(246, 246, 246));

        // ===== Category =====
        setCategoryBackgroundColor(new Color(238, 238, 238, 26));
        setCategoryTextColor(new Color(0, 0, 0));
        setCategoryHoveredBackgroundColor(new Color(238, 108, 255, 228));
        setCategoryHoveredTextColor(new Color(255, 255, 255));
        setCategoryCurrentBackgroundColor(new Color(230, 118, 255, 247));
        setCategoryCurrentTextColor(new Color(255, 255, 255));

        // ===== Modules =====
        setModuleBackgroundColor(new Color(238, 238, 238));
        setModuleEnabledBackgroundColor(new Color(248, 141, 255));
        setModuleTextColor(new Color(21, 21, 21));
        setModuleEnabledTextColor(new Color(255, 255, 255));
        setModuleHoveredBackgroundColor(new Color(236, 236, 236));
        setModuleHoveredTextColor(new Color(0, 0, 0));
        setModuleGearTextColor(new Color(40, 40, 40));
        setModuleHoveredGearTextColor(new Color(28, 28, 28));
        setModuleEnabledGearTextColor(new Color(255, 255, 255));

        // ===== Button =====
        setButtonCircleColor(new Color(232, 232, 232));
        setButtonInlineColor(new Color(243, 243, 243));
        setButtonEnabledCircleColor(new Color(250, 142, 255));
        setButtonEnabledInlineColor(new Color(255, 130, 250));
        setButtonBackgroundColor(new Color(222, 222, 222));
        setButtonHoveredBackgroundColor(new Color(245, 195, 255, 255));
        setButtonEnabledBackgroundColor(new Color(244, 181, 255, 255));
        setButtonHoveredInlineColor(new Color(243, 147, 255, 255));
        setButtonHoveredCircleColor(new Color(241, 162, 255, 255));

        // ===== Slider =====
        setSliderBackgroundColor(new Color(222, 222, 222));
        setSliderHoveredBackgroundColor(new Color(248, 209, 255, 255));
        setSliderCircleColor(new Color(232, 232, 232));
        setSliderInlineColor(new Color(243, 243, 243));
        setSliderHoveredCircleColor(new Color(240, 147, 255, 255));
        setSliderHoveredInlineColor(new Color(233, 141, 255, 255));
        setSliderClickedCircleColor(new Color(242, 156, 255, 255));
        setSliderClickedInlineColor(new Color(239, 135, 255, 255));
        setSliderValuedBackgroundColor(new Color(246, 142, 255));

        // ===== Options =====
        setOptionsTextColor(new Color(19, 19, 19));
        setOptionAreaBackgroundColor(new Color(255, 255, 255));

        // ===== HUD =====
        setHudEditorTipsTextColor(new Color(232, 232, 232));

        // ===== Hover =====
        setHoverComponentBackgroundColor(new Color(255, 255, 255, 255));
        setHoverComponentTextColor(new Color(255, 255, 255));
        setDescriptionBackgroundColor(new Color(255, 255, 255, 242));
        setDescriptionTextColor(new Color(16, 16, 16));

        // ===== TopBar =====
        setTopBarBackgroundColor(new Color(239, 239, 239, 239));
        setTopbarButtonBackgroundColor(new Color(236, 236, 236));
        setTopbarHoveredButtonBackgroundColor(new Color(255, 200, 246));
        setTopbarButtonIconColor(new Color(31, 31, 31, 221));
        setTopbarHoveredButtonIconColor(new Color(255, 119, 253));

        // ===== InputFrame =====
        setInputFrameBackgroundColor(new Color(241, 241, 241));
        setInputFrameTextColor(new Color(44, 44, 44));
        setInputFrameHoveredBackgroundColor(new Color(246, 246, 246));
        setInputFrameHoveredTextColor(new Color(243, 111, 255));

        // ===== Choices =====
        setChoicesBackgroundColor(new Color(241, 241, 241));
        setChoicesTextColor(new Color(44, 44, 44));
        setChoicesHoveredBackgroundColor(new Color(253, 228, 255));
        setChoicesHoveredTextColor(new Color(245, 140, 255));
        setChoicesDefaultBackgroundColor(new Color(238, 238, 238));
        setChoicesDefaultTextColor(new Color(44, 44, 44));
        setChoicesEnabledBackgroundColor(new Color(255, 158, 250));
        setChoicesEnabledTextColor(new Color(255, 255, 255));
        setChoicesAreaBackgroundColor(new Color(241, 241, 241));

        // ===== Color Palette =====
        setColorPaletteBackgroundColor(new Color(243, 243, 243));
        setColorPaletteOutlineColor(new Color(201, 201, 201));

        // ===== Theme Page Color =====
        setThemePageComponentChosenBackgroundColor(new Color(248, 141, 255));
        setThemePageComponentTextColor(new Color(21, 21, 21));
        setThemePageComponentAuthorsColor(new Color(45, 45, 45));
        setThemePageComponentHoveredAuthorsColor(new Color(16, 16, 16));
        setThemePageComponentChosenAuthorsColor(new Color(255, 215, 242));
        setThemePageComponentHoveredBackgroundColor(new Color(255, 255, 255));
        setThemePageComponentBackgroundColor(new Color(236, 236, 236));
        setThemePageComponentHoveredTextColor(new Color(0, 0, 0));
        setThemePageComponentChosenTextColor(new Color(245, 245, 245));

        // ===== Config Page Color =====
        setConfigPageComponentChosenBackgroundColor(new Color(248, 141, 255));
        setConfigPageComponentTextColor(new Color(21, 21, 21));
        setConfigPageComponentDateColor(new Color(45, 45, 45));
        setConfigPageComponentHoveredDateColor(new Color(16, 16, 16));
        setConfigPageComponentChosenDateColor(new Color(255, 215, 242));
        setConfigPageComponentHoveredBackgroundColor(new Color(255, 255, 255));
        setConfigPageComponentBackgroundColor(new Color(236, 236, 236));
        setConfigPageComponentHoveredTextColor(new Color(0, 0, 0));
        setConfigPageComponentChosenTextColor(new Color(245, 245, 245));

        INSTANCE = this;
    }
}
