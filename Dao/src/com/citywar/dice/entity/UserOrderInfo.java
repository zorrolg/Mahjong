package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserOrderInfo {
	
	private int id;
	
	private String channelName;
	private int userId;
	private String userName;
	private Timestamp createTime;
	private String originalPurchaseDatePst;
	
	private String purchaseDateMs;
	private String originalTransactionId;
	private String originalPurchaseDateMs;
	private String transactionId;
	private String quantity;
	private String bvrs;
	private String hostedIapVersion;
	private String productId;
	private String purchaseDate;
	private String originalPurchaseDate;
	private String purchaseDatePst;
	private String bid;
	private String itemId;
	private Float  money;
	private String status;
	
	public UserOrderInfo(){
		
	}
	
	public UserOrderInfo(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		
		this.channelName = rs.getString("channel_name");
		this.userId = rs.getInt("user_id");
		this.userName = rs.getString("user_name");
		this.createTime = rs.getTimestamp("create_time");
		this.originalPurchaseDatePst = rs.getString("original_purchase_date_pst");
		this.purchaseDateMs = rs.getString("purchase_date_ms");
		this.originalTransactionId = rs.getString("original_transaction_id");
		this.originalPurchaseDateMs = rs.getString("original_purchase_date_ms");
		this.transactionId = rs.getString("transaction_id");
		this.quantity = rs.getString("quantity");
		this.bvrs = rs.getString("bvrs");
		this.hostedIapVersion = rs.getString("hosted_iap_version");
		this.productId = rs.getString("product_id");
		this.purchaseDate = rs.getString("purchase_date");
		this.originalPurchaseDate = rs.getString("original_purchase_date");
		this.purchaseDatePst = rs.getString("purchase_date_pst");
		this.bid = rs.getString("bid");
		this.itemId = rs.getString("item_id");
		this.money = rs.getFloat("money");
		this.status = rs.getString("status");
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
		
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getOriginalPurchaseDatePst() {
		return originalPurchaseDatePst;
	}
	public void setOriginalPurchaseDatePst(String originalPurchaseDatePst) {
		this.originalPurchaseDatePst = originalPurchaseDatePst;
	}
	public String getPurchaseDateMs() {
		return purchaseDateMs;
	}
	public void setPurchaseDateMs(String purchaseDateMs) {
		this.purchaseDateMs = purchaseDateMs;
	}
	public String getOriginalTransactionId() {
		return originalTransactionId;
	}
	public void setOriginalTransactionId(String originalTransactionId) {
		this.originalTransactionId = originalTransactionId;
	}
	public String getOriginalPurchaseDateMs() {
		return originalPurchaseDateMs;
	}
	public void setOriginalPurchaseDateMs(String originalPurchaseDateMs) {
		this.originalPurchaseDateMs = originalPurchaseDateMs;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getBvrs() {
		return bvrs;
	}
	public void setBvrs(String bvrs) {
		this.bvrs = bvrs;
	}
	public String getHostedIapVersion() {
		return hostedIapVersion;
	}
	public void setHostedIapVersion(String hostedIapVersion) {
		this.hostedIapVersion = hostedIapVersion;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getOriginalPurchaseDate() {
		return originalPurchaseDate;
	}
	public void setOriginalPurchaseDate(String originalPurchaseDate) {
		this.originalPurchaseDate = originalPurchaseDate;
	}
	public String getPurchaseDatePst() {
		return purchaseDatePst;
	}
	public void setPurchaseDatePst(String purchaseDatePst) {
		this.purchaseDatePst = purchaseDatePst;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Float getMoney() {
		return money;
	}
	public void setMoney(Float money) {
		this.money = money;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
