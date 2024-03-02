package se.iths.jakartaeelab1.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }


    //This can be removed whenever, just for testing
    //@GET
    // @Produces(MediaType.APPLICATION_JSON)
    //@Path("/test-exception")
    // public Response testException() {
    //    throw new RuntimeException("Testing exception");
    // }

}