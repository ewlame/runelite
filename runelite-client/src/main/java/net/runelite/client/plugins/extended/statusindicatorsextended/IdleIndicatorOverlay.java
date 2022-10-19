package net.runelite.client.plugins.extended.statusindicatorsextended;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.extended.ExtendedIndicatorOverlay;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.Instant;

import static net.runelite.api.AnimationID.IDLE;

@Singleton
class IdleIndicatorOverlay extends ExtendedIndicatorOverlay {
    private final int lastAnimation = AnimationID.IDLE;
    private boolean idleCheck = false;
    private boolean animatedCheck = false;
    private Instant lastIdle;
    private Instant currentTime;
    private Instant lastMoving;
    private WorldPoint lastPosition;
    private boolean notifyPosition = false;

    @Inject
    public IdleIndicatorOverlay(final Client client, final StatusIndicatorsExtendedConfig config, final StatusIndicatorsExtendedPlugin plugin) {
        super(client, config, plugin);
        colorMethod = () -> config.idleColor();
        label = "idle";
    }

    @Override
    public boolean test() {
        final Player local = client.getLocalPlayer();
        final Duration waitDuration = Duration.ofMillis(0);
        if (config.displayIdle()) {
            if (client.getLocalPlayer().getAnimation() == IDLE && !idleCheck) {
                lastIdle = Instant.now();
                idleCheck = true;
                animatedCheck = false;
            }
            if (client.getLocalPlayer().getAnimation() != IDLE && !animatedCheck) {
                animatedCheck = true;
                idleCheck = false;
            }
            if (checkMovementIdle(waitDuration, local)) {
                idleCheck = false;
            }
            return idleCheck && timerComplete(lastIdle, config.idleDelay());
        } else {
            return false;
        }
    }

    public boolean timerComplete(Instant duration1, long duration2) {
        currentTime = Instant.now();
        Duration duration = Duration.between(duration1, currentTime);
        long secondsElapsed = duration.getSeconds(); // * 1000 to get milliseconds number.
        long limit = duration2;
        return (secondsElapsed * 1000) >= limit;
    }

    private boolean checkMovementIdle(Duration waitDuration, Player local) {
        if (lastPosition == null) {
            lastPosition = local.getWorldLocation();
            return false;
        }

        WorldPoint position = local.getWorldLocation();

        if (lastPosition.equals(position)) {
            if (notifyPosition
                    && local.getAnimation() == IDLE
                    && Instant.now().compareTo(lastMoving.plus(waitDuration)) >= 0) {
                notifyPosition = false;
                // Return true only if we weren't just breaking out of an animation
                return lastAnimation == IDLE;
            }
        } else {
            notifyPosition = true;
            lastPosition = position;
            lastMoving = Instant.now();
        }

        return false;
    }
}
