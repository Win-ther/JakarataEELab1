INSTRUCTIONS
- To run application in Docker Desktop.
    - Download project.
    - Download and start Docker Desktop.
    - Run command in terminal:
      - "mvn clean package"
      - "docker-compose up"
    - Application is now started in a docker-container.
    - Use Postman or Insomnia to run http methods GET, POST, PATCH, DELETE on URI:
      - http://localhost:8080/jakartaeelabb/persons
      - http://localhost:8080/jakartaeelabb/persons/ {id that you get from POST-method}

 - To run IntegrationTests.
    - Download project.
    - Download and start Docker Desktop.
    - Run command in terminal:
        - "mvn clean package"
    - Run PersonIT-class.
