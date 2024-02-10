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
        "branchData": {
            "main": "last commit sha on this branch",
            "secondBranch": "last commit sha on this branch",
            // more branches
        }
},
{
// another repository
},
// more repositories
]
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
Output:
```json
[
    {
        "repositoryName": "CLI-users-management",
        "ownerLogin": "sebastianbrzustowicz",
        "branchData": {
            "main": "0b025cbda0e64803e1e81f336b469111a5ff9d26"
        }
    },
    {
        "repositoryName": "Data-scraping-webapp",
        "ownerLogin": "sebastianbrzustowicz",
        "branchData": {
            "main": "48211a610cdd1cb7865694044f40f0f75a2aa076"
        }
    },
    {
        "repositoryName": "E-commerce-API-with-AI-assistant",
        "ownerLogin": "sebastianbrzustowicz",
        "branchData": {
            "main": "8a5773e673e20c98c7a2cc53eea0374b301c96e0"
        }
    },
    {
        "repositoryName": "Flight-quality-overview-microservice",
        "ownerLogin": "sebastianbrzustowicz",
        "branchData": {
            "main": "e4c49785c391ab6aab495e1c6630411d6d8656df"
        }
    },
// and so on
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
