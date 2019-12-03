package io.cms.core.module.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.cms.core.module.log.persistence.entity.Log;
import io.cms.core.module.log.persistence.repo.LogRepository;
import io.cms.core.module.log.service.interfaces.ILogService;

@Service
public class LogService implements ILogService {

	@Autowired
	private LogRepository logRepo;

	@Override
	public void save(Log entity) {
		Assert.notNull(entity, "[Assertion failed] - log entity must not be null");
		logRepo.save(entity);
	}
}
