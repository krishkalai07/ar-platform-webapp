package com.krishk.arplatform;

import org.json.JSONObject;

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
    private String structuresChildrenData = "";

    /**
     * Constructs the AR Tree. The tree is construted only once since it is Singleton.
     */
    ARTree() {
        this.rootNode = new ARNode(Type.PLATFORM, "Structures", "", 0, "");

        String polygon_data = "37.388616, -122.110888, 37.388610, -122.107366, 37.385015, -122.107379, 37.385034, -122.110901, 37.388616, -122.110888";
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

        polygon_data = "";
        ARNode wing700_floor2 = new ARNode(Type.FLOOR, "Math floor", polygon_data, 11, "All math buildings are on this floor");
        wing700.addChildNode(wing700_floor2);

        polygon_data = "37.38642893, -122.10951252, 37.38649573, -122.10958477, 37.38646007,-122.10937816, 37.38648869,-122.10947966, 37.38643915,-122.10937413, 37.38642285,-122.10940775, 37.38640919,-122.10934203, 37.38642432,-122.10935737, 37.38642893,-122.10951252";
        ARNode wing700_floor1_room710 = new ARNode(Type.ROOM, "Room 710", polygon_data, 11, "Mr.Dressen's Room");
        wing700_floor1.addChildNode(wing700_floor1_room710);

        //House
        polygon_data = "37.388616, -122.110888, 37.388610, -122.107366, 37.385015, -122.107379, 37.385034, -122.110901, 37.388616, -122.110888";
        ARNode house = new ARNode(Type.STRUCTURE, "House", polygon_data, 0, "A House");
        rootNode.addChildNode(house);

        constructStructuresData();
    }

    /**
     * Construct the structuresChildrenData and the Etag.
     */
    private void constructStructuresData() {
        ArrayList list = new ArrayList();
        String etag_string = "";
        for (int i = 0; i < rootNode.getChildrenNodes().size(); i++) {
            list.add(rootNode.getChildrenNodes().get(i).toJSON());
            etag_string += rootNode.getChildrenNodes().get(i).getId();
        }
        structuresChildrenData = list.toString();

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

    String toJSON() {
        JSONObject return_object = new JSONObject();
        return_object.put("StructruesData", structuresChildrenData);
        return_object.put("StructuresEtag", structuresETag);

        return return_object.toString();
    }

    public String getStructuresETag() {
        return structuresETag;
    }
}
