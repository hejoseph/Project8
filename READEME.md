# spring-boot
## Technical:

1. Framework: Spring Boot v2.2.7
2. Java 8
3. Docker

## Run app

Do the following commands in each folders (GpsUtil, Pricer, Reward, TourGuide)

```mvn 
mvn clean install
```

```Java (after compilation)
java -jar target/[COMPILE_NAME].jar
```

## Run app with Docker

After all jar compiled, do the following commands to build docker image for (GpsUtil, Pricer, Reward)

```Powershell commands
cd GpsUtil
docker build -t gpsutil .
cd ../Pricer 
docker build -t pricer .
cd ../Reward
docker build -t reward .

docker-compose up
```

```Run TourGuide separately
cd TourGuide
java -jar target/Project8-0.0.1-SNAPSHOT.jar
```


