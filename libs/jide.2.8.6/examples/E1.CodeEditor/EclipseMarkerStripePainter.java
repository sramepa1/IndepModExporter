/*
 * @(#)EclipseMarkerStripePainter.java 1/23/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.editor.marker.Marker;
import com.jidesoft.editor.marker.MarkerArea;
import com.jidesoft.editor.marker.MarkerStripePainter;
import com.jidesoft.utils.ColorUtils;

import java.awt.*;

/**
 * Default implementation of <code>MarkerStripePainter</code>.
 */
class EclipseMarkerStripePainter implements MarkerStripePainter {
    public void paint(MarkerArea markerArea, Graphics g, Marker marker, Rectangle rect) {
        Color color = markerArea.getColor(marker.getType());
        Color fillColor = ColorUtils.getDerivedColor(color, 0.875f);
        g.setColor(fillColor);
        g.fillRect(rect.x + 1, rect.y, rect.width - 2, rect.height);
        g.setColor(color);
        g.drawRect(rect.x + 1, rect.y, rect.width - 2, rect.height);
    }
}
