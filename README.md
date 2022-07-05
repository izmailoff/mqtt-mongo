# Build Status

[![Join the chat at https://gitter.im/izmailoff/mqtt-mongo](https://badges.gitter.im/izmailoff/mqtt-mongo.svg)](https://gitter.im/izmailoff/mqtt-mongo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/izmailoff/mqtt-mongo.png?branch=master)](https://travis-ci.org/izmailoff/mqtt-mongo)

# Test Coverage
[![Coverage Status](https://coveralls.io/repos/izmailoff/mqtt-mongo/badge.svg?branch=master)](https://coveralls.io/r/izmailoff/mqtt-mongo?branch=master)

Tests have been disabled due to incompatibility of Fongo with latest Scala Mongo driver. This needs to be resolved one 
way or the other. For now build without unit tests.
	
# MQTT-Mongo
This is a generic service that subscribes to MQTT broker and saves messages to MongoDB.

Say you have an MQTT broker that receives messages and you want those messages to be saved
in MongoDB. This service should be an ideal fit for such purpose. You can save a message in either
`String`, `JSON` or `Binary` format to one or more collections.

This service follows [Reactive Application Design](http://www.reactivemanifesto.org), i.e. responsive,
resilient, elastic, message driven. What that means that it should work fast and be able to recover from errors.

# Installation
This section describes steps required to build, configure and run the application.
TODO: add download options here

## Fastest way to get started
The instructions below apply to any OS, but command examples below are given for Redhat
based Linux distros like Fedora, CentOS, etc.

Install [mosquitto](http://mosquitto.org/download/) MQTT broker and client binaries:

    sudo yum install mosquitto

Install [MongoDB](http://docs.mongodb.org/manual/tutorial/install-mongodb-on-red-hat-centos-or-fedora-linux/).
Essentially run:

    sudo yum install mongodb-org

Keep default configuration (port, interface) for Mosquitto and MongoDB.
Start mosquitto and mongod services if not started already:

    sudo systemctl start mongod
    sudo systemctl start mosquitto

Download or build a MQTT-Mongo JAR.

Run MQTT-Mongo:

    java -jar mqtt-mongo-assembly-0.1.jar

Or from project root dir:

    java -jar target/scala-2.13/mqtt-mongo-assembly-0.1.jar

By now everything should be working and you can start testing the service.

Publish a message to the 'test' topic:

    mosquitto_pub -t "test" -m "Should work"
    mosquitto_pub -t "test" -m '{ "someField" : "Some value", "int" : 123 }'

Check mongo `mqtt` database, collection `messages` for 2 messages published above:

    echo 'db.messages.find({});' | mongo --quiet mqtt

You should be able to find the messages.

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

 * Java Runtime Environment (JRE) >= 1.8
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

# Questions and Answers
Q: Can I create a capped collection before I start using mqtt-mongo so that old messages get deleted automatically?

A: Absolutely, mqtt-mongo performs inserts only. A new collection is created upon first insert if it does not exist -
standard MongoDB behaviour.



Q: What happens if MongoDB can't keep up with insert rate?

A: ...



Q: What happens if connection to MongoDB gets lost?

A: ...


Q: What happens if connection to MQTT broker gets lost?

A: ...



Q: What happens if mqtt-broker encounters any other failures/exceptions?

A: ...



Q: Can I make it insert into multiple databases?

A: mqtt-mongo supports inserts into multiple collections. I thought support of
multiple databases would complicate configuration and implementation.
You can simply start another instance of mqtt-mongo with the same config
except for the database connection settings.



Q: What are other known alternatives?

A: 



Q: Do you have any performance test data?

A: Coming soon ...



Q: How do I scale this service?

A: There are multiple options.
   
   * Single instance:
   First of all check the config - you can customize how many threads to use and memory settings.
   Check that JVM is started with appropriate cmd args - enough heap memory, etc.
   
   * Multiple independent instances:
   Each mqtt-mongo instance can run on a single server, subscribe to a single topic and save messages to a single collection.
   This would be quite an unusual setup in case you have enourmous amount of messages being injested.
   
   * Mongo scaling:
   Use sharded collections to distribute writes. Use fire and forget write concern level.
   
   --If you exhaust all these possibilities or you have a single topic only and can't scale in other ways
   --this service can be implemented as a cluster that runs on multiple machines.



Q: What if I want to apply some processing steps before the message is saved to Mongo?

A: Currently preprocessing/filtering of messages is non-existent or customizable. You can
fork this project and implement that logic yourself in Scala or Java. If you are interested
in plugin support for this purpose let me know and I can easily add it.
