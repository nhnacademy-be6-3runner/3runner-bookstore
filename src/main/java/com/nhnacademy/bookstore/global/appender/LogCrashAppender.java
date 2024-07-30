package com.nhnacademy.bookstore.global.appender;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

@Setter
@Slf4j
public class LogCrashAppender extends AppenderBase<ILoggingEvent> {

	private String url;

	@Override
	protected void append(ILoggingEvent iLoggingEvent) {
		final var restTemplate = new RestTemplate();
		final var request = createJSONObject(iLoggingEvent);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<JSONObject> entity = new HttpEntity<>(request, headers);
		restTemplate.postForEntity(url, entity, String.class);
	}

	private JSONObject createJSONObject(ILoggingEvent event) {
		JSONObject obj = new JSONObject();
		obj.put("projectName", "Xyx7DoyszcG66ULx");
		obj.put("projectVersion", "1.0.0");
		obj.put("logVersion", "v2");
		obj.put("body", event.getFormattedMessage());
		obj.put("logSource", "http");
		obj.put("logType", "nelo2-http");
		obj.put("host", "3runner-bookstore");

		return obj;
	}
}
