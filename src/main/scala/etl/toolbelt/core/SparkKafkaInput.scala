package etl.toolbelt.core


import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.StreamingContext


abstract class SparkKafkaInput(ssc: StreamingContext) {

  val refreshRate: Int
  val topic: String
  val kafkaCluster: String
  val consumerGroup: String
  val partitions: Int

  /**
    * Gets stream from Kafka using Spark Streaming
    * @return DStream from Kafka
    */

  def inputStream: ReceiverInputDStream[(String, String)] = {
    KafkaUtils.createStream(ssc, kafkaCluster, consumerGroup, Map(topic -> partitions))
  }

}