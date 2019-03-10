package kim.sesame.framework.tablelog.db.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import kim.sesame.framework.db.utils.PageUtil;
import kim.sesame.framework.entity.GPage;
import kim.sesame.framework.tablelog.db.bean.TableOpLog;
import kim.sesame.framework.tablelog.db.service.TableOpLogService;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.controller.AbstractWebController;
import kim.sesame.framework.web.response.Response;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TableOpLogController
 * @author johnny
 * @date 2018-09-05 14:50:29
 * @Description: 表操作日志
 */
@CommonsLog
@RestController
@RequestMapping("/tableoplog")
public class TableOpLogController extends AbstractWebController {

	@Resource
	private TableOpLogService tableOpLogService;

	/**
	 * 查询list
	  * @author johnny
      * @date 2018-09-05 14:50:29
      * @Description:
	 */
	@RequestMapping("/list")
	public Response list(TableOpLog bean) {

		List<TableOpLog> list = tableOpLogService.searchList(bean);

		return returnSuccess(list);
	}
	
	/**
	 * 分页查询
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @ pageNum : 页码,默认1
     * @ pageSize :  页面大小
	 */
	@RequestMapping("/listPage")
	public Response listPage(TableOpLog bean, GPage gPage) {

		Page<TableOpLog> pages = PageHelper.startPage(gPage.getPageNum(), gPage.getPageSize())
                        .doSelectPage(() -> tableOpLogService.searchList(bean));
        List<TableOpLog> list = pages.getResult();
		
		return returnSuccess(list, PageUtil.recount(gPage, pages));
	}
	
	/**
	 * 保存
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description:	
	 */
	@RequestMapping(value = "/save",method = {RequestMethod.POST})
	public Response save(TableOpLog bean) {
		//IUser user = UserContext.getUserContext().getUser();
		int res = 0;
        if (StringUtil.isNotEmpty(bean.getId())) {
            // 主键不为空, 修改数据
            bean.initUpdate("");
            res = tableOpLogService.update(bean);
        } else {
            //主键为空, 新增数据
            bean.initCreateAndId("");
            res = tableOpLogService.add(bean);
        }
		return returnSuccess(res);
	}
		
	/**
	 * 删除
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description:	
	 */
	@RequestMapping("/delete")
	public Response delete(String ids,HttpServletRequest request) {
		int res = tableOpLogService.delete(ids);
		return returnSuccess(res);
	}
		
	/**
	 * 查询详情
	 * @author johnny
     * @date 2018-09-05 14:50:29
     * @Description:	
	 */
	@RequestMapping("/searchDetail")
	public Response searchDetail(TableOpLog bean,HttpServletRequest request) {
		bean = tableOpLogService.search(bean);
		return returnSuccess(bean);
	}
		
}
