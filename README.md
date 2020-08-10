# spring-boot-demo

## How to run
```bash
./mvnw spring-boot:run
```

## Get Token
```bash
curl -X POST 'https://keycloak.appswalker.com/auth/realms/Demo/protocol/openid-connect/token' \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --data-urlencode 'grant_type=password' \
 --data-urlencode 'client_id=springboot-microservice' \
 --data-urlencode 'client_secret=6236bfd5-2440-42d7-982c-170a92414ee2' \
 --data-urlencode 'username=demo_user' \
 --data-urlencode 'password=demopassword'
```

## Anonymous Access
```bash
curl --location --request GET 'http://localhost:8080/hello'
```

## Create User
```bash
curl --location --request POST 'http://localhost:8080/users' \
--header 'Authorization: Bearer {{access_token}}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Chan Tai Man"
}'
```

## List User
```bash
curl --location --request GET 'http://localhost:8080/users' \
--header 'Authorization: Bearer {{access_token}}'
```

## Update User
```bash
curl --location --request PUT 'http://localhost:8080/users' \
--header 'Authorization: Bearer {{access_token}}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 1,
    "name": "Leung Siu Pak"
}'
```

## Delete User
```bash
curl --location --request DELETE 'http://localhost:8080/users/1' \
--header 'Authorization: Bearer {{access_token}}'
```
