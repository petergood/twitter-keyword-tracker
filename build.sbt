name := "twitter-keyword-tracker"

version := "0.1"

scalaVersion := "2.13.1"

scalacOptions ++= Seq(
  "-encoding", "utf8", // Option and arguments on same line
  "-Xfatal-warnings",  // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.5"
libraryDependencies += "com.typesafe.akka" %% "akka-protobuf" % "2.6.5"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.5"
libraryDependencies += "com.danielasfregola" %% "twitter4s" % "6.2"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.0"