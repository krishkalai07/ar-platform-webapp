package com.krishk.arplatform;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("locate")
@Singleton
public class MyResource {
    private Structures structures;

    /**
     * This constructor will be called only once, since this is a Singleton class
     */
    public MyResource() {
        structures = new Structures();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocate(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String latitude = queryParams.getFirst("lati");
        String longitiude = queryParams.getFirst("long");
        GeoPoint current_location = new GeoPoint(latitude, longitiude);
        structures.locateStructure(current_location);

        //String str = structures.getJsonList().toString();

        String ret_value = structures.getJsonList().toString();
        return Response.status(200).entity(ret_value).build(); 
    }
}
