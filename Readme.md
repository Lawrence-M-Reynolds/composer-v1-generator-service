## Run in intellij using testcontainers
These don't persist data. Just run **ComposerLocal** compound configuration to start all instances.

## Running via Intellij using external docker containers
Start/Stop the mongo db.
````
docker run --rm --name generator_database -v .\DbData:/data/db -e MONGO_INITDB_ROOT_USERNAME=mongoadmin -e MONGO_INITDB_ROOT_PASSWORD=secret -d -p 27017:27017 mongo:7.0.9

docker stop generator_database
````
Run the **ComposerV1GeneratorServiceApplication** run configuration.

Note that it will connect using the "localhost" hostname.

To clear existing data.
````
rmdir .\DbData
````

## Running via docker container
Note that in this case the hostname of "generator-database" is used for the connection.

### Build
````
docker image rm lreynolds/composer-v1-generator
mvn clean install -DskipTests
docker build -t lreynolds/composer-v1-generator:latest .
````

### Run
To clear existing data.
````
rmdir .\DbData 
````
Create the network
````
docker network create -d bridge generator_network
````
Start/stop the mongo db (now with the new hostname, on the network and no exposed ports).
````
docker run --rm --name generator_database --hostname generator-database -v .\DbData:/data/db -e MONGO_INITDB_ROOT_USERNAME=mongoadmin -e MONGO_INITDB_ROOT_PASSWORD=secret -d --network=generator_network mongo:7.0.9

docker stop generator_database
````
Start/stop the application (using the new hostname and on the network).
````
docker run --rm --name composer_generator -e SPRING_PROFILES_ACTIVE=docker -e DB_HOST=generator-database -p 8080:8080 --network=generator_network lreynolds/composer-v1-generator:latest

docker stop composer_generator
````