# 分布式邮件调度平台-客户端sdk



## 项目初始化

### 初始化目录&代码

~~~txt
com-hzy-zymail-server
	- config
		RedisConfig # reids配置类
	- constant
		AppHttpCodeEnum # 状态码
	- utils
		FastJsonRedisSerializer # redis序列化
		RedisCache # RedisTemplate二次封装简化代码
		Result # 全局统一响应类
	 ZymailServerApp # 启动类
~~~

##### 依赖

~~~xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--redis依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!--fastjson依赖-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.33</version>
        </dependency>
    </dependencies>
~~~



##### RedisConfig

~~~java
@Configuration
public class RedisConfig {

    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
~~~

##### AppHttpCodeEnum 

~~~java
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),

    SYSTEM_ERROR(500,"出现错误");



    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

~~~

##### FastJsonRedisSerializer

~~~java
public class FastJsonRedisSerializer<T> implements RedisSerializer<T>
{

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    static
    {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public FastJsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz);
    }


    protected JavaType getJavaType(Class<?> clazz)
    {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
~~~

##### RedisCache

~~~java
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key
     * @param hkey
     */
    public void delCacheMapValue(final String key, final String hkey)
    {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hkey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }

}

~~~

## AOP 

### 全局异常处理

添加自定义异常类

~~~java
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}

~~~

GlobalExceptionHandler  全局异常拦截器

~~~java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public Result businessExceptionHandler(SystemException ex) {
        log.error("业务异常 -- 错误码：" + ex.getCode() + " 错误信息：" + ex.getMessage());
        return Result.errorResult(ex.getCode(), ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(Exception ex) {
        log.error("运行时异常 -- 错误信息：" + ex.getMessage());
        return Result.errorResult(500, ex.getMessage());
    }

}

~~~

SystemLog 自定义注解

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SystemLog {
    String value();
}
~~~

*LoggerAop* 日志切面类

~~~java
@Component // 注入容器
@Aspect // 告诉spring容器这是一个切面类
@Slf4j
public class LoggerAop {
    @Pointcut("@annotation(com.hzy.server.annotion.SystemLog)")
    public void controllerLog() {
    }

    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = attributes.getRequest();
        // 获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("接口描述   : {}", systemLog.value());
        log.info("URL : " + httpServletRequest.getRequestURL().toString());
        log.info("URL : " + httpServletRequest.getRequestURL().toString());
        log.info("HTTP_METHOD : " + httpServletRequest.getMethod());
        log.info("IP : " + httpServletRequest.getRemoteAddr());
        Enumeration<String> enu = httpServletRequest.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            log.info("name : " + name + ", value : " + httpServletRequest.getParameter(name));
        }
    }

    @AfterReturning(returning = "ret", pointcut = "controllerLog()")
    public void doAfterReturning(Object ret) {
        log.info("RESPONSE : " + ret);
    }
    private SystemLog getSystemLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SystemLog.class);
    }
}

~~~

## 调度源接入

### SourceController

~~~java
@RequestMapping("/sources")
@RestController
public class SourceController {
    @Resource
    private SourceService sourceService;
    /**
     * 客户端接入，添加调度源（远程调用）
     */
    @SystemLog("添加调度源")
    @PutMapping
    public Result addSource(@RequestBody Source source) {
        sourceService.addSource(source);
        return Result.okResult();
    }
}
~~~

### SourceService 及其实现类 

~~~java
public interface SourceService {
    /**
     * 添加调度源
     * @param source
     * @return
     */
    void addSource(Source source);
}

@Service
@Slf4j
public class SourceServiceImpl implements SourceService {

    @Autowired
    private RedisCache redisCache;
    @Override
    public void addSource(Source source) {
        if (StringUtils.isNotBlank(source.getAppId()) &&
                StringUtils.isNotBlank(source.getHost()) &&
                StringUtils.isNotBlank(source.getUsername()) &&
                StringUtils.isNotBlank(source.getUrl())) {
            String key = source.getAppId();
            redisCache.setCacheMapValue(SystemConstant.SOURCES_KEY,key,source);
            log.info(key+"接入成功");
        }
    }
}
~~~

### SystemConstant 系统常量类 

~~~java
public class SystemConstant {
    // 调度源key
    public final static String SOURCES_KEY = "sources";
}
~~~

### 测试

![image-20231003155746276](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714983.png)

![image-20231003155753534](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714984.png)

## server邮件发送接口实现(为管理平台提供)

### 添加依赖

~~~xml
        <!--mybatisPlus依赖-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.3</version>
        </dependency>
        <!--mysql数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.17</version>
        </dependency>
        <!--阿里巴巴数据库连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.13</version>
        </dependency>
