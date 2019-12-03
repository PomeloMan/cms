package io.cms.core.module.system.vo;

import io.cms.core.module.system.persistence.entity.User;
import io.swagger.annotations.ApiModel;

@ApiModel
public class IUser extends User {

	private static final long serialVersionUID = 1L;

	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

}
