package com.example.config;

import com.example.constants.HttpStatusEnum;
import com.example.exception.ApiException;
import com.example.pojo.Result;
import com.example.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 分支 develop
 * @ClassName ApplicationControllerExceptionHandler
 * @Description TODO
 * @Author admin
 * @Date 2022/2/25 13:07
 * @Version 1.0
 **/
@ControllerAdvice
public class ApplicationControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationControllerExceptionHandler.class);
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result<String> bindException(BindException ex){
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(
                error -> sb.append(error.getDefaultMessage()).append(";")
        );
        return ResultGenerator.getResultByMsg(HttpStatusEnum.BAD_REQUEST,sb.substring(0,sb.length()-1));
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> message.append(error.getDefaultMessage()).append(";"));
        return ResultGenerator.getResultByMsg(HttpStatusEnum.BAD_REQUEST,
                message.substring(0, message.length() - 1));
    }
    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public Result<String> handlerApi(ApiException e){
        logger.error(e.getMessage());
        return ResultGenerator.getResultByMsg(HttpStatusEnum.BAD_REQUEST,"权限不足,无法访问");
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<String> handlerError(HttpServletRequest req, Exception e) {
        // 输出异常信息到控制台
        logger.error(e.getMessage());
        return ResultGenerator.getResultByHttp(HttpStatusEnum.INTERNAL_SERVER_ERROR, "出现异常错误,请及时查看后台日志！");
    }
}
