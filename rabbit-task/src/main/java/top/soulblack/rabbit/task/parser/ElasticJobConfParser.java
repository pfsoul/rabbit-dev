package top.soulblack.rabbit.task.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import top.soulblack.rabbit.task.annotation.ElasticJobConfig;
import top.soulblack.rabbit.task.autoconfigure.JobZookeeperProperties;
import top.soulblack.rabbit.task.enums.ElasticJobTypeEnum;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lxf on 2020/10/13
 */
// 在所有的bean加载后，在进行对job进行解析 实现该接口ApplicationListener<ApplicationReadyEvent>
@Slf4j
public class ElasticJobConfParser implements ApplicationListener<ApplicationReadyEvent> {

    private JobZookeeperProperties jobZookeeperProperties;

    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ElasticJobConfParser(JobZookeeperProperties jobZookeeperProperties, ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.jobZookeeperProperties = jobZookeeperProperties;
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("开始配置job");
        try {
            ApplicationContext context = applicationReadyEvent.getApplicationContext();
            // 获取全部带该注解的Bean
            Map<String, Object> beanMap = context.getBeansWithAnnotation(ElasticJobConfig.class);

            for (Iterator<?> it = beanMap.values().iterator(); it.hasNext(); ) {
                Object confBean = it.next();
                Class<?> clazz = confBean.getClass();
                // 假如该类为匿名内部类或代理类
                if (clazz.getName().indexOf("$") > 0) {
                    String className = clazz.getName();
                    clazz = Class.forName(className.substring(0, className.indexOf("$")));
                }

                // 获取接口类型，用于判断时什么类型的任务
                String jobTypeName = null;
                for (Class<?> inter : clazz.getInterfaces()) {
                    if (ElasticJobTypeEnum.isElasticJob(inter.getSimpleName())) {
                        jobTypeName = inter.getSimpleName();
                    }
                }
                // 假如name 为空跳过该类
                if (StringUtils.isBlank(jobTypeName)) {
                    continue;
                }
                // 获取配置项 ElasticJobConfig
                ElasticJobConfig conf = clazz.getAnnotation(ElasticJobConfig.class);

                // 获取全部的配置项
                String jobClass = clazz.getName();
                String jobName = this.jobZookeeperProperties.getNamespace() + "." + conf.name();
                String cron = conf.cron();
                String description = conf.description();
                String jobParameter = conf.jobParameter();
                String scriptCommandLine = conf.scriptCommandLine();
                String shardingItemParameters = conf.shardingItemParameters();
                boolean failover = conf.failover();
                boolean misfire = conf.misfire();
                boolean monitorExecution = conf.monitorExecution();
                boolean overwrite = conf.overwrite();
                boolean streamingProcess = conf.streamingProcess();
                int shardingTotalCount = conf.shardingTotalCount();

                // 现将当当网esjob的相关configuration 实例化
                JobConfiguration config = JobConfiguration.newBuilder(jobName, shardingTotalCount)
                        .cron(cron)
                        .shardingItemParameters(shardingItemParameters)
                        .description(description)
                        .jobParameter(jobParameter)
                        .failover(failover)
                        .misfire(misfire)
                        .monitorExecution(monitorExecution)
                        .overwrite(overwrite)
                        .build();

                // 我到底要创建什么样的任务 当前任务即使clazz
                // 当前构造函数没有使用Listener 不含轨迹追踪
                List<?> listeners = getTargetElasticJobListener(conf);
//                new ScheduleJobBootstrap(zookeeperRegistryCenter, jobClass, config).schedule();

                // 创建一个Spring的beanDefinition
                BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ScheduleJobBootstrap.class);
//                factory.setInitMethodName("init");
                // 多个共用，多例模式
                factory.setScope("prototype");
                factory.addConstructorArgValue(zookeeperRegistryCenter);
                factory.addConstructorArgValue(context.getBean(conf.nickName()));
                factory.addConstructorArgValue(config);
                factory.addConstructorArgValue(listeners);

                // 把factory对象注入到Spring容器中
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
                String registerBeanName = conf.name() + "SpringJobScheduler";
                defaultListableBeanFactory.registerBeanDefinition(registerBeanName, factory.getBeanDefinition());

                ScheduleJobBootstrap scheduleJobBootstrap = (ScheduleJobBootstrap) context.getBean(registerBeanName);
//                ScheduleJobBootstrap scheduleJobBootstrap = new ScheduleJobBootstrap(zookeeperRegistryCenter, jobTypeName, config);
                scheduleJobBootstrap.schedule();

                log.info("启动elastic-job作业： {}", jobName);
            }
            log.info("共计启动作业数目： {}个", beanMap.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("elastic job启动异常，系统强制退出");
            System.exit(1);
        }


    }

    private List<BeanDefinition> getTargetElasticJobListener(ElasticJobConfig config) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = config.listener();
        if (StringUtils.isNotBlank(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope("prototype");
            result.add(factory.getBeanDefinition());
        }
        return result;
    }
}
