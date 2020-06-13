# SecureCommunicate
网路与信息安全 大作业2

RSA

AES, DES

中文不是utf-8会乱码

# Usage

server:

> java -jar SecureConnect-homework2-1.0-SNAPSHOT-jar-with-dependencies.jar -s -p your port

client:

> java -jar SecureConnect-homework2-1.0-SNAPSHOT-jar-with-dependencies.jar -c your server -p your port

# 存在的问题

- DES加密不可用
- 在控制台输入时有新内容输出，已经输入还未发送的内容会消失。
- cmd等终端的中文乱码（非utf-8）