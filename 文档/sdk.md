# 分布式邮件调度平台-客户端sdk



## 项目初始化

### 初始化目录&代码

~~~txt
com-hzy-zymail-client
	- api
		ZymailClient # 客户端对外接口
	- config
		ConfigProperties # 读取配置文件
		MailSenderConfig # JavaMailSender 配置文件
	- model
		- dto
			ToEmail # 邮件传输类
resources
	META-INF
		spring.factories # 配置自动装配

~~~

### ZymailClient

~~~java
@EnableConfigurationProperties({ConfigProperties.class})
@Import(MailSenderConfig.class)
public class ZymailClient {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfigProperties configProperties;

    public void sendMail(ToEmail toEmail){
        try {
            // 创建简单邮件消息
            MimeMessage message  = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁要接收
            helper.setFrom(configProperties.getMail().getUsername());
            helper.setTo(toEmail.getToUser());
            //邮件标题
            helper.setSubject(toEmail.getSubject());
            //邮件内容
            helper.setText(toEmail.getContent(),true);
            // 可以添加附件
            // helper.addAttachment("附件名称", new File("附件路径"));
            javaMailSender.send(message);
        }catch (Exception e){
            log.error("客户端邮件发送失败");
            e.printStackTrace();
        }
    }
}
~~~

### ConfigProperties

~~~java
@Data
@ConfigurationProperties(prefix = "zymail")
public class ConfigProperties {
    private String appId;
    private MailProperties mail = new MailProperties();
    private ServerProperties server = new ServerProperties();
    @Data
    public class MailProperties {
        private String host;
        private String username;
        private String password;
    }
    @Data
    public class ServerProperties {
        private String url;
    }
}
// 依赖该sdk后 yml模板
zymail:
  # 邮箱配置
  mail:
    host: smtp.qq.com
    username: 2791517764@qq.com
    password: ikpldrqevfevdgeb
  server:
    # 接入地址
    url: http://localhost:8888
  # id
  app-id: 测试系统
~~~

### MailSenderConfig

~~~java
@Data
@Configuration
@EnableConfigurationProperties({ConfigProperties.class})
@ConditionalOnClass(ZymailClient.class)
public class MailSenderConfig {
    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(configProperties.getHost());
        javaMailSender.setUsername(configProperties.getUsername());
        javaMailSender.setPassword(configProperties.getPassword());
        javaMailSender.setDefaultEncoding("utf-8");
        return javaMailSender;
    }
}
~~~

### 总结

现在已经是具备了基础的邮件发送功能，其他项目只需要引入坐标就可以使用

~~~xml
        <dependency>
            <groupId>com.hzy</groupId>
            <artifactId>zymail-client</artifactId>
            <version>1.0</version>
        </dependency>
~~~

## 引入Apache HttpClient 实现与server的通信

~~~xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>
~~~

### 创建config-HttpClientConfig

~~~java
@Configuration
public class HttpClientConfig {
    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }
}
~~~

### 添加init-ZymailInitListener

~~~java
@Component
public class ZymailInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CloseableHttpClient httpClient;

    // 这个监听器中编写自定义的业务逻辑，以便在应用程序启动时或ApplicationContext被刷新时执行一些初始化工作。
    // 在这里编写与zymail-server连接的代码
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 在应用程序启动或ApplicationContext被刷新时执行的代码
        System.out.println("ApplicationContext已刷新，可以执行初始化工作。");
        HttpGet httpGet = new HttpGet("http://localhost:8888/test");
        try {
            HttpResponse execute = httpClient.execute(httpGet);
            String string = EntityUtils.toString(execute.getEntity());
            System.out.println(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
~~~

### 测试

#### zymail-server添加测试代码

![image-20231003155656629](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031710490.png)

#### test项目依赖client

![image-20231003155705240](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031710491.png)

#### 成功与server通信

![image-20231003155711382](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031710492.png)

### Apache HttpClient 二次封装

#### 添加HttpClientUtils 提供Resful风格请求方式

~~~java
@Service
public class HttpClientUtils {
    @Autowired
    private CloseableHttpClient httpClient;

    public String get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return getResponseStr(response);
        }
    }
    /**
     *
     * @param url
     * @param data 数据用json传送
     * @return
     * @throws Exception
     */
    public String post(String url,String data) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return getResponseStr(response);
        }
    }
    public String put(String url,String data) throws Exception {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            return getResponseStr(response);
        }
    }
    public String delete(String url) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
            return getResponseStr(response);
        }
    }
    /**
     * 获取响应字符串
     * @param res
     * @return
     * @throws IOException
     */
    private static String getResponseStr(HttpResponse res) throws IOException {
        return EntityUtils.toString(res.getEntity(), StandardCharsets.UTF_8);
    }
}

