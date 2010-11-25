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

package com.postmark.java;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Postmark for Java
 * <p/>
 * This library can be used to leverage the postmarkapp.com functionality from a Java client
 * <p/>
 * http://github.com/jaredholdcroft/postmark-java
 */

// Wrapper class for Postmark message
public class PostmarkMessage {

    // The sender's email address.
    @SerializedName("From")
    private String fromAddress;

    // The recipients's email address.
    @SerializedName("To")
    private String toAddress;

    // The email address to reply to. This is optional.
    @SerializedName("ReplyTo")
    private String replyToAddress;

    // The email address to carbon copy to. This is optional.
    @SerializedName("Cc")
    private String ccAddress;

    // The message subject line.
    @SerializedName("Subject")
    private String subject;

    // The message body, if the message contains HTML.
    @SerializedName("HtmlBody")
    private String htmlBody;

    // The message body, if the message is plain text.
    @SerializedName("TextBody")
    private String textBody;

    // The message body, if the message is plain text.
    @SerializedName("Tag")
    private String tag;

    // A collection of optional message headers.
    @SerializedName("Headers")
    private List<NameValuePair> headers;

    @SkipMe
    private boolean isHTML;


    public PostmarkMessage(String fromAddress, String toAddress, String replyToAddress, String ccAddress, String subject, String body, boolean isHTML, String tag, List<NameValuePair> headers) {

        this.isHTML = isHTML;

        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.replyToAddress = replyToAddress;
        this.ccAddress = ccAddress;
        this.subject = subject;

        if (isHTML)
            this.htmlBody = body;
        else
            this.textBody = body;

        this.tag = tag;

        this.headers = (headers == null) ? new ArrayList<NameValuePair>() : headers;
    }

    public PostmarkMessage(String fromAddress, String toAddress, String replyToAddress, String ccAddress, String subject, String body, boolean isHTML, String tag) {

        this(fromAddress, toAddress, replyToAddress, ccAddress, subject, body, isHTML, tag, null);

    }

    // Copy Constructor

    public PostmarkMessage(PostmarkMessage message) {

        this.fromAddress = message.fromAddress;
        this.toAddress = message.toAddress;
        this.replyToAddress = message.replyToAddress;
        this.ccAddress = message.ccAddress;
        this.subject = message.subject;

        this.htmlBody = message.htmlBody;
        this.textBody = message.textBody;

        this.headers = message.headers;

        this.isHTML = message.isHTML;

    }

    public void clean() {
        this.fromAddress = this.fromAddress.trim();
        this.toAddress = this.toAddress.trim();
        this.subject = (this.subject == null) ? "" : this.subject.trim();
    }

    public void validate() throws PostmarkException {

        if ((this.fromAddress == null) || (this.fromAddress.equals(""))) {
            throw new PostmarkException("You must specify a valid 'From' email address.");
        }
        if ((this.toAddress == null) || (this.toAddress.equals(""))) {
            throw new PostmarkException("You must specify a valid 'To' email address.");
        }

        // TODO: add more validation using regex
    }


    /**
     * @return the from email address
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * @param fromAddress The email address the message is sent from
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * @return the to email address
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * @param toAddress The email address the message is sent to
     */
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    /**
     * @return the cc email address
     */
    public String getCcAddress() {
        return ccAddress;
    }

    /**
     * @param ccAddress The email address the message is sent to
     */
    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    /**
     * @return the reply-to email address
     */
    public String getReplyToAddress() {
        return replyToAddress;
    }

    /**
     * @param replyToAddress The reply-to email address of the message
     */
    public void setReplyToAddress(String replyToAddress) {
        this.replyToAddress = replyToAddress;
    }

    /**
     * @return the email subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject The email subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body of a HTML message
     */
    public String getHtmlBody() {
        return htmlBody;
    }

    /**
     * @param htmlBody The HTML body content of the message
     */
    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    /**
     * @return the body of a plain text message
     */
    public String getTextBody() {
        return textBody;
    }

    /**
     * @param textBody The plain text body content of the message
     */
    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    /**
     * @return the body of a plain text message
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag The plain text body content of the message
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the email headers
     */
    public List<NameValuePair> getHeaders() {
        return headers;
    }

    /**
     * @param headers A map of all the email headers
     */
    public void setHeaders(List<NameValuePair> headers) {
        this.headers = headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostmarkMessage that = (PostmarkMessage) o;

        if (ccAddress != null ? !ccAddress.equals(that.ccAddress) : that.ccAddress != null) return false;
        if (fromAddress != null ? !fromAddress.equals(that.fromAddress) : that.fromAddress != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (htmlBody != null ? !htmlBody.equals(that.htmlBody) : that.htmlBody != null) return false;
        if (replyToAddress != null ? !replyToAddress.equals(that.replyToAddress) : that.replyToAddress != null)
            return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (textBody != null ? !textBody.equals(that.textBody) : that.textBody != null) return false;
        if (toAddress != null ? !toAddress.equals(that.toAddress) : that.toAddress != null) return false;
        if (tag != null ? !tag.equals(that.toAddress) : that.tag != null) return false;
        if (isHTML != that.isHTML) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromAddress != null ? fromAddress.hashCode() : 0;
        result = 31 * result + (toAddress != null ? toAddress.hashCode() : 0);
        result = 31 * result + (ccAddress != null ? ccAddress.hashCode() : 0);
        result = 31 * result + (replyToAddress != null ? replyToAddress.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (htmlBody != null ? htmlBody.hashCode() : 0);
        result = 31 * result + (textBody != null ? textBody.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PostmarkMessage");
        sb.append("{ fromAddress='").append(fromAddress).append('\'');
        sb.append(", toAddress='").append(toAddress).append('\'');
        sb.append(", ccAddress='").append(ccAddress).append('\'');
        sb.append(", replyToAddress='").append(replyToAddress).append('\'');
        sb.append(", subject='").append(subject).append('\'');

        sb.append(", htmlBody='").append(htmlBody).append('\'');
        sb.append(", textBody='").append(textBody).append('\'');

        sb.append(", tag='").append(tag).append('\'');
        sb.append(", headers=").append(headers);
        sb.append('}');
        return sb.toString();
    }


}


