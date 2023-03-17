# Kafka Setup

bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties


# Topic creation

bin/kafka-topics.sh --create --topic my-topic --partitions 2 --bootstrap-server localhost:9092

# Building the sample code

./gradlew build
