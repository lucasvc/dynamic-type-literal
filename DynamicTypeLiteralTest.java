package com.github.lucasvc.cdi.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.enterprise.util.TypeLiteral;

import org.junit.Test;

@SuppressWarnings({ "serial", "rawtypes" })
public class DynamicTypeLiteralTest {

	@Test
	public void withGeneric() throws Exception {
		TypeLiteral<List<? extends Number>> listOfLongs = new DynamicTypeLiteral<List<? extends Number>>(Long.class) {
		};
		ParameterizedType paramType = (ParameterizedType) listOfLongs.getType();
		assertEquals(List.class, paramType.getRawType());
		assertEquals(Long.class, paramType.getActualTypeArguments()[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void withoutType() throws Exception {
		new DynamicTypeLiteral<List>(Long.class) {
		};
	}

	@Test(expected = IllegalArgumentException.class)
	public void withoutGeneric() throws Exception {
		new DynamicTypeLiteral(Long.class) {
		};
	}

	@Test(expected = NullPointerException.class)
	public void withNullType() throws Exception {
		new DynamicTypeLiteral<List<? extends Number>>(null) {
		};
	}

}
