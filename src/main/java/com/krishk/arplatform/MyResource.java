package com.krishk.arplatform;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * Root resource (exposed at "vq" path)
 */
@SuppressWarnings("ALL")
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

    /**
     * Method handling HTTP GET request. This function refreshes the user's e-tag per request if a change is needed.
     *
     * @param uriInfo Allows to get the query parameter from the URL.
     * @return Response of 304 if no change is needed. Response of 200 if a change is needed.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("structures")
    public Response getStructures(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String etag = queryParams.getFirst("etag");

        System.out.println(structures.getEtag());

        // If the etags are equal, then no change is necessary, response = 304
        // becuase there is no change to the data
        if (etag.equals(structures.getEtag())) {
            return Response.status(304).build();
        }
        else {
            String list = structures.getStructureJsonData();
            return Response.status(200).entity(structures.getEtag()).build();
        }

        //System.out.println("Etag: " + etag);
        // System.out.println("Lsit: " + list);
    }


}

//id=md5(node.name)
//E-tag = md5(concatenate(root.childrenID))