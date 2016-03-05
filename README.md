# Karaf server builder #

This project contains the metadata necessary to build a Karaf OSGi server based on Eclipse Equinox with
a default set of utilities and features, including a web console, SSH access, monitoring, and log
configuration.

It also adds the optional iPOJO service, enabling developers to add OSGi services using regular
Java interfaces and objects with a few annotations.

## Adding new OSGi features

In order to add new jars to the standard container, these need to be bundled as OSGi plugins and the
set of jars that make up a given application feature need to be bundled as a Karaf Feature.

Then, the following places need to be updated in this configuration for Maven to add the feature to
the build and for Karaf to load it at startup:

* pom.xml
    * If the feature is stored in a non-standard Maven or OBR repository, add a repository section
    with the URL Maven should use for accesing the repository.
    * Add a dependency to the feature itself to the dependencies section.
    * Add your feature's symbolic name under bootFeatures.
    * In the karaf-maven-plugin, add another reference to the repository under bootRepositories.
* src/main/resources/etc/org.apache.karaf.features.cfg
    * Add the URL to your feature's XML file to the featuresRepositories entry.
    * Add your new feature to the featuresBoot entry so that it starts with the container.

This configuration is unfortunately pretty repetitive, which seems common among Maven builds.

It would be nice to create a project template language describing just what features are desired
in a container that would generate a server project and POM for building it.

## The Makefile

* Run `make` without any arguments to build and run the current server container.
* Run `make clean` to remove all built artifacts and the unpacked test container.
* Run `make package` to build the server zip files.



