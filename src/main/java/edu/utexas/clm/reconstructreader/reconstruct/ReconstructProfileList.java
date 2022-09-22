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

import java.util.ArrayList;

import org.w3c.dom.Element;

public class ReconstructProfileList implements ContourSet
{

    private final int oid, id;
    private final ReconstructTranslator translator;
    private final String name;
    private final ArrayList<ReconstructProfile> profileList;

    public ReconstructProfileList(final Element e, final ReconstructTranslator t,
                                  final ReconstructSection sec)
    {
        translator = t;
        oid = translator.nextOID();
        id = translator.nextOID();
        name = e.getAttribute("name");
        profileList = new ArrayList<ReconstructProfile>();
        addContour(e, sec);
    }

    public String getName()
    {
        return name;
    }

    public void addContour(final Element e, final ReconstructSection sec)
    {
        ReconstructProfile rp = new ReconstructProfile(e, translator, sec);
        sec.addProfile(rp);
        profileList.add(rp);
    }

    public void appendProjectXML(final StringBuilder sb) {
        sb.append("<reconstruct_open_trace id=\"").append(oid)
                .append("\" title=\"").append(name).append("\" expanded=\"true\">\n" +
                "<profile_list id=\"").append(id).append("\" expanded=\"true\">\n");
        for (ReconstructProfile profile : profileList)
        {
            sb.append("<profile id=\"").append(profile.getID()).append("\" oid=\"")
                    .append(profile.getOID()).append("\"/>\n");
        }
        sb.append("</profile_list>\n</reconstruct_open_trace>\n");
    }

    public boolean equals(final Object o)
    {
        if (o instanceof ReconstructProfileList)
        {
            ReconstructProfileList rpl = (ReconstructProfileList)o;
            return name.equals(rpl.name);
        }
        else if (o instanceof Element)
        {
            Element e = (Element)o;
            return name.equals(e.getAttribute("name"));
        }
        else
        {
            return false;
        }
    }
}
