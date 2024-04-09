package elements.spring.explore.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Since this project not connected to url's using Controller at this point,
we should implement AuthenticationFailureHandler not extends SimpleUrlAuthenticationFailureHandler

if it would connect Url's using Controller which returns pages then we have to extends
SimpleUrlAuthenticationFailureHandler
 */

@Component
public class BankerUserFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    UserNotRoleService service;
    Logger logger= LoggerFactory.getLogger(BankerUserFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.info("onAuthenticationFailure invoked");
        String userName=request.getParameter("username");
        UserNotRole user=service.lookUsername(userName);
        if(user!=null){
            if(user.getStatus()!=0){
                if(user.getAttempts()< user.getMaxAttempts()-1){
                    user.setAttempts(user.getAttempts()+1);
                    service.updateChances(user);
                    logger.info("Failure attempt");
                    exception = new LockedException("Failure attempts");
                }
                else {
                    service.updateStatus(user);
                    exception = new LockedException("Account locked due to 3 failure attempts");
                }
            }
        }
        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
