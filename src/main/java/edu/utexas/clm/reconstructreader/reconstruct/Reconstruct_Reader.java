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

import ij.IJ;
import ij.gui.MessageDialog;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import ini.trakem2.Project;

public class Reconstruct_Reader implements PlugIn
{

    public void run(final String arg) {
        String fname;
        ReconstructTranslator translator;
        long sTime;

        if (arg.equals(""))
        {
            OpenDialog od = new OpenDialog("Select Reconstruct ser File", "");
            fname = od.getDirectory() + od.getFileName();
        }
        else
        {
            fname = arg;
        }

        IJ.log("Creating Reconstruct Translator.");
        translator = new ReconstructTranslator(fname);
        IJ.log("Done.");

        sTime = System.currentTimeMillis();
        IJ.log("Beginning translation");
        if (translator.process())
        {
            String projectFileName;
            float pTime = ((float)(System.currentTimeMillis() - sTime)) / 1000;
            IJ.log("Done translating, took " + pTime + " seconds. Writing XML");
            projectFileName = translator.writeTrakEM2();

            if (!translator.getPostTranslationMessage().isEmpty())
            {
                IJ.showMessage(translator.getPostTranslationMessage());
            }

            if (projectFileName != null)
            {
                Project t2p;
                IJ.log("Opening project " + projectFileName);
                t2p = Project.openFSProject(projectFileName);
                t2p.getRootLayerSet().setMinimumDimensions();

            }
        }
        else
        {
            IJ.log(translator.getLastErrorMessage());
            new MessageDialog(IJ.getInstance(), "Error", "Encountered an Error while translating");
        }

    }

}
