package org.rapidpm.vaadin.nano;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.rapidpm.dependencies.core.logger.HasLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.redirect;
import static io.undertow.servlet.Servlets.servlet;

public class MyVaadinApp {

  public static void main(String[] args) {
    try {
      new MyVaadinApp().startup();
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }

  public void startup() throws ServletException {
    DeploymentInfo servletBuilder
        = Servlets.deployment()
                  .setClassLoader(MyVaadinApp.class.getClassLoader())
                  .setContextPath("/")
                  .setDeploymentName("ROOT.war")
                  .setDefaultEncoding("UTF-8")
                  .addServlets(
                      servlet(
                          CoreServlet.class.getSimpleName(),
                          CoreServlet.class
                      ).addMapping("/*")
                       .setAsyncSupported(true)
                  );

    final DeploymentManager manager = Servlets
        .defaultContainer()
        .addDeployment(servletBuilder);
    manager.deploy();
    PathHandler path = path(redirect("/"))
        .addPrefixPath("/", manager.start());
    Undertow.builder()
            .addHttpListener(8899, "0.0.0.0")
            .setHandler(path)
            .build()
            .start();
  }

  @PreserveOnRefresh
  @Push
  public static class MyUI extends UI implements HasLogger {
    @Override
    protected void init(VaadinRequest request) {
      setContent(new Label("Hello World"));
    }
  }

  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet {
    //customize Servlet if needed
  }
}
