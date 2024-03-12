# FileIngestion
1. This is a simple project to take ful file path as input and create csv file for the result.
2. The project contains 2 main API: IngestFileNoDB and IngestFileDB
3. Data source properties(in application.properties) neeed to configure to point it to your local DB.
4.1 If no data source, please remove the following:
    spring.datasource.url
    spring.datasource.username
    spring.datasource.password
    spring.jpa.hibernate.ddl-auto
4.2 Update pom file to remove JPA dependencies
4.3 Update FileDTO.java to remove JPA injection
5. Import FileIngestionPostman.json to your postman and run the main API IngestFileNoDB/IngestFileDB
6. The output file will create under root path.
