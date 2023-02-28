# Microservice_metadata
Restful Microservice persisting metadata. 
Built using Spring boot, Hibernate, PostgreSQL

It has 3 asset levels - Programs , batches, classes
Program is the parent. You can create a Batch under a program. And you can create a Class under a Batch. 

Includes CRUD operations for these assests.
PostgreSQL database is used to persist the data. 
Junit testing added. 

Promotion of an Asset - The 'status' of Program, Batch and Class can be changed from Active to Inactive. 
When the status changes, it publises that event using Apache Kafka. 






