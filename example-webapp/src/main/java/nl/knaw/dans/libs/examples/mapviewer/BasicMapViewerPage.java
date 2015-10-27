/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package nl.knaw.dans.libs.examples.mapviewer;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.libs.map.MapViewerPanel;
import nl.knaw.dans.libs.map.Marker;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.util.ListModel;

public class BasicMapViewerPage extends WebPage {
    private static final long serialVersionUID = 1L;
    private static final int NUM_MARKERS = 20;

    public BasicMapViewerPage(final PageParameters parameters) {
        add(new Label("message", Integer.toString(NUM_MARKERS)));
        initGeoViewer();
    }

    private void initGeoViewer() {
        MapViewerPanel viewer = new MapViewerPanel("geoviewer", new ListModel<Marker>() {
            private static final long serialVersionUID = -8596739773617282567L;

            @Override
            public List<Marker> getObject() {
                List<Marker> loc = new ArrayList<Marker>();
                loc.addAll(MarkerGenerator.getRandomMarkers(NUM_MARKERS));
                loc.add(new Marker(0.0, 0.0, "<h2>More HTML content at origin</h2><p><a href='https://en.wikipedia.org/wiki/Lorem_ipsum'>Lorem ipsum</a> dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>"));
                return loc;
            }
        });

        add(viewer);
    }

}
