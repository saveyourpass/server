package pl.edu.uj.saveyourpass;


import org.hibernate.cfg.NotYetImplementedException;
import pl.edu.uj.saveyourpass.bo.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SaveYourPassSecurityContext implements SecurityContext {
    private User user;
    private String scheme;

    public SaveYourPassSecurityContext(User user, String scheme) {
        this.user = user;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        throw new NotYetImplementedException("not needed");
    }

    @Override
    public boolean isSecure() {
        return "https".equals(scheme);
    }

    @Override
    public String getAuthenticationScheme() {
        throw new NotYetImplementedException("not needed");
    }
}
