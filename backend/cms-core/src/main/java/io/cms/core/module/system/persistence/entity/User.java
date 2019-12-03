package io.cms.core.module.system.persistence.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.cms.core.module.system.enums.Status;
import io.cms.core.module.system.security.PasswordEncoderImpl;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "sys_user")
public class User extends VersionEntity implements UserDetails, Serializable {

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private long id;

	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	private String displayName;
	@ApiModelProperty(hidden = true)
	@JsonIgnore // json 不返回 password 字段
	private String password;

	private String avatar;
	private String email;
	private Integer gender;// 0:male / 1:female

	@ApiModelProperty(hidden = true)
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	@ApiModelProperty(hidden = true)
	@Transient
	private Collection<SimpleGrantedAuthority> authorities;

	public User(String username, Status status, Collection<SimpleGrantedAuthority> authorities) {
		super();
		this.username = username;
		this.status = status;
		this.authorities = authorities;
	}

	public User(String username) {
		super();
		this.username = username;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if (StringUtils.isNotEmpty(password)) {
			this.password = PasswordEncoderImpl.encode(password, username);
		} else {
			this.password = password;
		}
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Collection<SimpleGrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !this.status.equals(Status.Expired);
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.status.equals(Status.Valid);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !this.status.equals(Status.Expired);
	}

	@Override
	public boolean isEnabled() {
		return this.status.equals(Status.Valid);
	}

}
