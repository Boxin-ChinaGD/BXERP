package com.bx.erp.action.interceptor;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.bx.erp.util.DataSourceContextHolder;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private Log logger = LogFactory.getLog(DynamicDataSource.class);

	private Map<Object, Object> _targetDataSources;

	@Value("${driverClassName}")
	private String DRIVER_CLASS_NAME;

	@Value("${db.mysql.url}")
	private String DB_MYSQL_URL;

	@Value("${db.mysql.username}")
	private String DB_MYSQL_USERNAME;

	@Value("${db.mysql.password}")
	private String DB_MYSQL_PASSWORD;

	/** @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 * 
	 * @describe 数据源为空或者为0时，自动切换至默认数据源，即在配置文件中定义的dataSource数据源 */
	@Override
	protected Object determineCurrentLookupKey() {
		String dbName = DataSourceContextHolder.getDbName();

		if (dbName == null) {
			dbName = DataSourceContextHolder.DATASOURCE_jdbcDataSource_nbr_bx; // 默认使用公共DB：nbr_bx
		} else {
			this.selectDataSource(dbName);
			if (dbName.equals(DataSourceContextHolder.DATASOURCE_jdbcDataSource_nbr_bx)) {
				dbName = DataSourceContextHolder.DATASOURCE_jdbcDataSource_nbr_bx;
			}
		}
		logger.debug("--------> using datasource " + dbName);

		return dbName;
	}

	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		this._targetDataSources = targetDataSources;
		super.setTargetDataSources(this._targetDataSources);
		afterPropertiesSet();
	}

	public void addTargetDataSource(String key, BasicDataSource dataSource) {
		this._targetDataSources.put(key, dataSource);
		this.setTargetDataSources(this._targetDataSources);
	}

	/** 该方法为同步方法，防止并发创建两个相同的数据库 使用双检锁的方式，防止并发 */
	private synchronized DataSource createDataSource(String dbName) {
		String url = String.format(DB_MYSQL_URL, dbName);
		//
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(DRIVER_CLASS_NAME);
		dataSource.setUrl(url);
		dataSource.setUsername(DB_MYSQL_USERNAME);
		dataSource.setPassword(DB_MYSQL_PASSWORD);
		dataSource.setTestWhileIdle(true);
		dataSource.setValidationQuery("select 1");
		dataSource.setTestOnBorrow(true);

		return dataSource;
	}

	/** @describe 数据源存在时不做处理，不存在时创建新的数据源链接，并将新数据链接添加至缓存 */
	public void selectDataSource(String dbName) {
		String sid = DataSourceContextHolder.getDbName();
		if (DataSourceContextHolder.DATASOURCE_jdbcDataSource_nbr_bx.equals(dbName)) {
			DataSourceContextHolder.setDbName(DataSourceContextHolder.DATASOURCE_jdbcDataSource_nbr_bx);
			return;
		}

		Object obj = this._targetDataSources.get(dbName);
		if (obj != null && sid.equals(dbName)) {
			return;
		} else {
			BasicDataSource dataSource = this.getDataSource(dbName);
			if (null != dataSource)
				this.setDataSource(dbName, dataSource);
		}
	}

	/** 查询serverId对应的数据源记录 */
	public BasicDataSource getDataSource(String serverId) {
		return (BasicDataSource) createDataSource(serverId);
	}

	public void setDataSource(String serverId, BasicDataSource dataSource) {
		this.addTargetDataSource(serverId, dataSource);
		DataSourceContextHolder.setDbName(serverId);
	}

}
