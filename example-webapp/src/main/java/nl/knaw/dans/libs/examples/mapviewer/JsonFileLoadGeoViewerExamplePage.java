/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package nl.knaw.dans.libs.examples.mapviewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.libs.map.LazyLoadingMapViewerPanel;
import nl.knaw.dans.libs.map.Marker;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFileLoadGeoViewerExamplePage extends WebPage {
    private static final long serialVersionUID = 1L;
    private static final String JSON_FILE = "geolocation_example.json";
    
    LazyLoadingMapViewerPanel viewer = null;
    Label loadedMsg = null;

    public JsonFileLoadGeoViewerExamplePage(final PageParameters parameters) {
        add(new Label("message", "Loading markers from JSON: " + JSON_FILE));

        loadedMsg = new Label("loadedMsg", "...");
        loadedMsg.setOutputMarkupId(true);
        add(loadedMsg);

        initGeoViewer();
    }

    private void initGeoViewer() {
        viewer = new LazyLoadingMapViewerPanel("geoviewer") {
            private static final long serialVersionUID = 2887353735740669595L;

            @Override
            protected List<Marker> produceMarkers() {
                List<Marker> markerList = new ArrayList<Marker>();

                // load the json file and construct the markers...
                // open the file and parse the json
                // in this example from a java project resources folder
                File file = new File( getClass().getClassLoader().getResource(JSON_FILE).getFile());
                String jsonStr = readFile(file);

                try {
                    JSONObject rootObject = new JSONObject(jsonStr);
                    JSONArray markerArray = rootObject.getJSONArray("markers");
                    for (int i = 0; i < markerArray.length(); i++) {
                        JSONObject o = markerArray.getJSONObject(i);
                        System.out.println(o);
                        String info = o.getString("info");
                        Double lon = o.getDouble("lon");
                        Double lat = o.getDouble("lat");
                        markerList.add(new Marker(lon, lat, info));
                    }
                }
                catch (JSONException e) {
                    // unable to parse json
                    e.printStackTrace();
                    // nothing, just return an empty list
                }
                return markerList;
            }

            @Override
            public void addMarkers(List<Marker> markers, AjaxRequestTarget target) {
                super.addMarkers(markers, target);

                // update the loadedMsg
                java.util.Date date = new java.util.Date();
                long time = date.getTime();
                // convert to UTC and format
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy.MM.dd - HH:mm:ss z");
                DateTime dUtc = new DateTime(time).toDateTime(DateTimeZone.UTC);
                Label newloadedMsg = new Label("loadedMsg", "Data on " + dUtc.toString(fmt).toString());
                newloadedMsg.setOutputMarkupId(true);
                loadedMsg.replaceWith(newloadedMsg);
                loadedMsg = newloadedMsg;
                target.addComponent(newloadedMsg);
            }

        };
        add(viewer);
    }

    public static String readFile(File file) {
        StringBuffer buffer = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                buffer.append(line);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (in != null)
                try {
                    in.close();
                }
                catch (IOException e) {}
        }

        return buffer.toString();
    }

}
