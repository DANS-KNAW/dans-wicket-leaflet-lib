/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package nl.knaw.dans.libs.examples.mapviewer;

import java.util.List;

import nl.knaw.dans.libs.map.LazyLoadingMapViewerPanel;
import nl.knaw.dans.libs.map.Marker;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 */
public class LazyLoadingMapViewerPage extends WebPage {
    private static final long serialVersionUID = 1L;
    private static final int NUM_MARKERS = 5000;

    LazyLoadingMapViewerPanel viewer = null;
    Component indicator = null;

    public LazyLoadingMapViewerPage(final PageParameters parameters) {
        add(new Label("message", Integer.toString(NUM_MARKERS)));
        initGeoViewer();
    }

    private void initGeoViewer() {
        viewer = new LazyLoadingMapViewerPanel("geoviewer") {
            private static final long serialVersionUID = 2887353735740669595L;

            @Override
            protected List<Marker> produceMarkers() {
                return MarkerGenerator.getRandomMarkers(NUM_MARKERS);
            }
        };
        add(viewer);
    }
}
