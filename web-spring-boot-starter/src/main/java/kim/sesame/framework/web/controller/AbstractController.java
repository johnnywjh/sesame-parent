package kim.sesame.framework.web.controller;

import kim.sesame.framework.define.entity.ErrorCodeEnum;
import kim.sesame.framework.entity.GPage;
import kim.sesame.framework.entity.IErrorCode;
import kim.sesame.framework.exception.IException;
import kim.sesame.framework.web.config.ProjectConfig;
import kim.sesame.framework.response.Response;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controlle 基础抽象类
 **/
@CommonsLog
public abstract class AbstractController {

    //单例 普通的成功用公用的对象
    private static class ContextSuccess {
        private final static Response SUCCESS = Response.create().setSuccess(true);
    }

    public Response returnSuccess() {
//        return Response.create().setSuccess(true);
        return ContextSuccess.SUCCESS;
    }

    public Response returnSuccess(Object result) {
        return Response.create().setSuccess(true).setResult(result);
    }

    public Response returnSuccess(Object result, String msg) {
        return Response.create().setSuccess(true).setResult(result).setMessage(msg);
    }

    public Response returnSuccess(List list) {
        return Response.create().setSuccess(true).setResult(list);
    }

    public Response returnSuccess(List list, String msg) {
        return Response.create().setSuccess(true).setResult(list).setMessage(msg);
    }

    public Response returnSuccess(List list, GPage gPage) {
        return Response.create().setSuccess(true).setResult(list).setPage(gPage);
    }


//    @ExceptionHandler(UserNotLoginException.class)
//    public String handleUserNotLoginException() {
//        //用户未登录重定向到根目录
//        return "redirect:/index/login";
//    }

    /**
     * 异常处理
     *
     * @param exception exception
     * @return Response
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception exception) {

        log.error(exception.getMessage(), exception);

        if (exception instanceof IException) {
            IErrorCode ece = ((IException) exception).getErrorCodeEnum();
            return Response.create().setExceptionType(ece.name()).setCode(ece.getCode()).setMessage(exception.getMessage());
        }
        //系统异常
        else {
            IErrorCode ece = ErrorCodeEnum.SYSTEMERR;
            return Response.create().setExceptionType(ece.name()).setCode(ece.getCode()).setMessage(getExceptionMessage(exception));
        }
    }

    /**
     * 判断是否是 debug 模式,如果是就返回异常的详细信息
     */
    public String getExceptionMessage(Exception e) {
        String message = ProjectConfig.getSystemExceptionMessage();
        if (ProjectConfig.isDebug()) {
            message = e.getMessage();
        }
        return message;
    }
}
