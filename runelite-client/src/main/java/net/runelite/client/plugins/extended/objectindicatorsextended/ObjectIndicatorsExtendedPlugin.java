package net.runelite.client.plugins.extended.objectindicatorsextended;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@PluginDescriptor(
	name = "Object Indicators Extended",
	description = "Enable marking of objects using the Shift key",
	tags = {"overlay", "objects", "mark", "marker"},
	enabledByDefault = false
)
@Slf4j
public class ObjectIndicatorsExtendedPlugin extends Plugin
{
	private static final String CONFIG_GROUP = "objectindicatorsextended";
	private static final String MARK = "Mark object";
	private static final String UNMARK = "Unmark object";

	@Getter(AccessLevel.PACKAGE)
	private final List<ColorTileObject> objects = new ArrayList<>();
	private final Map<Integer, Set<ObjectPoint>> points = new HashMap<>();

	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ObjectIndicatorsExtendedOverlay overlay;

	@Inject
	private HoverIndicatorOverlay overlay2;

	@Inject
	private ObjectIndicatorsExtendedConfig config;

	@Inject
	private Gson gson;
	public Dimension DEFAULT_SIZE = new Dimension(16, 16);
	@Provides
	ObjectIndicatorsExtendedConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ObjectIndicatorsExtendedConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
		overlayManager.add(overlay2);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		overlayManager.remove(overlay2);
		points.clear();
		objects.clear();
	}

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		checkObjectPoints(event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		objects.removeIf(o -> o.getTileObject() == event.getWallObject());
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		checkObjectPoints(event.getGameObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
	{
		checkObjectPoints(event.getDecorativeObject());
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		objects.removeIf(o -> o.getTileObject() == event.getGameObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
	{
		objects.removeIf(o -> o.getTileObject() == event.getDecorativeObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		checkObjectPoints(event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		objects.removeIf(o -> o.getTileObject() == event.getGroundObject());
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		GameState gameState = gameStateChanged.getGameState();
		if (gameState == GameState.LOADING)
		{
			// Reload points with new map regions

			points.clear();
			for (int regionId : client.getMapRegions())
			{
				// load points for region
				final Set<ObjectPoint> regionPoints = loadPoints(regionId);
				if (regionPoints != null)
				{
					points.put(regionId, regionPoints);
				}
			}
		}

		if (gameStateChanged.getGameState() != GameState.LOGGED_IN && gameStateChanged.getGameState() != GameState.CONNECTION_LOST)
		{
			objects.clear();
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.EXAMINE_OBJECT.getId() || !client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			return;
		}

		final Tile tile = client.getScene().getTiles()[client.getPlane()][event.getActionParam0()][event.getActionParam1()];
		final TileObject tileObject = findTileObject(tile, event.getIdentifier());

		if (tileObject == null)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption(objects.stream().anyMatch(o -> o.getTileObject() == tileObject) ? UNMARK : MARK)
			.setTarget(event.getTarget())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setIdentifier(event.getIdentifier())
			.setType(MenuAction.RUNELITE)
			.onClick(this::markObject);
	}

	private void markObject(MenuEntry entry)
	{
		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		final int x = entry.getParam0();
		final int y = entry.getParam1();
		final int z = client.getPlane();
		final Tile tile = tiles[z][x][y];

		TileObject object = findTileObject(tile, entry.getIdentifier());
		if (object == null)
		{
			return;
		}

		// object.getId() is always the base object id, getObjectComposition transforms it to
		// the correct object we see
		ObjectComposition objectDefinition = getObjectComposition(object.getId());
		String name = objectDefinition.getName();
		// Name is probably never "null" - however prevent adding it if it is, as it will
		// become ambiguous as objects with no name are assigned name "null"
		if (Strings.isNullOrEmpty(name) || name.equals("null"))
		{
			return;
		}

		markObject(objectDefinition, name, object);
	}

	private void checkObjectPoints(TileObject object)
	{
		if (object.getPlane() < 0)
		{
			// object is under a bridge, which can't be marked anyway
			return;
		}

		final WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, object.getLocalLocation(), object.getPlane());
		final Set<ObjectPoint> objectPoints = points.get(worldPoint.getRegionID());

		if (objectPoints == null)
		{
			return;
		}

		ObjectComposition objectComposition = client.getObjectDefinition(object.getId());
		if (objectComposition.getImpostorIds() == null)
		{
			// Multiloc names are instead checked in the overlay
			String name = objectComposition.getName();
			if (Strings.isNullOrEmpty(name) || name.equals("null"))
			{
				// was marked, but name has changed
				return;
			}
		}

		for (ObjectPoint objectPoint : objectPoints)
		{
			if (worldPoint.getRegionX() == objectPoint.getRegionX()
					&& worldPoint.getRegionY() == objectPoint.getRegionY()
					&& worldPoint.getPlane() == objectPoint.getZ()
					&& objectPoint.getId() == object.getId())
			{
				log.debug("Marking object {} due to matching {}", object, objectPoint);
				objects.add(new ColorTileObject(object,
					objectComposition,
					objectPoint.getName(),
					objectPoint.getColor()));
				break;
			}
		}
	}

	private TileObject findTileObject(Tile tile, int id)
	{
		if (tile == null)
		{
			return null;
		}

		final GameObject[] tileGameObjects = tile.getGameObjects();
		final DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
		final WallObject tileWallObject = tile.getWallObject();
		final GroundObject groundObject = tile.getGroundObject();

		if (objectIdEquals(tileWallObject, id))
		{
			return tileWallObject;
		}

		if (objectIdEquals(tileDecorativeObject, id))
		{
			return tileDecorativeObject;
		}

		if (objectIdEquals(groundObject, id))
		{
			return groundObject;
		}

		for (GameObject object : tileGameObjects)
		{
			if (objectIdEquals(object, id))
			{
				return object;
			}
		}

		return null;
	}

	private boolean objectIdEquals(TileObject tileObject, int id)
	{
		if (tileObject == null)
		{
			return false;
		}

		if (tileObject.getId() == id)
		{
			return true;
		}

		// Menu action EXAMINE_OBJECT sends the transformed object id, not the base id, unlike
		// all of the GAME_OBJECT_OPTION actions, so check the id against the impostor ids
		final ObjectComposition comp = client.getObjectDefinition(tileObject.getId());

		if (comp.getImpostorIds() != null)
		{
			for (int impostorId : comp.getImpostorIds())
			{
				if (impostorId == id)
				{
					return true;
				}
			}
		}

		return false;
	}

	/** mark or unmark an object
	 *
	 * @param objectComposition transformed composition of object based on vars
	 * @param name name of objectComposition
	 * @param object tile object, for multilocs object.getId() is the base id
	 */
	private void markObject(ObjectComposition objectComposition, String name, final TileObject object)
	{
		final WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, object.getLocalLocation());
		final int regionId = worldPoint.getRegionID();
		final Color color = config.markerColor();
		final ObjectPoint point = new ObjectPoint(
			object.getId(),
			name,
			regionId,
			worldPoint.getRegionX(),
			worldPoint.getRegionY(),
			worldPoint.getPlane(),
			color);

		Set<ObjectPoint> objectPoints = points.computeIfAbsent(regionId, k -> new HashSet<>());

		if (objects.removeIf(o -> o.getTileObject() == object))
		{
			// Find the object point that caused this object to be marked, there are two cases:
			// 1) object is a multiloc, the name may have changed since marking - match from base id
			// 2) not a multiloc, but an object has spawned with an identical name and a different
			//    id as what was originally marked
			if (!objectPoints.removeIf(op -> ((op.getId() == -1 || op.getId() == object.getId()) || op.getName().equals(objectComposition.getName()))
				&& op.getRegionX() == worldPoint.getRegionX()
				&& op.getRegionY() == worldPoint.getRegionY()
				&& op.getZ() == worldPoint.getPlane()))
			{
				log.warn("unable to find object point for unmarked object {}", object.getId());
			}

			log.debug("Unmarking object: {}", point);
		}
		else
		{
			objectPoints.add(point);
			objects.add(new ColorTileObject(object,
				client.getObjectDefinition(object.getId()),
				name,
				color));
			log.debug("Marking object: {}", point);
		}

		savePoints(regionId, objectPoints);
	}

	private void savePoints(final int id, final Set<ObjectPoint> points)
	{
		if (points.isEmpty())
		{
			configManager.unsetConfiguration(CONFIG_GROUP, "region_" + id);
		}
		else
		{
			final String json = gson.toJson(points);
			configManager.setConfiguration(CONFIG_GROUP, "region_" + id, json);
		}
	}

	private Set<ObjectPoint> loadPoints(final int id)
	{
		final String json = configManager.getConfiguration(CONFIG_GROUP, "region_" + id);

		if (Strings.isNullOrEmpty(json))
		{
			return null;
		}

		Set<ObjectPoint> points = gson.fromJson(json, new TypeToken<Set<ObjectPoint>>()
		{
		}.getType());
		// Prior to multiloc support the plugin would mark objects named "null", which breaks
		// in most cases due to the specific object being identified being ambiguous, so remove
		// them
		return points.stream()
			.filter(point -> !point.getName().equals("null"))
			.collect(Collectors.toSet());
	}

	@Nullable
	private ObjectComposition getObjectComposition(int id)
	{
		ObjectComposition objectComposition = client.getObjectDefinition(id);
		return objectComposition.getImpostorIds() == null ? objectComposition : objectComposition.getImpostor();
	}
}