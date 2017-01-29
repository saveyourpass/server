package pl.edu.uj.saveyourpass;

import pl.edu.uj.saveyourpass.bo.User;
import pl.edu.uj.saveyourpass.dao.AuthTokenDao;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Secured
@Provider
public class SecurityFilter implements ContainerRequestFilter {
    @Inject
    private AuthTokenDao authTokenDao;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = requestContext.getHeaderString("X-AUTH");
        if (token != null && authTokenDao.isValid(token)) {
            User user = authTokenDao.getUserFor(token);
            SecurityContext securityContext = new SaveYourPassSecurityContext(
                    user,
                    requestContext.getUriInfo().getRequestUri().getScheme());
            requestContext.setSecurityContext(securityContext);
        }
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
