package pl.edu.uj.saveyourpass;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@Api(value = "hello")
public class Hello {
    @GET
    @Path("/json")
    @Produces("application/json")
    @ApiOperation(value = "Says hello", response = String.class)
    public String sayHello() {
        return "{\"status\":\"hello\"}";
    }
}
