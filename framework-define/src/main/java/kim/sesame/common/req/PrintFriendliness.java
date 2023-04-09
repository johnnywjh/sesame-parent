package kim.sesame.common.req;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;

import com.alibaba.fastjson2.JSON;

import java.io.Serializable;

/**
 * 自身内容能以可读方式输出
 */
public class PrintFriendliness implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":"
                + JSON.toJSONString(this);
    }

}