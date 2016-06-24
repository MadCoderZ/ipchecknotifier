package com.github.ipchecknotifier;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ReadPropFile {
    private String mailfrom = null;
    private String mailto = null;
    private String subject = null;
    private Integer frequencycheck = null;
    private CompositeConfiguration config = null;
    private String passwd = null;
    
    public String getPasswd() {
        return passwd;
    }

    public void getPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    public String getMailfrom() {
        return mailfrom;
    }

    public void setMailfrom(String mailfrom) {
        this.mailfrom = mailfrom;
    }

    public String getMailto() {
        return mailto;
    }

    public void setMailto(String mailto) {
        this.mailto = mailto;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getFrequencycheck() {
        return frequencycheck;
    }

    public void setFrequencycheck(Integer frequencycheck) {
        this.frequencycheck = frequencycheck;
    }
    
    ReadPropFile() throws ConfigurationException 
    {
        this.config = new CompositeConfiguration();
        config.addConfiguration(new PropertiesConfiguration("ipchecker.properties"));
 
//        System.out.println("--------------------------------------------------------");
//        System.out.println("= Reading configuration from ipchecker.properties file =");
//        System.out.println("--------------------------------------------------------");
        
//      Load field from .properties file into separated variables.
        this.mailfrom = config.getString("mailfrom");
        this.mailto = config.getString("mailto");
        this.subject = config.getString("subject");
        this.frequencycheck = config.getInt("frequencycheck");
        this.passwd = config.getString("passwd");
        }
}