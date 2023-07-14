package com.example.automoto.Model;

public class OrderList {

    String CartID, ItemName, ItemID, Price, Qty, ShopID, TotalPrice, date, time, ItemImage, Status;

    public OrderList(){}

    public OrderList(String cartID, String itemName, String itemID, String price, String qty, String shopID, String totalPrice, String date, String time, String itemImage, String Status) {
        this.CartID = cartID;
        this.ItemName = itemName;
        this.ItemID = itemID;
        this.Price = price;
        this.Qty = qty;
        this.ShopID = shopID;
        this.TotalPrice = totalPrice;
        this.date = date;
        this.time = time;
        this.ItemImage = itemImage;
        this.Status = Status;
    }

    public String getCartID() {
        return CartID;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
