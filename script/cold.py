from api import *

users = {
    "用户1": {"name": "冷测试1", "room": 169},
    "用户2": {"name": "冷测试2", "room": 170},
    "用户3": {"name": "冷测试3", "room": 171},
    "用户4": {"name": "冷测试4", "room": 172},
    "用户5": {"name": "冷测试5", "room": 173},
}

# 0

print("开始测试")

print("第 0 分钟")
last_time = get_time()

user1 = users["用户1"]
token1 = user_login(user1['name'], user1['room'])
if token1 is not None:
    print("房间1登录成功")
watch_ac(token1)

user2 = users["用户2"]
token2 = user_login(user2['name'], user2['room'])
if token2 is not None:
    print("房间2登录成功")
watch_ac(token2)

user3 = users["用户3"]
token3 = user_login(user3['name'], user3['room'])
if token3 is not None:
    print("房间3登录成功")
watch_ac(token3)

user4 = users["用户4"]
token4 = user_login(user4['name'], user4['room'])
if token4 is not None:
    print("房间4登录成功")
watch_ac(token4)

user5 = users["用户5"]
token5 = user_login(user5['name'], user5['room'])
if token5 is not None:
    print("房间5登录成功")
watch_ac(token5)

change(token1, temp=None, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")

# 1
print("第 1 分钟")

change(token1, 18, status=None)

change(token2, temp=None, status=None)

change(token5, temp=None, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 2
print("第 2 分钟")

change(token3, temp=None, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 3
print("第 3 分钟")

change(token2, 19, status=None)

change(token4, temp=None, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 4
print("第 4 分钟")

change(token5, temp=22, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 5
print("第 5 分钟")

change(token1, temp=None, status=3)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 6
print("第 6 分钟")

turn_off(token2)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 7
print("第 7 分钟")

change(token2, temp=None, status=None)

change(token5, temp=None, status=3)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 8
print("第 8 分钟")

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 9
print("第 9 分钟")

change(token1, temp=22, status=None)

change(token4, temp=18, status=3)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 10
print("第 10 分钟")

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")

# 11
print("第 11 分钟")

change(token2, temp=22, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 12
print("第 12 分钟")

change(token5, temp=None, status=1)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 13
print("第 13 分钟")

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 14
print("第 14 分钟")

turn_off(token1)

change(token3, temp=24, status=1)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 15
print("第 15 分钟")

change(token5, temp=20, status=3)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 16
print("第 16 分钟")

turn_off(token2)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 17
print("第 17 分钟")

change(token3, temp=None, status=3)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 18
print("第 18 分钟")

change(token1, temp=None, status=None)

change(token4, temp=20, status=2)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 19
print("第 19 分钟")

change(token2, temp=None, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 20
print("第 20 分钟")

change(token5, temp=25, status=None)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 21
print("第 21 分钟")

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 22
print("第 22 分钟")

turn_off(token3)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 23
print("第 23 分钟")

turn_off(token5)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 24
print("第 24 分钟")

turn_off(token1)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)

last_time = mysleep(1, last_time)
print("\n")


# 25
print("第 25 分钟")

turn_off(token2)

turn_off(token4)

get_status(token1, 1)
get_status(token2, 2)
get_status(token3, 3)
get_status(token4, 4)
get_status(token5, 5)
