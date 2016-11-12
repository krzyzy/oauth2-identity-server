package com.solidify.oauth2.dashboard;

import com.solidify.oauth2.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final UserRepository userRepository;

    @Autowired
    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(path = "/api/dashboard/users/report", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserReportDto getUserReport() {
        return new UserReportDto(userRepository.count());
    }

}
