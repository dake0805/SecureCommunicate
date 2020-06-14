# SecureCommunicate
网路与信息安全 大作业2

实现两个主机之间的密钥分发和加密传输。
要求：
1. 用RSA算法实现两个主机之间的密钥分发；
2. 用分发的密钥和DES加密，实现两个主机之间的加密数据传输；
3. 两个步骤在程序中自动执行完，无手动参与；
4. 实现代码，并写出实现技术文档。

# Features
需要开启一个服务端，所有的客户端连接到此服务端，就能互相发送消息。

RSA

AES, DES
可拓展

# Usage

server:

> java -jar SecureConnect-homework2-1.0-SNAPSHOT-jar-with-dependencies.jar -s -p your port

client:

> java -jar SecureConnect-homework2-1.0-SNAPSHOT-jar-with-dependencies.jar -c your server -p your port

已开启服务端 112.74.126.191:3456 用于测试

# 存在的问题

- DES加密不可用
- 在控制台输入时有新内容输出，已经输入还未发送的内容会消失。
- cmd等终端的中文乱码（非utf-8）
- 账户名称重复验证
