# Build Status
[![Build Status](https://travis-ci.org/izmailoff/mqtt-mongo.png?branch=master)](https://travis-ci.org/izmailoff/mqtt-mongo)
	
# MQTT-Mongo
This is a generic service that subscribes to MQQT broker and saves messages to MongoDB.
TODO: ... more details later...

# Installation
This section describes steps required to build, configure and run the application.
TODO: add download options here

## Building MQTT-Mongo

### Install
Install these applications on your dev machine in order to be able to build the src code:

 * Java Development Kit (JDK) >= 1.7
 * Optionally install SBT, or use one provided with the project (see sbt.* shell scripts)

### Run SBT to generate JAR
SBT is a build tool that downloads source code dependencies, compiles code, runs tests,
generates scaladocs, and produces executables.

Start up SBT from Unix/Windows shell:

    > sbt

or if it's not on a PATH:

    > ./sbt

In SBT shell type (note semicolons):

    ;clean; assembly

You can also run it as a single command from OS shell:

    > sbt clean assembly

This will run all tests and generate a single jar file named similar to: `mqtt-mongo-assembly-0.1.jar`.

Here is a full list of commands in case you want to generate projects and documentation, etc:

    > sbt clean compile test doc assembly
    
Look at the output to find where build artefacts go (docs, jars, etc).

## System Requirements
To run compiled JAR file you should have installed:

 * Java Runtime Environment (JRE) >= 1.7
 * MongoDB
 * mosquitto or any other MQTT broker

## Running MQTT-Mongo
Run from your OS shell:

    > java -jar mqtt-mongo-assembly-0.1.jar
	
This will run the application. In particular it will register with MQTT broker
on the topics you've configured and will save all received messages to MongoDB.

Please use run scripts in production environment. They take care of runtime settings
and environment, so that you don't get it wrong. Example scripts are in `scripts` directory of a
project's root directory.

### Advanced Configuration
The service uses [HOCON](https://github.com/typesafehub/config) configuration library. See it's documentation for
detailed information on how you can perform advanced configuration if necessary. Otherwise see brief description
below.
The packaged jar contains `reference.conf` file that has default settings for all configuration parameters. Thus
you can run the service as is if you just want to try it out. For example:

    java -jar mqtt-mongo-assembly-0.1.jar

In this case it will try to connect to localhost on port 27017 for MongoDB and port 1883 for MQTT broker (mosquitto).
It will subscribe to `test` topic and save all messages to database `mqtt`, collection `messages`.

In most cases you would want to override topics, location of MQTT broker and MongoDB in your config. Preferred way to
configure the application is via additional configuration file (`application.conf`). Settings in `application.conf`
will override default settings in `reference.conf`. Additionally you can provide settings as command line arguments
to JVM with a `-D` flag. Here are a few examples:

  1) Create a config file and provide it to the application. Contents of `application.conf`:

    application.mqttMongo.topicsToCollectionsMappings = [
      { "one" : "collectionOne" }, { "two" : "collectionTwo" }
    ]

   Run the application with this config:

    java -Dconfig.file=application.conf -jar mqtt-mongo-assembly-0.1.jar

  2) Provide settings via cmd args:

    java -Dapplication.mongo.host=127.0.0.1 -Dapplication.mongo.dbName=adhocdb -jar mqtt-mongo-assembly-0.1.jar

This application is based on Akka concurrency framework. If you want to fine tune or debug the application
take a look at Akka [docs](http://doc.akka.io/docs/akka/snapshot/general/configuration.html). Akka can be configured
the same way via conf file or cmd args as described above.



