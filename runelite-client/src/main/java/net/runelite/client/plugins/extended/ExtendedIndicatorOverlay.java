package net.runelite.client.plugins.extended;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.plugins.extended.statusindicatorsextended.StatusIndicatorsExtendedConfig;
import net.runelite.client.plugins.extended.statusindicatorsextended.StatusIndicatorsExtendedPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class ExtendedIndicatorOverlay extends Overlay {
    private static final int MINIMUM_SIZE = 16;
    public final Client client;
    public final StatusIndicatorsExtendedConfig config;
    public final StatusIndicatorsExtendedPlugin plugin;

    public String label = "DEFAULT";
    public GetColor colorMethod;
    public interface GetColor {
        public Color get();
    }

    @Inject
    public ExtendedIndicatorOverlay(final Client client, final StatusIndicatorsExtendedConfig config, final StatusIndicatorsExtendedPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        setMinimumSize(MINIMUM_SIZE);
        setResizable(true);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public boolean test () {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Dimension preferredSize = getPreferredSize();

        if (preferredSize == null) {
            preferredSize = plugin.DEFAULT_SIZE; // if this happens, reset to default
            setPreferredSize(preferredSize); // shouldn't be common, but alt+rightclick will force this
        }

        if (test())
        {
            graphics.setColor(colorMethod.get());
            graphics.fillRect(0, 0, preferredSize.width, preferredSize.height);
            final Point tickCounterPoint = new Point(preferredSize.width + 18 / 3, preferredSize.height - (preferredSize.height / 2) + 6);
            OverlayUtil.renderTextLocation(graphics, tickCounterPoint, label, colorMethod.get());
        }
        return preferredSize;
    }
}
