package pl.edu.uj.saveyourpass.api;

import org.mindrot.jbcrypt.BCrypt;
import pl.edu.uj.saveyourpass.Secured;
import pl.edu.uj.saveyourpass.bo.AuthToken;
import pl.edu.uj.saveyourpass.bo.LoginData;
import pl.edu.uj.saveyourpass.bo.User;
import pl.edu.uj.saveyourpass.dao.AuthTokenDao;
import pl.edu.uj.saveyourpass.dao.UserDao;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;

@Path("user")
public class UserResource {
    @Inject
    private UserDao userDao;
    @Inject
    private AuthTokenDao authTokenDao;
    @Inject
    private KeyResource keyResource;

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user, @Context UriInfo uriInfo) {
        userDao.add(user);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder()
                .path(user.getUsername());
        return Response.created(builder.build()).entity(user).build();
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getUser(@PathParam("username") String username) {
        Optional<User> user = userDao.get(username);
        if (user.isPresent()) {
            return Response.ok().entity(user.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginData loginData) {
        Optional<User> user = userDao.get(loginData.getUsername());
        if (user.isPresent()) {
            if (BCrypt.checkpw(loginData.getPassword(), user.get().getPassword())) {
                AuthToken token = new AuthToken(user.get());
                authTokenDao.add(token);
                return Response.ok(token).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("{username}/keys")
    public KeyResource keys(@PathParam("username") String username) {
        keyResource.setUsername(username);
        return keyResource;
    }
}
