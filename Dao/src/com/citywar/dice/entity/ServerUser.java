package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ServerUser {
    private Integer id;

    private Integer parentid;

    private Boolean enabled;

    private Date lastlogintime;

    private String loginname;

    private String passphrase;

    private Boolean passwordexpired;

    private Date registertime;

    private String salt;

    private String code;

    private String departmentid;

    private String email;

    private String idnumber;

    private String mobile;

    private String name;

    private Integer ordinal;

    private String type;

    private Integer cardcount;

    private Integer usecardcount;

    public ServerUser()
    {
    }
    
    public ServerUser(ResultSet rs) throws SQLException{
		
		
    	setId(rs.getInt("id"));
    	setParentid(rs.getInt("parentId"));
    	setEnabled(rs.getBoolean("enabled"));
    	setLastlogintime(rs.getDate("lastLoginTime"));
    	setLoginname(rs.getString("loginName"));
    	setPassphrase(rs.getString("passphrase"));
    	setPasswordexpired(rs.getBoolean("passwordExpired"));
    	setRegistertime(rs.getDate("registerTime"));
    	setSalt(rs.getString("salt"));
    	setCode(rs.getString("code"));
    	setDepartmentid(rs.getString("departmentId"));
    	setEmail(rs.getString("email"));
    	setIdnumber(rs.getString("idNumber"));
    	setMobile(rs.getString("mobile"));
    	setName(rs.getString("name"));
    	setOrdinal(rs.getInt("ordinal"));
    	setType(rs.getString("type"));
    	setCardcount(rs.getInt("cardCount"));
    	setUsecardcount(rs.getInt("useCardCount"));
    	
 	
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname == null ? null : loginname.trim();
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase == null ? null : passphrase.trim();
    }

    public Boolean getPasswordexpired() {
        return passwordexpired;
    }

    public void setPasswordexpired(Boolean passwordexpired) {
        this.passwordexpired = passwordexpired;
    }

    public Date getRegistertime() {
        return registertime;
    }

    public void setRegistertime(Date registertime) {
        this.registertime = registertime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid == null ? null : departmentid.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber == null ? null : idnumber.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getCardcount() {
        return cardcount;
    }

    public void setCardcount(Integer cardcount) {
        this.cardcount = cardcount;
    }

    public Integer getUsecardcount() {
        return usecardcount;
    }

    public void setUsecardcount(Integer usecardcount) {
        this.usecardcount = usecardcount;
    }
}