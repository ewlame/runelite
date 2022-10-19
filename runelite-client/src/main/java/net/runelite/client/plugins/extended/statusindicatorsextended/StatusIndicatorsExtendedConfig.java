package net.runelite.client.plugins.extended.statusindicatorsextended;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

import java.awt.*;

@ConfigGroup("statusindicatorsextended")
public interface StatusIndicatorsExtendedConfig extends Config {

    @ConfigItem(
            keyName = "displayIdle",
            name = "Idle animation",
            description = "Displays an indicator when idle.",
            position = 001
    )
    default boolean displayIdle() {
        return false;
    }

    @ConfigItem(
            keyName = "idleDelay",
            name = "Idle Indication Delay",
            description = "The indicator delay after the player is idle.",
            position = 002
    )
    @Units(Units.MILLISECONDS)
    default int idleDelay() {
        return 2000;
    }


    @ConfigItem(
            keyName = "idleColor",
            name = "Idle color",
            description = "The idle indicator color.",
            position = 003
    )
    default Color idleColor() {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName = "displayConnected",
            name = "Logged in",
            description = "Displays an indicator when logged in.",
            position = 004
    )
    default boolean displayConnected() {
        return false;
    }

    @ConfigItem(
            keyName = "connectedColor",
            name = "Connected color",
            description = "The connected indicator color.",
            position = 005
    )
    default Color connectedColor() {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "bankInterface",
            name = "Bank",
            description = "Displays an indicator if the bank interface is open.",
            position = 006
    )
    default boolean displayBank() {
        return false;
    }

    @ConfigItem(
            keyName = "bankColor",
            name = "The indicator color",
            description = "The color of the indicator.",
            position = 007
    )
    default Color bankColor() {
        return Color.ORANGE;
    }

//    @ConfigItem(
//    		keyName = "showLocation",
//    		name = "Player location",
//    		description = "Outputs the players current location.",
//    		position = 500
//    )
//    default boolean playerLocation()
//    {
//        return false;
//    }
//
//    @ConfigItem(
//            position = 3,
//            keyName = "combat",
//            name = "Combat",
//            description = "Color"
//    )
//    default Color combatColor() { return new Color(0,255,255); }
//
//    @ConfigItem(
//            position = 4,
//            keyName = "skills",
//            name = "Skills",
//            description = "Color"
//    )
//    default Color skillsColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 5,
//            keyName = "quest",
//            name = "Quests",
//            description = "Color"
//    )
//    default Color questColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 6,
//            keyName = "inventory",
//            name = "Inventory",
//            description = "Color"
//    )
//    default Color inventoryColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 7,
//            keyName = "equipment",
//            name = "Equipment",
//            description = "Color"
//    )
//    default Color equipmentColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 8,
//            keyName = "prayer",
//            name = "Prayer",
//            description = "Color"
//    )
//    default Color prayerColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 9,
//            keyName = "magic",
//            name = "Magic",
//            description = "Color"
//    )
//    default Color magicColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 10,
//            keyName = "friends",
//            name = "Friends",
//            description = "Color"
//    )
//    default Color friendsColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 11,
//            keyName = "account",
//            name = "Account",
//            description = "Color"
//    )
//    default Color accountColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 12,
//            keyName = "clan",
//            name = "Clan",
//            description = "Color"
//    )
//    default Color clanColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 13,
//            keyName = "logout",
//            name = "Logout",
//            description = "Color"
//    )
//    default Color logoutColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 14,
//            keyName = "settings",
//            name = "Settings",
//            description = "Color"
//    )
//    default Color settingsColor() { return Color.CYAN; }
//    @ConfigItem(
//            position = 15,
//            keyName = "emote",
//            name = "Emote",
//            description = "Color"
//    )
//    default Color emoteColor() { return Color.CYAN; }
//
//    @ConfigItem(
//            position = 16,
//            keyName = "music",
//            name = "Music",
//            description = "Color"
//    )
//    default Color musicColor() { return Color.CYAN; }
}
