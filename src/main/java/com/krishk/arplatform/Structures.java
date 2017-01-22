package com.krishk.arplatform;

import java.util.ArrayList;
import java.util.Vector;

@SuppressWarnings("ALL")
public class Structures {
    private Structure platform_node; //Root node of the tree
    //private Vector<String> json_list = new Vector<>();
    private ArrayList<String> json_list = new ArrayList<>();

    Structures() {
        this.platform_node = new Structure(Type.PLATFORM, "Structures", "", 0, "");

        String polygon_data = "37.388616, -122.110888, 37.388610, -122.107366, 37.385015, -122.107379, 37.385034, -122.110901, 37.388616, -122.110888";
        Structure los_altos_high_school = new Structure(Type.STRUCTURE, "Los Altos high School", polygon_data, 0, "Mah school");
        platform_node.addChildNode(los_altos_high_school);

        polygon_data = "37.385647, -122.109474, 37.385727, -122.109137, 37.385686, -122.109118, 37.385704, -122.109037, 37.385587, -122.108991, 37.385599, -122.108940, 37.385519, -122.108905, 37.385536, -122.108825, 37.385417, -122.108779, 37.385364, -122.108982, 37.385406, -122.108995, 37.385337, -122.109252, 37.385374, -122.109271, 37.385354, -122.109360, 37.385647, -122.109474";
        Structure eagle_theater = new Structure(Type.BUILDING, "Eagle Theater", polygon_data, 0, "Mah school");
        los_altos_high_school.addChildNode(eagle_theater);

        polygon_data = "37.386006, -122.109427, 37.386649, -122.109426, 37.386652, -122.109093, 37.386463, -122.109093, 37.386463, -122.109030, 37.386149, -122.109028, 37.386152, -122.108939, 37.386007, -122.108936, 37.386006, -122.109427";
        Structure wing700 = new Structure(Type.BUILDING, "700 Wing", polygon_data, 0, "Science and math buildings");
        los_altos_high_school.addChildNode(wing700);

        polygon_data = "";
        Structure wing700_floor1 = new Structure(Type.FLOOR, "Science floor", polygon_data, 10, "All science buildings are on this floor");
        wing700.addChildNode(wing700_floor1);

        polygon_data = "";
        Structure wing700_floor2 = new Structure(Type.FLOOR, "Math floor", polygon_data, 11, "All math buildings are on this floor");
        wing700.addChildNode(wing700_floor2);

        //polygon_data = ""; //TODO: needs to be created. maps.google.com is not accurate enough
        //Structure wing700_floor2_room710 = new Structure(Type.ROOM, "Room 710", polygon_data, 11, "Mr.Dressen's Room");
        //wing700_floor1.addChildNode(wing700_floor2_room710);

    }

    public ArrayList<String> getJsonList() {
        return json_list;
    }

    void locateStructure(GeoPoint point) {
        System.out.println("Is inside locate.");
        locateStructuresHelper(point, platform_node);
    }

    private void locateStructuresHelper(GeoPoint point, Structure node) {
        // Base case
        if (node == null) {
            return;
        }

        //System.out.println("Testing " + node.getName());
        if (node.isInsidePolygon(point)) {
            System.out.println("Found inside " + node.getName());
            json_list.add(node.toJSON());
            for (int i = 0; i < node.getChildrenNodes().size(); i++) {
                locateStructuresHelper(point, node.getChildrenNodes().get(i));
            }
        }
    }

    public void handleOutsideStructures() {
        for (Structure structure : platform_node.getChildrenNodes()) {
            json_list.add(structure.toJSON());
        }
    }
}
