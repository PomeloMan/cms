package io.cms.core.module.system.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.cms.core.module.system.enums.Status;

@Entity
@Table(name = "sys_dict")
public class Dictionary implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT(20) COMMENT '主键'")
	private Long id;
	@Column(columnDefinition = "VARCHAR(50) COMMENT '字典编码'")
	private String code;
	@Column(columnDefinition = "VARCHAR(25) COMMENT '字典类型'")
	private String type;
	@Column(columnDefinition = "VARCHAR(255) COMMENT '字典值'")
	private String value;
	@Column(columnDefinition = "TEXT COMMENT '字典值（国际化），用|分隔'")
	private String valueI18n;
	@Column(columnDefinition = "INT(11) COMMENT '字典状态'")
	private Status status;
	@Column(columnDefinition = "VARCHAR(255) COMMENT '备注'")
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueI18n() {
		return valueI18n;
	}

	public void setValueI18n(String valueI18n) {
		this.valueI18n = valueI18n;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
