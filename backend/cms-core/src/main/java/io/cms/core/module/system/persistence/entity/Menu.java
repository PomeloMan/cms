package io.cms.core.module.system.persistence.entity;

import java.io.Serializable;
import java.util.List;
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
@Table(name = "sys_menu")
public class Menu extends VersionEntity implements Comparable<Menu>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单主键
	 */
	@Id
	private Integer id;
	/**
	 * 父菜单主键
	 */
	private Integer pid;
	/**
	 * 菜单名称/字典编码
	 */
	private String name;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 菜单路径
	 */
	private String url;
	/**
	 * 菜单顺序
	 */
	@Column(name = "queue")
	private Integer queue;
	/**
	 * 用户角色（双向关联） 注：查询菜单时不显示角色信息，角色信息中有菜单信息，以免出现死循环
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_menu", // 关联表名
			inverseJoinColumns = @JoinColumn(name = "role_name"), // 被维护端外键
			joinColumns = @JoinColumn(name = "menu_id"))
	@JsonIgnore
	private Set<Role> role;
	@ManyToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Role> roles;
	/**
	 * 子菜单列表
	 */
	@Transient
	private List<Menu> children;
	/**
	 * 字典表数据
	 */
	@Transient
	private Dictionary dname;

	@Override
	public int compareTo(Menu target) {
		return this.queue - target.queue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getQueue() {
		return queue;
	}

	public void setQueue(Integer queue) {
		this.queue = queue;
	}

	public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public Dictionary getDname() {
		return dname;
	}

	public void setDname(Dictionary dname) {
		this.dname = dname;
	}
}
