package kim.sesame.framework.util.accessory.service;

import kim.sesame.framework.entity.GMap;
import kim.sesame.framework.util.accessory.bean.Accessory;
import kim.sesame.framework.util.accessory.dao.AccessoryDao;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.context.UserContext;
import kim.sesame.framework.web.entity.IUser;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AccessoryService
 *
 * @author wangjianghai
 * @date 2018-07-23 16:54:27
 * @Description: 附件表
 */
@CommonsLog
@Service
public class AccessoryService {

    @SuppressWarnings("all")
    @Resource
    private AccessoryDao accessoryDao;

    /**
     * 查询list
     *
     * @author wangjianghai
     * @date 2018-07-23 16:54:27
     * @Description: 分页时要注意
     */
    public List<Accessory> searchList(Accessory bean) {

        List<Accessory> list = accessoryDao.searchList(bean);

        return list;
    }

    /**
     * 新增
     *
     * @author wangjianghai
     * @date 2018-07-23 16:54:27
     * @Description:
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(Accessory bean) {

        int res = accessoryDao.insert(bean);

        return res;
    }

    public Accessory add(String name, String url) {
        IUser user = UserContext.getUserContext().getUser();
        Accessory bean = new Accessory();
        bean.initCreateAndId(user.getAccount());
        bean.setName(name);
        bean.setUrl(url);

        int res = accessoryDao.insert(bean);
        if (res <= 0) {
            bean = null;
        }
        return bean;
    }

    /**
     * 修改
     *
     * @author wangjianghai
     * @date 2018-07-23 16:54:27
     * @Description:
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Accessory bean) {

        int res = accessoryDao.update(bean);

        return res;
    }

    /**
     * 删除
     *
     * @author wangjianghai
     * @date 2018-07-23 16:54:27
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
        accessoryDao.delete(id);

        //逻辑删除
//    Accessory bean = new Accessory();
//    	bean.setId(id);
//    	bean.setActive(GData.BOOLEAN.NO);
//    	bean.setUpdateTime(new Date());
//    	update(bean);
    }

    /**
     * 查询--返回bean
     *
     * @author wangjianghai
     * @date 2018-07-23 16:54:27
     * @Description:
     */
    public Accessory search(Accessory bean) {
        return accessoryDao.search(bean);
    }

    public void processTheAttachment(String type, String exId, List<String> ids) {
        if (StringUtil.isNotEmpty(type) && StringUtil.isNotEmpty(exId)) {
            ids.stream().forEach(id -> {
                Accessory bean = new Accessory(type, exId);
                bean.setId(id);
                update(bean);
            });
        }
    }

    /**
     * 根据id 的集合查询附件列表
     *
     * @param ids
     * @return
     */
    public List<Accessory> queryListByids(String ids) {
        List<Accessory> list = new ArrayList<>();
        if (StringUtil.isEmpty(ids)) {
            return list;
        }

        // 处理ids
        StringBuilder sb = new StringBuilder();
        List<String> list_id = Stream.of(ids.split(",")).map(String::trim).distinct().filter(StringUtil::isNotEmpty).collect(Collectors.toList());
        for (int i = 0; i < list_id.size(); i++) {
            if (i == 0) {
                sb.append("'").append(list_id.get(i)).append("'");
            } else {
                sb.append(",").append("'").append(list_id.get(i)).append("'");
            }
        }
        GMap params = GMap.newMap();
        params.putAction("ids",sb.toString());
        list = accessoryDao.queryListByids(params);
        return list;
    }

}
