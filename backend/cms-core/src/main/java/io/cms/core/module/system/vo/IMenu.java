package io.cms.core.module.system.vo;

import io.cms.core.module.system.persistence.entity.Menu;

public class IMenu extends Menu {

	private static final long serialVersionUID = 1L;

	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
}
