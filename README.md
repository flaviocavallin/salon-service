# **Salon Service**

## 1. To run the application execute
`./salon-service.sh`

## 2. If you have Postman:
Import the salon.postman_collection.json file
Note: In future release I will include swagger

## 3. Endpoints

### **New Client**
Example:

POST http://localhost:8080/salon-service/api/v1/clients

Body
```json
{"firstName": "name57", "lastName": "lastName57", "email": "a571@a1.com",  "phone": "phone57", "gender": "Male"}
```
Response
```json
{
    "id": "ef71a69c-da54-478d-b632-500b46826478",
    "firstName": "name57",
    "lastName": "lastName57",
    "email": "a571@a1.com",
    "phone": "phone57",
    "gender": "Male",
    "banned": false
}
```

### **Get Client**
Example:

GET http://localhost:8080/salon-service/api/v1/clients/{clientId}
Eg. clientId=ef71a69c-da54-478d-b632-500b46826478

Response
```json
{
    "id": "ef71a69c-da54-478d-b632-500b46826478",
    "firstName": "name57",
    "lastName": "lastName57",
    "email": "a571@a1.com",
    "phone": "phone57",
    "gender": "Male",
    "banned": false
}
```

### **Delete Client**
Example:

DELETE http://localhost:8080/salon-service/api/v1/clients/{clientId}
Eg. clientId=ef71a69c-da54-478d-b632-500b46826478

Response: HTTP status code: 200 Ok


### **Ban Client**
Example:

PATCH http://localhost:8080/salon-service/api/v1/clients/{clientId}
Eg. clientId=ef71a69c-da54-478d-b632-500b46826478

Response: HTTP status code: 200 Ok


### **New Appointment**
Example:

POST http://localhost:8080/salon-service/api/v1/appointments

Body
```json
{"clientId": "4d916ca9-9bcb-486d-92e8-1dd771af4bdf", 
 "startTime": "2016-04-02 12:45:00 +0100", 
 "endTime": "2016-04-02 13:45:00 +0100",
 "treatments":[
 		{"name": "Full Head Colour1111", "price":85.0, "loyaltyPoints":1}
 	],
 "purchases":[
 		{"name": "Shampoo11111", "price":19.5, "loyaltyPoints":2}
 	]
}
```
Response: HTTP status code: 200 Ok


Body
```json
{"clientId": "4d916ca9-9bcb-486d-92e8-1dd771af4bdf", 
 "startTime": "2016-04-02 12:45:00 +0100", 
 "endTime": "2016-04-02 13:45:00 +0100",
 "treatments":[
 		{"name": "Full Head Colour1111", "price":85.0, "loyaltyPoints":1}
 	]
}
```
Response: HTTP status code: 200 Ok


### **Delete Appointment**
Example:

DELETE http://localhost:8080/salon-service/api/v1/appointments/{appointmentId}
Eg. appointmentId=7416ebc3-12ce-4000-87fb-82973722ebf4

Response: HTTP status code: 200 Ok

### **New Purchase**
Example:

POST http://localhost:8080/salon-service/api/v1/appointments/{appointmentId}/purchase
eg. appointmentId=7416ebc3-12ce-4000-87fb-82973722ebf4

{"appointmentId": "7416ebc3-12ce-4000-87fb-82973722ebf4", name": "Shampo0456", "price":19.5, "loyaltyPoints":30}

Body
```json
{"clientId": "4d916ca9-9bcb-486d-92e8-1dd771af4bdf", 
 "startTime": "2016-04-02 12:45:00 +0100", 
 "endTime": "2016-04-02 13:45:00 +0100",
 "treatments":[
 		{"name": "Full Head Colour1111", "price":85.0, "loyaltyPoints":1}
 	],
 "purchases":[
 		{"name": "Shampoo11111", "price":19.5, "loyaltyPoints":2}
 	]
}
```
Response: HTTP status code: 200 Ok


### **Upload File**
Example:

POST http://localhost:8080/salon-service/api/v1/files/{fileType}
eg. fileType=clients
eg. fileType=appointments
eg. fileType=services
eg. fileType=purchases

body: file = clients.csv
Note: the name of the file has to match with the fileType.  eg. clients-something.csv

The files has to be imported in the following order: 
1 - clients
2- appointments
3 - services
4 - purchases

### **Get Top Loyal Clients**
Example:

GET http://localhost:8080/salon-service/api/v1/clients/top/{limit}/loyal?dateFrom={date}

eg. limit = 5
eg. date = 2010-01-01

```json
[
    {
        "id": "aa1b3e64-8379-4e64-8b1f-d9f542058a44",
        "email": "bobwunsch@wehner.info",
        "points": 965
    },
    {
        "id": "da076851-9b11-46dc-8da0-1da71bf7d909",
        "email": "kirsten@larkinrosenbaum.biz",
        "points": 945
    },
    {
        "id": "78afc583-c1ea-472b-b24d-4316e74d071d",
        "email": "adah@doyle.net",
        "points": 905
    },
    {
        "id": "263f67fa-ce8f-447b-98cf-317656542216",
        "email": "romeorunolfon@corwin.co",
        "points": 895
    },
    {
        "id": "271c2bb5-689f-4a00-a552-83605affe02b",
        "email": "juliandibbert@rodriguez.org",
        "points": 875
    }
]
```

