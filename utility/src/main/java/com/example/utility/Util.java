package com.example.utility;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;

public class Util {

	private static final Logger LOGGER= LoggerFactory.getLogger(Util.class);
	
	public static String getMessageByLocale(String id, MessageSource messageSource) {
		String message;
		try {
			message = messageSource.getMessage(id, null, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException ex) {
			message = messageSource.getMessage(ErrorConstant.SERVICE_EXCEPTION, null, LocaleContextHolder.getLocale());
			LOGGER.debug("Exception message : {}", ex);
		}
		return message;
	}
	
	public static String getTrustStorePath(ClassPathResource classPathResource,String prefix) {
		File somethingFile = null;
		try {
			try(InputStream inputStream = classPathResource.getInputStream();) {
				somethingFile = File.createTempFile(prefix, ".p12");
				java.nio.file.Files.copy(inputStream, somethingFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Util| getTrustStorePath::{}",e);
		}
		return somethingFile.getPath();
	}
	
	private Util() {
		new IllegalArgumentException("Cannot Instantiate");
	}
}
