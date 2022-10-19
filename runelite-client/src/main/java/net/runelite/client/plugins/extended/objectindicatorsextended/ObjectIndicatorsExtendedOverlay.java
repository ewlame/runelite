package net.runelite.client.plugins.extended.objectindicatorsextended;

import com.google.common.base.Strings;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import java.awt.*;

class ObjectIndicatorsExtendedOverlay extends Overlay {
    private final Client client;
    private final ObjectIndicatorsExtendedConfig config;
    private final ObjectIndicatorsExtendedPlugin plugin;
    private final ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private ObjectIndicatorsExtendedOverlay(Client client, ObjectIndicatorsExtendedConfig config, ObjectIndicatorsExtendedPlugin plugin,
                                            ModelOutlineRenderer modelOutlineRenderer) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.modelOutlineRenderer = modelOutlineRenderer;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setResizable(true);
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        Stroke stroke = new BasicStroke((float) config.borderWidth());
        for (ColorTileObject colorTileObject : plugin.getObjects()) {
            TileObject object = colorTileObject.getTileObject();
            Color color = colorTileObject.getColor();

            if (object.getPlane() != client.getPlane()) {
                continue;
            }

            ObjectComposition composition = colorTileObject.getComposition();
            if (composition.getImpostorIds() != null) {
                // This is a multiloc
                composition = composition.getImpostor();
                // Only mark the object if the name still matches
                if (composition == null
                        || Strings.isNullOrEmpty(composition.getName())
                        || "null".equals(composition.getName())
                        || !composition.getName().equals(colorTileObject.getName())) {
                    continue;
                }
            }

            if (color == null || !config.rememberObjectColors()) {
                // Fallback to the current config if the object is marked before the addition of multiple colors
                color = config.markerColor();
            }

            if (config.highlightHull()) {
                renderConvexHull(graphics, object, color, stroke);
            }

            if (config.highlightOutline()) {
                modelOutlineRenderer.drawOutline(object, (int) config.borderWidth(), color, config.outlineFeather());
            }

            if (config.highlightClickbox()) {
                Shape clickbox = object.getClickbox();
                if (clickbox != null) {
                    Color clickBoxColor = ColorUtil.colorWithAlpha(color, color.getAlpha() / 12);
                    OverlayUtil.renderPolygon(graphics, clickbox, color, clickBoxColor, stroke);
                    int x = (int) clickbox.getBounds().getCenterX() - config.boxSize() / 2;
                    int y = (int) clickbox.getBounds().getCenterY() - config.boxSize() / 2;
                    graphics.setColor(color);
                    graphics.fillRect(x, y, config.boxSize(), config.boxSize());
                }
            }

            if (config.highlightTile()) {
                Polygon tilePoly = object.getCanvasTilePoly();
                if (tilePoly != null) {
                    Color tileColor = ColorUtil.colorWithAlpha(color, color.getAlpha() / 12);
                    OverlayUtil.renderPolygon(graphics, tilePoly, color, tileColor, stroke);
                }
            }
        }

        return null;
    }

    private void renderConvexHull(Graphics2D graphics, TileObject object, Color color, Stroke stroke) {
        final Shape polygon;
        Shape polygon2 = null;

        if (object instanceof GameObject) {
            polygon = ((GameObject) object).getConvexHull();
        } else if (object instanceof WallObject) {
            polygon = ((WallObject) object).getConvexHull();
            polygon2 = ((WallObject) object).getConvexHull2();
        } else if (object instanceof DecorativeObject) {
            polygon = ((DecorativeObject) object).getConvexHull();
            polygon2 = ((DecorativeObject) object).getConvexHull2();
        } else if (object instanceof GroundObject) {
            polygon = ((GroundObject) object).getConvexHull();
        } else {
            polygon = object.getCanvasTilePoly();
        }

        if (polygon != null) {
            OverlayUtil.renderPolygon(graphics, polygon, color, stroke);
        }

        if (polygon2 != null) {
            OverlayUtil.renderPolygon(graphics, polygon2, color, stroke);
        }
    }
}