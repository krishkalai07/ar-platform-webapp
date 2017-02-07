package com.krishk.arplatform;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.security.MessageDigest;

@SuppressWarnings("ALL")
public class Structures {
    private HashMap<String, Node> IDMap = new HashMap<>();
    private Node platform_node; //Root node of the tree
    private ArrayList<String> json_list = new ArrayList<>();
    private String eTag = "";
    private String node_json_data = "";

    /**
     * Constructs the Structrues with 2 buildings.
     */
    Structures() {
        this.platform_node = new Node(Type.PLATFORM, "Structures", "", 0, "");

        String polygon_data = "37.388616, -122.110888, 37.388610, -122.107366, 37.385015, -122.107379, 37.385034, -122.110901, 37.388616, -122.110888";
        Node los_altos_high_school = new Node(Type.STRUCTURE, "Los Altos high School", polygon_data, 0, "Mah school");
        platform_node.addChildNode(los_altos_high_school);
        IDMap.put(los_altos_high_school.getId(), los_altos_high_school);

        polygon_data = "37.385647, -122.109474, 37.385727, -122.109137, 37.385686, -122.109118, 37.385704, -122.109037, 37.385587, -122.108991, 37.385599, -122.108940, 37.385519, -122.108905, 37.385536, -122.108825, 37.385417, -122.108779, 37.385364, -122.108982, 37.385406, -122.108995, 37.385337, -122.109252, 37.385374, -122.109271, 37.385354, -122.109360, 37.385647, -122.109474";
        Node eagle_theater = new Node(Type.BUILDING, "Eagle Theater", polygon_data, 0, "Mah school");
        los_altos_high_school.addChildNode(eagle_theater);
        IDMap.put(eagle_theater.getId(), eagle_theater);

        polygon_data = "37.386006, -122.109427, 37.386649, -122.109426, 37.386652, -122.109093, 37.386463, -122.109093, 37.386463, -122.109030, 37.386149, -122.109028, 37.386152, -122.108939, 37.386007, -122.108936, 37.386006, -122.109427";
        Node wing700 = new Node(Type.BUILDING, "700 Wing", polygon_data, 0, "Science and math buildings");
        los_altos_high_school.addChildNode(wing700);
        IDMap.put(wing700.getId(), eagle_theater);

        polygon_data = "";
        Node wing700_floor1 = new Node(Type.FLOOR, "Science floor", polygon_data, 10, "All science buildings are on this floor");
        wing700.addChildNode(wing700_floor1);
        IDMap.put(wing700_floor1.getId(), wing700_floor1);

        polygon_data = "";
        Node wing700_floor2 = new Node(Type.FLOOR, "Math floor", polygon_data, 11, "All math buildings are on this floor");
        wing700.addChildNode(wing700_floor2);
        IDMap.put(wing700_floor2.getId(), wing700_floor2);

        //polygon_data = ""; //TODO: needs to be created. maps.google.com is not accurate enough
        //Structure wing700_floor2_room710 = new Structure(Type.ROOM, "Room 710", polygon_data, 11, "Mr.Dressen's Room");
        //wing700_floor1.addChildNode(wing700_floor2_room710);

        //House
        polygon_data = "37.388616, -122.110888, 37.388610, -122.107366, 37.385015, -122.107379, 37.385034, -122.110901, 37.388616, -122.110888";
        Node house = new Node(Type.STRUCTURE, "House", polygon_data, 0, "A House");
        platform_node.addChildNode(house);
        IDMap.put(house.getId(), house);


        constructJSONDataAndEtag();
    }

    /**
     * Public function to determine if the person is in the polygon
     *
     * @param point The user's current location geo-coordinate
     */
    public void locateStructure(GeoPoint point) {
        locateStructuresHelper(point, platform_node);
    }

    /**
     * Internal recursive function for locating structures.
     *
     * @param point The current user's geo-coordinate
     * @param node To node to be tested on
     */
    private void locateStructuresHelper(GeoPoint point, Node node) {
        // Base case
        if (node == null) {
            return;
        }

        if (node.isInsidePolygon(point)) {
            if (node != platform_node) {
                json_list.add(node.toJSON());
            }
            for (int i = 0; i < node.getChildrenNodes().size(); i++) {
                locateStructuresHelper(point, node.getChildrenNodes().get(i));
            }
        }
    }

    /**
     * Adds all the structures to the json_list
     */
    public void handleOutsideStructures() {
        for (Node structure : platform_node.getChildrenNodes()) {
            json_list.add(structure.toJSON());
        }
    }

    /**
     * Creates the node_json_data and the Etag
     */
    private void constructJSONDataAndEtag() {
        ArrayList list = new ArrayList();
        String etag_string = "";
        for (int i = 0 ; i < platform_node.getChildrenNodes().size(); i++) {
            list.add(platform_node.getChildrenNodes().get(i).toJSON());
            etag_string += platform_node.getChildrenNodes().get(i).getId();
        }
        node_json_data = list.toString();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(etag_string.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xFF & digest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            eTag = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 failed me");
            e.printStackTrace(System.out);
        }

    }

    /**
     * Print the entire map.
     *
     * @param mp the map to be printed.
     */
    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("Key = " + pair.getKey() + "\nValue = " + ((Node) (pair.getValue())).getName());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public HashMap<String, Node> getIDMap() {
        return IDMap;
    }

    public String getStructureJsonData() {
        return node_json_data;
    }

    public String geteTag() {
        return eTag;
    }

    public ArrayList<String> getJsonList() {
        return json_list;
    }
}
