/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package nl.knaw.dans.libs.map;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.libs.map.Marker;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Note: the panel must be placed in a html-div element, and it's size can be set by using CSS on that div
 */
public class MapViewerPanel extends Panel {
    private static final Logger logger = LoggerFactory.getLogger(MapViewerPanel.class);
    private static final long serialVersionUID = 2436241416837489208L;

    private static final String MAPVIEWER_CSSFILE = "MapViewer.css";
    private static final String MAPVIEWER_JSFILE = "MapViewer.js";
    private static final ResourceReference MAPVIEWER_JS = new ResourceReference(MapViewerPanel.class, MAPVIEWER_JSFILE);
    private static final String LEAFLET_JSFILE = "leaflet/leaflet.js";
    private static final String LEAFLET_CSSFILE = "leaflet/leaflet.css";
    private static final String LEAFLET_MARKERCLUSTER_JSFILE = "leaflet/markercluster/leaflet.markercluster.js";
    private static final String LEAFLET_MARKERCLUSTER_CSSFILE = "leaflet/markercluster/MarkerCluster.css";
    private static final String LEAFLET_MARKERCLUSTER_DEFAULT_CSSFILE = "leaflet/markercluster/MarkerCluster.Default.css";

    protected static final String JS_MAPVIEWER_OBJ = "geoMapViewer";

    private List<Marker> markers;
    private boolean initialMarkers = false;

    // A model-less constructor, just for displaying a map
    public MapViewerPanel(String id) {
        super(id);

        // No Model, so create empty list, markers can be added later on via Ajax
        markers = new ArrayList<Marker>();

        this.setOutputMarkupId(true);
        add(new MapViewerBehavior());
    }

    public MapViewerPanel(String id, IModel<List<Marker>> model) {
        super(id, model);
        logger.debug("Construction WITH a model");
        initialMarkers = true;

        // Note: the model is retrieved in the Behaviour renderHead

        this.setOutputMarkupId(true);
        add(new MapViewerBehavior());
    }

    class MapViewerBehavior extends AbstractBehavior {
        private static final long serialVersionUID = -7899259933312363881L;

        @Override
        public void onRendered(Component component) {
            logger.debug("onRendered");

            AjaxRequestTarget target = AjaxRequestTarget.get();
            if (target != null) {
                target.appendJavascript(getMarkersJS(markers));
            }

            super.onRendered(component);
        }

        @Override
        public void renderHead(IHeaderResponse response) {
            logger.debug("renderHead");

            response.renderCSSReference(new ResourceReference(MapViewerPanel.class, LEAFLET_CSSFILE));
            response.renderCSSReference(new ResourceReference(MapViewerPanel.class, LEAFLET_MARKERCLUSTER_CSSFILE));
            response.renderCSSReference(new ResourceReference(MapViewerPanel.class, LEAFLET_MARKERCLUSTER_DEFAULT_CSSFILE));
            // GeoViewer specific css
            response.renderCSSReference(new ResourceReference(MapViewerPanel.class, MAPVIEWER_CSSFILE));

            response.renderJavascriptReference(new ResourceReference(MapViewerPanel.class, LEAFLET_JSFILE));
            response.renderJavascriptReference(new ResourceReference(MapViewerPanel.class, LEAFLET_MARKERCLUSTER_JSFILE));
            response.renderJavascriptReference(MAPVIEWER_JS);

            // get the locations from the model,
            // which might have changed after construction of the GeoViewer
            if (MapViewerPanel.this.getDefaultModel() != null) {
                @SuppressWarnings("unchecked")
                List<Marker> modelLocations = (List<Marker>) MapViewerPanel.this.getDefaultModel().getObject();
                if (modelLocations != null)
                    markers = modelLocations;
            }

            if (initialMarkers) {
                // Initialize the viewer and also show initial markers
                response.renderOnDomReadyJavascript(getInitializationJS() + getMarkersJS(markers));
            } else {
                response.renderOnDomReadyJavascript(getInitializationJS());
                // the next line is 'commented out' because
                // 'reload this page' on browser should bring page back to initial state!
                // so any markers that where dynamically edited are lost
                // response.renderOnLoadJavascript(getMarkersJS(markers));
            }

            super.renderHead(response);
        }
    }

    public void addMarkers(List<Marker> markers, AjaxRequestTarget target) {
        this.markers.addAll(markers);

        target.appendJavascript(getMarkersJS(markers));
    }

    public void clearMarkers() {
        markers.clear(); // remove from list

        AjaxRequestTarget target = AjaxRequestTarget.get();
        if (target != null) {
            target.appendJavascript(getClearMarkersJS());
        }
    }

    public boolean hasMarkers() {
        return !markers.isEmpty();
    }

    // --- script code generators below ---

    protected String getInitializationJS() {
        String id = getMarkupId();
        logger.debug("Initializing: " + id);
        String script = "";

        // call JS function for init of the "Viewer" object
        script += JS_MAPVIEWER_OBJ + ".init(\'" + id + "\');\n";

        return script;
    }

    private static String getMarkersJS(List<Marker> markers) {
        StringBuilder script = new StringBuilder();

        if (markers.isEmpty())
            return ""; // no markers

        script.append(JS_MAPVIEWER_OBJ + ".addMarkers([");
        boolean isFirst = true;
        for (Marker marker : markers) {
            if (isFirst)
                isFirst = false;
            else
                script.append(",");

            script.append("{" + "lon:" + marker.getLon() + "," + "lat:" + marker.getLat() + "," + "info:" + "\"" + marker.getInfo().replaceAll("[\\r\\n]", " ") + "\"" + "}");
        }
        script.append("]);");
        return script.toString();
    }

    private static String getClearMarkersJS() {
        return JS_MAPVIEWER_OBJ + ".clearMarkers();";
    }
}
