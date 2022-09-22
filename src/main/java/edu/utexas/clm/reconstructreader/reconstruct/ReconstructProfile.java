/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2011 - 2022 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.utexas.clm.reconstructreader.reconstruct;

import edu.utexas.clm.reconstructreader.Utils;

import org.w3c.dom.Element;

public class ReconstructProfile {
    private final Element profile;
    private final int id, oid;
    private final ReconstructTranslator translator;
    private final double mag;
    private final ReconstructSection section;

    public ReconstructProfile(final Element e, final ReconstructTranslator t, ReconstructSection sec)
    {
        double m;

        translator = t;
        profile = e;
        section = sec;
        id = translator.nextOID();
        oid = translator.nextOID();

        m = Utils.getMag(e);
        mag = Double.isNaN(m) ? t.getMag() : m;
    }

    public int getOID()
    {
        return oid;
    }

    public int getID()
    {
        return id;
    }

    public void appendXML(final StringBuilder sb)
    {
        String colorHex = Utils.hexColor(profile.getAttribute("fill"));
        double[] pts = Utils.getTransformedPoints(profile, section.getHeight(), mag);
        double[] wh = Utils.getPathExtent(pts);
        double width = wh[0];
        double height = wh[1];

        sb.append("<t2_profile\n" +
                "oid=\"").append(getOID()).append("\"\n" +
                "width=\"").append(width).append("\"\n" +
                "height=\"").append(height).append("\"\n" +
                "transform=\"matrix(1.0,0.0,0.0,1.0,0,0)\"\n" +
                "title=\"").append(profile.getAttribute("name")).append("\"\n" +
                "links=\"\"\n" +
                "style=\"fill:none;stroke-opacity:1.0;stroke:#").append(colorHex)
                .append(";stroke-width:1.0px;\"\n");

        sb.append("d=\"");
        Utils.appendBezierPathXML(sb, pts);
        sb.append("\"\n>\n" +
            "</t2_profile>\n");
    }

}
