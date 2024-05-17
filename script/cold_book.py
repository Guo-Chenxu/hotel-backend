import requests
import json

url = "http://localhost:29050/api/server/room/book"
properties_url = "http://10.29.12.98:29050/api/server/cool/properties"

payload1 = json.dumps({
    "customerName": "冷测试1",
    "startTime": "2024-05-17 11:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "32"
})
payload2 = json.dumps({
    "customerName": "冷测试2",
    "startTime": "2024-05-17 11:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "125",
    "deposit": "0",
    "indoorTemperature": "28"
})
payload3 = json.dumps({
    "customerName": "冷测试3",
    "startTime": "2024-05-17 11:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "150",
    "deposit": "0",
    "indoorTemperature": "30"
})
payload4 = json.dumps({
    "customerName": "冷测试4",
    "startTime": "2024-05-17 11:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "200",
    "deposit": "0",
    "indoorTemperature": "29"
})
payload5 = json.dumps({
    "customerName": "冷测试5",
    "startTime": "2024-05-17 11:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "35"
})

properties = json.dumps({
    "mode": 0,
    "count": 3,
    "upperBoundTemperature": 28,
    "lowerBoundTemperature": 18,
    "defaultTargetTemp": 25,
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

token = '0b94aee0-3ddc-47c4-a7fc-8f352d607cb4'
headers = {
    'Authorization': token,
    'Content-Type': 'application/json',
    'Accept': '*/*',
    'Connection': 'keep-alive'
}


response = requests.request("POST", url, headers=headers, data=payload1)
response = requests.request("POST", url, headers=headers, data=payload2)
response = requests.request("POST", url, headers=headers, data=payload3)
response = requests.request("POST", url, headers=headers, data=payload4)
response1 = requests.request("POST", url, headers=headers, data=payload5)
response2 = requests.request(
    "POST", properties_url, headers=headers, data=properties)

print(response1.text)
print(response2.text)
