package com.github.lucasvc.cdi.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.util.TypeLiteral;

@SuppressWarnings("serial")
public abstract class DynamicTypeLiteral<T> extends TypeLiteral<T> {

	// se podria usar una implementacion mas "potente" como
	// org.jboss.weld.util.reflection.ParameterizedTypeImpl
	private class SimpleParameterizedType implements ParameterizedType {

		private Type raw;
		private Type[] parameterizeds;

		public SimpleParameterizedType(Type raw, Type parameterized) {
			this.raw = raw;
			if (parameterized != null) {
				this.parameterizeds = new Type[] { parameterized };
			}
		}

		@Override
		public Type getRawType() {
			return raw;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

		@Override
		public Type[] getActualTypeArguments() {
			return parameterizeds;
		}

	}

	public DynamicTypeLiteral(Type typeArgument) throws Exception {
		super();
		if (typeArgument == null)
			throw new NullPointerException("typeArgument");

		Type type = getClass().getGenericSuperclass();
		if (!(type instanceof ParameterizedType))
			throw new IllegalArgumentException(getClass() + " does not specify the raw type for argument " + typeArgument);

		Type fullRawType = ((ParameterizedType) type).getActualTypeArguments()[0];
		if (!(fullRawType instanceof ParameterizedType))
			throw new IllegalArgumentException(fullRawType + " does not specify any parameterized type");

		ParameterizedType parameterizedType = new SimpleParameterizedType(((ParameterizedType) fullRawType).getRawType(), typeArgument);
		setParentType(parameterizedType);
	}

	private void setParentType(Type changedType) throws Exception {
		Field actualField = getClass().getSuperclass().getSuperclass().getDeclaredField("actualType");
		actualField.setAccessible(true);
		actualField.set(this, changedType);
	}

}
