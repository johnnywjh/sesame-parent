/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kim.sesame.framework.locks.jdbc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * The default implementation of the {@link LockRepository} based on the
 * table from the script presented in the {@code org/springframework/integration/jdbc/schema-*.sql}.
 * <p>
 * This repository can't be shared between different {@link JdbcLockRegistry} instances.
 * Otherwise it opens a possibility to break {@link java.util.concurrent.locks.Lock} contract,
 * where {@link JdbcLockRegistry} uses non-shared {@link java.util.concurrent.locks.ReentrantLock}s
 * for local synchronizations.
 *
 * @author Dave Syer
 * @author Artem Bilan
 * @since 4.3
 */
@Transactional
public class DefaultLockRepository implements LockRepository, InitializingBean {

	/**
	 * Default value for the table prefix property.
	 */
	public static final String DEFAULT_TABLE_NAME = "T_LOCK";

	/**
	 * Default value for the time-to-live property.
	 */
	public static final int DEFAULT_TTL = 10000;

	private final String id = UUID.randomUUID().toString();

	private final JdbcTemplate template;

	private int ttl = DEFAULT_TTL;

	private String tableName = DEFAULT_TABLE_NAME;

	private String region = "DEFAULT";

	private String deleteQuery = "DELETE FROM %S WHERE REGION=? AND LOCK_KEY=? AND CLIENT_ID=?";

	private String deleteExpiredQuery = "DELETE FROM %S WHERE REGION=? AND LOCK_KEY=? AND CREATED_DATE<?";

	private String deleteAllQuery = "DELETE FROM %S WHERE REGION=? AND CLIENT_ID=?";

	private String updateQuery = "UPDATE %S SET CREATED_DATE=? WHERE REGION=? AND LOCK_KEY=? AND CLIENT_ID=?";

	private String insertQuery = "INSERT INTO %S (REGION, LOCK_KEY, CLIENT_ID, CREATED_DATE) VALUES (?, ?, ?, ?)";

	private String countQuery = "SELECT COUNT(REGION) FROM %S WHERE REGION=? AND LOCK_KEY=? AND CLIENT_ID=? AND CREATED_DATE>=?";


	public DefaultLockRepository(JdbcTemplate jdbcTemplate) {
		this.template = jdbcTemplate;
	}

	/**
	 * A unique grouping identifier for all locks persisted with this store. Using
	 * multiple regions allows the store to be partitioned (if necessary) for different
	 * purposes. Defaults to <code>DEFAULT</code>.
	 * @param region the region name to set
	 */
	public void setRegion(String region) {
		Assert.hasText(region, "Region must not be null or empty.");
		this.region = region;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Specify the time (in milliseconds) to expire dead locks.
	 * @param timeToLive the time to expire dead locks.
	 */
	public void setTimeToLive(int timeToLive) {
		this.ttl = timeToLive;
	}

	@Override
	public void afterPropertiesSet() throws SQLException {
		this.deleteQuery = String.format(this.deleteQuery, this.tableName);
		this.deleteExpiredQuery = String.format(this.deleteExpiredQuery, this.tableName);
		this.deleteAllQuery = String.format(this.deleteAllQuery, this.tableName);
		this.updateQuery = String.format(this.updateQuery, this.tableName);
		this.insertQuery = String.format(this.insertQuery, this.tableName);
		this.countQuery = String.format(this.countQuery, this.tableName);
		createLockTable();
	}

	private void createLockTable() throws SQLException{
		DatabaseMetaData dbMetaData =  this.template.getDataSource().getConnection().getMetaData();
		try (ResultSet resultSet = dbMetaData.getTables(null, null, this.tableName, new String[]{"TABLE"})) {
			if (!resultSet.next()) {
				String createTableSql = "CREATE TABLE " + this.tableName + "("
						+ "LOCK_KEY VARCHAR(200),"
						+ "REGION VARCHAR(100), "
						+ "CLIENT_ID CHAR(36) NOT NULL, "
						+ "CREATED_DATE DATETIME(6) NOT NULL, "
						+ "constraint LOCK_PK primary key (LOCK_KEY, REGION));";
				this.template.execute(createTableSql);
			}
		}
	}

	@Override
	public void close() {
		this.template.update(this.deleteAllQuery, this.region, this.id);
	}

	@Override
	public void delete(String lock) {
		this.template.update(this.deleteQuery, this.region, lock, this.id);
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, timeout = 1)
	@Override
	public boolean acquire(String lock) {
		deleteExpired(lock);
		if (this.template.update(this.updateQuery, new Date(), this.region, lock, this.id) > 0) {
			return true;
		}
		try {
			return this.template.update(this.insertQuery, this.region, lock, this.id, new Date()) > 0;
		}
		catch (DuplicateKeyException e) {
			return false;
		}
	}

	@Override
	public boolean isAcquired(String lock) {
		deleteExpired(lock);
		return this.template.queryForObject(this.countQuery, Integer.class, this.region, lock, this.id,
				new Date(System.currentTimeMillis() - this.ttl)) == 1;
	}

	private int deleteExpired(String lock) {
		return this.template.update(this.deleteExpiredQuery, this.region, lock,
				new Date(System.currentTimeMillis() - this.ttl));
	}

}
