package kim.sesame.framework.web.controller;

import kim.sesame.framework.entity.GPage;
import kim.sesame.framework.exception.BusinessException;
import kim.sesame.framework.web.config.ProjectConfig;
import kim.sesame.framework.web.response.Response;
import kim.sesame.framework.web.security.exception.AccessNotAllowException;
import kim.sesame.framework.web.security.exception.FunctionNotValidException;
import kim.sesame.framework.web.security.exception.UserNotLoginException;
import kim.sesame.framework.web.validator.exception.DataValidatorException;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
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

    public static interface ErrorCode {
        static final String VALIDATOR = "1000"; //数据验证错误
        static final String NOT_LOGIN = "1001"; //用户为登录
        static final String BUSINESS = "2000"; //业务异常错误
        static final String SYSTEM = "9999"; //系统异常
        static final String NOTVALID = "1001"; //无效异常
    }

    public static interface ExceptionType {
        static final String VALIDATOR = "VALIDATOR";//数据校验异常
        static final String NOT_LOGIN = "NOT_LOGIN";//用户为登录
        static final String BUSINESS = "BUSINESS"; //业务异常
        static final String SYSTEM = "SYSTEM"; //系统异常
        static final String NOTVALID = "NOTVALID"; //无效异常
    }

    //    @Autowired(required = false)*****
    //    private ILogExceptionWriter logExceptionWriter;
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


    @ExceptionHandler(UserNotLoginException.class)
    public String handleUserNotLoginException() {
        //用户未登录重定向到根目录
        return "redirect:/index/login";
    }

    /**
     * 异常处理
     *
     * @param exception exception
     * @return Response
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception exception) {
        //打印异常******
        log.error(exception.getMessage(), exception);
        //        if(logExceptionWriter != null){
        //            logExceptionWriter.write(exception);
        //        }
        //参数校验异常
        if (exception instanceof IllegalArgumentException) {
            String errorCode = ErrorCode.BUSINESS;
            return Response.create().setExceptionType(ExceptionType.BUSINESS).setErrorCode(errorCode).setMessage(exception.getMessage());
        }
        //数据验证异常
        else if (exception instanceof DataValidatorException) {
            String errorCode = ((DataValidatorException) exception).getErrorCode();
            errorCode = StringUtils.isEmpty(errorCode) ? ErrorCode.VALIDATOR : errorCode;
            return Response.create().setExceptionType(ExceptionType.VALIDATOR).setErrorCode(errorCode).setMessage(exception.getMessage());
        }
        //业务异常
        else if (exception instanceof BusinessException) {
            String errorCode = ((BusinessException) exception).getErrorCode();
            errorCode = StringUtils.isEmpty(errorCode) ? ErrorCode.BUSINESS : errorCode;
            return Response.create().setExceptionType(ExceptionType.BUSINESS).setErrorCode(errorCode).setMessage(exception.getMessage());
        }
        //权限不足
        else if (exception instanceof AccessNotAllowException) {
            String errorCode = ((AccessNotAllowException) exception).getErrorCode();
            errorCode = StringUtils.isEmpty(errorCode) ? ErrorCode.VALIDATOR : errorCode;
            return Response.create().setExceptionType(ExceptionType.VALIDATOR).setErrorCode(errorCode).setMessage("权限不足!");
        }
        // 功能无效==>交互异常
        else if (exception instanceof FunctionNotValidException) {
            String errorCode = ((AccessNotAllowException) exception).getErrorCode();
            errorCode = StringUtils.isEmpty(errorCode) ? ErrorCode.NOTVALID : errorCode;
            return Response.create().setExceptionType(ExceptionType.NOTVALID).setErrorCode(errorCode).setMessage(exception.getMessage());
        }
        //系统异常
        else {
            return Response.create().setExceptionType(ExceptionType.SYSTEM).setErrorCode(ErrorCode.SYSTEM).setMessage(getExceptionMessage(exception));
        }
    }

    /**
     * 判断是否是 debug 模式,如果是就返回异常的详细信息
     *
     * @param e
     * @return
     */
    public String getExceptionMessage(Exception e) {
        String message = ProjectConfig.getSystemExceptionMessage();
        if (ProjectConfig.isDebug()) {
            message = e.getMessage();
        }
        return message;
    }
}
