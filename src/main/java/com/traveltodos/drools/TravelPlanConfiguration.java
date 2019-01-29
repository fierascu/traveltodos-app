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
import com.traveltodos.drools.TravelPlan;

public class TravelPlanConfiguration {

	private KieSession kieSession;

	public TravelPlanConfiguration(String fileName) {
		this.kieSession = createKieSession(fileName);
	}

	public KieSession createKieSession(String rulesxlsFilename) {
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

	public String getWantedValue(String type) {
		TravelPlan plan = new TravelPlan(type);
		kieSession.insert(plan);
		kieSession.fireAllRules();
		return plan.getWanted();
	}
}
