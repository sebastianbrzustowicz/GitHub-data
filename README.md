# GitHub-data
Java + Spring Boot. Application for retrieving repository specific information from GitHub API and processing received data.    
All repositories that are fork type are filtered.      
The GitHub API has a limited number of requests from the same IP address.      
There is some error handling in this situation and when the user is not found.    

The data collected from the GitHub API through various queries is as follows:     

```json
[
{
        "repositoryName": "repository name",
        "ownerLogin": "repository owner login",
        "size": size of repo in KB,
        "branchData": {
            "main": "last commit sha on this branch",
            "secondBranch": "last commit sha on this branch"
        }
        "language": "major programming language"
}
]
```

# Launching application
When errors occur, take into account that the application was created with:         
- Java 21.0.1        
- Apache Maven 3.9.5        
- Spring Boot 3.2.2
  
Paste this code into console to run GitHub-data app:         
```console
git clone https://github.com/sebastianbrzustowicz/GitHub-data
cd ./GitHub-data/GHData
mvn clean install
mvn spring-boot:run

```

# Endpoint
The `Content-Type` header in the endpoint request must be `application/json`:
| HTTP method | endpoint | description | request type | response type |
| -------------- | -------------- | -------------- | -------------- | -------------- |
| :green_circle: GET | /GitHub/getData/{username} | get repositories data | - | JSON data |

# Example
Input in bash console:
```console
curl -X GET -H "Content-Type: application/json" http://localhost:8080/GitHub/getData/sebastianbrzustowicz
```
Part of the result:
```json
[
    {
        "repositoryName": "CLI-users-management",
        "ownerLogin": "sebastianbrzustowicz",
        "size": 124,
        "branches": {
            "main": "0b025cbda0e64803e1e81f336b469111a5ff9d26"
        },
        "language": "Python"
    },
    {
        "repositoryName": "Data-scraping-webapp",
        "ownerLogin": "sebastianbrzustowicz",
        "size": 25,
        "branches": {
            "main": "48211a610cdd1cb7865694044f40f0f75a2aa076"
        },
        "language": "Python"
    },
    {
        "repositoryName": "E-commerce-API-with-AI-assistant",
        "ownerLogin": "sebastianbrzustowicz",
        "size": 151,
        "branches": {
            "main": "8a5773e673e20c98c7a2cc53eea0374b301c96e0"
        },
        "language": "Java"
    }
]
```
If user is not found:    
```json
{
    "status": 404,
    "message": "User not found"
}
```
If the requests limit is reached:    
```json
{
    "status": 403,
    "message": "The limit of requests for the GitHub API from this IP address has been reached"
}
```

## License

GitHub-data is released under the MIT license.

## Author

Sebastian Brzustowicz &lt;Se.Brzustowicz@gmail.com&gt;
