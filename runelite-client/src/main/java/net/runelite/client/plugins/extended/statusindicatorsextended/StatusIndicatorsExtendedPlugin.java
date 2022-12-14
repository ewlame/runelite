package net.runelite.client.plugins.extended.statusindicatorsextended;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
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
    private LoggedInIndicatorOverlay loggedinoverlay;
    @Inject
    private IdleIndicatorOverlay idleoverlay;
    @Inject
    public BankingIndicatorOverlay bankingoverlay;
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
        overlayManager.add(loggedinoverlay);
        overlayManager.add(idleoverlay);
        overlayManager.add(bankingoverlay);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (config.displayBank() == true) {
            overlayManager.add(bankingoverlay);
        }
        else overlayManager.remove(bankingoverlay);
        if (config.displayIdle() == true) {
            overlayManager.add(idleoverlay);
        }
        else overlayManager.remove(idleoverlay);
        if (config.displayConnected() == true) {
            overlayManager.add(loggedinoverlay);
        }
        else overlayManager.remove(loggedinoverlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(loggedinoverlay);
        overlayManager.remove(idleoverlay);
        overlayManager.remove(bankingoverlay);

    }
}
