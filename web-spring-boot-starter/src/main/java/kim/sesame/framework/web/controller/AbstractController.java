package kim.sesame.framework.web.controller;

import kim.sesame.common.entity.IErrorCode;
import kim.sesame.common.exception.IException;
import kim.sesame.common.response.ErrorCodeEnum;
import kim.sesame.common.response.Response;
import kim.sesame.common.response.ResponseBuild;
import kim.sesame.framework.web.config.ProjectConfig;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controlle 基础抽象类
 **/
@CommonsLog
public abstract class AbstractController {


    public Response returnSuccess() {
        return ResponseBuild.ContextSuccess.SUCCESS;
    }

    public Response returnSuccess(Object result) {
        return Response.create().setSuccess(true).setResult(result);
    }

    public Response returnSuccess(Object result, String msg) {
        return Response.create().setSuccess(true).setResult(result).setMsg(msg);
    }

    public Response returnSuccess(List list) {
        return Response.create().setSuccess(true).setResult(list);
    }

    public Response returnSuccess(List list, String msg) {
        return Response.create().setSuccess(true).setResult(list).setMsg(msg);
    }

//    public Response returnSuccess(List list, GPage gPage) {
//        return Response.create().setSuccess(true).setResult(list).setPage(gPage);
//    }


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
            return Response.create().setErrorType(ece.name()).setCode(ece.getCode()).setMsg(exception.getMessage());
        }
        //系统异常
        else {
            IErrorCode ece = ErrorCodeEnum.SYSTEMERR;
            return Response.create().setErrorType(ece.name()).setCode(ece.getCode()).setMsg(getExceptionMessage(exception));
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
