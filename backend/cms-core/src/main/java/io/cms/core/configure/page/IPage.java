package io.cms.core.configure.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class IPage<O> {

	private final static int Default_Page = 0;
	private final static int Default_Size = 10;

	private Pageable pageable;

	int page = Default_Page;
	int size = Default_Size;

	String order;
	String dir;

	String search;

	O object;

	public O getObject() {
		return object;
	}

	public void setObject(O object) {
		this.object = object;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Pageable getPageable() {
		Sort sort = null;
		if (pageable == null) {
			if (StringUtils.isNotEmpty(getOrder()) && StringUtils.isNoneEmpty(getDir())) {
				sort = Sort.by(Direction.fromString(getDir()), getOrder());
			} else {
				sort = Sort.unsorted();
			}
		}
		pageable = IPage.getPageRequest(getPage(), getSize(), sort);
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public static Pageable getDefaultPageRequest() {
		return PageRequest.of(Default_Page, Default_Size);
	}

	public static Pageable getPageRequest(int page, int size) {
		return getPageRequest(page, size, Sort.unsorted());
	}

	public static Pageable getPageRequest(int page, int size, Sort sort) {
		if (page < 0)
			page = Default_Page;
		if (size < 1)
			size = Default_Size;
		return PageRequest.of(page, size, sort);
	}
}
