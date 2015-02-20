# Build Status
[![Build Status](https://travis-ci.org/izmailoff/mqtt-mongo.png?branch=master)](https://travis-ci.org/izmailoff/mqtt-mongo)
	
# MQTT-Mongo
This is a generic service that subscribes to MQQT broker and saves messages to MongoDB.
TODO: ... more details later...

# Installation
This section describes steps required to build, configure and run the application.

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
and environment, so that you don't get it wrong.

### Advanced Configuration
TODO: changing defaults, etc


