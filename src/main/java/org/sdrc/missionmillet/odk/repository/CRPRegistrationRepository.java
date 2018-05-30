package org.sdrc.missionmillet.odk.repository;

import java.util.List;


public interface CRPRegistrationRepository {

	List<Object[]> getCRPRegistration();

	List<Object[]> getHiringCenterChecklist();

	List<Object[]> getFarmerRegistration();

	List<Object[]> getHiringCenterMemberRegistration();

	List<Object[]> getHiringCenterRegistration();

	List<Object[]> getEntrepreneurshipRegistration();

	List<Object[]> getFieldVisitChecklist();

	List<Object[]> getMeetingChecklist();

	List<Object[]> getProcessingChecklist();

	List<Object[]> getProductionChecklist();

	List<Object[]> getSeedCenterRegistration();

	List<Object[]> getSeedProductionInSeedCenterChecklist();

	List<Object[]> getSeedProducerChecklist();

	List<Object[]> getSeedProducerRegistration();

	List<Object[]> getTrainingChecklist();

}
