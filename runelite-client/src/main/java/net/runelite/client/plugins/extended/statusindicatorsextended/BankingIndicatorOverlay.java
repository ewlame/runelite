package net.runelite.client.plugins.extended.statusindicatorsextended;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.extended.ExtendedIndicatorOverlay;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class BankingIndicatorOverlay extends ExtendedIndicatorOverlay {
    @Inject
    public BankingIndicatorOverlay(Client client, StatusIndicatorsExtendedConfig config, StatusIndicatorsExtendedPlugin plugin) {
        super(client, config, plugin);
        colorMethod = () -> config.bankColor();
        label = "banking";
    }

    @Override
    public boolean test() {
        if (config.displayBank()) {
            Widget bankWidget = client.getWidget(WidgetInfo.BANK_TITLE_BAR);
            return bankWidget != null && !bankWidget.isHidden();
        } else {
            return false;
        }
    }
}