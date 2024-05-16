import requests
import json

url = "http://localhost:29050/api/server/room/book"

payload1 = json.dumps({
    "customerName": "热测试1",
    "startTime": "2024-05-16 20:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "10"
})
payload2 = json.dumps({
    "customerName": "热测试2",
    "startTime": "2024-05-16 20:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "125",
    "deposit": "0",
    "indoorTemperature": "15"
})
payload3 = json.dumps({
    "customerName": "热测试3",
    "startTime": "2024-05-16 20:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "150",
    "deposit": "0",
    "indoorTemperature": "18"
})
payload4 = json.dumps({
    "customerName": "热测试4",
    "startTime": "2024-05-16 20:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "200",
    "deposit": "0",
    "indoorTemperature": "12"
})
payload5 = json.dumps({
    "customerName": "热测试5",
    "startTime": "2024-05-16 20:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "14"
})

token = '5595ddc1-8156-4978-a52d-cff7ed9b0c27'
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
response = requests.request("POST", url, headers=headers, data=payload5)

print(response.text)
