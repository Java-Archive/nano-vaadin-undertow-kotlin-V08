
<center>
<a href="https://vaadin.com">
 <img src="https://vaadin.com/images/hero-reindeer.svg" width="200" height="200" /></a>
</center>


# Nano Vaadin - Ramp up in a second.
A nano project to start a Vaadin project. Perfect for Micro-UIs packed as fat jar in a docker image.

## target of this project
The target of this project is a minimal rampup time for a first hello world.
Why we need one more HelloWorld? Well, the answer is quite easy. 
If you have to try something out, or you want to make a small POC to present something,
there is no time and budget to create a demo project.
You don´t want to copy paste all small things together.
Here you will get a Nano-Project that will give you all in a second.

Clone the repo and start editing the class ```MyVaadinApp``` or ```MyVaadinKotlinApp```.
Nothing more. 

## How does it work?
This project will not use any additional maven plugin or technology.
Core Java and the Vaadin Dependencies are all that you need to put 
a Vaadin app into a Servlet-container.

Here we are using the plain **undertow** as Servlet-Container.

As mentioned before, there is not additional technology involved.
No DI to wire all things together. The way used here is based on good old Properties.

But let´s start from the beginning.

## Start the Servlet-Container (Java)
The class ``NanoVaadinApp``` will ramp up the Container and 
holds the Servlet- and UI- class as inner static classes.

Here all the basic stuff is done. The start will init. a ServletContainer at port **8899**.
The WebApp will deployed as **ROOT.war**. 


```java
  public static void main(String[] args) throws ServletException {
    new CoreUIService().startup();
  }

  public void startup() throws ServletException {
    DeploymentInfo servletBuilder
        = Servlets.deployment()
                  .setClassLoader(CoreUIService.class.getClassLoader())
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
```

The Servlet itself will only bind the UI Class to the Vaadin Servlet.


```java
  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet {
    //customize Servlet if needed
  }
```

The UI itself will hold the graphical elements. 

```java
@PreserveOnRefresh
  @Push
  public static class MyUI extends UI implements HasLogger {
    @Override
    protected void init(VaadinRequest request) {
      setContent(new Label("Hello World"));
    }
  }
```


After this you can start the app invoking the main-method.


## Now switching to kotlin

First you have to make sure the kotlin dependencies (including compiler config) are available.

```xml
    <!--Adding Kotlin stuff-->
    <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-stdlib</artifactId>
      <version>${kotlin-stdlib.version}</version>
    </dependency>

    <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-reflect</artifactId>
      <version>${kotlin-stdlib.version}</version>
    </dependency>
    <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-test-junit</artifactId>
      <version>${kotlin-stdlib.version}</version>
    </dependency>
```

```xml
      <plugin>
        <artifactId>kotlin-maven-plugin</artifactId>
        <groupId>org.jetbrains.kotlin</groupId>
        <version>${kotlin-stdlib.version}</version>
        <executions>
          <execution>
            <id>compile</id>
            <goals>
              <goal>compile</goal>
            </goals>
            <configuration>
              <sourceDirs>
                <sourceDir>${project.basedir}/src/main/kotlin</sourceDir>
                <sourceDir>${project.basedir}/src/main/java</sourceDir>
              </sourceDirs>
            </configuration>
          </execution>
          <execution>
            <id>test-compile</id>
            <goals>
              <goal>test-compile</goal>
            </goals>
            <configuration>
              <sourceDirs>
                <sourceDir>${project.basedir}/src/test/kotlin</sourceDir>
                <sourceDir>${project.basedir}/src/test/java</sourceDir>
              </sourceDirs>
            </configuration>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.5.1</version>
        <executions>
          <!-- Replacing default-compile as it is treated specially by maven -->
          <execution>
            <id>default-compile</id>
            <phase>none</phase>
          </execution>
          <!-- Replacing default-testCompile as it is treated specially by maven -->
          <execution>
            <id>default-testCompile</id>
            <phase>none</phase>
          </execution>
          <execution>
            <id>java-compile</id>
            <phase>compile</phase>
            <goals>
              <goal>compile</goal>
            </goals>
          </execution>
          <execution>
            <id>java-test-compile</id>
            <phase>test-compile</phase>
            <goals>
              <goal>testCompile</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```

The converted Java into Kotlin code looks like the following.

```kotlin
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
```



Happy Coding.

if you have any questions: ping me on Twitter [https://twitter.com/SvenRuppert](https://twitter.com/SvenRuppert)
or via mail.
