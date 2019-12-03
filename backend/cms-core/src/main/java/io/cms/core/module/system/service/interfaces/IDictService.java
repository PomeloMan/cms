package io.cms.core.module.system.service.interfaces;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import io.cms.core.module.system.persistence.entity.Dictionary;
import io.cms.core.module.system.vo.IDictionary;

/**
 * @ClassName IDictService.java
 * @Description TODO
 * @author PomeloMan
 */
@Transactional(readOnly = true)
public interface IDictService {

	/**
	 * @param view
	 * @return
	 */
	Collection<Dictionary> query(IDictionary view);
}
