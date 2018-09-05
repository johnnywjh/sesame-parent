package kim.sesame.framework.tablelog.db.dao;

import kim.sesame.framework.tablelog.db.bean.TableOpLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * TableOpLogDao
 * @author johnny
 * @date 2018-09-05 14:50:29
 * @Description: 表操作日志
 */
 @Mapper
public interface TableOpLogDao {

	/** 新增  */
	int insert(TableOpLog bean);
	
	/** 删除  */
	int delete(String id);
	
	/** 修改  */
	int update(TableOpLog bean);
	
	/** 查询所有 */
	List<TableOpLog> searchList(TableOpLog bean);
	
	/** 查询单个,返回bean */
	TableOpLog search(TableOpLog bean);
	

}
