# DANS Wicket Leaflet Library

With this Java library you can place an interactive geographical map on your (Wicket) web page 
and have markers placed on it. 
This functionality is build with the [Leaflet JavaScript library](http://leafletjs.com) 
and by default it shows the [OpenStreatMap (OSM)](http://www.openstreetmap.org/about) map tiles.
Leaflet was simpler to use than OpenLayers and gives good quality default behaviour. 
The addition of the 'Leaflet.markercluster' plugin was essential for handling large amounts of markers on the map. 


This is a Java library (jar) for use with the [Apache Wicket](https://wicket.apache.org/) web application framework
At this time we are still using Wicket1.4!

Features
- Shows markers with 'balloon' icons, and pop-ups when clicked.
- Marker clustering and lazy loading of markers 
This makes the map work even when there are thousands of markers on it. 

Issues: you cannot add more than one panel on a page!
- A lot of default settings and not much configurable, but that was a design choice. 


## Building
This library is built using [Apache Maven](http://maven.apache.org/).
To build it run:

    mvn clean install

## Using in your Maven project
    <dependency>
        <groupId>nl.knaw.dans.libs</groupId>
        <artifactId>wicket-leaflet-mapviewer</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>