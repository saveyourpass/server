package pl.edu.uj.saveyourpass.api;

import pl.edu.uj.saveyourpass.bo.User;
import pl.edu.uj.saveyourpass.dao.UserDao;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserResource {
    @Inject
    private UserDao userDao;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        userDao.add(user);
        String result = "User registered " + user;
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(result).build();
    }
}
