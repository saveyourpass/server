package pl.edu.uj.saveyourpass.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Version {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("version")
    public String version() {
        return "0.0.1";
    }
}
