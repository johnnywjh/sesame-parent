package kim.sesame.common.web;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Getter
@Setter
public class BasePageRequest  extends BaseRequest {

    /**
     * 页面索引
     */
    @Min(1)
//    @QueryParam("pageIndex")
    private int pageIndex;

    /**
     * 每页展示数量
     */
    @Range(min = 1, max = 100)
//    @QueryParam("pageSize")
    private int pageSize;


}
