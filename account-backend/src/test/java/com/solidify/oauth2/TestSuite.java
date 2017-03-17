package com.solidify.oauth2;

import com.solidify.oauth2.user.local.SecurityTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SecurityTestSuite.class
})
public class TestSuite {
}
