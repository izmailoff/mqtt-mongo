rem Download this JAR to this dir: https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.7/sbt-launch.jar?_ga=1.47612455.1882964693.1406200696

set SCRIPT_DIR=%~dp0
java -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256m -Xmx1024M -Xss2M -jar "%SCRIPT_DIR%\sbt-launch.jar" %*
