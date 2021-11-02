# Introduction 
The project is a very good demo to show functionality of the customer relationship with his/her accounts and addresses.
The project has been built in Java for creating microservices using the spring-boot library.


# Prerequisite

## User account(s)
You are required to have below user account(s) credentials to perform basic operation(s) on the source code.
Please visit the site(s) in Internal reference(s), if you would like to know more about requesting account or administrator of an account for below tools.
* [AZURE DevOps](https://dev.azure.com) - Azure DevOps (Product backlog, version management, CI/CD pipeline) tool
* [AZURE Portal](https://portal.azure.com) - Azure portal resource management tool
* [NEXUS](https://maven.rabobank.nl/nexus) - Nexus Maven artifact management tool
* [APIARY](https://apiary.io) - APIARY document management tool
* [ORACLE-API](https://apiprod-rabointegration1.apiplatform.ocp.oraclecloud.com/apiplatform) - ORACLE API management tool
* [SONAR](http://sonar.rabobank.nl:9000) - Sonarqube code quality tool
* [FORTIFY](https://fortify.rabobank.nl) - Fortify security violation report tool
* [CLM / NexusIq](https://clm.dev.rabobank.nl) - CLM/NexusIq dependency violation report tool
* [SPLUNK](https://splunk.rabobank.com:8000) - Splunk log monitor tool
* [SIGNAL-FX](https://rabobank.signalfx.com) - signalfx performance monitor tool

## Third party software(s)
It is recommended to install the software(s) with default configuration and everything in one folder (for example: 'C:/apps/' or 'C:/software(s)/').
* [Notepad++](https://notepad-plus-plus.org/downloads) - Source code editor tool
* [Ultra-edit](https://www.ultraedit.com/downloads) - Text editor tool
* [JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) - Java™ Platform, Standard Edition Development Kit
* [Maven](https://maven.apache.org/download.cgi) - Dependency Management for the Java application
* [Git](https://git-scm.com) - Free and Open-Source distributed version control system
* [IntelliJ IDEA](https://www.jetbrains.com/idea/download) - Integrated Development Kit
* [SoapUI](hhttps://www.soapui.org/downloads/soapui) - SOAP service(s) test tool
* [Postman](https://www.getpostman.com) - API test tool

## Spring framework(s)
It is recommended to visit the following very important spring framework(s).
* [Spring framework(s)](https://spring.io/projects/spring-framework) - An overview for spring framework(s)
* [Spring-quickstart](https://spring.io/quickstart) - Getting quick started for spring boot framework
* [Spring-initializr](https://start.spring.io) - Getting quick started for code with spring boot framework
* [Spring-boot](https://spring.io/projects/spring-boot) - Getting started for spring boot framework
* [Spring-cloud](https://spring.io/projects/spring-cloud) - Getting started for spring cloud framework
* [Spring-CLI](https://docs.spring.io/spring-boot/docs/current/reference/html/cli.html) - Commandline interface for spring boot framework
* [Spring repository](https://repo.spring.io/ui/builds) - Build for spring boot framework
* [Spring releases](https://spring.io/blog/category/releases) - Releases for spring boot framework

# Getting Started
Once you are good with the preconditions, you can get started with the next step(s).
You need to perform some important action(s)/operation(s) before working on the project source code.
##1.	Installation process
###         1.1 System variables configuration
In order to compile the project, the system must know where the above software(s) are installed.
- Open Control panel in windows. 
- Open System Properties-> View advanced system settings -> Environment Variables -> User Variables.
- Add new variables 'JAVA_HOME', 'M2_HOME' and update an existing variable 'PATH' with downloaded respective software(s) root/base path.
###         1.2 Java configuration
###         1.3 Maven configuration
The project uses maven to download the external dependencies and therefore the nexus user account details must be set with maven configuration.
- Open the folder for the maven installation in the file explorer.
- Open maven's 'conf' (configuration) folder.
- Rename the file 'settings.xml' to 'settings-original.xml'.
- Copy the 'mvn-settings.xml' file from the project 'config' (configuration) folder to the configuration folder for maven (%M2_HOME%/conf/ or $M2_HOME/conf/).
- Rename the mvn-settings.xml to settings.xml.
- Replace the 'NEXUS_USERNAME' with your nexus account username in the username tag and '********' with your crowd account password.
###         1.4 Git configuration
The project uses git to manage the source code version(s) and therefore the git user account must be configured.
- Open the user profile folder (G: drive).
- Rename the '.gitconfig' file to '.gitconfig-original'.
- Copy the '.gitconfig' file from the project 'config' (configuration) folder to the user profile folder.
###         1.5 Intellij-idea configuration
The project uses intellij idea to develop the source code and therefore java, maven and git must be configured with the intellij idea.
*   [IDE configuration](https://www.jetbrains.com/help/idea/configuring-project-and-ide-settings.html) - Generic code style configuration
*   [Run/Debug configuration](https://www.jetbrains.com/help/idea/run-debug-configuration-spring-boot.html) - Spring-boot/tests run/debug congiration
*   [Java project(s)](https://www.jetbrains.com/help/idea/creating-and-managing-projects.html) - Java project(s) creation/management
*   [Maven project(s)](https://www.jetbrains.com/help/idea/maven-support.html) - Maven project(s) creation/management
*   [Project structure](https://www.jetbrains.com/help/idea/project-settings-and-structure.html) - Java sdk and project structure settings
*   [Compiler setting(s)](https://www.jetbrains.com/help/idea/java-compiler.html) - Java compiler settings
*   [Maven setting(s)](https://www.jetbrains.com/help/idea/maven.html) - Maven build settings
*   [Plugin(s)](https://www.jetbrains.com/help/idea/managing-plugins.html) - Plugin(s) management
###         1.6 Clone
To get started with the project, you can easily clone this repository with git by running the command below in the terminal window.
  ```
  git clone https://raboweb@dev.azure.com/raboweb/LCA/_git/customer-accounts-demo
  ```
##2.	Software dependencies
* [JAVA](https://www.oracle.com/java) - Oracle Java™ Platform SDK's latest version
* [MAVEN](https://maven.apache.org) - Apache Maven's latest version for the Java application
* [GIT](https://git-scm.com) - Bitbucket Git's latest version
##3.	Latest releases
##4.	API references

# Build and Test
You can build a single executable JAR file that contains all necessary dependencies, classes and resources with the command below.
  ```
  mvn clean install
  ```
You can run the tests with the command below.
  ```
  mvn -Dtest='PACKAGE/TEST CLASS/METHOD NAME' test
  ```
>Instead of `mvn`, you can also use the maven wrapper `./mvnw` to make sure you have everything you need to run the Maven build.

# Contribute
