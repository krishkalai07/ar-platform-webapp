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

    private ArrayList<String> locateList = new ArrayList<>();

    /**
     * Constructs the AR Tree. The tree is construted only once since it is Singleton.
     */
    ARTree() {
        this.rootNode = new ARNode(Type.PLATFORM, "Structures", "", 0, "");
        String polygon_data = "";
/*
        polygon_data = "37.388616, -122.110888, 37.388610, -122.107366, 37.385015, -122.107379, 37.385034, -122.110901, 37.388616, -122.110888";
        ARNode los_altos_high_school = new ARNode(Type.STRUCTURE, "Los Altos high School", polygon_data, 0, "Mah school");
        rootNode.addChildNode(los_altos_high_school);

        polygon_data = "37.385647, -122.109474, 37.385727, -122.109137, 37.385686, -122.109118, 37.385704, -122.109037, 37.385587, -122.108991, 37.385599, -122.108940, 37.385519, -122.108905, 37.385536, -122.108825, 37.385417, -122.108779, 37.385364, -122.108982, 37.385406, -122.108995, 37.385337, -122.109252, 37.385374, -122.109271, 37.385354, -122.109360, 37.385647, -122.109474";
        ARNode eagle_theater = new ARNode(Type.BUILDING, "Eagle Theater", polygon_data, 0, "Mah school");
        los_altos_high_school.addChildNode(eagle_theater);

        polygon_data = "37.386006, -122.109427, 37.386649, -122.109426, 37.386652, -122.109093, 37.386463, -122.109093, 37.386463, -122.109030, 37.386149, -122.109028, 37.386152, -122.108939, 37.386007, -122.108936, 37.386006, -122.109427";
        ARNode wing700 = new ARNode(Type.BUILDING, "700 Wing", polygon_data, 0, "Science and math buildings");
        los_altos_high_school.addChildNode(wing700);

        polygon_data = "";
        ARNode wing700_floor1 = new ARNode(Type.FLOOR, "Science floor", polygon_data, 10, "All science buildings are on this floor");
        wing700.addChildNode(wing700_floor1);

        //polygon_data = "";
        //ARNode wing700_floor2 = new ARNode(Type.FLOOR, "Math floor", polygon_data, 11, "All math buildings are on this floor");
        //wing700.addChildNode(wing700_floor2);

        polygon_data = "37.38642893, -122.10951252, 37.38649573, -122.10958477, 37.38646007,-122.10937816, 37.38648869,-122.10947966, 37.38643915,-122.10937413, 37.38642285,-122.10940775, 37.38640919,-122.10934203, 37.38642432,-122.10935737, 37.38642893,-122.10951252";
        ARNode wing700_floor1_room710 = new ARNode(Type.ROOM, "Room 710", polygon_data, 11, "Mr.Dressen's Room");
        wing700_floor1.addChildNode(wing700_floor1_room710);
        */

        //House

        polygon_data = "37.404406, -122.079025, 37.403889, -122.079055, 37.403893, -122.078698, 37.404378, -122.078683, 37.404406, -122.079025";
        ARNode townhouse = new ARNode(Type.STRUCTURE, "Townhouse", polygon_data, 0, "Townhouse");
        rootNode.addChildNode(townhouse);

        polygon_data = "37.404178, -122.078966, 37.404123, -122.078969, 37.404113, -122.078743, 37.404175, -122.078738, 37.404178, -122.078966";
        ARNode house = new ARNode(Type.BUILDING, "House", polygon_data, 0, "A House");
        townhouse.addChildNode(house);

        ARNode house_floor1 = new ARNode(Type.FLOOR, "Floor 1", "", 10, "First floor of a house.");
        house.addChildNode(house_floor1);

        polygon_data = "37.404178, -122.078906, 37.404119, -122.078909, 37.404113, -122.078743, 37.404175, -122.078738, 37.404178, -122.078906";
        ARNode kitchen = new ARNode(Type.ROOM, "Kitchen", polygon_data, 0, "Kitchen of the house");
        house_floor1.addChildNode(kitchen);

        polygon_data = "37.404178, -122.078966, 37.404123, -122.078969, 37.404119, -122.078909, 37.404178, -122.078906, 37.404178, -122.078966";
        ARNode living_room = new ARNode(Type.ROOM, "Living Room", polygon_data, 0, "Living Room");
        house_floor1.addChildNode(living_room);

        ARNode house_floor2 = new ARNode(Type.FLOOR, "Floor 2", "", 12, "Second floor of a house.");
        house.addChildNode(house_floor2);

        polygon_data = "37.404178, -122.078906, 37.404119, -122.078909, 37.404113, -122.078743, 37.404175, -122.078738, 37.404178, -122.078906";
        ARNode master_bedroom = new ARNode(Type.ROOM, "Master Bedroom", polygon_data, 0, "Master Bedroom");
        house_floor2.addChildNode(master_bedroom);

        polygon_data = "37.404178, -122.078966, 37.404123, -122.078969, 37.404119, -122.078909, 37.404178, -122.078906, 37.404178, -122.078966";
        ARNode small_bedrooms = new ARNode(Type.ROOM, "Small Bedroom", polygon_data, 0, "Small bedroom");
        house_floor2.addChildNode(small_bedrooms);

        constructStructuresData();
    }

    /**
     * Construct the structuresChildrenData and the Etag.
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

    public void locatePoint(String id, GeoPoint point) {
        System.out.println("Entered locatePoint");

        ARNode node = getNodeFromID(id);

        if (node != null) {
            System.out.println("locatePoint id: " + node.getId());
            locateList.clear();
            traverse(node, point);
            System.out.println("List: " + locateList);
        }
        else {
            System.out.println("Node is null");
        }
    }

    /**
     * Method to determine if the ID matches
     *
     * @param id
     * @return
     */
    private ARNode getNodeFromID(String id) {
        System.out.println("Entered getNodeFromID " + id);
        for (ARNode node: rootNode.getChildrenNodes()) {
            System.out.println("getNode " + node.getName() + " " + node.getId());
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    private void traverse(ARNode node, GeoPoint point) {
        if (node == null) {
            return;
        }
        System.out.println("Traverse name: " + node.getName());
        System.out.println("Traverse children: " + node.getChildrenNodes());
        if (node.isInsidePolygon(point)) {
            locateList.add(node.toJSON());
            System.out.println("traverse: " + node.getName() + " " + node.getId());
        }
        for (ARNode child : node.getChildrenNodes()) {
            traverse(child, point);
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