package net.runelite.client.plugins.extended.objectindicatorsextended;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;

class HoverIndicatorOverlay extends Overlay {
    private static final int MINIMUM_SIZE = 16;
    private final Client client;
    private final ObjectIndicatorsExtendedConfig config;
    private final ObjectIndicatorsExtendedPlugin plugin;

    @Inject
    private HoverIndicatorOverlay(final Client client, final ObjectIndicatorsExtendedConfig config,
                                  final ObjectIndicatorsExtendedPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        setMinimumSize(MINIMUM_SIZE);
        setResizable(true);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Dimension preferredSize = getPreferredSize();

        if (preferredSize == null) {
            preferredSize = plugin.DEFAULT_SIZE; // if this happens, reset to default
            setPreferredSize(preferredSize); // shouldn't be common, but alt+rightclick will force this
        }

        for (ColorTileObject colorTileObject : plugin.getObjects()) {
            TileObject object = colorTileObject.getTileObject();
            if (config.highlightClickbox()) {
                Shape clickbox = object.getClickbox();
                if (clickbox != null) {
                    if (config.showMouseHover() && object.getClickbox().contains(client.getMouseCanvasPosition().getX(),
                            client.getMouseCanvasPosition().getY())) {
                        graphics.setColor(config.mouseHoverIndicatorColour());
                        graphics.fillRect(0, 0, preferredSize.width, preferredSize.height);
                        final Point tickCounterPoint = new Point(preferredSize.width + 18 / 3,
                                preferredSize.height - (preferredSize.height / 2) + 6);
                        OverlayUtil.renderTextLocation(graphics, tickCounterPoint, "hovering highlight",
                                config.mouseHoverIndicatorColour());
                    }
                }
            }
        }
        return preferredSize;
    }
}