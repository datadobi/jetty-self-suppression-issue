package com.datadobi.bugreports.uploaddemo;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class Main
{
    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);

        ServletHolder servletHolder = new ServletHolder("hello-servlet", UploadServlet.class);
        ServletContextHandler servletContextHandler = new ServletContextHandler("/");
        servletContextHandler.addServlet(servletHolder, "/upload");

        ContextHandlerCollection collection = new ContextHandlerCollection();
        collection.addHandler(servletContextHandler);
        server.setHandler(collection);

        server.start();
    }
}