~~~

### 配置文件

~~~yml
spring:
 datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    #driver-class-name: com.mysql.cj.jdbc.Driver
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/zymail?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: 120125hzy.
  mybatis-plus:
    # 开启雪花算法生成ID
    global-config:
      db-config:
        id-type: 1
    # 将字段名大写切换为下划线
        column-underline: true
~~~

 ### 添加 apache httpclient 配置类 工具类 邮件配置类

~~~java
代码看client
~~~

### 添加配置类  javaMailSender ConfigProperties

~~~java
代码看client
~~~

### 添加邮件实体类 mapper servive  

~~~java
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mail")
public class Mail implements Serializable {
    // 邮件id@TableId
    private Long emilId;

    // 调度源(发件人)
    private String source;
    // 收件人
    private String toUser;
    // 邮件主题
    private String subject;
    // 邮件内容
    private String content;
    // 是否被保存为模板
    private Integer isTemplate;
    // 发送时间
    private Date sendTime;

    // 下面的字段数据库不存在 用于定时任务
    /**
     * 是否定时
     */
    @TableField(exist = false)// 该字段在表中不存在
    private Boolean timer;

    /**
     * 开始时间
     */
    @TableField(exist = false)// 该字段在表中不存在
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)// 该字段在表中不存在
    private Date endTime;

    /**
     * crontab表达式
     */
    @TableField(exist = false)// 该字段在表中不存在
    private String crontab;

}
public interface MailMapper extends BaseMapper<Mail> {

}
public interface MailService extends IService<Mail> {

}
@Service
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail> implements MailService {

}
~~~

### 添加本地调度源邮件服务类

~~~java
public interface LocalMailService {
    /**
     * 发送邮件（定时/单次）
     * @param mail
     */
    void sendMail(Mail mail);

    /**
     *  发送邮件
     * @param toUser
     * @param subject
     * @param content
     */
    void sendHtmlMail(String toUser, String subject, String content);
}
@Service
public class LocalMailServiceImpl implements LocalMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfigProperties configProperties;
    @Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()){
            // 未定时直接发送
            sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
        }else {
            // TODO 本地调度源定时邮件

        }
    }

    @Override
    public void sendHtmlMail(String toUser, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(configProperties.getMail().getUsername());
            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }
}
~~~

### 添加远程调度眼服务类

~~~java
public interface RemoteMailService {
    /**
     * 发送邮件（定时/单次）
     * @param mail
     */
    void sendMail(Mail mail);
}
@Service
public class RemoteMailServiceImpl implements RemoteMailService {

    @Autowired
    private ZymailClientApi zymailClientApi;

    @Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()){
            try {
                zymailClientApi.sendEmail(mail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            // TODO 远程调度源定时发送

        }
    }
}
~~~

### 添加 MailController 和 ZymailClientApi  实现管理平台邮件发送功能

~~~java
@RequestMapping("/mails")
@RestController
public class MailController {
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private LocalMailService localMailService;
    @Autowired
    private RemoteMailService remoteMailService;

    /**
     * 给管理界面的接口
     * @param mail
     * @return
     */
    @PostMapping
    public Result sendEmail(@RequestBody Mail mail) {

        // 是本地源
        if (mail.getSource().equals(configProperties.getAppId())) {
            localMailService.sendMail(mail);
        } else {
            remoteMailService.sendMail(mail);
        }
        return Result.okResult();
    }
}

@Component
public class ZymailClientApi {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private HttpClientUtils httpClientUtils;

