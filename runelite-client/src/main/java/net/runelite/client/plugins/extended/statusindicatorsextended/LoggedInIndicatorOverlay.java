package net.runelite.client.plugins.extended.statusindicatorsextended;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.plugins.extended.ExtendedIndicatorOverlay;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LoggedInIndicatorOverlay extends ExtendedIndicatorOverlay {
    @Inject
    public LoggedInIndicatorOverlay(Client client, StatusIndicatorsExtendedConfig config, StatusIndicatorsExtendedPlugin plugin) {
        super(client, config, plugin);
        colorMethod = () -> config.connectedColor();
        label = "connected";
    }

    @Override
    public boolean test() {
        return config.displayConnected() && client.getGameState() == GameState.LOGGED_IN;
    }
}
