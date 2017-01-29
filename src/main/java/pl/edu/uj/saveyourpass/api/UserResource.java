package pl.edu.uj.saveyourpass.api;

import org.mindrot.jbcrypt.BCrypt;
import pl.edu.uj.saveyourpass.Secured;
import pl.edu.uj.saveyourpass.bo.*;
import pl.edu.uj.saveyourpass.dao.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;
import java.util.Set;

@Path("user")
public class UserResource {
    @Inject
    private UserDao userDao;
    @Inject
    private AuthTokenDao authTokenDao;
    @Inject
    private KeyDao keyDao;
    @Inject
    private PasswordDao passwordDao;
    @Inject
    private EncryptedPasswordDao encryptedPasswordDao;

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

    // ---------------------- Keys ----------------------

    @GET
    @Path("{username}/keys")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllKeys(@PathParam("username") String username) {
        User user = userDao.getByName(username);
        return Response.ok(user.getKeys()).build();
    }

    @POST
    @Path("{username}/keys/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newKey(@PathParam("username") String username, Key key) {
        User user = userDao.getByName(username);
        key.setOwner(user);
        keyDao.insert(key);
        return Response.ok().build();
    }

    @GET
    @Path("{username}/keys/{keyname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKey(@PathParam("username") String username, @PathParam("keyname") String keyname) {
        Optional<Key> key = keyDao.getByName(keyname);
        if (key.isPresent()) {
            return Response.ok(key.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{username}/keys/{keyname}/passwords")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPasswordsForKey(@PathParam("username") String username, @PathParam("keyname") String keyname) {
        Optional<Key> key = keyDao.getByName(keyname);
        if (key.isPresent()) {
            return Response.ok(key.get().getEncryptedPasswords()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{username}/passwords")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPasswords(@PathParam("username") String username) {
        User user = userDao.getByName(username);
        Set<Password> passwords = user.getPasswords();
        return Response.ok(passwords).build();
    }

    @POST
    @Path("{username}/passwords/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewPassword(@PathParam("username") String username, Password password) {
        User user = userDao.getByName(username);
        password.setOwner(user);
        passwordDao.insert(password);
        return Response.ok().build();
    }

    @GET
    @Path("{username}/passwords/{passwordname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPassword(
            @PathParam("username") String username,
            @PathParam("passwordname") String passwordName) {
        Optional<Password> password = passwordDao.getByName(passwordName);
        if (password.isPresent()) {
            return Response.ok(password.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{username}/passwords/{passwordname}/uploadEncrypted/{keyname}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadEncryptedPassword(
            @PathParam("username") String username,
            @PathParam("passwordname") String passwordName,
            @PathParam("keyname") String keyName,
            EncryptedPassword encryptedPassword) {
        Optional<Key> key = keyDao.getByName(keyName);
        Optional<Password> password = passwordDao.getByName(passwordName);
        if (key.isPresent() && password.isPresent()) {
            encryptedPassword.setKey(key.get());
            encryptedPassword.setPassword(password.get());
            encryptedPasswordDao.insert(encryptedPassword);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("{username}/passwords/{passwordname}/share/{sharewith}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sharePassword(
            @PathParam("username") String username,
            @PathParam("passwordname") String passwordName,
            @PathParam("sharewith") String shareWith) {
        User userToShare = userDao.getByName(shareWith);
        Optional<Password> passwordOpt = passwordDao.getByName(passwordName);
        if (passwordOpt.isPresent()) {
            Password password = passwordOpt.get();
            password.getSharedWith().add(userToShare);
            passwordDao.update(password);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
