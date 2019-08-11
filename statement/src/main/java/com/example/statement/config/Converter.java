package com.example.statement.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.stereotype.Component;

import com.example.utility.EncryptUtil;
import com.example.utility.model.Encrypted;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Converter extends AbstractHttpMessageConverter<Object> {

	@Autowired
	private ObjectMapper objectMapper;

	public Converter(){
		super(MediaType.APPLICATION_JSON_UTF8,
				new MediaType("application", "*+json", StandardCharsets.UTF_8));
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException {
		return objectMapper.readValue(decrypt(inputMessage.getBody()),clazz);
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException {
		outputMessage.getBody().write(encrypt(objectMapper.writeValueAsString(o)));
	}

	private String decrypt(InputStream inputStream) throws IOException{
		Encrypted encrypted=objectMapper.readValue(inputStream,Encrypted.class);
		return EncryptUtil.decrypt(encrypted.getEncryptedValue());
	}

	private byte[] encrypt(String stringToEncrypt) throws JsonProcessingException{
		Encrypted encrypt = new Encrypted(EncryptUtil.encrypt(stringToEncrypt));
		return objectMapper.writeValueAsBytes(encrypt);
	}
}