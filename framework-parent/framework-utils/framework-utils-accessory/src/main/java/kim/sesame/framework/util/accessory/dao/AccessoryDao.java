package kim.sesame.framework.util.accessory.dao;

import kim.sesame.framework.entity.GMap;
import kim.sesame.framework.util.accessory.bean.Accessory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AccessoryDao
 * @author wangjianghai
 * @date 2018-07-23 16:54:27
 * @Description: 附件表
 */
 @Mapper
public interface AccessoryDao {

	void checkTableAndCreate();

	/** 新增  */
	int insert(Accessory bean);
	
	/** 删除  */
	int delete(String id);
	
	/** 修改  */
	int update(Accessory bean);
	
	/** 查询所有 */
	List<Accessory> searchList(Accessory bean);
	
	/** 查询单个,返回bean */
	Accessory search(Accessory bean);


    List<Accessory> queryListByids(GMap params);
}
