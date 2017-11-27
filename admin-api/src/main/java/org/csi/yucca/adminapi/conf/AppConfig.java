package org.csi.yucca.adminapi.conf;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
// @EnableSwagger2
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan(basePackages = "org.csi.yucca.adminapi")
// @ComponentScan(basePackages={"org.csi.yucca.adminapi"},
// excludeFilters={
// @ComponentScan.Filter(type= FilterType.ANNOTATION, value=EnableWebMvc.class)
// })
@MapperScan("org.csi.yucca.adminapi.mapper")
@PropertySource(value = { "classpath:datasource.properties" })
public class AppConfig extends WebMvcConfigurerAdapter {

	@Value("${driver.class.name}")
	private String driverClassName;

	@Value("${url}")
	private String url;

	@Value("${datasource.username}")
	private String username;

	@Value("${password}")
	private String password;

	@Value("${max.idle}")
	private int maxIdle;

	@Value("${max.active}")
	private int maxActive;

	@Value("${store.url}")
	private String storeUrl;
	

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// registry.addResourceHandler("swagger-ui.html")
	// .addResourceLocations("classpath:/META-INF/resources/");
	//
	// registry.addResourceHandler("/webjars/**")
	// .addResourceLocations("classpath:/META-INF/resources/webjars/");
	// }
	//
	// @Bean
	// public Docket api() {
	// return new
	// Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
	// .paths(PathSelectors.any()).build();
	// }

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(this.driverClassName);
		dataSource.setUrl(this.url);

		dataSource.setMaxIdle(this.maxIdle);
		dataSource.setMaxActive(this.maxActive);

		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(getDataSource());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(getDataSource());
		return sessionFactory.getObject();
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public StoreConfig getStoreConfig() {
		return new StoreConfig(this.storeUrl);
	}

}
