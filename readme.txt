Git hub repo : https://github.com/rajeevweyburn/test-repo1

Step 1 : Clone https://github.com/rajeevweyburn/test-repo1.git

Step 2 : Run mvn clean package spring-boot:run -Dserver.port=8080 -DskipTests from test-repo1\proj directory ( Alternatively you could run 
         the project as spring boot application from Eclipse from com.example --> src/main/java ) 
         
Step 3 : BiddingSpringbootApplication creates static data on startup for creating 2 employers, 2 projects and for each project
         2 bids and for 2'nd project 1 bid.
         
Step 4 : http://localhost:8080/projects --> Gets all projects from all employers 

Step 5 : http://localhost:8080/employer/1 --> gets all projects created by employer with id 1 
Content-Type --> application/json Accept --> application/json

Step 6 : http://localhost:8080/employer/1/project/1 --> get lowest bid for a project id ... returns only the lowest valid 
bid which is less than project end date 

Step 7 : POST --> http://localhost:8080/employer/1 --> creates a new project for employer with id 1 

{
    "budgetAmount": 8,
    "projEndDate": "2018-02-25 17:06",
    "description": "description_new1"
}

Step 8 : POST http://localhost:8080/project/2 --> Bid a project with id 2 

{
    "bidAmount": 99
}


         

