package pl.edu.uj.saveyourpass;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.edu.uj.saveyourpass.api.JAXActivator;
import pl.edu.uj.saveyourpass.api.Version;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class VersionTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addClass(JAXActivator.class).addClass(Version.class);
    }

    @Test
    @RunAsClient
    public void shouldBeAbleTo(@ArquillianResteasyResource("api") final WebTarget webTarget) {
        final Response response = webTarget
                .path("version")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertEquals("0.0.1", response.readEntity(String.class));
    }
}
