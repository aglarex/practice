package com.hello.project.configurer;

import com.github.pagehelper.PageHelper;
import com.hello.project.core.ProjectConstant;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration //相当于spring配置文件中的beans标签
public class MybatisConfigurer {
	
	@Bean//相当于Bean标签
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
		 SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		 factory.setDataSource(dataSource);
		 factory.setTypeAliasesPackage(ProjectConstant.MODEL_PACKAGE);
		 
		 //配置分页插件，详情请查阅官方文档
		 PageHelper pageHelper = new PageHelper();
		 Properties properties = new Properties();
		 //分页尺寸为0时查询所有纪录不再执行分页
		 properties.setProperty("pageSizeZero", "true");
		 //支持通过 Mapper 接口参数来传递分页参数
		 properties.setProperty("supportMethodsArguments", "true");
		 pageHelper.setProperties(properties);
		//添加插件
		 factory.setPlugins(new Interceptor[]{pageHelper});
		 
		 ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		 factory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
		 return factory.getObject();
	}
	
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		 MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		 mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
		 mapperScannerConfigurer.setBasePackage(ProjectConstant.MAPPER_PACKAGE);
		 
		 Properties properties = new Properties();
		 properties.setProperty("mappers", ProjectConstant.MAPPER_INTERFACE_REFERENCE);
		 properties.setProperty("notEmpty", "false");
	     properties.setProperty("IDENTITY", "MYSQL");
	     mapperScannerConfigurer.setProperties(properties);
		 
		 return mapperScannerConfigurer;
	}
}
