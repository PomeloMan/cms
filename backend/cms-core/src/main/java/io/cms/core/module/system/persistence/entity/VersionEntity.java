package io.cms.core.module.system.persistence.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class VersionEntity extends DefaultEntity {

	@Version
	@JsonIgnore
	protected int version;// spring 乐观锁@*Service.saveOne(T entity)

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
