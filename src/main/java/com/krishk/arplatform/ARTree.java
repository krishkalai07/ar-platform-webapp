package com.krishk.arplatform;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.security.MessageDigest;

@SuppressWarnings("ALL")
public class ARTree {
    /**
     * Root node of the AR tree.
     */
    private ARNode rootNode;

    /**
     * structuresETag = MD5(structure's.eTag)
     * Cumilative eTag of all structures under the root node.
     * The purpose is to keep track of any changes to the structures.
     */
    private String structuresETag = "";

    /**
     * JSON formatted structures data. Contains structure's eTag.
     * The putpose is to optimize interaction between client and server.
     */
    private ArrayList<String> structuresList = new ArrayList<>();

    /**
     * JSON formatted data repersenting the nodes that the user is in.
     */
    private ArrayList<String> locateList = new ArrayList<>();

    /**
     * Constructs the AR Tree. The tree is construted only once since it is Singleton.
     */
    public ARTree() {
        this.rootNode = new ARNode(Type.PLATFORM, "Structures", "", 0, "");
        String polygon_data = "";

        //School
        polygon_data = "37.388616, -122.110888, 37.388610, -122.107366, 37.385015, -122.107379, 37.385034, -122.110901, 37.388616, -122.110888";
        ARNode los_altos_high_school = new ARNode(Type.STRUCTURE, "Los Altos high School", polygon_data, 0, "Welcome to Los Altos High School. ");
        rootNode.addChildNode(los_altos_high_school);

        polygon_data = "37.385647, -122.109474, 37.385727, -122.109137, 37.385686, -122.109118, 37.385704, -122.109037, 37.385587, -122.108991, 37.385599, -122.108940, 37.385519, -122.108905, 37.385536, -122.108825, 37.385417, -122.108779, 37.385364, -122.108982, 37.385406, -122.108995, 37.385337, -122.109252, 37.385374, -122.109271, 37.385354, -122.109360, 37.385647, -122.109474";
        ARNode eagle_theater = new ARNode(Type.BUILDING, "Eagle Theater", polygon_data, 0, "Eagle Theater");
        los_altos_high_school.addChildNode(eagle_theater);

        polygon_data = "37.386006, -122.109427, 37.386649, -122.109426, 37.386652, -122.109093, 37.386463, -122.109093, 37.386463, -122.109030, 37.386149, -122.109028, 37.386152, -122.108939, 37.386007, -122.108936, 37.386006, -122.109427";
        ARNode wing700 = new ARNode(Type.BUILDING, "700 Wing", polygon_data, 0, "You are in the 700 Wing, ");
        los_altos_high_school.addChildNode(wing700);

        polygon_data = "";
        ARNode wing700_floor1 = new ARNode(Type.FLOOR, "Science floor", polygon_data, 47.7, "first floor, ");
        wing700.addChildNode(wing700_floor1);

        //polygon_data = "";
        //ARNode wing700_floor2 = new ARNode(Type.FLOOR, "Math floor", polygon_data, 11, "All math buildings are on this floor");
        //wing700.addChildNode(wing700_floor2);

        //polygon_data = "37.38642893, -122.10951252, 37.38649573, -122.10958477, 37.38646007,-122.10937816, 37.38648869,-122.10947966, 37.38643915,-122.10937413, 37.38642285,-122.10940775, 37.38640919,-122.10934203, 37.38642432,-122.10935737, 37.38642893,-122.10951252";
        polygon_data = "37.386616, -122.109424, 37.386475, -122.109423, 37.386478, -122.109306, 37.386616, -122.109306, 37.386616, -122.109424";
        ARNode wing700_floor1_room710 = new ARNode(Type.ROOM, "Room 710", polygon_data, 11, "room 710. Enjoy your Chemistry or ASI classes with Mr. Dressen.");
        wing700_floor1.addChildNode(wing700_floor1_room710);

        //House
        polygon_data = "37.404406, -122.079025, 37.403889, -122.079055, 37.403893, -122.078698, 37.404378, -122.078683, 37.404406, -122.079025";
        ARNode the_house = new ARNode(Type.STRUCTURE, "TheHouse", polygon_data, 0, "The house");
        rootNode.addChildNode(the_house);

        polygon_data = "37.404178, -122.078966, 37.404183, -122.079059, 37.404183, -122.079059, 37.404124, -122.078976";
        ARNode front_yard = new ARNode(Type.BUILDING, "FrontYard", polygon_data, 0, "Front yard");

        polygon_data = "37.404115, -122.078849, 37.404116, -122.078803, 37.404177, -122.078794, 37.404176, -122.078853, 37.404115, -122.078849,";
        ARNode back_yard = new ARNode(Type.BUILDING, "BackYard", polygon_data, 0, "Back yard");

        polygon_data = "37.404110, -122.078707, 37.404115, -122.078798, 37.404178, -122.078794, 37.404169, -122.078712, 37.404110, -122.078707";
        ARNode carport = new ARNode(Type.BUILDING, "Carport", polygon_data, 0, "Carport");

        polygon_data = "37.404178, -122.078966, 37.404123, -122.078969, 37.404113, -122.078743, 37.404175, -122.078738, 37.404178, -122.078966";
        ARNode living_area = new ARNode(Type.BUILDING, "LivingArea", polygon_data, 0, "Living area");
        the_house.addChildNode(living_area);

        ARNode floor1 = new ARNode(Type.FLOOR, "Floor 1", "", 13.0, "First floor");
        living_area.addChildNode(floor1);

        polygon_data = "37.404178, -122.078906, 37.404119, -122.078909, 37.404113, -122.078743, 37.404175, -122.078738, 37.404178, -122.078906";
        ARNode kitchen = new ARNode(Type.ROOM, "Kitchen", polygon_data, 0, "Kitchen");
        floor1.addChildNode(kitchen);

        polygon_data = "37.404178, -122.078966, 37.404123, -122.078969, 37.404119, -122.078909, 37.404178, -122.078906, 37.404178, -122.078966";
        ARNode living_room = new ARNode(Type.ROOM, "Living Room", polygon_data, 0, "Living Room");
        floor1.addChildNode(living_room);

        ARNode floor2 = new ARNode(Type.FLOOR, "Floor 2", "", 16.5, "Second floor");
        living_area.addChildNode(floor2);

        polygon_data = "37.404178, -122.078906, 37.404119, -122.078909, 37.404113, -122.078743, 37.404175, -122.078738, 37.404178, -122.078906";
        ARNode master_bedroom = new ARNode(Type.ROOM, "MasterBedroom", polygon_data, 0, "Master Bedroom");
        floor2.addChildNode(master_bedroom);

        polygon_data = "37.404178, -122.078966, 37.404123, -122.078969, 37.404119, -122.078909, 37.404178, -122.078906, 37.404178, -122.078966";
        ARNode other_bedrooms = new ARNode(Type.ROOM, "OtherBedrooms", polygon_data, 0, "Other bedrooms");
        floor2.addChildNode(other_bedrooms);

        //South Hall (Fair location)
        polygon_data = "37.327869, -121.888824, 37.329413, -121.886721, 37.328937, -121.886134, 37.327229, -121.888344, 37.327869, -121.888824";
        ARNode full_building = new ARNode(Type.STRUCTURE, "San Jose Convention Center", polygon_data, 0, "You are in the San Jose Convention Center. ");
        rootNode.addChildNode(full_building);

        polygon_data = "37.329197, -121.886725, 37.328073, -121.888139, 37.327918, -121.888110, 37.327849, -121.887987, 37.327859, -121.887852, 37.328952, -121.886417, 37.329197, -121.886725";
        ARNode south_hall = new ARNode(Type.BUILDING, "South Hall", polygon_data, 0, "Welcome to the Synopsis science fair in the South Hall. ");
        full_building.addChildNode(south_hall);

        constructStructuresData();
    }

