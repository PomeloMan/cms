package io.cms.core.module.system.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.cms.core.module.system.persistence.entity.Authority;
import io.cms.core.module.system.persistence.entity.Dictionary;
import io.cms.core.module.system.persistence.repo.DictRepository;
import io.cms.core.module.system.service.interfaces.IDictService;
import io.cms.core.module.system.vo.IDictionary;
import io.cms.util.BeanUtils;
import io.cms.util.commons.DateUtil;

@Service
public class DictService implements IDictService {

	private final Log logger = LogFactory.getLog(DictService.class);

	@Autowired
	DictRepository dictRep;

	private Specification<Dictionary> getQueryClause(IDictionary view) {
		return new Specification<Dictionary>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Dictionary> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug(view);
					}
					return null;
				}

				String search = view.getSearch();

				String name = null;
				Authority authority = BeanUtils.transform(view, Authority.class);
				if (authority != null) {
					name = authority.getName();
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

				query.where(builder.and(restrictions.toArray(new Predicate[restrictions.size()])));
				return query.getGroupRestriction();
			}
		};
	}

	@Override
	public Collection<Dictionary> query(IDictionary view) {
		return dictRep.findAll(getQueryClause(view));
	}
}
