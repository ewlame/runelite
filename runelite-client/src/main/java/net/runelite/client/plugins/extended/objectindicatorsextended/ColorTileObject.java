package net.runelite.client.plugins.extended.objectindicatorsextended;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;
import java.awt.*;

/**
 * Used to denote marked objects and their colors.
 * Note: This is not used for serialization of object indicators; see {@link ObjectPoint}
 */

@Value
@RequiredArgsConstructor
public
class ColorTileObject
{
	private final TileObject tileObject;
	/**
	 * Non-transformed object composition for the object
	 */
	private final ObjectComposition composition;
	/**
	 * Name to highlight for multilocs
	 */
	private final String name;
	private final Color color;
}
