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
    private ARTree arTree;

    /**
     * This constructor will be called only once, since this is a Singleton class.
     * The constructor will initialize the structures.
     */
    public MyResource() {
        arTree = new ARTree();
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
        //System.out.println("ID: " + id);
        //System.out.println(arTree);
        System.out.println("ID: " + id);

        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String latitude = queryParams.getFirst("lati");
        String longitude = queryParams.getFirst("long");
        String elevation = queryParams.getFirst("elev");

        GeoPoint point = new GeoPoint(latitude, longitude);
        System.out.println("getLocate point " + point);
        arTree.locatePoint(id, point, Double.parseDouble(elevation));
        return Response.status(200).entity(arTree.getLocateList().toString()).build();
    }

    /**
     * Method handling HTTP GET request. This function refreshes the user's e-tag and structres list per request if a change is needed.
     *
     * @param uriInfo Allows to get the query parameter from the URL.
     * @return If a change is needed, then a response of 200 with the new etag and structuresData. If not, then 200 with "Not Modified.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("structures")
    public Response getStructures(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String etag = queryParams.getFirst("etag");

        // If the etags are equal, then no change is necessary, response = 304
        // becuase there is no change to the data
        if (etag.equals(arTree.getStructuresETag())) {
            return Response.status(200).entity("Not Modified").build();
        }
        else {
            return Response.status(200).header("ETag", arTree.getStructuresETag()).entity(arTree.getStructuresList().toString()).build();
        }
    }
}

//id=md5(node.name)
//E-tag = md5(concatenate(root.childrenID))