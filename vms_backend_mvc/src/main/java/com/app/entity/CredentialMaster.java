package com.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "master_crediantial")
public class CredentialMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "username")
	private String username;

	@Column(name = "user_password")
	private String password;

	@Column(name = "created_by")
	private String created_by;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private Date updatedOn;

	@Column(name = "is_active")
	private Boolean isActive;

	private Integer otp;

	private Long otpTimestamp;

	public Long getOtpTimestamp() {
		return otpTimestamp;
	}

	public void setOtpTimestamp(Long otpTimestamp) {
		this.otpTimestamp = otpTimestamp;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public CredentialMaster(Integer id, User user, String username, String password) {
		super();
		this.id = id;
		this.user = user;
		this.username = username;
		this.password = password;
	}

	public CredentialMaster() {
		super();

	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getupdatedOn() {
		return updatedOn;
	}

	public void setupdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	@Transient
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public String passwordEncoder(String password) {
		return passwordEncoder.encode(password);
	}

	public Boolean passwordMatcher(String password, String passwordInDB) {
		return passwordEncoder.matches(password, passwordInDB);
	}

}