    /**
     * Construct the <pre>structuresChildrenData</pre> and the <pre>Etag</pre>.
     */
    private void constructStructuresData() {
        String etag_string = "";
        for (int i = 0; i < rootNode.getChildrenNodes().size(); i++) {
            structuresList.add(rootNode.getChildrenNodes().get(i).toJSON());
            etag_string += rootNode.getChildrenNodes().get(i).getId();
        }

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
            structuresETag = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 failed me");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Public function to fill <pre>locateList</pre>.
     *
     * @param id ID of the structure
     * @param point User's current geocoordinate
     * @param elevation User's current elevation
     */
    public void locatePoint(String id, GeoPoint point, double elevation) {
        //System.out.println("Entered locatePoint");

        ARNode node = getNodeFromID(id);

        if (node != null) {
            //System.out.println("locatePoint id: " + node.getId());
            locateList.clear();
            //System.out.println("List (clear): " + locateList);
            traverse(node, point, elevation);
            System.out.println("List: " + locateList);
            System.out.println("Length " + locateList.size());
        }
        else {
            System.out.println("Node is null");
        }
    }

    /**
     * Method to determine if the ID matches any of the structures' ID
     *
     * @param id ID of the structure
     * @return The structure node with the corresponding ID as <pre>id</pre>, or <pre>null</pre> if no node exists
     */
    private ARNode getNodeFromID(String id) {
        //System.out.println("Entered getNodeFromID " + id);
        for (ARNode node: rootNode.getChildrenNodes()) {
            //System.out.println("getNode " + node.getName() + " " + node.getId());
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Traverse the tree, and append to <pre>locateList</pre> is the user is inside the node
     * @param node The node to be tested and added.
     * @param point The user's current location.
     * @param elevation The user's current elevation.
     */
    private void traverse(ARNode node, GeoPoint point, double elevation) {
        if (node == null) {
            return;
        }
        //System.out.println("Traverse name: " + node.getName());
        //System.out.println("Traverse children: " + node.getChildrenNodes());
        if (node.isInsidePolygon(point)) {
            if (node.getType() == Type.FLOOR) {
                //System.out.println("I should not be in here.");
                if (node.isInFoor(elevation)) {
                    locateList.add(node.toJSON());
                }
                else {
                    return;
                }
            }
            else {
                locateList.add(node.toJSON());
            }
        }
        for (ARNode child : node.getChildrenNodes()) {
            traverse(child, point, elevation);
        }
    }


    public String getStructuresETag() {
        return structuresETag;
    }

    public ArrayList<String> getLocateList() {
        return locateList;
    }

    public ArrayList<String> getStructuresList() {
        return structuresList;
    }

    @Override
    public String toString() {
        return rootNode.toString();
    }
}