package com.sunx.test.restmi.model;

import java.io.Serializable;
/**
 * @author 
 * @version 1.0
 * @since 1.0
 */
import java.util.Date;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8675476429092194384L;
	// columns START
	private String logName;
	private String logPwd;
	private String trueName;
	private String mobile;
	private String email;
	private Integer roleId;
	private Integer status;
	private Date created;
	private Date modified;

	// columns END
	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getLogPwd() {
		return logPwd;
	}

	public void setLogPwd(String logPwd) {
		this.logPwd = logPwd;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

}
