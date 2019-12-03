package io.cms.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class BeanUtils<S, T> {

	public static <S, T> T transform(S source, Class<T> targetClass, String... ignoreProperties) {
		T target = null;
		try {
			target = targetClass.newInstance();
			org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return target;
	}

	public static <S, T> Collection<T> transform(Collection<S> sources, Class<T> targetClass,
			String... ignoreProperties) {
		return sources.stream().map(source -> {
			return transform(source, targetClass, ignoreProperties);
		}).collect(Collectors.toList());
	}
}
