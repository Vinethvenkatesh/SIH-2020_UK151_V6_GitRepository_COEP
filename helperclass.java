package com.example.atsproject;

public class helperclass {
    String sname,smail,sphone;

    public helperclass(String sname, String smail, String sphone) {
        this.sname = sname;
        this.smail = smail;
        this.sphone = sphone;
    }

    public helperclass() {
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSmail() {
        return smail;
    }

    public void setSmail(String smail) {
        this.smail = smail;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }
}
