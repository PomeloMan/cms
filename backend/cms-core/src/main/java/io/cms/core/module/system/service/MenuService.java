package io.cms.core.module.system.service;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.cms.core.module.system.enums.Status;
import io.cms.core.module.system.initial.GlobalVariable;
import io.cms.core.module.system.persistence.entity.Menu;
import io.cms.core.module.system.persistence.entity.Role;
import io.cms.core.module.system.persistence.entity.User;
import io.cms.core.module.system.persistence.repo.DictRepository;
import io.cms.core.module.system.persistence.repo.MenuRepository;
import io.cms.core.module.system.persistence.repo.UserRepository;
import io.cms.core.module.system.service.interfaces.IMenuService;
import io.cms.core.module.system.vo.IMenu;
import io.cms.util.BeanUtils;
import io.cms.util.commons.DateUtil;

@Service
public class MenuService implements IMenuService {

	private final Log logger = LogFactory.getLog(MenuService.class);

	@Autowired
	DictRepository dictRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	MenuRepository menuRep;

	private Specification<Menu> getQueryClause(IMenu view) {
		return new Specification<Menu>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug(view);
					}
					return null;
				}

				String search = view.getSearch();

				String name = null;
				Set<Role> roles = null;
				Menu menu = BeanUtils.transform(view, Menu.class);
				if (menu != null) {
					name = menu.getName();
					roles = menu.getRoles();
				}

				List<Predicate> restrictions = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(search)) {
					Predicate fuzzyPredicate = null;
					try {
						fuzzyPredicate = builder.equal(root.get("createdDate"),
								DateUtils.parseDate(search, DateUtil.YYYY_MM_DD));
					} catch (ParseException e) {
						fuzzyPredicate = builder.like(root.get("name"), "%" + search + "%");
					}
					restrictions.add(fuzzyPredicate);
				}

				if (StringUtils.isNotEmpty(name)) {
					Predicate likePredicate = builder.like(root.get("name"), "%" + name + "%");
					restrictions.add(likePredicate);
				}

				// add role condition
				if (CollectionUtils.isNotEmpty(roles)) {
					Join<Menu, Role> roleJoin = root.join("roles", JoinType.LEFT); // left outer join users_roles
					Iterator<Role> iterator = roles.iterator();
					In<String> in = builder.in(roleJoin.get("name").as(String.class));
					while (iterator.hasNext()) {
						in.value(iterator.next().getName());
					}
					restrictions.add(in);
				}

				query.where(builder.and(restrictions.toArray(new Predicate[restrictions.size()])));
				return query.getGroupRestriction();
			}
		};
	}

	@Override
	public List<IMenu> query(IMenu view, boolean isTreeMode) {
		view = view != null ? view : new IMenu();
		Object detail = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if (detail instanceof User) {
			view.setRoles(((User) detail).getRoles());
		} else {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof String) {
				User user = userRep.findById(principal.toString())
						.orElseThrow(() -> new NullPointerException(principal.toString()));
				view.setRoles(user.getRoles());
			}
		}
		List<IMenu> menus = Lists.newArrayList();
		List<Menu> _menus = menuRep.findAll(getQueryClause(view), Sort.by(Direction.ASC, "pid", "queue"));
		if (isTreeMode) {
			Map<Integer, IMenu> mapOfMenu = Maps.newHashMap();
			_menus.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toSet()).stream().forEach(menu -> {
				IMenu imenu = BeanUtils.transform(menu, IMenu.class);
//				imenu.setDname(dictRep.findByCode(menu.getName()).orElse(null));
				imenu.setDname(GlobalVariable.cache.get(imenu.getName()));
				imenu.setDstatus(GlobalVariable.cache.get(imenu.getStatus().getDictCode()));
				mapOfMenu.put(imenu.getId(), imenu);
			});
			_menus.stream().filter(menu -> menu.getPid() != null).collect(Collectors.toSet()).stream().forEach(menu -> {
				IMenu imenu = BeanUtils.transform(menu, IMenu.class);
//				imenu.setDname(dictRep.findByCode(imenu.getName()).orElse(null));
				imenu.setDname(GlobalVariable.cache.get(imenu.getName()));
				imenu.setDstatus(GlobalVariable.cache.get(imenu.getStatus().getDictCode()));
				Menu pMenu = mapOfMenu.get(imenu.getPid());
				if (pMenu != null) {
					List<Menu> children = pMenu.getChildren();
					if (children == null) {
						children = Lists.newArrayList();
					}
					children.add(imenu);
					pMenu.setChildren(children);
				}
			});
			mapOfMenu.keySet().stream().forEach(key -> {
				IMenu imenu = mapOfMenu.get(key);
				Collections.sort(imenu.getChildren());
				menus.add(imenu);
				Collections.sort(menus);
			});
		} else {
			_menus.stream().forEach(menu -> {
				IMenu imenu = BeanUtils.transform(menu, IMenu.class);
//				imenu.setDname(dictRep.findByCode(imenu.getName()).orElse(null));
				imenu.setDname(GlobalVariable.cache.get(imenu.getName()));
				imenu.setDstatus(GlobalVariable.cache.get(imenu.getStatus().getDictCode()));
				menus.add(imenu);
			});
		}
		return menus;
	}

	@Override
	public IMenu saveOne(IMenu view) {
		return this.saveOne(BeanUtils.transform(view, Menu.class));
	}

	@Override
	public IMenu saveOne(Menu entity) {
		Assert.notNull(entity, "Menu must not be null");
		Assert.notNull(entity.getName(), "Menu name must not be null");
		String username = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof Principal) {
			username = ((Principal) principal).getName();
		} else {
			username = principal.toString();
		}
		entity.setModifier(username);
		Menu _entity = menuRep.findById(entity.getId()).orElse(null);
		if (_entity != null) {
			entity.setVersion(_entity.getVersion());
			entity.setRole(_entity.getRole());
		}
		Menu menu = menuRep.save(entity);
		return BeanUtils.transform(menu, IMenu.class);
	}

	@Override
	public Collection<Menu> save(Collection<Menu> entities) {
		List<Menu> result = new ArrayList<Menu>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void logicDelete(Integer id) {
		String username = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof Principal) {
			username = ((Principal) principal).getName();
		} else {
			username = principal.toString();
		}
		Menu menu = menuRep.findById(id).orElse(null);
		if (menu != null) {
			menu.setStatus(Status.Deleted);
			menu.setModifier(username);
			this.saveOne(menu);
		}
	}

	@Override
	public void logicDelete(Collection<Integer> ids) {
		ids.stream().forEach(id -> {
			this.logicDelete(id);
		});
	}

}
