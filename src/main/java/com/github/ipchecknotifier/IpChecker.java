package com.github.ipchecknotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
 
public class IpChecker {
    private static final String AMAZONURL = "http://checkip.amazonaws.com";
    
    public String showIp() throws Exception
    {
        return getIp();
    }
 
    private static String getIp() throws Exception {
        URL whatismyip = new URL(IpChecker.AMAZONURL);
        BufferedReader in = null;
        
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
}