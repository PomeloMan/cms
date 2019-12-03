package io.cms.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import io.cms.core.datasource.annotation.DataSource.DataSources;

/**
 * Dynamic dataSource
 * 
 * @author Pomelor
 */
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
