package net.runelite.client.plugins.extended.statusindicatorsextended;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import java.awt.Dimension;
import static net.runelite.api.AnimationID.*;

@Singleton
class IdleIndicatorOverlay extends Overlay {
	private final Client client;
	private final StatusIndicatorsExtendedConfig config;
	private final StatusIndicatorsExtendedPlugin plugin;
	private boolean idleCheck = false;
	private boolean animatedCheck = false;
	private Instant lastIdle;
	private Instant currentTime;
	private int lastAnimation = AnimationID.IDLE;
	private Instant lastMoving;
	private WorldPoint lastPosition;
	private boolean notifyPosition = false;
	private static final int MINIMUM_SIZE = 16; // too small and resizing becomes impossible, requiring a reset

	@Inject
	private IdleIndicatorOverlay(final Client client, final StatusIndicatorsExtendedConfig config, final StatusIndicatorsExtendedPlugin plugin) {
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
			if (idleCheck) {
				if (timerComplete(lastIdle, config.idleDelay())) {
					graphics.setColor(config.idleColor());
					graphics.fillRect(0, 0, preferredSize.width, preferredSize.height);
					final Point tickCounterPoint = new Point(preferredSize.width + 18 / 3, preferredSize.height - (preferredSize.height / 2) + 6);
					OverlayUtil.renderTextLocation(graphics, tickCounterPoint, "idle", config.idleColor());
					if (client.getLocalPlayer().getAnimation() != IDLE)
						idleCheck = false;
				}
			}
		}

		return preferredSize;
	}

	public boolean timerComplete(Instant duration1, long duration2) {
		currentTime = Instant.now();
		Duration duration = Duration.between(duration1, currentTime);
		long secondsElapsed = duration.getSeconds(); // * 1000 to get milliseconds number.
		long limit = duration2;
		if ((secondsElapsed * 1000) >= limit) {
			return true;
		}

		return false;
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
