package io.cms.core.module.system.service.interfaces;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import io.cms.core.configure.page.IPage;
import io.cms.core.module.system.persistence.entity.User;
import io.cms.core.module.system.vo.IUser;

@Transactional(readOnly = true)
public interface IUserService extends UserDetailsService {

	/**
	 * 根据用户名获取用户权限列表
	 * @param username
	 * @return
	 */
	Collection<? extends GrantedAuthority> loadUserAuthorities(String username);

	/**
	 * @param username
	 * @return
	 */
	User findOne(String username);

	/**
	 * 悲观锁，此查询方法会执行数据库行锁 执行此方法需要@Transactional(readOnly = false)
	 * {@link UserRepository.findOneForUpdate()}
	 * 
	 * @param username
	 * @return
	 */
	@Transactional
	User findOneForUpdate(String username);

	/**
	 * @param view
	 * @return
	 */
	Collection<User> query(IUser view);

	/**
	 * @param view
	 * @param pageable
	 * @return
	 */
	Page<IUser> query(IPage<IUser> pageView, Pageable pageable);

	/**
	 * @param view
	 * @return
	 */
	@Transactional
	User saveOne(IUser view);

	/**
	 * @param user
	 * @return
	 */
	@Transactional
	User saveOne(User entity);

	/**
	 * @param users
	 * @return
	 */
	@Transactional
	Collection<User> save(Collection<User> entities);

	/**
	 * @param id
	 */
	@Transactional
	void logicDelete(String id);

	/**
	 * @param ids
	 */
	@Transactional
	void logicDelete(Collection<String> ids);

	/**
	 * @param id
	 */
	@Transactional
	void physicalDelete(String id);

	/**
	 * @param ids
	 */
	@Transactional
	void physicalDelete(Collection<String> ids);
}
