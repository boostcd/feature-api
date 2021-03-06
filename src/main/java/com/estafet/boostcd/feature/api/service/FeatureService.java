package com.estafet.boostcd.feature.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.boostcd.commons.date.DateUtils;
import com.estafet.boostcd.feature.api.dao.CommitDAO;
import com.estafet.boostcd.feature.api.dao.EnvDAO;
import com.estafet.boostcd.feature.api.dao.FeatureDAO;
import com.estafet.boostcd.feature.api.dao.RepoDAO;
import com.estafet.boostcd.feature.api.model.Env;
import com.estafet.boostcd.feature.api.model.Feature;
import com.estafet.boostcd.feature.api.model.Repo;
import com.estafet.boostcd.feature.api.model.RepoCommit;
import com.estafet.openshift.boost.messages.features.FeatureMessage;

@Service
public class FeatureService {

	private static final Logger log = LoggerFactory.getLogger(FeatureService.class);

	@Autowired
	private EnvDAO envDAO;
	
	@Autowired
	private FeatureDAO featureDAO;

	@Autowired
	private RepoDAO repoDAO;

	@Autowired
	private CommitDAO commitDAO;

	@Transactional(readOnly = true)
	public List<Feature> getIncompleteFeatures() {
		return featureDAO.getIncompleteFeatures();
	}
	
	@Transactional
	public void processFeature(FeatureMessage message) {
		log.info(message.toJSON());
		Feature feature = featureDAO.getFeatureById(message.getFeatureId());
		if (feature == null) {
			feature = createFeature(message);
			featureDAO.create(feature);
		}
		Repo repo = repoDAO.getRepo(message.getRepo());
		if (!getRepoFeatures(message).contains(feature)) {
			RepoCommit commit = commitDAO.getCommit(repo.getName(), message.getCommitId());
			commit.setFeature(feature);
			commitDAO.updateRepoCommit(commit);
		} else {
			feature.update(createFeature(message));
			featureDAO.update(feature);
		}
		updateEnvs();
	}

	private void updateEnvs() {
		for (Env env : envDAO.getEnvs()) {
			env.setUpdatedDate(DateUtils.newDate());
			envDAO.updateEnv(env);
		}
	}
	
	private Set<Feature> getRepoFeatures(FeatureMessage message) {
		return new HashSet<Feature>(featureDAO.getFeaturesByRepo(message.getRepo(), message.getCommitId()));
	}

	private Feature createFeature(FeatureMessage message) {
		Feature feature = Feature.builder()
				.setUrl(message.getFeatureURL())
				.setDescription(message.getDescription())
				.setFeatureId(message.getFeatureId())
				.setStatus(message.getStatus().getValue())
				.setTitle(message.getTitle()).build();
		return feature;
	}

}
