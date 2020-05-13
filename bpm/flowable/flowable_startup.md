#启动

## Load Configuration File

```
@ConfigurationProperties(prefix = "flowable")
public class FlowableProperties {
  ...
  private String processDefinitionLocationPrefix = "classpath*:/processes/";
  ...
  private List<String> processDefinitionLocationSuffixes = Arrays.asList("**.bpmn20.xml", "**.bpmn");
  ...
}

@Configuration
@ConditionalOnProcessEngine
@EnableConfigurationProperties({
    FlowableProperties.class,
    FlowableMailProperties.class,
    FlowableProcessProperties.class,
    FlowableIdmProperties.class
})
@AutoConfigureAfter({
    FlowableTransactionAutoConfiguration.class
})
@Import({
    FlowableJobConfiguration.class
})
public class ProcessEngineAutoConfiguration extends AbstractSpringEngineAutoConfiguration {

    @Autowired(required = false)
    private List<EngineConfigurationConfigurer<SpringProcessEngineConfiguration>> processEngineConfigurationConfigurers = new ArrayList<>();
    protected final FlowableProcessProperties processProperties;
    protected final FlowableIdmProperties idmProperties;
    protected final FlowableMailProperties mailProperties;

    public ProcessEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableProcessProperties processProperties,
        FlowableIdmProperties idmProperties, FlowableMailProperties mailProperties) {
        super(flowableProperties);
        this.processProperties = processProperties;
        this.idmProperties = idmProperties;
        this.mailProperties = mailProperties;
    }
    ...

    // Load bpm2.0 xml file and build ProcessEngineConfiguration 
    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager,
        @Process ObjectProvider<AsyncExecutor> asyncExecutorProvider) throws IOException {

        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();

        List<Resource> resources = this.discoverDeploymentResources(
            flowableProperties.getProcessDefinitionLocationPrefix(),
            flowableProperties.getProcessDefinitionLocationSuffixes(),
            flowableProperties.isCheckProcessDefinitions()
        );
        ...
    }

    ...
}
```
![image](https://flowable.com/open-source/docs/assets/cmmn/cmmn.api-call-flow.png)


## inteceptors

0 = {LogInterceptor@7482} 
1 = {SpringTransactionInterceptor@7483} 
2 = {CommandContextInterceptor@7484} 
3 = {TransactionContextInterceptor@7485} 
4 = {BpmnOverrideContextInterceptor@7486} 
5 = {CommandInvoker@7487} 


## 传递参数
通过variable来给process instance传递参数，所以它在整个process instance生命周期中是全局共享的的，
随时可以通过process instance增删改查。
```
        ProcessInstance processInstance = createProcess(process)
                .variable(REQUESTOR, process.getEmployName())
                .variable(REQUEST_DAY, process.getRequestDays())
                .variable(REASON, process.getReason())
                .start();
```

再传递到cmd中再执行。
```
public StartProcessInstanceCmd(String processDefinitionKey, String processDefinitionId, String businessKey, Map<String, Object> variables) {
        this.processDefinitionKey = processDefinitionKey;
        this.processDefinitionId = processDefinitionId;
        this.businessKey = businessKey;
        this.variables = variables;
    }
```


## 参考资料
- [flowable表简要说明](https://www.cnblogs.com/xiohao/p/11446622.html)