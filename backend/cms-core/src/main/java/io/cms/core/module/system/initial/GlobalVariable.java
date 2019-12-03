package io.cms.core.module.system.initial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import io.cms.core.module.system.persistence.entity.Dictionary;
import io.cms.core.module.system.service.DictService;

@Configuration
public class GlobalVariable {

	public static Map<String, Dictionary> cache = new HashMap<>();

	@Autowired
	DictService dictService;

	@PostConstruct()
	public void init() {
		List<Dictionary> dicts = Lists.newArrayList(dictService.query(null));
		dicts.stream().forEach(dict -> {
			cache.put(dict.getCode(), dict);
		});
	}
}
