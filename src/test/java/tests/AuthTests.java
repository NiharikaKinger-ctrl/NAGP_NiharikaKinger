package tests;

import base.BaseTest;
import utils.AuthUtils;
import utils.ConfigManager;

import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTests extends BaseTest {
	private static final Logger logger = Logger.getLogger(AuthUtils.class.getName());

    @Test
    public void testCreateToken() {
        String token = AuthUtils.createToken(
        		ConfigManager.getProperty("username"), 
                ConfigManager.getProperty("password")
        		);
        
        logger.info("Generated Token: " + token);
        Assert.assertNotNull(token, "Token should not be null");
    }
}
