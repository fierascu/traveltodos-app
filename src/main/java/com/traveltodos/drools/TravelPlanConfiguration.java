package com.traveltodos.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class TravelPlanConfiguration {

	private final Environment env;

	private KieSession kieSession;

	@Autowired
	public TravelPlanConfiguration(Environment env) {
		this.env = env;
		this.kieSession = createKieSession();
	}

	public KieSession getKieSession() {
		return kieSession;
	}

	private KieSession createKieSession() {

		String rulesxlsFilename = env.getProperty("traveltodos.rulesxls-filename");

		KieServices kieServices = KieServices.Factory.get();
		Resource dt = ResourceFactory.newClassPathResource(rulesxlsFilename, getClass());

		KieFileSystem kieFileSystem = kieServices.newKieFileSystem().write(dt);

		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();

		KieRepository kieRepository = kieServices.getRepository();

		ReleaseId krDefaultReleaseId = kieRepository.getDefaultReleaseId();
		KieContainer kieContainer = kieServices.newKieContainer(krDefaultReleaseId);

		return kieContainer.newKieSession();
	}

}
