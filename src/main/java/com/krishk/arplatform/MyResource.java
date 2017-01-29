package com.krishk.arplatform;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Root resource (exposed at "vq" path)
 */
@SuppressWarnings("all")
@Path("v1")
@Singleton
public class MyResource {
    private Structures structures;

    /**
     * This constructor will be called only once, since this is a Singleton class.
     * The constructor will initialize the structures.
     */
    public MyResource() {
        structures = new Structures();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "MediaType.Application_Json" media type.
     *
     * @return Response of 200 if there is valid coordinates and ID.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("locate/{id}")
    public Response getLocate(@Context UriInfo uriInfo, @PathParam("id") String id) {
        System.out.println("ID: " + id);

        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String latitude = queryParams.getFirst("lati");
        String longitude = queryParams.getFirst("long");
        GeoPoint current_location = new GeoPoint(latitude, longitude);
        structures.locateStructure(current_location);

        if (structures.getJsonList().size() == 1) {
            structures.handleOutsideStructures();
        }

        String ret_value = structures.getJsonList().toString();
        return Response.status(200).entity(ret_value).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("structures")
    public Response getStructures(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String id = queryParams.getFirst("id");
        HashMap<String, Structure> map = structures.getIDMap();

        System.out.println("getStructures");
        System.out.println(id);
        map.put(id, new Structure(Type.STRUCTURE, "d", "", 3, "dfa"));
        System.out.println("Map " + map.get(id));

        if(map.get(id) != null) {
            return Response.status(200).entity(map.get(id).toString()).build();
        }
        else {
            return Response.status(404).build();
        }
    }
}
