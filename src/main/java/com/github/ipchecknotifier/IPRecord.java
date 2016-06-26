package com.github.ipchecknotifier;

/**
 *
 * @author Geronimo
 */
public class IPRecord {

    private String IP;
    private String date;
    private String comments;

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIP() {
        return IP;
    }

    public String getDate() {
        return date;
    }

    public String getComments() {
        return comments;
    }
}

