package elements.spring.explore.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Since this project not connected to url's using Controller at this point,
we should implement AuthenticationSuccessHandler not extends SimpleUrlAuthenticationSuccessHandler

if it would connect Url's using Controller which returns pages then we have to extends
SimpleUrlAuthenticationSuccessHandler
 */

@Component
public class BankerUserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    UserNotRoleService service;

    Logger logger= LoggerFactory.getLogger(BankerUserSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("onAuthenticationSuccess invoked");
        UserNotRole userNotRole = (UserNotRole) authentication.getPrincipal();
        if(userNotRole.getStatus()!=0){
            if(userNotRole.getAttempts()>0){
                userNotRole.setAttempts(1);
                service.updateChances(userNotRole);
            }
            //super.setDefaultTargetUrl("/web/dash");
            super.setDefaultTargetUrl("/template/");
            super.onAuthenticationSuccess(request, response, authentication);
        }
        else {
            logger.error("Maximum attempt reached contact admin to reset");
//            super.setDefaultTargetUrl("/web/login");
            super.setDefaultTargetUrl("/login");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
