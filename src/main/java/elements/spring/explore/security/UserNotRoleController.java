package elements.spring.explore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserNotRoleController {
    @Autowired
    UserNotRoleService service;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/sign")
    public UserNotRole signingUp(@RequestBody UserNotRole userNotRole){
        userNotRole.setPassword(encoder.encode(userNotRole.getPassword()));
        return service.signIp(userNotRole);
    }

}
