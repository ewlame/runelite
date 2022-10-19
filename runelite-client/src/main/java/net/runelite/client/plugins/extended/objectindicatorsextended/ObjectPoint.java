package net.runelite.client.plugins.extended.objectindicatorsextended;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.awt.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ObjectPoint
{
	private int id = -1;
	private String name;
	private int regionId;
	private int regionX;
	private int regionY;
	private int z;
	private Color color;
}
