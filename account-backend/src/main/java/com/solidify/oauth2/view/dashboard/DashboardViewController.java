package com.solidify.oauth2.view.dashboard;

import com.solidify.oauth2.user.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardViewController {

    private final LocalUserRepository localUserRepository;

    @Autowired
    public DashboardViewController(LocalUserRepository localUserRepository) {
        this.localUserRepository = localUserRepository;
    }

    @RequestMapping(path = "/view/dashboard/users/report", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserReportDto getUserReport() {
        return new UserReportDto(localUserRepository.count());
    }

}
