package com.github.ipchecknotifier;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;

/**
 * IPCheckNotifier v0.1 - 2016 - gera.canosa@gmail.com
 * @author Gerardo Canosa and Geronimo Poppino
 */
public class Main {    
    public static void main(String[] args)
    {
        new IPCheckerCli().parse(args);
    }
}