package io.cms.core.module.system.service.interfaces;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.cms.core.configure.page.IPage;
import io.cms.core.module.system.persistence.entity.Role;
import io.cms.core.module.system.vo.IRole;

/**
 * @ClassName IRoleService.java
 * @Description TODO
 * @author PomeloMan
 */
@Transactional(readOnly = true)
public interface IRoleService {

	/**
	 * @param view
	 * @return
	 */
	Collection<Role> query(IRole view);

	/**
	 * @param view
	 * @param pageable
	 * @return
	 */
	Page<IRole> query(IPage<IRole> pageView, Pageable pageable);

	/**
	 * @param view
	 * @return
	 */
	@Transactional
	Role saveOne(IRole view);

	/**
	 * @param role
	 * @return
	 */
	@Transactional
	Role saveOne(Role entity);

	/**
	 * @param roles
	 * @return
	 */
	@Transactional
	Collection<Role> save(Collection<Role> entities);
}
