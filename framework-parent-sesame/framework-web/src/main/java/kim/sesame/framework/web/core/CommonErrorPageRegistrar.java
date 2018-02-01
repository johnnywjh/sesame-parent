package kim.sesame.framework.web.core;

import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CommonErrorPageRegistrar implements ErrorPageRegistrar {
	public void registerErrorPages(ErrorPageRegistry registry) {
		
		ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
		ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

		registry.addErrorPages(e404, e500);
	}
}
