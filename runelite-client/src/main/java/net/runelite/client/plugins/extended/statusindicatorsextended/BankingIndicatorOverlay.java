package net.runelite.client.plugins.extended.statusindicatorsextended;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
class BankingIndicatorOverlay extends Overlay {
    private static final int MINIMUM_SIZE = 16; // too small and resizing becomes impossible, requiring a reset
    private final Client client;
    private final StatusIndicatorsExtendedConfig config;
    private final StatusIndicatorsExtendedPlugin plugin;

    @Inject
    private BankingIndicatorOverlay(final Client client, final StatusIndicatorsExtendedConfig config, final StatusIndicatorsExtendedPlugin plugin) {
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

        if (config.displayBank()) {
            Widget bankWidget = client.getWidget(WidgetInfo.BANK_TITLE_BAR);

            if (bankWidget != null && !bankWidget.isHidden()) {
                graphics.setColor(config.bankColor());
                graphics.fillRect(0, 0, preferredSize.width, preferredSize.height);
                final Point tickCounterPoint = new Point(preferredSize.width + 18 / 3, preferredSize.height - (preferredSize.height / 2) + 6);
                OverlayUtil.renderTextLocation(graphics, tickCounterPoint, "banking", config.bankColor());
            }
        }

        return preferredSize;
    }
}