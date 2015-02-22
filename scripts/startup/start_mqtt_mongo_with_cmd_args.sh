#!/bin/bash

# Run the service overriding settings via JDK cmd args. 
java -Dapplication.mongo.host=127.0.0.1 -Dapplication.mongo.dbName=adhocdb -jar /home/alex/repos/mqtt-mongo/target/scala-2.11/mqtt-mongo-assembly-0.1.jar
