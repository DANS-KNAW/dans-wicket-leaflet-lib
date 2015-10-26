/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package nl.knaw.dans.libs.examples.mapviewer;

import nl.knaw.dans.libs.map.MapViewerPanel;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class AjaxMapViewerPage extends WebPage {
    private static final long serialVersionUID = 1L;
    private static final int NUM_MARKERS = 20;

    MapViewerPanel viewer = null;
    Component indicator = null;

    // Note that in this simple example 
    // the added markers are not kept in a model, if you reload the page everything is lost!
    
    public AjaxMapViewerPage(final PageParameters parameters) {
        add(new Label("message", Integer.toString(NUM_MARKERS)));
        initGeoViewer();

        @SuppressWarnings("rawtypes")
        IndicatingAjaxLink link = new IndicatingAjaxLink("add") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                viewer.addMarkers(MarkerGenerator.getRandomMarkers(NUM_MARKERS), target);
            }
        };
        link.setOutputMarkupId(true);
        add(link);

        @SuppressWarnings("rawtypes")
        IndicatingAjaxLink clearLink = new IndicatingAjaxLink("clear") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                viewer.clearMarkers();
            }
        };
        clearLink.setOutputMarkupId(true);
        add(clearLink);

    }

    private void initGeoViewer() {
        // Add Viewer without markers
        viewer = new MapViewerPanel("geoviewer");
        add(viewer);
    }

}
