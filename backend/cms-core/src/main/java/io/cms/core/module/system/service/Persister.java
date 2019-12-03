package io.cms.core.module.system.service;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.cms.core.module.system.service.interfaces.IPersister;

@Service("persister")
public class Persister implements IPersister {

	@Value("${persister.cache}")
	private int cache;

	@PersistenceContext
	protected EntityManager em;

	@Override
	public void merge(Collection<?> col) {
		int i = 0;
		for (Object entity : col) {
			em.merge(entity);
			if (i != 0 && i % cache == 0) {
				em.flush();
				em.clear();
			}
			i++;
		}
	}

	@Override
	public void persist(Collection<?> col) {
		int i = 0;
		for (Object entity : col) {
			em.persist(entity);
			if (i != 0 && i % cache == 0) {
				em.flush();
				em.clear();
			}
			i++;
		}
	}

	public int getCache() {
		return cache;
	}

	public void setCache(int cache) {
		this.cache = cache;
	}

}
