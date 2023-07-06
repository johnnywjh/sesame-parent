package kim.sesame.common.web.controller;

import kim.sesame.common.context.TraceIdContext;
import kim.sesame.common.entity.IErrorCode;
import kim.sesame.common.exception.BizArgumentException;
import kim.sesame.common.exception.IException;
import kim.sesame.common.result.ApiResult;
import kim.sesame.common.result.ApiResultBuild;
import kim.sesame.common.result.ErrorCodeEnum;
import kim.sesame.common.web.config.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controlle 基础抽象类
 **/
@Slf4j
public abstract class AbstractController {


    public ApiResult success() {
        return ApiResultBuild.ContextSuccess.SUCCESS;
    }

    public ApiResult success(Object result) {
        return ApiResult.create().setSuccess(true).setData(result);
    }

    public ApiResult success(Object result, String msg) {
        return ApiResult.create().setSuccess(true).setData(result).setMessage(msg);
    }

    public ApiResult success(List list) {
        return ApiResult.create().setSuccess(true).setData(list);
    }

    public ApiResult success(List list, String msg) {
        return ApiResult.create().setSuccess(true).setData(list).setMessage(msg);
    }


//    @ExceptionHandler(UserNotLoginException.class)
//    public String handleUserNotLoginException() {
//        //用户未登录重定向到根目录
//        return "redirect:/index/login";
//    }

    @ExceptionHandler(BizArgumentException.class)
    @ResponseBody
    public ApiResult handleException(BizArgumentException exception) {
        log.error(exception.getMessage(), exception);
        ErrorCodeEnum erroeEnum = ErrorCodeEnum.PARAMS_ERR;
        return ApiResult.create()
                .setErrorType(erroeEnum.name())
                .setCode(erroeEnum.getCode())
                .setMessage(exception.getMessage())
                .setTraceInfo(TraceIdContext.getContext().getTraceId());
    }

    /**
     * 异常处理
     *
     * @param exception exception
     * @return Response
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult handleException(Exception exception) {

        log.error(exception.getMessage(), exception);

        if (exception instanceof IException) {
            IErrorCode ece = ((IException) exception).getErrorCodeEnum();
            return ApiResult.create()
                    .setErrorType(ece.name())
                    .setCode(ece.getCode())
                    .setMessage(exception.getMessage())
                    .setTraceInfo(TraceIdContext.getContext().getTraceId());
        }
        //系统异常
        else {
            IErrorCode ece = ErrorCodeEnum.SYSTEMERR;
            return ApiResult.create()
                    .setErrorType(ece.name())
                    .setCode(ece.getCode())
                    .setMessage(getExceptionMessage(exception))
                    .setTraceInfo(TraceIdContext.getContext().getTraceId());
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
