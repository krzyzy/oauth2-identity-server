package com.solidify.oauth2.user;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocalAuthenticationProviderTest.class,
        LocalUserProfileViewControllerTest.class
})
public class SecurityTestSuite {
}
