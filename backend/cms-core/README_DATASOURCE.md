# SpringBoot Dynamic DATASOURCE
添加动态数据源

## 配置
### application-dev.properties
```
## MYSQL
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# JPA (JpaBaseConfiguration, HibernateJpaAutoConfiguration)
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming.strategy = org.hibernate.cfg.ImprovedNamingStrategy
## Dynamic DataSource
# druid link http://${server}:${port}/druid/sql.html
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.druid.primary.url=jdbc:mysql://127.0.0.1:3306/cms?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.druid.primary.username=root
spring.datasource.druid.primary.password=root
spring.datasource.druid.secondary.url=jdbc:mysql://127.0.0.1:3306/cms_bak?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.druid.secondary.username=root
spring.datasource.druid.secondary.password=root
```

### Application
```
......
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class Application {
	......
}
```

### DynamicDataSourceConfiguration
动态数据源配置
```
@Configuration
public class DynamicDataSourceConfiguration {

  // 配置主数据源
  @Bean
  @Qualifier("primaryDataSource")
  @ConfigurationProperties("spring.datasource.druid.primary")
  public DataSource primaryDataSource() {
    return DruidDataSourceBuilder.create().build();
  }

  // 配置副数据源
  @Bean
  @Qualifier("secondaryDataSource")
  @ConfigurationPropertie("spring.datasource.druid.secondary")
  public DataSource secondaryDataSource() {
    return DruidDataSourceBuilder.create().build();
  }

  @Bean
  public Map<Object, Object> dataSourcesMapper() {
    Map<Object, Object> dataSourcesMapper = new HashMap<Object, Object>();
    dataSourcesMapper.put(DataSources.PRIMARY, primaryDataSource());
    dataSourcesMapper.put(DataSources.SECONDARY, secondaryDataSource());
    return dataSourcesMapper;
  }

  @Bean
  @Primary
  public DynamicDataSource dynamicDataSource() {
    DynamicDataSource dynamicDataSource = new DynamicDataSource();
    dynamicDataSource.setTargetDataSources(dataSourcesMapper());
    dynamicDataSource.setDefaultTargetDataSource(primaryDataSource());
    return dynamicDataSource;
  }
}
```

### DynamicDataSource
```
public class DynamicDataSource extends AbstractRoutingDataSource {

  private static final ThreadLocal<DataSources> context = new ThreadLocal<>();

  @Override
  protected Object determineCurrentLookupKey() {
    return getDataSource();
  }

  public static void setDataSource(DataSources dataSource) {
    context.set(dataSource);
  }

  public static DataSources getDataSource() {
    return context.get();
  }

  public static void clearDataSource() {
    context.remove();
  }
}
```

### DataSource
数据源
```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

  public enum DataSources {
    PRIMARY, SECONDARY
  };

  // DataSources value() default DataSources.WRITE;
  DataSources value();
}
```

### DataSourceAspect
数据源切面
```
@Aspect
@Component
@Order(Integer.MIN_VALUE)
public class DataSourceAspect {

  private final static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

  @Pointcut("@annotation(io.cms.core.datasource.annotation.DataSource)")
  public void dataSourcePointCut() {
  }

  @Around("dataSourcePointCut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    MethodSignature signature = (MethodSignature) point.getSignature();
    Method method = signature.getMethod();

    DataSource datasource = method.getAnnotation(DataSource.class);
    if (datasource == null) {
      DynamicDataSource.setDataSource(DataSources.PRIMARY);
      logger.info("Set datasource: " + DataSources.PRIMARY);
    } else {
      DynamicDataSource.setDataSource(datasource.value());
      logger.info("Set datasource: " + datasource.value());
    }

    try {
      return point.proceed();
    } finally {
      DynamicDataSource.clearDataSource();
      logger.debug("clean datasource");
    }
  }
}
```

## 使用
### UserController
```
@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "UserController")
public class UserController {
  ......
  @PostMapping("query")
  @DataSource(DataSources.SECONDARY)
  public ResponseEntity<Collection<User>> query(@RequestBody @ApiParam(name = "object", value = "传入json格式", required = true) IUser view) {
    ......
  }
}
```