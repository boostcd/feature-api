package com.estafet.openshift.boost.console.api.feature.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.openshift.boost.console.api.feature.message.EnvFeatureMessage;
import com.estafet.openshift.boost.console.api.feature.service.FeatureService;

import io.opentracing.Tracer;

@Component
public class NewEnvironmentFeatureConsumer {

	public final static String TOPIC = "new.environment.feature.topic";

	@Autowired
	private Tracer tracer;
	
	@Autowired
	private FeatureService featureService;

	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message) {
		try {
			EnvFeatureMessage envFeatureMessage = EnvFeatureMessage.fromJSON(message);
			featureService.processEnvFeature(envFeatureMessage);
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();
			}
		}
	}
	
}
