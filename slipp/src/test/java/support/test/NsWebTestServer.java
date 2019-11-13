package support.test;


import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class NsWebTestServer {
    private static final Logger logger = LoggerFactory.getLogger(NsWebTestServer.class);
    private final int port;

    public NsWebTestServer(int port) {
        this.port = port;
    }

    public void start() {
        new Thread(() -> {
            try {
                String webappDirLocation = "webapp/";
                Tomcat tomcat = new Tomcat();
                tomcat.setPort(port);
                String path = new File(webappDirLocation).getAbsolutePath();
                logger.debug(">> {}" , path);
                tomcat.addWebapp("/", path);
                logger.info("configuring app with basedir: {}", new File(webappDirLocation).getAbsolutePath());

                tomcat.start();
                tomcat.getServer().await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}