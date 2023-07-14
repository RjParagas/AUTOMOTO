package com.example.automoto.Model;

public class Products {


    String productID, Sid, productname, productprice, productimage, productimage1, productimage2, productimage3, Warranty, Stocks, description, Category, time, date;

    public Products(){}

    public Products(String productID, String Sid, String productname, String productprice, String productprice1, String productprice2, String productprice3, String productimage, String warranty, String stocks, String description, String category, String time, String date) {
        this.productID = productID;
        this.Sid = Sid;
        this.productname = productname;
        this.productprice = productprice;
        this.productimage = productimage;
        this.productimage1 = productimage1;
        this.productimage2 = productimage2;
        this.productimage3 = productimage3;
        this.Warranty = warranty;
        this.Stocks = stocks;
        this.description = description;
        this.Category = category;
        this.time = time;
        this.date = date;
    }


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String Sid) {
        this.Sid = Sid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductimage1() {
        return productimage1;
    }

    public void setProductimage1(String productimage1) {
        this.productimage1 = productimage1;
    }

    public String getProductimage2() {
        return productimage2;
    }

    public void setProductimage2(String productimage2) {
        this.productimage2 = productimage2;
    }

    public String getProductimage3() {
        return productimage3;
    }

    public void setProductimage3(String productimage3) {
        this.productimage3 = productimage3;
    }

    public String getWarranty() {
        return Warranty;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public String getStocks() {
        return Stocks;
    }

    public void setStocks(String stocks) {
        Stocks = stocks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
