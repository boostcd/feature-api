package com.estafet.openshift.boost.console.api.feature.model;

public class EnvFeatureBuilder {

	private String deployedDate;

	private String migratedDate;

	private Feature feature;
	
	private Env env;

	public EnvFeatureBuilder setEnv(Env env) {
		this.env = env;
		return this;
	}

	public EnvFeatureBuilder setDeployedDate(String deployedDate) {
		this.deployedDate = deployedDate;
		return this;
	}

	public EnvFeatureBuilder setMigratedDate(String migratedDate) {
		this.migratedDate = migratedDate;
		return this;
	}

	public EnvFeatureBuilder setFeature(Feature feature) {
		this.feature = feature;
		return this;
	}
	
	public EnvFeature build() {
		EnvFeature envFeature = new EnvFeature();
		feature.addEnvFeature(envFeature);
		env.addEnvFeature(envFeature);
		envFeature.setDeployedDate(deployedDate);
		envFeature.setMigratedDate(migratedDate);
		return envFeature;
	}
	
}
