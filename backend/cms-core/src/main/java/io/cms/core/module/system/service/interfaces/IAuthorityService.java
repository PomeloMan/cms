package io.cms.core.module.system.service.interfaces;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.cms.core.configure.page.IPage;
import io.cms.core.module.system.persistence.entity.Authority;
import io.cms.core.module.system.vo.IAuthority;

/**
 * @ClassName IAuthorityService.java
 * @Description TODO
 * @author PomeloMan
 */
@Transactional(readOnly = true)
public interface IAuthorityService {

	/**
	 * @param view
	 * @return
	 */
	Collection<Authority> query(IAuthority view);

	/**
	 * @param view
	 * @param pageable
	 * @return
	 */
	Page<IAuthority> query(IPage<IAuthority> pageView, Pageable pageable);

	/**
	 * @param view
	 * @return
	 */
	@Transactional
	Authority saveOne(IAuthority view);

	/**
	 * @param authority
	 * @return
	 */
	@Transactional
	Authority saveOne(Authority entity);

	/**
	 * @param authorities
	 * @return
	 */
	@Transactional
	Collection<Authority> save(Collection<Authority> entities);
}
