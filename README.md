# 分布式邮件调度系统介绍

为开发者(为应用集成邮件功能，统一管理调度邮件)与用户(调度邮件)提供的邮件统一管理平台

技术亮点： 

- 解耦多个应用中都需要使用的邮件功能，抽象成服务(管理平台 Server )/客户端模式使应用开发轻量化。 
- 服务端提供可视化管理平台和高级邮件功能，客户端 SDK 提供基础邮件功能与接入服务端功能。 
-  基于 Spring Boot Starter 自主设计客户端 SDK ，只需引入 SDK 即可为应用集成邮件功能和简约可视化的管理平台。 
- 使用 Nacos 进行服务注册，服务端可启动多个实列，使用 Spring Cloud Gateway 实现路由请求与负载均衡。 5. 使用 RabbitMQ 实现服务注销或者意外掉线与调度器操作通知，实现及时的任务重载与全局同步调度器状态。 
- 使用 Quartz 实现邮件的定时、延时任务发布并提供暂停、移除和修改任务的接口。 
- 使用 Redis 进行关键数据的缓存，实现服务端重启时的调度源重加载，任务重载等，提高系统的性能、可靠性和可用性。
- 使用 RPC 远程调用实现客户端和服务端的通信，实现应用解耦。

开发记录：不完全，后续会更新[服务端](./文档/server.md)、[客户端](./文档/sdk.md)、[前端](./文档/前端.md)

## 演示

### 邮件功能

支持富文本，支持图片，支持定时，可以选择调度源

![image-20231101225528522](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307243.png)

可以保存邮件为模板

![image-20231101225615777](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307244.png)

分页查询历史邮件

![image-20231101225653990](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307245.png)

模板可直接复制使用

![image-20231101225727902](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307246.png)

### 调度功能

可以对调度源与任务进行操作

![image-20231101225806806](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307247.png)

可以对任务参数进行修改

![image-20231101225837581](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307248.png)

服务重启时会对任务进行重载

![image-20231101230027238](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307249.png)

![image-20231101225905297](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307250.png)

### 日志管理

记录操作日志

![image-20231101230138982](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307251.png)

![image-20231101230121466](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311012307252.png)
