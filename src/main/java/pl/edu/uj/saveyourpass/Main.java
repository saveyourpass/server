package pl.edu.uj.saveyourpass;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.uj.saveyourpass.bo.User;

import javax.enterprise.inject.Produces;

public class Main {
    private SessionFactory sessionFactory;

//    public static void main(String[] args) throws Exception {
//        Swarm swarm = new Swarm();
//
//        SwaggerArchive archive = ShrinkWrap.create(SwaggerArchive.class, "swagger.war");
//        archive.setVersion("0.0.1"); // our API version
//        archive.setResourcePackages("pl.edu.uj.saveyourpass");
//
//        JAXRSArchive deployment = archive.as(JAXRSArchive.class)
//                .addPackage("pl.edu.uj.saveyourpass.api");
//        deployment.addAllDependencies();
//        swarm.start().deploy(deployment);
//    }

    @Produces
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }
}