~~~

#### 修改HttpClientConfig 

~~~java
@Configuration
public class HttpClientConfig {
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200); // 设置最大连接数
        connectionManager.setDefaultMaxPerRoute(100); // 设置每个路由的最大连接数
        return connectionManager;
    }
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(5000) // 连接超时时间
                .setSocketTimeout(5000) // 请求超时时间
                .build();
    }
    @Bean
    public CloseableHttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = poolingConnectionManager();
        RequestConfig requestConfig = requestConfig();
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .addInterceptorFirst(new RequestDefaultHeaders())
                .build();
    }
    // 配置默认请求头
    class RequestDefaultHeaders implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) {
            // 添加默认请求头
            Header defaultHeader = new BasicHeader("Content-Type", "application/json");
            request.addHeader(defaultHeader);
        }
    }
}
~~~

#### 对比：重新封装之后代码见简洁度提高

![image-20231003155720837](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031710493.png)

## 调度源接入

### 添加调度源实体类 Source

~~~java
@Data
@Component
public class Source implements Serializable {

    /**
     * 项目名，作为本地源key
     */
    private String appId;

    /**
     * 邮件服务器
     */
    private String host;

    /**
     * 发送方邮箱地址
     */
    private String username;

    /**
     * 调度源url
     */
    private String url;

    /**
     * 启动时间
     */
    private Date startTime = new Date();

    private static final long serialVersionUID = -7011290275941486363L;

}

~~~

### 修改 ZymailInitListener

~~~java
@Component
@Slf4j
public class ZymailInitListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ZymailServerApi zymailServerApi;
    @Autowired
    private ConfigProperties configProperties;
    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 接入server
        Source source = new Source();
        source.setAppId(configProperties.getAppId());
        source.setHost(configProperties.getMail().getHost());
        source.setUsername(configProperties.getMail().getUsername());
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        source.setUrl(String.format("http://%s:%s%s", host, port, contextPath));

        // 调用接入接口
        zymailServerApi.addSource(source);

        log.info("应用接入Server成功！");

    }
}
~~~

### 添加ZymailServerApi 远程调用接口‘

~~~java
@Component
public class ZymailServerApi {
    @Autowired
    private HttpClientUtils httpClientUtils;
    @Autowired
    private ConfigProperties configProperties;
    public void addSource(Source source) {

        try {
            httpClientUtils.put(configProperties.getServer().getUrl()+ "/sources",
                    JSONObject.toJSONString(source));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
~~~

### 添加的依赖

~~~xml
        <!--fastjson依赖-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.33</version>
        </dependency>
~~~

### 测试

测试看zymail-server记录

## 远程调用发送邮件实现

### 引入web依赖

~~~xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
~~~

### 添加 AppHttpCodeEnum Mail Result

~~~
代码在server开发记录查看
~~~

### 创建controller.MailController 类

~~~java
@RestController
@RequestMapping("/mails")
public class MailController {
    @Autowired
    private ZymailClient zymailClient;

    @PostMapping
    public Result sendEmail(@RequestBody Mail mail) {
        zymailClient.sendMail(mail);
        return Result.okResult();
    }
}
~~~

### 修改HttpClientConfig超时时间

~~~java
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(15000) // 连接超时时间
                .setSocketTimeout(15000) // 请求超时时间
                .build();
    }
~~~

