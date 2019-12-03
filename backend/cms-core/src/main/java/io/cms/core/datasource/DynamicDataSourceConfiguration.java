package io.cms.core.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

import io.cms.core.datasource.annotation.DataSource.DataSources;

/**
 * DynamicDataSourceConfiguration.class
 */
@Configuration
public class DynamicDataSourceConfiguration {

	@Bean
	@Qualifier("primaryDataSource")
	@ConfigurationProperties("spring.datasource.druid.primary")
	public DataSource primaryDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean
	@Qualifier("secondaryDataSource")
	@ConfigurationProperties("spring.datasource.druid.secondary")
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
