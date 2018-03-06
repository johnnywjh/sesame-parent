package kim.sesame.framework.mybatis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * BaseDao
 *
 * @author johnny
 * date :  2017-11-29 13:05
 * Description: 基础Dao
 */
@Component
public class BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 判断表是否存在
     *
     * @param tableName 表名
     * @return
     */
    public boolean checkTableExists(String tableName) {
        String sql = "SHOW TABLES LIKE ?";
        String res = jdbcTemplate.queryForObject(sql, new Object[]{tableName}, String.class);
        return res.equals(tableName);

    }

}