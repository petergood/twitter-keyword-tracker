name := "twitter-keyword-tracker"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.5"
libraryDependencies += "com.typesafe.akka" %% "akka-protobuf" % "2.6.5"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.5"
libraryDependencies += "com.danielasfregola" %% "twitter4s" % "6.2"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "3.4.2",
  "com.h2database"  %  "h2"                % "1.4.200",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"

libraryDependencies += "com.sparkjava" % "spark-core" % "2.9.0"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.7.0-M4"

libraryDependencies += "com.rometools" % "rome" % "1.13.0"