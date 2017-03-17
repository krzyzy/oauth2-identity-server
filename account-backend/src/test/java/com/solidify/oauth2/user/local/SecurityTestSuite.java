package com.solidify.oauth2.user.local;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocalAuthenticationProviderTest.class,
        LocalUserProfileControllerTest.class
})
public class SecurityTestSuite {
}
