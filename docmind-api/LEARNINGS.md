----------> Kafka KRaft Mode — how it works <------------------
Traditional Kafka needed a separate Zookeeper process to manage the cluster. KRaft mode removes that — Kafka manages itself. One less container, simpler setup.

Environment variables explained
KAFKA_NODE_ID: 1
Every Kafka broker needs a unique ID in the cluster. We only have one broker so it's 1.
KAFKA_PROCESS_ROLES: broker,controller
In KRaft mode one node can play both roles. broker handles messages. controller manages cluster metadata (who's leader, which partitions exist). In production these are separate nodes.
KAFKA_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093
Two ports Kafka listens on internally. 9092 for clients (your Spring Boot app). 9093 for controller communication (internal cluster management only).
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
What address Kafka tells clients to connect to. Your Spring Boot app is on Windows so it uses localhost:9092.
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
Maps each listener name to a security protocol. Both use PLAINTEXT (no SSL) — fine for local dev.
KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka:9093
List of all controller nodes in format id@host:port. We have one controller — node ID 1, reachable at kafka:9093 inside Docker network.
KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
Tells Kafka which listener is used for controller traffic.
KAFKA_LOG_DIRS: /var/lib/kafka/data
Where Kafka stores messages on disk inside the container.
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
All set to 1 because we have only one broker. In production with 3 brokers these would be 3 — meaning every message is copied to 3 brokers so no data is lost if one dies.
KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
When a consumer group starts, Kafka normally waits a few seconds before assigning partitions — in case more consumers are joining. Set to 0 for local dev so there's no delay.
CLUSTER_ID: ${CLUSTER_ID}
Unique identifier for this Kafka cluster. KRaft requires this. Generated once, never changes — changing it on an existing cluster wipes all data.


## Environment Variables — how we solved it

### Problem
spring-dotenv 3.0.0 and 4.0.0 both failed to resolve
${POSTGRES_USER} in application.yml on Spring Boot 3.5

### Solution
IntelliJ EnvFile plugin:
- Install plugin: Settings → Plugins → search "EnvFile"
- Run Configuration → Enable EnvFile → point to .env file
- Plugin injects all .env variables as JVM env vars at startup
- Spring Boot resolves ${VAR} normally from those env vars

Working Directory must be set to project root in run config.

### Why working directory matters
Spring Boot reads .env relative to the working directory.
If working directory is wrong, .env is never found even if
the file exists.

### Production equivalent
In prod, env vars are injected by the platform:
- AWS ECS → Secrets Manager → Task Definition env vars
- Kubernetes → kubectl create secret → pod env vars
- GitHub Actions → Repository Secrets → pipeline env vars
  Code never changes — only where the values come from changes.

### Why UUID not IDENTITY

So the real reasons are:

UUID — ID generated in application memory, no DB round trip needed
UUID — ID known before save, safe to use in Kafka events immediately
UUID — no central sequence bottleneck under high concurrent load
UUID — harder to enumerate/guess (/documents/1, /documents/2 is a security risk)
