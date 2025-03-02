# Notification Service 

This project implements a Notification Service capable of sending SMS messages to users. It leverages Java, Spring Boot, MySQL, Kafka, Redis, and Elastic Search to handle message queuing, storage, caching, and indexing.

## Features

- Sends SMS messages to specified phone numbers
- Stores SMS request details in a MySQL database
- Utilizes Kafka for message queuing and asynchronous processing
- Implements a Redis cache for blacklisting phone numbers
- Indexes SMS data in Elastic Search for efficient searching
- Provides REST APIs for sending SMS, managing blacklisted numbers, and retrieving SMS details

## Flow

1. The Notification Service receives an SMS request via the `/sms/send` API.
2. It stores the request in the `sms` table and publishes the request ID to a Kafka topic.
3. Notification consumers retrieve the request ID from Kafka.
4. They fetch request details from the database.
5. They check if the phone number is blacklisted using Redis.
6. They call a 3rd party API to send the SMS.
7. They update the status and details in the database.
8. They index the SMS data in Elastic Search.

## API Endpoints

- `POST /v1/blacklist`: Adds phone numbers to the blacklist.
- `DELETE /v1/blacklist`: Removes phone numbers from the blacklist.
- `GET /v1/blacklist`: Retrieves the list of blacklisted phone numbers.
- `POST /v1/sms/send`: Sends an SMS to a specified phone number.
- `GET /v1/sms/send/<request_id>`: Retrieves SMS details by request ID.
- `GET /v1/sms/send`: Retrieves SMS details for all requests.
- `GET /elas`: Retrieves all the sent SMS messages.
- `GET /elas/query`: Retrieves SMS messages sent to a phone number within a specified timeframe (with pagination).
- `GET /elas/search`: Retrieves SMS messages containing specific text (with pagination).

## Implementation

- **Database**: MySQL
- **Message Queue**: Kafka
- **Cache**: Redis
- **Search Index**: Elastic Search
- **Framework**: Spring Boot
- **ORM**: Spring Data JPA
- **Logging**: Log4j 
- **Unit Testing**: JUnit

