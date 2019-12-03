package io.cms.core.module.system.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import io.cms.core.module.system.persistence.entity.Role;

public interface RoleRepository extends CrudRepository<Role, String>, JpaSpecificationExecutor<Role> {

}
