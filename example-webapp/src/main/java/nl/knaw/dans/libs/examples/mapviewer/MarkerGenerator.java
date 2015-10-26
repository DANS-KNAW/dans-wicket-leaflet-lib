/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package nl.knaw.dans.libs.examples.mapviewer;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.libs.map.Marker;

public class MarkerGenerator {

    // produce random markers for testing
    public static List<Marker> getRandomMarkers(int num) {
        List<Marker> loc = new ArrayList<Marker>();
        double lon, lat;
        for (int i = 0; i < num; i++) {
            lon = 4.0 + Math.random() * 15.0;
            lat = 45.0 + Math.random() * 15.0;
            loc.add(new Marker(lon, lat, "Marker: " + i));

            // delay... to simulate a real situation with data processing
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException ie) {
                // Ignore exception
            }
        }
        return loc;
    }
}
