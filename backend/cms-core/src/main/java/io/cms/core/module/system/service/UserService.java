package io.cms.core.module.system.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

import io.cms.core.configure.page.IPage;
import io.cms.core.module.system.enums.Status;
import io.cms.core.module.system.initial.GlobalVariable;
import io.cms.core.module.system.persistence.entity.Authority;
import io.cms.core.module.system.persistence.entity.Role;
import io.cms.core.module.system.persistence.entity.User;
import io.cms.core.module.system.persistence.repo.UserRepository;
import io.cms.core.module.system.service.interfaces.IUserService;
import io.cms.core.module.system.vo.IUser;
import io.cms.util.BeanUtils;
import io.cms.util.commons.DateUtil;

@Service
public class UserService implements IUserService {

	private final Log logger = LogFactory.getLog(UserService.class);

	@Autowired
	ConfigurableApplicationContext context;

	@Autowired
	UserRepository userRep;

	private Specification<User> getQueryClause(IUser view) {
		return new Specification<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug(view);
					}
					return null;
				}

				String search = view.getSearch();

				String username = null;
				Collection<Role> roles = null;
				User user = BeanUtils.transform(view, User.class);
				if (user != null) {
					username = user.getUsername();
					roles = user.getRoles();
				}

				List<Predicate> restrictions = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(search)) {
					Predicate fuzzyPredicate = null;
					try {
						fuzzyPredicate = builder.equal(root.get("createdDate"),
								DateUtils.parseDate(search, DateUtil.YYYY_MM_DD));
					} catch (ParseException e) {
						fuzzyPredicate = builder.or(builder.equal(root.get("username").as(String.class), search),
								builder.equal(root.get("displayName").as(String.class), search));
					}
					restrictions.add(fuzzyPredicate);
				}
				// add username condition
				if (StringUtils.isNotEmpty(username)) {
//					Predicate likePredicate = builder.like(root.get("username"), "%" + username + "%");
//					restrictions.add(likePredicate);
					// 优化模糊查询，使用locate；locate(?, user.username) >= 1
					Predicate likePredicate = builder.ge(builder.locate(root.get("username"), username), 1);
					restrictions.add(likePredicate);
				}
				// add role condition
				if (CollectionUtils.isNotEmpty(roles)) {
					Join<User, Role> roleJoin = root.join("roles", JoinType.LEFT); // left outer join users_roles
					Iterator<Role> iterator = roles.iterator();
					In<String> in = builder.in(roleJoin.get("name").as(String.class));
					while (iterator.hasNext()) {
						in.value(iterator.next().getName());
					}
					restrictions.add(in);
				}
				// add where condition
				query.where(builder.and(restrictions.toArray(new Predicate[restrictions.size()])));
				return query.getRestriction();
			}
		};
	}

	/**
	 * {@link UserDetailsService}
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRep.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
				user.getStatus().equals(Status.Valid), !user.getStatus().equals(Status.Expired), true,
				!user.getStatus().equals(Status.Invalid), loadUserAuthorities(username));
	}

	/**
	 * {@link loadUserByUsername(String username)}
	 * 
	 * @param username
	 * @return
	 */
	public Collection<? extends GrantedAuthority> loadUserAuthorities(String username) {
		Collection<Authority> authorities = userRep.findAuthoriesByUsername(username);
		if (!authorities.isEmpty()) {
			return authorities.stream().map(auth -> new SimpleGrantedAuthority(auth.getName()))
					.collect(Collectors.toSet());
		}
		return Collections.emptyList();
	}

	@Override
	public User findOne(String username) {
		return userRep.findById(username).orElse(null);
	}

	@Override
	public User findOneForUpdate(String username) {
		return userRep.findOneForUpdate(username);
	}

	@Override
	public Collection<User> query(IUser view) {
		return userRep.findAll(getQueryClause(view));
	}

	@Override
	public Page<IUser> query(IPage<IUser> pageView, Pageable pageable) {
		if (pageable == null) {
			pageable = pageView.getPageable();
		}
		IUser iiuser =new IUser();
		iiuser.setRoles(Sets.newHashSet(new Role("ADMIN")));
		pageView.setObject(iiuser);
		Page<User> upage = userRep.findAll(getQueryClause(pageView.getObject()), pageable);
		Page<IUser> iupage = new PageImpl<IUser>(upage.getContent().stream().map(user -> {
			IUser iuser = BeanUtils.transform(user, IUser.class, "password");
			iuser.setDstatus(GlobalVariable.cache.get(iuser.getStatus().getDictCode()));
			iuser.getRoles().forEach(role -> {
				role.setDname(GlobalVariable.cache.get(role.getDcode()));
			});
			return iuser;
		}).collect(Collectors.toList()), upage.getPageable(), upage.getTotalElements());
		return iupage;
	}

	@Override
	public User saveOne(IUser view) {
		return this.saveOne(BeanUtils.transform(view, User.class));
	}

	@Override
	public User saveOne(User entity) {
		Assert.notNull(entity, "[Assertion failed] - user entity must not be null");
		Assert.notNull(entity.getUsername(), "[Assertion failed] -  username must not be null");
		User _entity = userRep.findById(entity.getUsername()).orElse(null);
		if (_entity != null) {
			entity.setVersion(_entity.getVersion());
		}
		return userRep.save(entity);
	}

	@Override
	public Collection<User> save(Collection<User> entities) {
		List<User> result = new ArrayList<User>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void logicDelete(String id) {
		User user = this.findOne(id);
		if (user != null) {
			user.setStatus(Status.Deleted);
			this.saveOne(user);
		}
	}

	@Override
	public void logicDelete(Collection<String> ids) {
		ids.stream().forEach(id -> {
			this.logicDelete(id);
		});
	}

	@Override
	public void physicalDelete(String id) {
		User user = this.findOne(id);
		if (user != null) {
			userRep.deleteById(id);
		}
	}

	@Override
	public void physicalDelete(Collection<String> ids) {
		ids.stream().forEach(id -> {
			this.physicalDelete(id);
		});
	}

}
