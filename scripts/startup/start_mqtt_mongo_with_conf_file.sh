#!/bin/bash

# Run the service with alternative config file
java -Dconfig.file=application.conf -jar ../../target/scala-2.11/mqtt-mongo-assembly-0.1.jar

