import requests
import json

book_url = "http://localhost:29050/api/server/room/book"
properties_url = "http://10.29.12.98:29050/api/server/cool/properties"

payload1 = json.dumps({
    "customerName": "热测试1",
    "startTime": "2024-05-17 10:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "10"
})
payload2 = json.dumps({
    "customerName": "热测试2",
    "startTime": "2024-05-17 10:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "125",
    "deposit": "0",
    "indoorTemperature": "15"
})
payload3 = json.dumps({
    "customerName": "热测试3",
    "startTime": "2024-05-17 10:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "150",
    "deposit": "0",
    "indoorTemperature": "18"
})
payload4 = json.dumps({
    "customerName": "热测试4",
    "startTime": "2024-05-17 10:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "200",
    "deposit": "0",
    "indoorTemperature": "12"
})
payload5 = json.dumps({
    "customerName": "热测试5",
    "startTime": "2024-05-17 10:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "14"
})

properties = json.dumps({
    "mode": 1,
    "count": 3,
    "upperBoundTemperature": 25,
    "lowerBoundTemperature": 18,
    "defaultTargetTemp": 22,
    "defaultStatus": 2,
    "high": {
        "changeTemperature": 1,
        "price": "1"
    },
    "middle": {
        "changeTemperature": 0.5,
        "price": "0.5"
    },
    "low": {
        "changeTemperature": 0.333,
        "price": "0.333"
    }
})

token = '9810eb5f-76f4-4f1b-905f-a26ab1136c92'
headers = {
    'Authorization': token,
    'Content-Type': 'application/json',
    'Accept': '*/*',
    'Connection': 'keep-alive'
}


response = requests.request("POST", book_url, headers=headers, data=payload1)
response = requests.request("POST", book_url, headers=headers, data=payload2)
response = requests.request("POST", book_url, headers=headers, data=payload3)
response = requests.request("POST", book_url, headers=headers, data=payload4)
response = requests.request("POST", book_url, headers=headers, data=payload5)
response1 = requests.request("POST", properties_url, headers=headers, data=properties)

print(response.text)
print(response1.text)
