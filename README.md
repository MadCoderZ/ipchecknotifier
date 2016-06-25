#**IPCheckNotifier** v0.1 - 2016
##Written by Gerardo Canosa and Geronimo Poppino


This is an application written for testing purposes mostly, and learning java. Many different concepts have been applied (DB, Socks, Properties file configuration read/write access, Mail, Arguments cmd line).

This little application consists of two files: .properties and .sqlite, first for app config purpose, and the .sqlite is the database where all the IP changes will be recorded.

##Running the Application for the very first time
To get the application running you first must set your email, password, and other stuff into the .properties file. You will understand how to config it in the next section. Once you modified and configured please remove or comment the following line:  

**comment.me = true**

Then you will be able to run the app, otherwise if the line exists the application will quit with an error message.
##**ipchecker.properties** format

These are the fields required and parsed by the application in order to run properly.

mail.from = mail@from.com  
mail.to = mail@to.com  
mail.smtp = smtp.gmail.com  
mail.port = 587 
mail.subject = IPCheckNotifier update  
mail.password = REPLACE_PASSWORD_HERE   
frequencycheck = IN_MINUTES  

##Contact the author (Gerardo Canosa/EtherNet)

If you need support, or want to suggest a feature, you're welcome to reach me out by email at: gera.canosa@gmail.com or irc.mundochat.com.ar #MundoChat under the nickname: EtherNet
