README
======

This library allows you to leverage the [Postmark](http://postmarkapp.com) REST API to send emails.

It is adapted from https://github.com/bitformed/postmark-java to include maven-ification, 
simplification and support for the Spring Framework by reuse of its Mail classes.

For now only SimpleMailMessage and MailSender are supported, but this can be easily 
extended to support JavaMailSender and MimeMailMessage (with attachments). This work
may be performed in the future, depending on my needs. 

Otherwise, feel free to improve the library ;)

If you use maven, this project is hosted at 

	<repository>
		<id>Github Imaginatio</id>
		<url>https://github.com/Imaginatio/Maven-repository/raw/master</url>
	</repository>
		
To import this artifact add this to your dependencies

	<dependency>
		<groupId>com.postmark</groupId>
		<artifactId>postmark-client</artifactId>
		<version>0.3.1</version>
	</dependency>