package com.github.ipchecknotifier;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.ConfigurationException;

public class IPCheckerCli {
    private DBInterface db;
    private PropertyFileReader propReader;
    private final Mailer mailer;
    private final Options options;

    IPCheckerCli()
{
        try {
            this.db = new DBInterface();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(IPCheckerCli.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.propReader = new PropertyFileReader();
        } catch (ConfigurationException ex) {
            System.err.println(ex.getMessage());
        }
        
        this.mailer = new Mailer();
        this.options = new Options();
        
        this.buildOptions();
    }

    private void buildOptions()
    {
        this.options.addOption("h", "help", false, "Shows help");
        this.options.addOption("l", "lastip", false, "Shows last IP record from DB.");
        this.options.addOption("g", "getip", false, "Get actual public IP.");
        this.options.addOption("r", "records", false, "Shows last 10 IPs from DB.");
        this.options.addOption("a", "about", false, "About this program.");
        this.options.addOption("c", "cleandb", false, "Clean all recorded IPs from DB.");
    }

    private void showLastIP()
    {
        String ip = this.db.fetchIP();

        System.out.println("Last IP from the DB (not the current one)");
        if (ip != null)
            System.out.println(ip);
        else
            System.err.println("No previous IP recorded.");
    }

    private void getIP()
    {
        String publicIP = null;
        try {
            publicIP = IPChecker.getIP();
        } catch (Exception ex) {
            Logger.getLogger(IPCheckerCli.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        System.out.println("Shows actual public IP.");
        if (this.db.istheIPnew(publicIP)) {
            this.db.insertDB(publicIP, DateMgmt.getDate(), "ip added");
            System.out.println("Sending email to: " + this.propReader.getMailto() + " -->> " + publicIP);
            // Send email with new IP
            this.mailer.sendMail(this.propReader, publicIP);
        } else {
            System.out.println("IP didn't change, not added to the DB: " + publicIP);
        }
    }

    private void showRecords()
    {
        System.out.println("Show a list from sqlite db with the last IPs assigned by the ISP");
        try {
            this.db.fetchAll();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IPCheckerCli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showAbout()
    {
        System.out.println();
        System.out.println();
        System.out.println("\t\t***********************************");
        System.out.println("\t\t** IPCheckerNotifier v0.1 - 2016 **");
        System.out.println("\t\t**** Written by Gerardo Canosa ****");
        System.out.println("\t\t****** and Geronimo Poppino *******");
        System.out.println("\t\t***********************************");
        System.out.println();        
    }
    
    private void cleanDB()
    {
        this.db.cleanDB();
    }
    
    private void showHelp()
    {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("ipchecknotifier", this.options);
    }

    public void parse(String[] args)
    {  
        // comment.me KEY must be first removed from properties file otherwise it won't run.
        if (!this.propReader.isConfigured()) {
            System.err.println("ipchecker.properties hasn't been properly configured.");
            System.exit(1);
        }

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h"))
                this.showHelp();
            else if (cmd.hasOption("l"))
                this.showLastIP();
            else if (cmd.hasOption("g"))
                this.getIP();
            else if (cmd.hasOption("r"))
                this.showRecords();
            else if (cmd.hasOption("a"))
                this.showAbout();
            else if (cmd.hasOption("c"))
                this.cleanDB();
            else
                this.showHelp();

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            this.showHelp();
        }
    }
}