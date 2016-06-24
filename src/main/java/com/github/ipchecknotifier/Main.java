package com.github.ipchecknotifier;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;

/**
 * IPCheckNotifier v0.1 - 2016 - gera.canosa@gmail.com
 * @author Gerardo Canosa
 */
public class Main {
    public static String publicIP = null;
    
    public static void main(String[] args) throws ClassNotFoundException, ConfigurationException
    {
        DBinterface db = new DBinterface();
        ReadPropFile readpropfile = new ReadPropFile();
        Mail mail = new Mail();

        try {
            IpChecker ipchecker = new IpChecker();
            Main.publicIP = ipchecker.showIp(); 
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        // IMPLEMENT CLEAN COMMAND LINE ARGUMENT TO CLEAN DE INFO/HISTORY TABLE AND START FROM SCRATCH, FOR NEW INSTALLATIONS.
        
        // check if no given arguments on command-line
        if (args.length != 0) {
            // if ip passed as argument shows the current ip
            if (args[0].startsWith("ip")) {
                System.out.println("Shows actual public IP.");
                // add DB record with information and current ip only if the Public IP changed from the last record
                // on the DB. istheIPnew() compares and returns true/false depending on the status.
                if (db.istheIPnew(publicIP)) {
                    db.insertDB(Main.publicIP, DateMgmt.getDate(), "ip added");
                    System.out.println("Sending email to: " + readpropfile.getMailto() + " -->> " + Main.publicIP);
                    // Send email with new IP
                    mail.sendMail(readpropfile);
                } else {
                    System.out.println("IP didn't change, not added to the DB: " + Main.publicIP);
                }
            // if history is passed as argument show the lasts IP records from the DB.
            } else if (args[0].startsWith("history")) {
                System.out.println("Show a list from sqlite db with the last IPs assigned by the ISP");
                db.fetchAll();
            } else if (args[0].startsWith("lastip")) {
                System.out.println("Last IP from the DB (not the current one)");
                System.out.println(db.fetchIP());
            } else if (args[0].startsWith("cleandb")) {
                // IMPLEMENT CLEAN COMMAND LINE ARGUMENT TO CLEAN DE INFO/HISTORY TABLE AND START FROM SCRATCH, FOR NEW INSTALLATIONS.
            } else if (args[0].startsWith("about")) {
                System.out.println();
                System.out.println();
                System.out.println("\t\t***********************************");
                System.out.println("\t\t** IPCheckerNotifier v0.1 - 2016 **");
                System.out.println("\t\t**** Written by Gerardo Canosa ****");
                System.out.println("\t\t***********************************");
                System.out.println();
            } else {
                System.out.println("Invalid command-line parameter(s) given. Try with ip/history/lastip/cleandb/about");
                System.out.println("Showing public IP: " + Main.publicIP);
            }
        } else {
//            no parameters given showing default message.
            System.err.println("No command-line parameters given. Try with ip/history/lastip/cleandb/about");
            System.out.println("Showing public IP: " + Main.publicIP);
        }
    }
}