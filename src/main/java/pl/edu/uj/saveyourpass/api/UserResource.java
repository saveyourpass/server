package pl.edu.uj.saveyourpass.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mindrot.jbcrypt.BCrypt;
import pl.edu.uj.saveyourpass.Secured;
import pl.edu.uj.saveyourpass.bo.*;
import pl.edu.uj.saveyourpass.dao.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;

@Path("user")
@Api(value = "/user")
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
    @ApiOperation(value = "Register new user")
    public Response registerUser(User user, @Context UriInfo uriInfo) {
        userDao.add(user);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder()
                .path(user.getUsername());
        return Response.created(builder.build()).entity(user).build();
    }

    @GET
    @Path("{username}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get user")
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
    @ApiOperation(value = "Login -> get auth token needed for other API endpoints")
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
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all public keys for user")
    public Response getAllKeys(@PathParam("username") String username) {
        Optional<User> user = userDao.get(username);
        if (user.isPresent()) {
            return Response.ok(user.get().getKeys()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{username}/keys/new")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add new public key for user")
    public Response newKey(
            @Context SecurityContext securityContext,
            @PathParam("username") String username,
            Key key) {
        User authUser = userDao.getByName(securityContext.getUserPrincipal().getName());
        Optional<User> user = userDao.get(username);
        if (user.isPresent() && user.get().equals(authUser)) {
            key.setOwner(authUser);
            keyDao.insert(key);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("{username}/keys/{keyname}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get specific public key for user")
    public Response getKey(
            @PathParam("username") String username,
            @PathParam("keyname") String keyname) {
        Optional<User> user = userDao.get(username);
        if (user.isPresent()) {
            Optional<Key> key = user.get().getKeys().stream().filter(k -> k.getName().equals(keyname)).findFirst();
            if (key.isPresent()) {
                return Response.ok(key.get()).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{username}/keys/{keyname}/passwords")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all encrypted passwords for specific key")
    public Response getPasswordsForKey(
            @Context SecurityContext securityContext,
            @PathParam("username") String username,
            @PathParam("keyname") String keyname) {
        Optional<User> user = userDao.get(username);
        User authUser = userDao.getByName(securityContext.getUserPrincipal().getName());
        if (user.isPresent() && user.get().equals(authUser)) {
            Optional<Key> key = user.get().getKeys().stream().filter(k -> k.getName().equals(keyname)).findFirst();
            if (key.isPresent()) {
                return Response.ok(key.get().getEncryptedPasswords()).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("{username}/passwords")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all passwords for user")
    public Response getUserPasswords(
            @Context SecurityContext securityContext,
            @PathParam("username") String username) {
        Optional<User> user = userDao.get(username);
        User authUser = userDao.getByName(securityContext.getUserPrincipal().getName());
        if (user.isPresent() && user.get().equals(authUser)) {
            return Response.ok(user.get().getPasswords()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{username}/passwords/new")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add new password for user")
    public Response addNewPassword(
            @Context SecurityContext securityContext,
            @PathParam("username") String username,
            Password password) {
        Optional<User> user = userDao.get(username);
        User authUser = userDao.getByName(securityContext.getUserPrincipal().getName());
        if (user.isPresent() && user.get().equals(authUser)) {
            password.setOwner(user.get());
            passwordDao.insert(password);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{username}/passwords/{passwordname}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get specific password for user")
    public Response getUserPassword(
            @Context SecurityContext securityContext,
            @PathParam("username") String username,
            @PathParam("passwordname") String passwordName) {
        Optional<User> user = userDao.get(username);
        User authUser = userDao.getByName(securityContext.getUserPrincipal().getName());
        if (user.isPresent() && user.get().equals(authUser)) {
            Optional<Password> password = passwordDao.getByName(passwordName);
            if (password.isPresent()) {
                return Response.ok(password.get()).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{username}/passwords/{passwordname}/uploadEncrypted")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add new encrypted password for key for user")
    public Response uploadEncryptedPassword(
            @Context SecurityContext securityContext,
            @PathParam("username") String username,
            @PathParam("passwordname") String passwordName,
            @QueryParam("shareWith") String shareWith,
            @QueryParam("keyname") String keyName,
            EncryptedPassword encryptedPassword) {
        if (shareWith != null && keyName != null) {
            Optional<User> user = userDao.get(username);
            User authUser = userDao.getByName(securityContext.getUserPrincipal().getName());
            if (user.isPresent() && user.get().equals(authUser)) {
                Optional<Password> password = user.get()
                        .getPasswords().stream().filter(pass -> pass.getName().equals(passwordName)).findFirst();
                if (password.isPresent()) {
                    Optional<User> shareWithUser = userDao.get(shareWith);
                    if (shareWithUser.isPresent()) {
                        Optional<Key> key = shareWithUser.get().getKeys()
                                .stream().filter(k -> k.getName().equals(keyName)).findFirst();
                        if (key.isPresent()) {
                            encryptedPassword.setKey(key.get());
                            encryptedPassword.setPassword(password.get());
                            encryptedPasswordDao.insert(encryptedPassword);
                            return Response.ok().build();
                        }
                    }
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

//    @POST
//    @Path("{username}/passwords/{passwordname}/share/{sharewith}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response sharePassword(
//            @PathParam("username") String username,
//            @PathParam("passwordname") String passwordName,
//            @PathParam("sharewith") String shareWith) {
//        User userToShare = userDao.getByName(shareWith);
//        Optional<Password> passwordOpt = passwordDao.getByName(passwordName);
//        if (passwordOpt.isPresent()) {
//            Password password = passwordOpt.get();
//            password.getSharedWith().add(userToShare);
//            passwordDao.update(password);
//            return Response.ok().build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
}
