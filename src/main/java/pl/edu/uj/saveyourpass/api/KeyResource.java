package pl.edu.uj.saveyourpass.api;

import pl.edu.uj.saveyourpass.bo.Key;
import pl.edu.uj.saveyourpass.bo.User;
import pl.edu.uj.saveyourpass.dao.KeyDao;
import pl.edu.uj.saveyourpass.dao.UserDao;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("")
public class KeyResource {
    @Inject
    private KeyDao keyDao;
    @Inject
    private UserDao userDao;
    private String username;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        User user = userDao.getByName(username);
        return Response.ok(user.getKeys()).build();
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newKey(Key key) {
        User user = userDao.getByName(username);
        key.setOwner(user);
        keyDao.insert(key);
        return Response.ok().build();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKey(@PathParam("name") String name) {
        Optional<Key> key = keyDao.getByName(name);
        if (key.isPresent()) {
            return Response.ok(key.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{name}/passwords")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPasswords(@PathParam("name") String name) {
        Optional<Key> key = keyDao.getByName(name);
        if (key.isPresent()) {
            return Response.ok(key.get().getEncryptedPasswords()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
