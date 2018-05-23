package org.rapidpm.vaadin.nano

import com.vaadin.annotations.PreserveOnRefresh
import com.vaadin.annotations.Push
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import io.undertow.Undertow
import io.undertow.server.handlers.PathHandler
import io.undertow.servlet.Servlets
import io.undertow.servlet.api.DeploymentInfo
import io.undertow.servlet.api.DeploymentManager
import org.rapidpm.dependencies.core.logger.HasLogger

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet

import io.undertow.Handlers.path
import io.undertow.Handlers.redirect
import io.undertow.servlet.Servlets.servlet

class NanoVaadinKotlinApp {

  @Throws(ServletException::class)
  fun startup() {
    val servletBuilder = Servlets.deployment()
        .setClassLoader(NanoVaadinKotlinApp::class.java.classLoader)
        .setContextPath("/")
        .setDeploymentName("ROOT.war")
        .setDefaultEncoding("UTF-8")
        .addServlets(
            servlet(
                CoreServlet::class.java.simpleName,
                CoreServlet::class.java
            ).addMapping("/*")
                .setAsyncSupported(true)
        )

    val manager = Servlets
        .defaultContainer()
        .addDeployment(servletBuilder)
    manager.deploy()
    val path = path(redirect("/"))
        .addPrefixPath("/", manager.start())
    Undertow.builder()
        .addHttpListener(8899, "0.0.0.0")
        .setHandler(path)
        .build()
        .start()
  }

  @PreserveOnRefresh
  @Push
  class MyUI : UI(), HasLogger {
    override fun init(request: VaadinRequest) {
      content = Label("Hello World")
    }
  }

  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = NanoVaadinKotlinApp.MyUI::class)
  class CoreServlet : VaadinServlet()//customize Servlet if needed

  companion object {

    @JvmStatic
    fun main(args: Array<String>) {
      try {
        MyVaadinApp().startup()
      } catch (e: ServletException) {
        e.printStackTrace()
      }

    }
  }
}
