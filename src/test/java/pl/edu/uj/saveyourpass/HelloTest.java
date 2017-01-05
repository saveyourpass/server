package pl.edu.uj.saveyourpass;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class HelloTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addClass(JAXActivator.class).addClass(Hello.class);
    }

    @Test
    @RunAsClient
    public void shouldBeAbleTo(@ArquillianResteasyResource("rest/json") final WebTarget webTarget) {
        final Response response = webTarget
                .path("/")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertEquals("{\"status\":\"hello\"}", response.readEntity(String.class));
    }
}
