package io.cms.core.module.system.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import io.cms.core.module.system.enums.Status;
import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public abstract class DefaultEntity {

	@ApiModelProperty(hidden = true)
	protected Status status = Status.Init;
	protected String creator;
	protected String modifier;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@org.hibernate.annotations.CreationTimestamp
	protected Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@org.hibernate.annotations.UpdateTimestamp
	protected Date modifiedDate;

	@Transient
	protected Dictionary dstatus;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Dictionary getDstatus() {
		return dstatus;
	}

	public void setDstatus(Dictionary dstatus) {
		this.dstatus = dstatus;
	}

}
