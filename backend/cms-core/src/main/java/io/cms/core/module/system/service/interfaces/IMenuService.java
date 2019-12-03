package io.cms.core.module.system.service.interfaces;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import io.cms.core.module.system.persistence.entity.Menu;
import io.cms.core.module.system.vo.IMenu;

/**
 * @ClassName IMenuService.java
 * @Description TODO
 * @author PomeloMan
 */
@Transactional(readOnly = true)
public interface IMenuService {

	/**
	 * @param view
	 * @param isTreeMode
	 * @return
	 */
	List<IMenu> query(IMenu view, boolean isTreeMode);

	/**
	 * @param view
	 * @return
	 */
	@Transactional
	IMenu saveOne(IMenu view);

	/**
	 * @param Menu
	 * @return
	 */
	@Transactional
	IMenu saveOne(Menu entity);

	/**
	 * @param authorities
	 * @return
	 */
	@Transactional
	Collection<Menu> save(Collection<Menu> entities);

	/**
	 * 逻辑删除
	 * @param id
	 */
	@Transactional
	void logicDelete(Integer id);
	/**
	 * 逻辑删除
	 * @param ids
	 */
	@Transactional
	void logicDelete(Collection<Integer> ids);
}
