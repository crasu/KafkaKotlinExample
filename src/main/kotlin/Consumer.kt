import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.TopicPartition
import java.time.Duration
import java.util.*

const val TOPIC = "my-topic"

fun testConsumer() {
    val props = Properties()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    props[ConsumerConfig.GROUP_ID_CONFIG] = "test"
    props[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = "1000"
    props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = org.apache.kafka.common.serialization.IntegerDeserializer::class.java
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = org.apache.kafka.common.serialization.StringDeserializer::class.java

    val consumer: KafkaConsumer<Int, String> = KafkaConsumer(props)
    consumer.subscribe(listOf(TOPIC))

    while (true) {
        val records: ConsumerRecords<Int?, String?> = consumer.poll(Duration.ofMillis(5000))

        if(!records.isEmpty) {
            for (record in records) {
                println(
                    "offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}, partition ${record.partition()}"
                )
            }
        } else {
            println("no message(s)")
        }

        if(consumer.assignment().isEmpty()) {
            println("no parititions assigned yet")
        }
    }
}



fun main(args: Array<String>) {
    testConsumer();

    println("Program arguments: ${args.joinToString()}")
}