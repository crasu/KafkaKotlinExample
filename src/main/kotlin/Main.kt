import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.util.*

const val TOPIC = "my-topic"

fun testProducer() {
    val props = Properties()
    props["bootstrap.servers"] = "localhost:9092"
    props["acks"] = "all"
    props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"

    val producer: Producer<String, String> = KafkaProducer(props)
    for (i in 0..99) producer.send(
        ProducerRecord(
            TOPIC,
            Integer.toString(i), Integer.toString(i)
        )
    )
    producer.close()
}

fun testConsumer() {
    val props = Properties()
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test")
    props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")

    val consumer: KafkaConsumer<String, String> = KafkaConsumer(props)
    consumer.subscribe(listOf(TOPIC))
    while (true) {
        val records: ConsumerRecords<String?, String?> = consumer.poll(Duration.ofMillis(5000))
        if(!records.isEmpty) {
            for (record in records) println(
                "offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}"
            )
        } else {
            println("no message(s)")
        }
    }
}
fun main(args: Array<String>) {
    testProducer();
    testConsumer();

    println("Program arguments: ${args.joinToString()}")
}