package etl.toolbelt.core

import java.util.Properties
import org.apache.kafka.clients.producer.{ProducerRecord, KafkaProducer}

abstract class KafkaOutput {

  val kafkaServers: String
  val topic: String
  val keySerializer: String
  val valueSerializer: String

  /**
    * Loads configuration based on the subclass attributes
    * @return a properties object
    */

  def loadConfig: Properties = {
    val props = new Properties()

    props.put("bootstrap.servers", kafkaServers)
    props.put("key.serializer", keySerializer)
    props.put("value.serializer", valueSerializer)
    props
  }

  /**
    * Creates a kafka producer
    * @tparam K key type for the kafka producer
    * @tparam V value type for the kafka messages
    * @return a kafka producer
    */

  def producer[K, V]: KafkaProducer[K, V] = {
    val props = loadConfig
    new KafkaProducer[K, V](props)
  }

  /**
    * Sends a message to a topic
    * @param key message key
    * @param value message value
    * @tparam K key type
    * @tparam V value type
    */

  def send[K, V](key: K, value: V): Unit = {
    producer.send(new ProducerRecord[K, V](topic, key, value))
  }


}