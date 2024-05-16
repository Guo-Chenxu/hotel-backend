import requests
import json

url = "http://localhost:29050/api/server/room/book"

payload1 = json.dumps({
    "customerName": "冷测试1",
    "startTime": "2024-05-16 18:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "32"
})
payload2 = json.dumps({
    "customerName": "冷测试2",
    "startTime": "2024-05-16 18:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "125",
    "deposit": "0",
    "indoorTemperature": "28"
})
payload3 = json.dumps({
    "customerName": "冷测试3",
    "startTime": "2024-05-16 18:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "150",
    "deposit": "0",
    "indoorTemperature": "30"
})
payload4 = json.dumps({
    "customerName": "冷测试4",
    "startTime": "2024-05-16 18:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "200",
    "deposit": "0",
    "indoorTemperature": "29"
})
payload5 = json.dumps({
    "customerName": "冷测试5",
    "startTime": "2024-05-16 18:00:00",
    "leaveTime": "2024-05-20 00:00:00",
    "price": "100",
    "deposit": "0",
    "indoorTemperature": "35"
})

token = 'd2ef6e69-58c3-4c50-86cb-c39138f8dd65'
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
