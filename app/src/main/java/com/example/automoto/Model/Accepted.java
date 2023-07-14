package com.example.automoto.Model;

public class Accepted {

    String rid, Sid, bookingtype, concern, date, image, rideraddress, ridersname, riderscontact, Service, Status;

    public Accepted(){
    }

    public Accepted(String pid, String Sid, String bookingtype, String concern, String date, String image, String rideraddress, String ridersname, String riderscontact, String Service, String Status) {
        this.rid = pid;
        this.Sid = Sid;
        this.bookingtype = bookingtype;
        this.concern = concern;
        this.date = date;
        this.image = image;
        this.rideraddress = rideraddress;
        this.ridersname = ridersname;
        this.riderscontact = riderscontact;
        this.Service = Service;
        this.Status = Status;
    }

    public String getRid() {
        return rid;
    }

    public void setPid(String pid) {
        this.rid = pid;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getBookingtype() {
        return bookingtype;
    }

    public void setBookingtype(String bookingtype) {
        this.bookingtype = bookingtype;
    }

    public String getConcern() {
        return concern;
    }

    public void setConcern(String concern) {
        this.concern = concern;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRideraddress() {
        return rideraddress;
    }

    public void setRideraddress(String rideraddress) {
        this.rideraddress = rideraddress;
    }

    public String getRidersname() {
        return ridersname;
    }

    public void setRidersname(String ridersname) {
        this.ridersname = ridersname;
    }

    public String getRiderscontact() {
        return riderscontact;
    }

    public void setRiderscontact(String riderscontact) {
        this.riderscontact = riderscontact;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
