package com.PropertyData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class loadProperty {
    public String URL;
    public String userNumber;
    public String password;
    public String betUrl;
    public String liveUrl;

    public void loadProperties() throws IOException {
        String projectPath = System.getProperty("user.dir");
        InputStream input = new FileInputStream(projectPath + "/src/main/java/com/PropertyData/config.properties");

        Properties prop = new Properties();
        prop.load(input);
        URL = prop.getProperty("login.url");
        userNumber = prop.getProperty("login.usernumber");
        password = prop.getProperty("login.password");
        betUrl = prop.getProperty("launchUrl.betUrl");
        liveUrl=prop.getProperty("visitLiveUrl.liveUrl");

    }
}