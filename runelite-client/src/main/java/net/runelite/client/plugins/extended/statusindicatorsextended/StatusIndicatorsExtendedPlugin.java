package net.runelite.client.plugins.extended.statusindicatorsextended;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;


@PluginDescriptor(
        name = "Status Indicators Extended",
        description = "A plugin to show Status Indicators for more client/player variables.",
        tags = {"overlay", "status", "indicators", "overlays", "variables", "player"}
)

@Slf4j
public class StatusIndicatorsExtendedPlugin extends Plugin {

    public Dimension DEFAULT_SIZE = new Dimension(16, 16);
    @Inject
    private Client client;
    @Inject
    private StatusIndicatorsExtendedConfig config;

//	@Inject
//	private StatusIndicatorsExtendedSceneOverlay sceneOverlay;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private LoggedInIndicatorOverlay loggedInOverlay;
    @Inject
    private IdleIndicatorOverlay idleoverlay;
    @Inject
    private BankingIndicatorOverlay bankingoverlay;
    @Inject
    private ConfigManager configManager;

    public StatusIndicatorsExtendedPlugin() {
    }

    @Provides
    StatusIndicatorsExtendedConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(StatusIndicatorsExtendedConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(loggedInOverlay);
        overlayManager.add(idleoverlay);
        overlayManager.add(bankingoverlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(loggedInOverlay);
        overlayManager.remove(idleoverlay);
        overlayManager.remove(bankingoverlay);

    }
}
