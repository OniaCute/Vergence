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

        // ===== Notification =====
        setNotificationBackgroundColor(new Color(227, 227, 227, 182));
        setNotificationTextColor(new Color(255, 255, 255));
        setNotificationHighlightTextColor(new Color(255, 127, 226));

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

        // ===== Client Tips =====
        setChatPrefixColor(new Color(237, 151, 255));

        // ===== TopBar =====
        setTopBarBackgroundColor(new Color(239, 239, 239, 239));
        setTopBarResearchFrameBackgroundColor(new Color(229, 229, 229, 255));
        setTopBarResearchFrameTextColor(new Color(49, 49, 49));
        setTopBarHoveredResearchFrameBackgroundColor(new Color(231, 231, 231));
        setTopBarHoveredResearchFrameTextColor(new Color(21, 21, 21));
        setTopBarFocusedResearchFrameBackgroundColor(new Color(241, 241, 241));
        setTopBarFocusedResearchFrameTextColor(new Color(9, 9, 9));

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

        INSTANCE = this;
    }
}
