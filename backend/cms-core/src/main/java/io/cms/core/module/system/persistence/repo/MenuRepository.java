package io.cms.core.module.system.persistence.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import io.cms.core.module.system.persistence.entity.Menu;

public interface MenuRepository extends CrudRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {

	/**
	 * 查询 pid 下所有子菜单
	 * 
	 * @param pid
	 * @return
	 */
	Set<Menu> findByPid(Integer pid);
}
