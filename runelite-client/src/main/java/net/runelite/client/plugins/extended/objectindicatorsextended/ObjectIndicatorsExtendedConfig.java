package net.runelite.client.plugins.extended.objectindicatorsextended;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("objectindicatorsextended")
public interface ObjectIndicatorsExtendedConfig extends Config {
    @ConfigSection(
    		name = "Render style",
            description = "The render style of object highlighting",
            position = 0
    )
    String renderStyleSection = "renderStyleSection";

	@ConfigItem(
            position = 0,
            keyName = "highlightHull",
            name = "Highlight hull",
            description = "Configures whether or not object should be highlighted by hull",
            section = renderStyleSection
    )
    default boolean highlightHull() {
        return true;
    }

    @ConfigItem(
            position = 1,
            keyName = "highlightOutline",
            name = "Highlight outline",
            description = "Configures whether or not the model of the object should be highlighted by outline",
            section = renderStyleSection
    )
    default boolean highlightOutline() {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "highlightClickbox",
            name = "Highlight clickbox",
            description = "Configures whether the object's clickbox should be highlighted",
            section = renderStyleSection
    )
    default boolean highlightClickbox() {
        return false;
    }

    @ConfigItem(
            position = 3,
            keyName = "highlightTile",
            name = "Highlight tile",
            description = "Configures whether the object's tile should be highlighted",
            section = renderStyleSection
    )
    default boolean highlightTile() {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 4,
            keyName = "markerColor",
            name = "Marker color",
            description = "Configures the color of object marker",
            section = renderStyleSection
    )
    default Color markerColor() {
        return Color.WHITE;
    }

    @ConfigItem(
            position = 5,
            keyName = "borderWidth",
            name = "Border Width",
            description = "Width of the marked object border",
            section = renderStyleSection
    )
    default double borderWidth() {
        return 2;
    }

    @ConfigItem(
            position = 6,
            keyName = "outlineFeather",
            name = "Outline feather",
            description = "Specify between 0-4 how much of the model outline should be faded",
            section = renderStyleSection
    )
    @Range(
            min = 0,
            max = 4
    )
    default int outlineFeather() {
        return 0;
    }

    @ConfigItem(
            position = 5,
            keyName = "rememberObjectColors",
            name = "Remember color per object",
            description = "Color objects using the color from time of marking"
    )
    default boolean rememberObjectColors() {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "boxSize",
            name = "Box size",
            description = "The size of the box displayed in the centre of the marked ground tile."
    )
    default int boxSize() {
        return 16;
    }


    @ConfigItem(
            position = 8,
            keyName = "notifyHover",
            name = "Mouse hovering indication",
            description = "Displays a indicator if the mouse is hovering over a marked object."
    )
    default boolean showMouseHover() {
        return false;
    }

    @ConfigItem(
            position = 13,
            keyName = "hoverIndicatorColor",
            name = "Hover color",
            description = "The color of the indicator when hovering over a marked tile."
    )
    default Color mouseHoverIndicatorColour() {
        return Color.CYAN;
    }
}
