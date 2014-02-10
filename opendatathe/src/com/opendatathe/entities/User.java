package com.opendatathe.entities;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.codec.digest.DigestUtils;

import com.opendatathe.utils.JDOUtils;

@PersistenceCapable(detachable="true")
public class User implements Serializable{

	private static final long serialVersionUID = 8290821445126307008L;

	@PrimaryKey
	@Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	private String email;
	
	private String password;
	
	private Boolean confirmed = false;
	private Integer confirmCode;

	public Integer getConfirmCode() {
		return confirmCode;
	}

	public void setConfirmCode(Integer confirmCode) {
		this.confirmCode = confirmCode;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
     * logic to accept or reject access to the page, check log in status
     * @return true when authentication is deemed valid
     */
    public boolean isAuth(JDOUtils jdo){
    	List<User> result = jdo.findByAttribute(User.class, "email",  this.email);
    	if(result.iterator().hasNext()){
    		User user = result.iterator().next();
    		if(this.confirmed && user.getPassword().equals(DigestUtils.md5Hex(this.password))) 
    			return true;
    	}
		return false;
    }
}
