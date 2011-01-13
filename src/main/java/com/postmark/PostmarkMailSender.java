// The MIT License
//
// Copyright (c) 2010 Jared Holdcroft
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.postmark;

import java.lang.reflect.Type;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

/**
 * Postmark for Java
 * <p/>
 * This library can be used to leverage the postmarkapp.com functionality from a Java client
 * <p/>
 *
 * http://github.com/nt/postmark-java
 * forked from http://github.com/jaredholdcroft/postmark-java
 */

// Class that does the heavy lifting
public class PostmarkMailSender implements MailSender {

    private static Logger logger = Logger.getLogger("com.postmark.java");
    private String serverToken;

    private static GsonBuilder gsonBuilder = new GsonBuilder();

    static {
        gsonBuilder.setPrettyPrinting();
    	gsonBuilder.disableHtmlEscaping();
        gsonBuilder.registerTypeAdapter(SimpleMailMessage.class, new SimpleMailMessageAdapter());
        gsonBuilder.registerTypeAdapter(PostmarkMessage.class, new SimpleMailMessageAdapter());
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter());
        logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.ALL);
    }


    /**
     * Initializes a new instance of the PostmarkClient class.
     * <p/>
     * If you do not have a server token you can request one by signing up to
     * use Postmark: http://postmarkapp.com.
     *
     * @param serverToken the postmark server token
     */
    public PostmarkMailSender(String serverToken) {

        this.serverToken = serverToken;

    }

	@Override
	public void send(SimpleMailMessage message) throws MailException {

        HttpClient httpClient = new DefaultHttpClient();
        PostmarkResponse theResponse = new PostmarkResponse();

        try {

            // Create post request to Postmark API endpoint
            HttpPost method = new HttpPost("http://api.postmarkapp.com/email");

            // Add standard headers required by Postmark
            method.addHeader("Accept", "application/json");
            method.addHeader("Content-Type", "application/json; charset=utf-8");
            method.addHeader("X-Postmark-Server-Token", serverToken);
            method.addHeader("User-Agent", "Postmark-Java");


            // Convert the message into JSON content
            Gson gson = gsonBuilder.create();
            String messageContents = gson.toJson(message);
            logger.info("Message contents: " + messageContents);

            // Add JSON as payload to post request
            StringEntity payload = new StringEntity(messageContents);
            method.setEntity(payload);


            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            try {
                String response = httpClient.execute(method, responseHandler);
                logger.info("Message response: " + response);
                theResponse = gsonBuilder.create().fromJson(response, PostmarkResponse.class);
                theResponse.status = PostmarkResponseStatus.SUCCESS;
            } catch (HttpResponseException hre) {
                switch(hre.getStatusCode()) {
                    case 401:
                    case 422:
                        logger.log(Level.SEVERE, "There was a problem with the email: " + hre.getMessage());
                        theResponse.setMessage(hre.getMessage());
                        theResponse.status = PostmarkResponseStatus.USERERROR;
                        throw new MailSendException("Postmark returned: "+theResponse);
                    case 500:
                        logger.log(Level.SEVERE, "There has been an error sending your email: " + hre.getMessage());
                        theResponse.setMessage(hre.getMessage());
                        theResponse.status = PostmarkResponseStatus.SERVERERROR;
                        throw new MailSendException("Postmark returned: "+theResponse);
                    default:
                        logger.log(Level.SEVERE, "There has been an unknow error sending your email: " + hre.getMessage());
                        theResponse.status = PostmarkResponseStatus.UNKNOWN;
                        theResponse.setMessage(hre.getMessage());
                        throw new MailSendException("Postmark returned: "+theResponse);

                }
            }


        } catch (Exception e) {
            logger.log(Level.SEVERE, "There has been an error sending your email: " + e.getMessage());
            throw new MailSendException("There has been an error sending your email", e);
        }
        finally {

            httpClient.getConnectionManager().shutdown();
        }

		
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		// TODO Auto-generated method stub
		
	}
	
	
	
	enum PostmarkResponseStatus {
		UNKNOWN, SUCCESS, USERERROR, SERVERERROR
	}

	public static class PostmarkResponse {
	    @Override
		public String toString() {
			return "PostmarkResponse [errorCode=" + errorCode + ", message="
					+ message + ", status=" + status + ", submittedAt="
					+ submittedAt + ", to=" + to + "]";
		}

		// The status outcome of the response.
	    @SerializedName("Status")
	    public PostmarkResponseStatus status;

	    // The message from the API.
	    // In the event of an error, this message may contain helpful text.
	    @SerializedName("Message")
	    public String message;

	    // The time the request was received by Postmark.
	    @SerializedName("SubmittedAt")
	    public DateTime submittedAt;

	    // The recipient of the submitted request.
	    @SerializedName("To")
	    public String to;

	    // The error code returned from Postmark.
	    // This does not map to HTTP status codes.
	    @SerializedName("ErrorCode")
	    public int errorCode;

	    public PostmarkResponseStatus getStatus() {
	        return status;
	    }

	    public void setStatus(PostmarkResponseStatus status) {
	        this.status = status;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public DateTime getSubmittedAt() {
	        return submittedAt;
	    }

	    public void setSubmittedAt(DateTime submittedAt) {
	        this.submittedAt = submittedAt;
	    }

	    public String getTo() {
	        return to;
	    }

	    public void setTo(String to) {
	        this.to = to;
	    }

	    public int getErrorCode() {
	        return errorCode;
	    }

	    public void setErrorCode(int errorCode) {
	        this.errorCode = errorCode;
	    }
	}
	
	public static class SimpleMailMessageAdapter implements JsonSerializer<SimpleMailMessage> {

		@Override
		public JsonElement serialize(SimpleMailMessage src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonO = new JsonObject();
			if(src.getFrom()==null)
				throw new MailParseException("You must specify a from adress");
			jsonO.addProperty("From", src.getFrom());
			if(src.getTo()==null)
				throw new MailParseException("You must specify a to adress");
			jsonO.addProperty("To", mergeMailAdresses(src.getTo()));
			if(src.getCc()!=null)
				jsonO.addProperty("Cc", mergeMailAdresses(src.getCc()));
			if(src.getBcc()!=null)
				jsonO.addProperty("Bcc", mergeMailAdresses(src.getBcc()));
			if(src.getSubject()==null)
				throw new MailParseException("You must specify a Subject field");
			jsonO.addProperty("Subject", src.getSubject());
			if(src instanceof PostmarkMessage)
				if(((PostmarkMessage) src).getTag()!=null)
					jsonO.addProperty("Tag", ((PostmarkMessage) src).getTag());
			if(src.getText()==null)
				throw new MailParseException("You must specify a Text field");
			jsonO.addProperty("TextBody", src.getText());
			if(src.getReplyTo()!=null)
				jsonO.addProperty("ReplyTo", src.getReplyTo());
			return jsonO;
		}
		
		private String mergeMailAdresses(String[] adresses) {
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<adresses.length;i++) {
				sb.append(adresses[i]);
				if(i<adresses.length-1) sb.append(",");
			}
			return sb.toString();
		}

	}

}
