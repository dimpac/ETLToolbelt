import sbt._

object Dependencies {

  val typesafeConfigVersion = sys.env.getOrElse("TSCONFIG_VERSION", "1.3.0")
  val typesageLoggingVersion = sys.env.getOrElse("TSLOGGING_VERSION", "3.1.0")
  val jsoupVersion = sys.env.getOrElse("JSOUP_VERSION", "1.6.1")
  val kafkaVersion = sys.env.getOrElse("KAFKA_VERSION", "0.9.0.0")
  val scalaTestVersion = sys.env.getOrElse("SCALATEST_VERSION", "2.2.6")
  val logbackVersion = sys.env.getOrElse("LOGBACK_VERSION", "1.1.2")
  val sparkVersion = sys.env.getOrElse("SPARK_VERSION", "1.6.0")
  val elasticSearchVersion = sys.env.getOrElse("ES_VERSION", "2.2.0")


  lazy val sparkDeps = Seq (
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" %% "spark-mllib" % sparkVersion,
    "org.apache.spark" %% "spark-streaming" % sparkVersion,
    "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion
  )

  lazy val elasticSearchDep = Seq(
    "org.elasticsearch" %% "elasticsearch-spark" % elasticSearchVersion
  )

  lazy val sparkCsvDep = Seq(
    "com.databricks" %% "spark-csv" % "1.4.0"
  )

  lazy val typesafeDeps = Seq (
    "com.typesafe" % "config" % typesafeConfigVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % typesageLoggingVersion
  )

  lazy val testDeps = Seq (
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )

  lazy val logbackDep = Seq (
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  lazy val jsoupDep = Seq (
    "org.jsoup" % "jsoup" % jsoupVersion
  )

  lazy val kafkaDep = Seq (
    "org.apache.kafka" %% "kafka"  % kafkaVersion
  )

  lazy val coreDeps = kafkaDep ++ jsoupDep ++ typesafeDeps ++ logbackDep ++ sparkDeps ++ elasticSearchDep

}