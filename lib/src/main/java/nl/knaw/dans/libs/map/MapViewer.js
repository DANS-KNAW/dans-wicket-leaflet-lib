/*******************************************************************************
 * Copyright 2015 DANS - Data Archiving and Networked Services
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
// geoMapViewer
// the one and only global var serves as a sort of 'namespace'
var geoMapViewer = {
	// Works with only one map/viewer per page,
	// but should have a list with JS objects for each view and have a lookup by
	// the markup id
	Instance : null,
	// Initialize the (single) viewer
	init : function(id, options) {
		this.Instance = new GeoMapViewer(id, options);
	},

	// Add a marker at the given location on the map
	addMarker : function(lon, lat, info) {
		this.Instance.addMarker(lon, lat, info);
	},

	addMarkers : function(markerList) {
		this.Instance.addMarkers(markerList);
	},

	// Remove all markers from the map
	clearMarkers : function() {
		this.Instance.clearMarkers();
	},

	clustering : false,

	// Marker Icons can be shared among viewers
	DefaultMarkerIconPath : "", // 'global setting' must be set prior to
								// creating viewers!
	DefaultMarkerIcon : null,
	LetterIcons : [],
	hasInitializedMarkerIcons : false
};

// The viewer object/class (constructor like function)
//
// Note:
// The maps are in (google's) spherical mercator (EPSG:900913)
// But the Marker locations are in WGS84 (EPSG:4326)
function GeoMapViewer(id, options) {
	// this.clustering = geoMapViewer.clustering;
	this.options = options || {
		// default options here
		clustering : geoMapViewer.clustering
	};
	// use the options
	this.clustering = this.options.clustering;

	// Initialize the (map) viewer
	this.map = L.map(id).setView([54.0, 9.0], 3);
	L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
	    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
	}).addTo(this.map);

	if (this.clustering)
		this.markers = L.markerClusterGroup();
	else
		this.markers = L.featureGroup();
	
	this.map.addLayer(this.markers);
	

	// Add a marker at the given location on the map
	this.addMarker = function(lon, lat, info) {
		// REMOVE?
	};

	// Remove all markers from the map
	this.clearMarkers = function() {
		this.markers.clearLayers();
	};

	// example
	// [{lon:52.0,lat:4.9,info:"A"},{lon:52.01,lat:4.91,info:"B"}]
	// could compress JSON even further and make each marker an array!
	this.addMarkers = function(markerList) {
		var markerArray = [];
		for (var i = 0; i < markerList.length; i++) {
			var m = markerList[i];
			markerArray.push(L.marker([m.lat, m.lon])
					.bindPopup(m.info));
		}
	
		for (var i = 0; i < markerArray.length; i++) 
			this.markers.addLayer( markerArray[i] );

		// zoom to extend; show all markers but zoomed in as much as possible
		this.map.fitBounds(this.markers.getBounds());
	};

}
