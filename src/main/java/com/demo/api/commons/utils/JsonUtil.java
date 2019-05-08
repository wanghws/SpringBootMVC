package com.demo.api.commons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by wanghw on 2019-03-11.
 */
public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper()
			.registerModule(new ParameterNamesModule())
			.registerModule(new Jdk8Module())
			.registerModule(new JavaTimeModule());
	
	public static String writeValueAsString(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static <T> T readValue(String src, Class<T> t) {
		try {
			return mapper.readValue(src, t);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Collection<String> writeValueAsStringList(List list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		Collection<String> stringList = Lists.newArrayList();
		for (Object object : list) {
			String objectStr = writeValueAsString(object);
			if (StringUtils.isBlank(objectStr)) {
				continue;
			}
			stringList.add(objectStr);
		}
		return stringList;
	}

	public static <T> List<T> readValues(Collection<String> stringList, Class<T> t) {
		if (stringList == null || stringList.isEmpty()) {
			return null;
		}
		List<T> objList = Lists.newArrayList();
		for (String strVal : stringList) {
			T object = readValue(strVal, t);
			if (object == null) {
				continue;
			}
			objList.add(object);
		}
		return objList;
	}
}
