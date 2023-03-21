import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.Cluster
import java.time.Duration
import java.util.*

class MyPartitioner: Partitioner {
    override fun configure(configs: MutableMap<String, *>?) {}

    override fun close() {}

    override fun partition(
        topic: String?,
        key: Any?,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster?
    ): Int {
        if (key is Int) {
            return key.mod(2)
        }

        return 0
    }
}

fun testProducer() {
    val props = Properties()
    props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    props[ProducerConfig.ACKS_CONFIG] = "all"
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = org.apache.kafka.common.serialization.IntegerSerializer::class.java
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = org.apache.kafka.common.serialization.StringSerializer::class.java
    props[ProducerConfig.PARTITIONER_CLASS_CONFIG] = MyPartitioner::class.java

    val producer: Producer<Int, String> = KafkaProducer(props)
    for (i in 0..99) producer.send(
        ProducerRecord(
            TOPIC,
            i, Integer.toString(i)
        )
    )
    producer.close()
    println("Produced some messages")
}


fun main(args: Array<String>) {
    testProducer();
}