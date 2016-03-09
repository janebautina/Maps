package lifeexpectancy;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class LifeExpectancy extends PApplet{
private static final long serialVersionUID = 1L;
UnfoldingMap map;
 Map<String, Float> lifeExpByCountry;//String CountryID, float lifeExp
 List<Feature> countries;
 List<Marker> countryMarkers;
 public void setup() {
	 size(800, 600, OPENGL);
	 map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
	 MapUtils.createDefaultEventDispatcher(this, map);
	 lifeExpByCountry = loadLifeExpectanceFromCSV("LifeExpectancyWorldBank.csv");
	 countries = GeoJSONReader.loadData(this, "countries.geo.json");
	 countryMarkers = MapUtils.createSimpleMarkers(countries);
	 map.addMarkers(countryMarkers);
	 shadeCounties();
 }
 private void shadeCounties() {
	 for (Marker marker: countryMarkers){
		 String countryId = marker.getId();
		 for (Entry<String, Float> entry : lifeExpByCountry.entrySet()) {
			    String key = entry.getKey().toString();
			    Float value = entry.getValue();
			    System.out.println("key, " + key + " value " + value);
			}
		 //System.out.println(lifeExpByCountry.get());
		 if (lifeExpByCountry.containsKey(countryId)){
			 float lifeExp = lifeExpByCountry.get(countryId);
			 int colorLevel = (int)map(lifeExp, 40, 90, 10, 255);
			 marker.setColor(color(255-colorLevel, 100, colorLevel));
		 } else{
			 marker.setColor(color(150, 150, 150));
		 }
	 }
}
private Map<String, Float> loadLifeExpectanceFromCSV(String fileName) {
	Map<String, Float> lifeExpMap = new HashMap<String, Float>();
	Table table = loadTable(fileName);
	for  (TableRow row: table.rows()){
        if (!row.getString(4).equals(".."))
        	lifeExpMap.put(row.getString(3), row.getFloat(4));
	}
	return lifeExpMap;
}
public void draw() {
	 map.draw();
 }
}
