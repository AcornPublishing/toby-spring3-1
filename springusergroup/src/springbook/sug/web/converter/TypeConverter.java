package springbook.sug.web.converter;

import org.springframework.core.convert.converter.Converter;

import springbook.sug.domain.Type;

public class TypeConverter {
	private TypeConverter() {}

	public static class TypeToString implements Converter<Type, String> {
		public String convert(Type type) {
			return (type == null) ? "" : String.valueOf(type.getValue());
		}
	}
	
	public static class StringToType implements Converter<String, Type> {
		public Type convert(String text) {
			return Type.valueOf(Integer.valueOf(text));
		}
	}
}
