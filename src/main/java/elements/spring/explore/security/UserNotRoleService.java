package elements.spring.explore.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserNotRoleService implements UserDetailsService {
    Logger logger= LoggerFactory.getLogger(UserNotRoleService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public UserNotRole signIp(UserNotRole userNotRole){
        jdbcTemplate.update("insert into dum_emp values(?,?)",new Object[]{userNotRole.getUsername(),userNotRole.getPassword()});
        return userNotRole;
    }
    public UserNotRole lookUsername(String username){
        UserNotRole userNotRole = jdbcTemplate.queryForObject("select * from dum_emp where username=?",new Object[]{username},
                new BeanPropertyRowMapper<>(UserNotRole.class));
        return userNotRole;
    }
    public void updateChances(UserNotRole userNotRole){
        logger.info("Updating chances "+userNotRole.getAttempts()+" for "+userNotRole.getUsername());
        jdbcTemplate.update("update dum_emp set attempts=? where username=?",
                new Object[]{userNotRole.getAttempts(),userNotRole.getUsername()});
        logger.info("updateChances success");
    }
    public void updateStatus(UserNotRole userNotRole){
        jdbcTemplate.update("update dum_emp set status=0 where username=?",
                new Object[]{userNotRole.getUsername()});
        logger.info("updateStatus success");
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserNotRole userNotRole=lookUsername(username);
        if(userNotRole==null){
            return (UserDetails) new UsernameNotFoundException("Invalid Username entered");
        }
        return userNotRole;
    }
}
