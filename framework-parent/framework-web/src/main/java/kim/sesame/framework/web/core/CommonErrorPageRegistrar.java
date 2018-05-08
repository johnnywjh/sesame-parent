package kim.sesame.framework.web.core;

import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CommonErrorPageRegistrar implements ErrorPageRegistrar {
    public void registerErrorPages(ErrorPageRegistry registry) {

        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/page/error?e=404");
        ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/page/error?e=500");

        registry.addErrorPages(e404, e500);
    }
}
