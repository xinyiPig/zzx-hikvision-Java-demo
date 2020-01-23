package com.example.hkws.util.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * 对象序列化
 * 
 * @param <T>
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private Class<T> clazz;

	public FastJson2JsonRedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		String str = new String(bytes, DEFAULT_CHARSET);

		return (T) JSON.parseObject(str, clazz);
	}

	@SuppressWarnings("unused")
	private boolean isEmpty(byte[] bytes) {
		return (bytes == null || bytes.length == 0);
	}
}
