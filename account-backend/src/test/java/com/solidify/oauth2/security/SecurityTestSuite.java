package com.solidify.oauth2.security;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocalAuthenticationProviderTest.class,
        UserTransformerTest.class,
        UserControllerTest.class
})
public class SecurityTestSuite {
}
