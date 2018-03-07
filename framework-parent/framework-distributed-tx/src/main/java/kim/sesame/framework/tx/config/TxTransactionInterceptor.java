package kim.sesame.framework.tx.config;

import com.codingapi.tx.springcloud.interceptor.TxManagerInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * aop 切面
 */
@Aspect
@Component
public class TxTransactionInterceptor implements Ordered {


    @Override
    public int getOrder() {
        return 1;
    }

    @Autowired
    private TxManagerInterceptor txManagerInterceptor;

    //@Around("execution(* kim.sesame.txtest.service.*Service.*(..))")
    @Around("execution(* kim.sesame..service.*Service.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return txManagerInterceptor.around(point);
    }
}
