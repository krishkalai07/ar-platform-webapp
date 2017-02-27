package com.krishk.arplatform;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.*;

import static java.lang.Math.min;
import static java.lang.Math.max;

@SuppressWarnings("ALL")
enum Type {
    PLATFORM(0), STRUCTURE(1), BUILDING(2), FLOOR(3), ROOM(4);

    private int value;
    Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

@SuppressWarnings("ALL")
class GeoPoint {
    private double latitude, longitude;

    GeoPoint (String s_point) {
        s_point = s_point.substring(1, s_point.length()-1);
        String delims = "[,]";
        String[] string_array = s_point.split(delims);

        this.latitude = Double.parseDouble(string_array[0]);
        this.longitude = Double.parseDouble(string_array[1]);
    }

    GeoPoint (String s_latitude, String s_longitude) {
        this.latitude = Double.parseDouble(s_latitude);
        this.longitude = Double.parseDouble(s_longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }
}

@SuppressWarnings("ALL")
class ARNode {
    /**
     * An ID created using MD5 based on the name.
     */
    private String id;

    /**
     * An unique name.
     */
    private String name;

    /**
     * Enum type, represents platform, structure, building, floor, or room.
     */
    private Type type;

    /**
     * A collection of coordinates representing a polygon.
     */
    private ArrayList<GeoPoint> polygon;

    /**
     * The elevation of the floor at standing height.
     */
    private double elevation;

    /**
     * The information about the building.
     */
    private String information;

    /**
     * The refernece to the list of children nodes.
     */
    private ArrayList<ARNode> children_nodes;

    /**
     * Contrsucts the node
     *
     * @param type A choice of platform, structure, building, floor, or room.
     * @param name The name of the node as a String
     * @param polygon_data The data of the polygon. If the type is floor, this field should be an empty string.
     * @param floor_height The height of the floor. If the type is not a floor, this should be 0.
     * @param information Information of the building.
     */
    ARNode (Type type, String name, String polygon_data, double elevation, String information) {
        this.type = type;
        this.name = name;
        this.polygon = constructPolygon(polygon_data);
        this.elevation = elevation;
        this.information = information;
        this.id = generateID();
        this.children_nodes = new ArrayList<ARNode>();
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public ArrayList<GeoPoint> getPolygon() {
        return polygon;
    }

    public double getElevation() {
        return elevation;
    }

    public String getInformation() {
        return information;
    }

    public ArrayList<ARNode> getChildrenNodes() {
        return children_nodes;
    }

    public void setId() {
        this.id = generateID();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setPolygon(ArrayList<GeoPoint> polygon) {
        this.polygon = polygon;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * Add a child to the list of children node.
     *
     * @param node The node to be added
     */
    void addChildNode(ARNode node) {
        if (node != null) {
            children_nodes.add(node);
        }
    }

    /**
     * Removes a child node from the list of children nodes.
     *
     * @param node The node to be removed.
     */
    void removeChildNode(ARNode node) {
        if (node != null) {
            children_nodes.remove(node);
        }
    }

    /**
     * Contructs an ArrayList of polygons from a string of geo-coordiantes
     *
     * @param polygon_data The String of coordinates to be converted. The coordinates must be comma seperated.
     * @return ArrayList of GeoPoints.
     */
    private ArrayList<GeoPoint> constructPolygon (String polygon_data) {
        ArrayList<GeoPoint> polygon = new ArrayList<>();
        String[] tokens;
        String delimiters = "[,]";

        if (polygon_data.length() == 0){
            return polygon;
        }

        tokens = polygon_data.split(delimiters);

        for (int i = 0; i < tokens.length; i+=2) {
            String latitude = tokens[i];
            String longitude = tokens[i+1];

            polygon.add(new GeoPoint(latitude, longitude));
        }

        return polygon;
    }

    /**
     * Returns true if the given point is part (inside of) the polygon.
     *
     * @param point The point to be tested
     * @return true if the point is inside the polygon
     */
    public boolean isInsidePolygon(GeoPoint point) {
        int counter = 0;
        int i;
        double x_intercept;
        GeoPoint p1, p2;

        if (getPolygon().size() == 0) {
            return true;
        }

        p1 = polygon.get(0);
        for (i = 1; i < polygon.size(); i++) {
            p2 = polygon.get(i%(polygon.size()-1));
            if (point.getLongitude() >= min(p1.getLongitude(), p2.getLongitude())) {
                if (point.getLongitude() <= max(p1.getLongitude(), p2.getLongitude())) {
                    if (point.getLatitude() <= max(p1.getLatitude(), p2.getLatitude())) {
                        if (p1.getLongitude() != p2.getLongitude()) {
                            x_intercept = (point.getLongitude() - p1.getLongitude()) * (p2.getLatitude() - p1.getLatitude()) / (p2.getLongitude() - p1.getLongitude()) + p1.getLatitude();
                            if (p1.getLatitude() == p2.getLatitude() || point.getLatitude() <= x_intercept) {
                                counter++;
                            }
                        }
                    }
                }
            }
            p1 = p2;
        }
        return counter % 2 != 0;
    }

    /**
     * Checks if the user's elevation is within 1 meter of the node's elevation. This function will be called only when testing floors.
     *
     * @param elevation The user's current elevation.
     * @return True if the user's elevation is within 1 meter of the floor's elevation.
     */
    boolean isInFoor(double elevation) {
        return Math.abs(elevation - this.elevation) < 1;
    }

    @Override
    public String toString() {
        String ret_value = "";

        ret_value += "ID = " + id + "\n";
        ret_value += "Name = " + name + "\n";
        ret_value += "Type = " + type + "\n";
        ret_value += "Polygon = " + polygon.toString() + "\n";
        ret_value += "Floor_height = " + elevation + "\n";
        ret_value += "information = " + information + "\n";
        ret_value += "Children nodes = " + children_nodes + "\n";

        return ret_value;
    }

    /**
     * Convert the object to a String in JSON format
     *
     * @return A String in Json format.
     */
    String toJSON() {
        JSONObject return_object = new JSONObject();
        return_object.put("Type", type.getValue());
        return_object.put("ID", id);
        return_object.put("Name", name);

        String polygon_string = "";
        for (int i = 0; i < polygon.size(); i++) {
            polygon_string += polygon.get(i).getLatitude() + "," + polygon.get(i).getLongitude();
            if (i < polygon.size()-1) {
                polygon_string += ",";
            }
        }
        return_object.put("Polygon", polygon_string);
        return_object.put("Elevation", elevation);
        return_object.put("Information", information);

        String id_string = "";
        for (int i = 0; i < children_nodes.size(); i++) {
            id_string += children_nodes.get(i).getId();
            if (i < children_nodes.size()-1) {
                id_string += ",";
            }
        }
        return_object.put("Children IDs", id_string);

        return return_object.toString();
    }

    /**
     * Generates an ID from the name.
     *
     * @return A string ID.
     */
    private String generateID() {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(getName().getBytes());
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xFF & digest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 failed me");
        }
        //System.out.println("id: " + hexString.toString());
        return hexString.toString();
    }
}