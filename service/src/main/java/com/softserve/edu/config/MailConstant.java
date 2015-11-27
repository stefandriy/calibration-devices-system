package com.softserve.edu.config;

/**
 * Constants for mail config
 */
public class MailConstant {

    static final String DEFAULT_ENCODING = "UTF-8";
    static final String MAIL = "mail";
    static final String CONFIG = "config";
    static final String MAIL_CONFIG = MAIL + "." + CONFIG;
    static final String CONFIG_HOST = MAIL_CONFIG + "." + "host";
    static final String CONFIG_PORT = MAIL_CONFIG + "." + "port";
    static final String CONFIG_PROTOCOL = MAIL_CONFIG + "." + "protocol";
    static final String CREDENTIAL_USERNAME = "mail.credentials.username";
    private static final char HIDDEN_CREDENTIAL_PASSWORD[] = {'m','a','i','l','.','c','r','e','d','e','n','t','i','a','l','s','.','p','a','s','s','w','o','r','d'};
    static final String CREDENTIAL_PASSWORD = new String(HIDDEN_CREDENTIAL_PASSWORD);
    //static final String CREDENTIAL_PASSWORD = "mail.credentials.password";
    //static final String DEFAULT_ENCODING = "UTF-8";
    //static final String DEFAULT_ENCODING = "UTF-8";
    //static final String DEFAULT_ENCODING = "UTF-8";
    //static final String DEFAULT_ENCODING = "UTF-8";
}
