package kim.sesame.framework.tablelog.db.service;

import kim.sesame.framework.tablelog.db.bean.TableOpLog;
import kim.sesame.framework.tablelog.db.dao.TableOpLogDao;
import kim.sesame.framework.utils.StringUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TableOpLogService
 * @author johnny
 * @date 2018-09-05 14:50:29
 * @Description: 表操作日志
 */
@CommonsLog
@Service
public class TableOpLogService {

    @SuppressWarnings("all")
	@Resource
	private TableOpLogDao tableOpLogDao;

	/**
	 * 查询list
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description: 分页时要注意
	 */
	public List<TableOpLog> searchList(TableOpLog bean) {
		
		List<TableOpLog> list = tableOpLogDao.searchList(bean);

		return list;
	}
	
	/**
	 * 新增
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description:	
	 */
	@Transactional(rollbackFor = Exception.class)
	public int add(TableOpLog bean)  {

		int res = tableOpLogDao.insert(bean);
		
		return res;
	}
	
	/**
	 * 修改
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description:	
	 */
	@Transactional(rollbackFor = Exception.class)
	public int update(TableOpLog bean)  {

		int res = tableOpLogDao.update(bean);
		
		return res;
	}
	
	/**
	 * 删除
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description:	
	 */
	@Transactional(rollbackFor = Exception.class)
    public int delete(String ids) {

        List<String> list_id = Stream.of(ids.split(",")).map(String::trim).distinct().filter(StringUtil::isNotEmpty).collect(Collectors.toList());
        list_id.stream().forEach(this::deleteById);

        return list_id.size();
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
         //物理删除
        tableOpLogDao.delete(id);

        //逻辑删除
//    TableOpLog bean = new TableOpLog();
//    	bean.setId(id);
//    	bean.setActive(GData.BOOLEAN.NO);
//    	bean.setUpdateTime(new Date());
//    	update(bean);
    }
	
	/**
	 * 查询--返回bean
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description:	
	 */
	public TableOpLog search(TableOpLog bean)  {

		return tableOpLogDao.search(bean);
	}
	
}
