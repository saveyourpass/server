package pl.edu.uj.saveyourpass;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.swagger.SwaggerArchive;

public class Main {
    public static void main(String[] args) throws Exception {
        Swarm swarm = new Swarm();

        SwaggerArchive archive = ShrinkWrap.create(SwaggerArchive.class, "swagger.war");
        archive.setVersion("0.0.1"); // our API version
        archive.setResourcePackages("pl.edu.uj.saveyourpass");

        JAXRSArchive deployment = archive.as(JAXRSArchive.class)
                .addPackage("pl.edu.uj.saveyourpass");
        deployment.addAllDependencies();
        swarm.start().deploy(deployment);
    }
}
