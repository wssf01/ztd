package com.bike.ztd.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.flywaydb.core.Flyway;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * mybatis-plus配置
 */
@Configuration
@MapperScan("com.bike.ztd.mapper") //扫描mapper的包，或者读者可以在对应的mapper上加上@Mapper的注解
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLocalPage(true);//开启分页支持
        return paginationInterceptor;
    }

    /**
     * mybatis-plus SQL执行效率插件,生产环境可以关闭
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    @Bean
    public DataSource platDataSource() throws SQLException {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "flyway", initMethod = "migrate")
    public Flyway flyway(@Qualifier("platDataSource") DataSource platDataSource) {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);

        flyway.setOutOfOrder(true);
        flyway.setLocations("classpath:db");
        flyway.setDataSource(platDataSource);

        // 不能修改该参数
        flyway.setValidateOnMigrate(true);

        return flyway;
    }

    @Bean(name = "sessionFactoryPlat")
    public SqlSessionFactory sessionFactoryPlat(@Qualifier("platDataSource") DataSource platDataSource)
            throws Exception {
        // TODO: 2018/5/19 flywaydb加载顺序先于mybatis执行，防止mybatis的服务类初始化方法执行失败
        flyway(platDataSource);

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(platDataSource);
//        sqlSessionFactory.setGlobalConfig(globalConfiguration());
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/*Mapper.xml"));


        // mybatisPlus configuration
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setCacheEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactory.setConfiguration(configuration);


        // mybatisPlus globalConfig
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration.setIdType(IdType.INPUT.getKey());
        sqlSessionFactory.setGlobalConfig(globalConfiguration);


        sqlSessionFactory.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
                paginationInterceptor() //添加分页功能
        });
        //sqlSessionFactory.setGlobalConfig(globalConfiguration());
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "transactionManagerPlat")
    public DataSourceTransactionManager dataSourceTransactionManager2() throws SQLException {
        return new DataSourceTransactionManager(platDataSource());
    }


}
