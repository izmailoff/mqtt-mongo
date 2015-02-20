#!/bin/bash

# Downloads the latest version of SBT runner script which in turn downloads
# SBT launcher JAR and provides lots of convenience methods.

curl -s https://raw.githubusercontent.com/paulp/sbt-extras/master/sbt > sbt && chmod 0755 sbt

