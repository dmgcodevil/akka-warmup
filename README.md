# akka-warmup
Simple REST service implemented using akka-http and mongodb-scala-client

## How to run

Just run `java -jar app.jar` from release-1.x folder

## REST API

Resource | HHTP command | Request Body | Response 
-------- | ------------ |--------------|---------
/api/users | POST | {"name": "<user_name>", "email":"<user_email>" } | /api/users/{user_id}
/api/users/{user_id} | GET | |{"name": "<user_name>", "email":"<user_email>" } 
/api/users/{user_id} | PUT | {"name":"<new_user_name(optional)>", "email":"<new_user_email(optional)>"} | "200 user[id={user_id})] has been updated" or BAD_REQUEST if both fields are absent
/api/users/{user_id} | DELETE | | "200 user[id={user_id}] has been deleted" or NOT_FOUND or BAD_REQUEST
