## 使用流程
### 申请SecretId和SecretKey
### 初始化配置
Application的onCreate()中调用：
```java
AirX.getInstance().init(this, "SecretId", "SecretKey");
```
开启OkHttp日志：
```java
AirX.getInstance().setDebugMode(true);
```
### 接口调用
见项目sample模块中MainActivity.class相关代码
### 混淆
见项目airx模块proguard-rules.pro文件
## 参考
- [API列表](https://api.airdwing.com/doc/)
- 错误码：见AirXError类
## 注意
短信接口限额3次/日，若因超出限额而被拉入黑名单，后果自负