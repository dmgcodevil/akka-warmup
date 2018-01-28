name := "akka-warmup"

version := "1.0"

scalaVersion := "2.12.1"

val akkaHttp = "10.0.11"
val akka = "2.4.19"

libraryDependencies ++= Seq(
  // akka dependencies
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.typesafe" % "config" % "1.3.2",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp,
  "com.typesafe.akka" %% "akka-slf4j" % akka,

  "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",

  //test libraries
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",
  "org.mongodb" % "mongo-java-driver" % "3.4.0" % "test",
  "com.github.fakemongo" % "fongo" % "2.1.0" % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test" // not supported but required by scalatest
)

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)
