package io.cms.core.module.system.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import io.cms.core.module.system.persistence.entity.Dictionary;

public interface DictRepository extends CrudRepository<Dictionary, Long>, JpaSpecificationExecutor<Dictionary> {

	Optional<Dictionary> findByCode(String name);
}
