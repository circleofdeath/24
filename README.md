## Building

sql setup commands:
```sql
CREATE TABLE users (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username varchar(32) UNIQUE,
    password varchar(64),
    salt varchar(64),
    token varchar(64)
);

CREATE TABLE books (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar(32) UNIQUE,
    author INT,
    content varchar
);
```

command setup:
```bash
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12  -validity 3650
```

## Running

to run project use this command:
```bash
./mvnw clean package && sudo docker compose up --build
```
