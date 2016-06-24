#**IPCheckNotifier** v0.1 - 2016
##Written by Gerardo Canosa - gera.canosa@gmail.com

This is an application written for testing purposes mostly, and learning java. Many different concepts have been applied (DB, Socks, Properties file configuration read/write access, Mail, Arguments cmd line).

This little application consists of two files: .properties and .sqlite, first for app config purpose, and the .sqlite is the database where all the IP changes will be recorded.

##**ipchecker.properties** format

These are the fields required and parsed by the application in order to run properly.

mailfrom = YOUR@email.here  
mailto = YOUR@email.here  
passwd = YOUR_EMAIL_PASSWD_HERE  
subject = IPCheckerNotifier status  
smtp = smtp.gmail.com  
smtp_port = 587  
frequencycheck = 15  

##Contact the author (Gerardo Canosa/EtherNet)

If you need support, or want to suggest a feature, you're welcome to reach me out by email at: gera.canosa@gmail.com or irc.mundochat.com.ar #MundoChat under the nickname: EtherNet