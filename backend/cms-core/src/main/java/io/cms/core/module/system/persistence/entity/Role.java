package io.cms.core.module.system.persistence.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sys_role")
public class Role extends VersionEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	/**
	 * 字典表Code
	 */
	@Column(name = "dict_code")
	private String dcode;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Authority> authorities;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_menu", // 关联表名
			inverseJoinColumns = @JoinColumn(name = "menu_id"), // 被维护端外键
			joinColumns = @JoinColumn(name = "role_name"))
	@JsonIgnore
	private Set<Menu> menu;
	@ManyToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private Set<Menu> menus;

	/**
	 * dcode 对应的字典表数据
	 */
	@Transient
	private Dictionary dname;

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<Menu> getMenu() {
		return menu;
	}

	public void setMenu(Set<Menu> menu) {
		this.menu = menu;
	}

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	public Dictionary getDname() {
		return dname;
	}

	public void setDname(Dictionary dname) {
		this.dname = dname;
	}

}
