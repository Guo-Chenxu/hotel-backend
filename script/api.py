import datetime
import json
import time
import requests

base_url = "http://localhost:29050/api/customer"
login_url = f"{base_url}/customer/login?name=%s&room=%s"
watch_url = f"{base_url}/cool/watchAC"
change_url = f"{base_url}/cool/change"
ac_status_url = f"{base_url}/cool/acStatus"
turn_off_url = f"{base_url}/cool/turnOff"
time_url = f"http://localhost:29050/api/timer/timer/time"


def user_login(name, room: str):
    url = login_url % (name, room)
    payload = {}
    headers = {
        'Accept': '*/*',
        'Connection': 'keep-alive'
    }
    response = requests.request(
        "POST", url, headers=headers, data=payload)
    return response.json()['data']['token']


def watch_ac(token):
    headers = {
        'Authorization': token,
        'Accept': '*/*',
        'Connection': 'keep-alive',
    }
    requests.request("GET", watch_url, headers=headers, data={})


def change(token: str, temp: float, status: int):
    data = {}
    if status is not None:
        data['status'] = status
    if temp is not None:
        data['targetTemperature'] = temp
    payload = json.dumps(data)
    headers = {
        'Authorization': token,
        'Content-Type': 'application/json',
        'Accept': '*/*',
        'Connection': 'keep-alive',
    }

    requests.request(
        "POST", change_url, headers=headers, data=payload)


def get_status(token, no):
    headers = {
        'Authorization': token,
        'Accept': '*/*',
        'Connection': 'keep-alive',
    }

    response = requests.request(
        "GET", ac_status_url, headers=headers, data={})
    status = response.json()['data']
    print(f"房间 {no} 的空调状态是：{status}")


def turn_off(token: str):
    headers = {
        'Authorization': token,
        'Accept': '*/*',
        'Connection': 'keep-alive',
    }

    requests.request(
        "POST", turn_off_url, headers=headers, data={})


def get_time():
    headers = {
        'Accept': '*/*',
        'Connection': 'keep-alive',
    }

    response = requests.request(
        "GET", time_url, headers=headers, data={})
    return response.json()['data']


def sleep(minutes, last_time) -> int:
    print(datetime.datetime.utcfromtimestamp(
        float(last_time / 1000.0)).strftime("%Y-%m-%d %H:%M:%S.%f"))
    while get_time() - last_time < minutes * 1000 * 60:
        time.sleep(1)
    print(datetime.datetime.utcfromtimestamp(
        float(get_time() / 1000.0)).strftime("%Y-%m-%d %H:%M:%S.%f"))
    return get_time()