    public void sendEmail(Mail mail) throws IOException {

        try {
            Source source = redisCache.getCacheMapValue(RedisKeyConstant.SOURCES_KEY,
                    mail.getSource());
            httpClientUtils.post(source.getUrl() + "/mails", JSONObject.toJSONString(mail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
~~~







### 测试

远程调度源发送

![image-20231003155807407](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714985.png)

本地调度源发送

![image-20231003155812742](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714986.png)

## 定时邮件功能实现 

### 引入 Quartz 依赖

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>

~~~

### 创建QuartzService及其实现类

~~~java
public interface QuartzService {


    /**
     * 添加一个定时任务
     * @param jobName          Job名
     * @param jobGroupName     Job组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         自定义工作类
     * @param cron             设置定时规则
     * @param startTime        开始时间
     * @param endTime          结束时间
     * @param params           参数
     */
    void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                Class jobClass, String cron, Date startTime, Date endTime, Map<String, Mail> params);
}
@Service
@Slf4j
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    private Scheduler scheduler;
    @Override
    public void addJob(String jobName, String jobGroupName, // 工作名 ， 工作组名(调度源)
                       String triggerName, String triggerGroupName, // 触发器名 ， 触发器组名(调度源)
                       Class jobClass, String cron,  // 自定义工作类 ，
                       Date startTime, Date endTime,
                       Map<String, Mail> params) { // 参数(邮件)
            try {
                // 创建作业对象
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
                // 创建触发器
                if (params != null) {
                    // 将参数传递给作业
                    jobDetail.getJobDataMap().putAll(params);
                }
                // 构建触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                if (startTime != null){
                    triggerBuilder.startAt(startTime);
                }else {
                    triggerBuilder.startNow();
                }
                if (endTime != null){
                    triggerBuilder.endAt(endTime);
                }
                // 触发器时间设定   cron 为空就只执行一次
                if (cron != null && !cron.isEmpty()) {
                    // 使用cron表达式创建触发器
                    triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                } else {
                    // 创建一个只执行一次的触发器
                    triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(0).withRepeatCount(0));
                }
                // 创建Trigger对象
                Trigger trigger = triggerBuilder.build();
                // 启动
                scheduler.start();
                scheduler.scheduleJob(jobDetail, trigger);
                log.info("任务设置成功");
            }catch (Exception e){
                e.printStackTrace();
                throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
            }

    }
}
~~~

### 创建具体工作类 LocalSendMailJob 	RemoteSendMailJob 

~~~java
@Component
@Slf4j
public class LocalSendMailJob implements Job {
    @Autowired
    private LocalMailService localMailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("定时任务开始执行");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Mail mail = (Mail) dataMap.get("mail");
        localMailService.sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
        log.info("定时任务执行完毕");
    }
}
@Component
@Slf4j
public class RemoteSendMailJob implements Job {
    @Autowired
    private ZymailClientApi zymailClientApi;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("远程-定时任务开始执行");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Mail mail = (Mail) dataMap.get("mail");
        try {
            zymailClientApi.sendEmail(mail);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("远程-定时任务执行完毕");
    }
}
~~~

### 完善LocalMailServiceImpl 	RemoteMailServiceImpl  代码

~~~java
@Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()) {
            // 未定时直接发送
            sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
        } else {
            // TODO 本地调度源定时邮件
            createSendMailJob(mail);
        }
    }

    // 创建 定时任务
    private void createSendMailJob(Mail mail) {
        Map<String, Mail> params = new HashMap<>();
        params.put("mail", mail);
        String uniKey = UUID.randomUUID().toString();
        // 组设置为调度源appId
        quartzService.addJob("sendMail-" + uniKey,
                mail.getSource(),
                "sendMail-" + uniKey,
                mail.getSource(),
                LocalSendMailJob.class,
                mail.getCron(),
                mail.getStartTime(),
                mail.getEndTime(),
                params);
    }


@Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()){
            try {
                zymailClientApi.sendEmail(mail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            // TODO 远程调度源定时发送
            createSendMailJob(mail);
        }
    }

    // 创建 定时任务
    private void createSendMailJob(Mail mail) {
        Map<String, Mail> params = new HashMap<>();
        params.put("mail", mail);
        String uniKey = UUID.randomUUID().toString();
        log.info("添加远程任务");
        quartzService.addJob("sendMail-" + uniKey,
                mail.getSource(),
                "sendMail-" + uniKey,
                mail.getSource(),
                RemoteSendMailJob.class,
                mail.getCron(),
                mail.getStartTime(),
                mail.getEndTime(),
                params);
    }
~~~

### 日期格式化

~~~java
@Configuration
public class MyFastJsonConfig {

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        converter.setFastJsonConfig(config);
        return converter;
    }
}
~~~

### 测试

#### 定时邮件

启动时间 和 结束时间 都是我们设置的值

![image-20231003155822516](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714987.png)

![image-20231003155831154](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714988.png)

邮件成功发送

![image-20231003155835441](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714989.png)

#### 延时邮件

![image-20231003155840034](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714991.png)

![image-20231003155845013](https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202310031714992.png)

## 用户登录

为前端提高登录接口

~~~java
@Data
public class LoginDto {

    private String username;

    private String password;
}
@RequestMapping
@RestController

public class LoginController {
    @Autowired
    private ConfigProperties configProperties;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDto user){
        if (user.getPassword().equals(configProperties.getAdmin().getPassword()) &&
         user.getUsername().equals(configProperties.getAdmin().getUsername())){
            Map<String, Object> map = new HashMap<>();
            map.put("token", "admin");
            return Result.okResult(map);
        }
        return Result.errorResult(405,"用户名或密码错误");
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.okResult(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.okResult();
    }
}
~~~



