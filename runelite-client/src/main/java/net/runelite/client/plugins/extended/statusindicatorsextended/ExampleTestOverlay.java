package net.runelite.client.plugins.extended.statusindicatorsextended;

import java.awt.*;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.Graphics2D;
import java.awt.datatransfer.StringSelection;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import java.awt.Dimension;
import static net.runelite.api.AnimationID.*;
//@Singleton
//class StatusIndicatorsExtendedSceneOverlay extends Overlay {
//	private final Client client;
//	private final StatusIndicatorsExtendedConfig config;
//	private final StatusIndicatorsExtendedPlugin plugin;
//	private boolean idleCheck = false;
//	private boolean animatedCheck = false;
//	private Instant lastIdle;
//	private Instant currentTime;
//	private Instant lastAnimating;
//	private int lastAnimation = AnimationID.IDLE;
//	private Instant lastInteracting;
//	private Actor lastInteract;
//	private Instant lastMoving;
//	private WorldPoint lastPosition;
//	private boolean notifyPosition = false;
//	private boolean notifyHitpoints = true;
//	private boolean notifyPrayer = true;
//	private boolean notifyOxygen = true;
//	private boolean notifyIdleLogout = true;
//	private boolean notify6HourLogout = true;
//	private int lastSpecEnergy = 1000;
//	private int lastCombatCountdown = 0;
//	private Instant sixHourWarningTime;
//	private boolean ready;
//	private Instant lastTimeItemsUsedUp;
//	private List<Integer> itemIdsPrevious = new ArrayList<>();
//	private List<Integer> itemQuantitiesPrevious = new ArrayList<>();
//	private final List<Integer> itemQuantitiesChange = new ArrayList<>();
//	private boolean lastInteractWasCombat;
//	private boolean interactingNotified;
//	private SkullIcon lastTickSkull = null;
//	private boolean isFirstTick = true;
//	private boolean resourceDoorReady = false;
//	private static final int MINIMUM_SIZE = 16; // too small and resizing becomes impossible, requiring a reset
//
//	@Inject
//	private StatusIndicatorsExtendedSceneOverlay(final Client client, final StatusIndicatorsExtendedConfig config, final StatusIndicatorsExtendedPlugin plugin) {
//		this.client = client;
//		this.config = config;
//		this.plugin = plugin;
//		setPosition(OverlayPosition.BOTTOM_LEFT);
//		setMinimumSize(MINIMUM_SIZE);
//		setResizable(true);
//		setLayer(OverlayLayer.ABOVE_SCENE);
//	}
//
//	@Override
//	public Dimension render(Graphics2D graphics) {
//		Dimension preferredSize = getPreferredSize();
//
//		if (preferredSize == null) {
//			preferredSize = plugin.DEFAULT_SIZE; // if this happens, reset to default
//			setPreferredSize(preferredSize); // shouldn't be common, but alt+rightclick will force this
//		}
//
//		final Player local = client.getLocalPlayer();
//		final Duration waitDuration = Duration.ofMillis(0);
//		int Spacer = -1;
//
//		if (config.displayConnected()) {
//			Spacer++;
//			if (client.getGameState() == GameState.LOGGED_IN) {
//				graphics.setColor(config.connectedColor());
//				graphics.fillRect(0, -preferredSize.height * Spacer, preferredSize.width, preferredSize.height);
//				final Point tickCounterPoint = new Point(preferredSize.width + 18 / 3, -preferredSize.height * Spacer + (preferredSize.height / 2) + 6);
//				OverlayUtil.renderTextLocation(graphics, tickCounterPoint, "connected", config.connectedColor());
//			}
//		}
//
//		if (config.displayIdle()) {
//			Spacer++;
//			if (client.getLocalPlayer().getAnimation() == IDLE && !idleCheck) {
//				lastIdle = Instant.now();
//				idleCheck = true;
//				animatedCheck = false;
//			}
//			if (client.getLocalPlayer().getAnimation() != IDLE && !animatedCheck) {
//				animatedCheck = true;
//				idleCheck = false;
//			}
//			if (checkMovementIdle(waitDuration, local)) {
//				idleCheck = false;
//			}
//			if (idleCheck) {
//				if (timerComplete(lastIdle, config.idleDelay())) {
//					graphics.setColor(config.idleColor());
//					graphics.fillRect(0, -preferredSize.height * Spacer, preferredSize.width, preferredSize.height);
//					final Point tickCounterPoint = new Point(preferredSize.width + 18 / 3, -preferredSize.height * Spacer + (preferredSize.height / 2) + 6);
//					OverlayUtil.renderTextLocation(graphics, tickCounterPoint, "idle", config.idleColor());
//					if (client.getLocalPlayer().getAnimation() != IDLE)
//						idleCheck = false;
//				}
//			}
//		}
//
//		if (config.displayBank()) {
//			Spacer++;
//			Widget bankWidget = client.getWidget(WidgetInfo.BANK_TITLE_BAR);
//			if (bankWidget != null && !bankWidget.isHidden()) {
//				graphics.setColor(config.bankColor());
//				graphics.fillRect(0, -preferredSize.height * Spacer, preferredSize.width, preferredSize.height);
//				final Point tickCounterPoint = new Point(preferredSize.width + 18 / 3, -preferredSize.height * Spacer + (preferredSize.height / 2) + 6);
//				OverlayUtil.renderTextLocation(graphics, tickCounterPoint, "banking", config.bankColor());
//			}
//		}
//
////		if (config.playerLocation()) {
////			int currentX = client.getLocalPlayer().getWorldLocation().getX();
////			int currentY = client.getLocalPlayer().getWorldLocation().getY();
////			int currentRegion = client.getLocalPlayer().getWorldLocation().getPlane();
////			int tickCount = client.getTickCount();
////			String myString = currentX + ", " + currentY + ", " + currentRegion + ", " + tickCount;
////			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(myString), null);
////		}
//		return preferredSize;
//	}
//
//	public boolean timerComplete(Instant duration1, long duration2) {
//		currentTime = Instant.now();
//		Duration duration = Duration.between(duration1, currentTime);
//		long secondsElapsed = duration.getSeconds(); // * 1000 to get milliseconds number.
//		long limit = duration2;
//		if ((secondsElapsed * 1000) >= limit) {
//			return true;
//		}
//		return false;
//
//	}
//
//	private void renderNpcOverlay(Graphics2D graphics, NPC actor, Color color) {
//		Shape npc = actor.getConvexHull();
//		int x = (int) (actor.getConvexHull().getBounds().getCenterX() - 4 / 2);
//		int y = (int) (actor.getConvexHull().getBounds().getCenterY() - 4 / 2);
//		graphics.setColor(color);
//		graphics.fillRect(x, y, 4, 4);
//
//	}
//
//	private boolean checkMovementIdle(Duration waitDuration, Player local) {
//		if (lastPosition == null) {
//			lastPosition = local.getWorldLocation();
//			return false;
//		}
//
//		WorldPoint position = local.getWorldLocation();
//
//		if (lastPosition.equals(position)) {
//			if (notifyPosition
//					&& local.getAnimation() == IDLE
//					&& Instant.now().compareTo(lastMoving.plus(waitDuration)) >= 0) {
//				notifyPosition = false;
//				// Return true only if we weren't just breaking out of an animation
//				return lastAnimation == IDLE;
//			}
//		} else {
//			notifyPosition = true;
//			lastPosition = position;
//			lastMoving = Instant.now();
//		}
//		return false;
//
//	}
//}