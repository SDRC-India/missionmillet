package org.sdrc.missionmillet.odk.springdatajparepository;

import java.util.List;

import org.sdrc.missionmillet.odk.domain.CRPRegistration;
import org.sdrc.missionmillet.odk.repository.CRPRegistrationRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SpringDataCRPRegistrationRepository extends CRPRegistrationRepository, Repository<CRPRegistration, Integer> {
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, QS_3 "
			+ "from crp_registration_05022018_v1_core", nativeQuery=true)
	public List<Object[]> getCRPRegistration();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, CENTER "
			+ "from custom_hiring_center_checklist_05022018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getHiringCenterChecklist();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DEMO_DISTRICT,"
			+ " DEMO_BLOCK, DEMO_GRAMPANCHAYAT, DEMO_VILLAGE, DEMO_MONITORING_DATE, DEMO_FARMER_NAME "
			+ "from mission33018_v1_core", nativeQuery=true)
	public List<Object[]> getFarmerRegistration();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, QS_1, QS_2 "
			+ "from custom_382018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getHiringCenterMemberRegistration();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, Q1 "
			+ "from custom_hiring_center_registration_05022018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getHiringCenterRegistration();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, NAME "
			+ "from entrepreneurship_registration_05022018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getEntrepreneurshipRegistration();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, QS_3 "
			+ "from field_visit_checklist_05022018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getFieldVisitChecklist();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, LEVEL, NAME1 "
			+ "from meeting_checklist_05022018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getMeetingChecklist();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, NAME1, MONTH "
			+ "from processing_checklist_07052018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getProcessingChecklist();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, FARMERNAME, SEASON "
			+ "from production_checklist_07052018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getProductionChecklist();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, SEED "
			+ "from seed_center_registration_05022018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getSeedCenterRegistration();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, CENTER "
			+ "from seed_pr382018_v1_core2 where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getSeedProductionInSeedCenterChecklist();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE "
			+ "from seed_producer_checklist_07052018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getSeedProducerChecklist();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, DISTRICT, "
			+ "BLOCK, GRAMPANCHAYAT, VILLAGE, MONITORING_DATE, Q1, Q2 "
			+ "from seed_producer_registration_05022018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getSeedProducerRegistration();
	
	@Override
	@Query(value="select _URI, _CREATOR_URI_USER, _SUBMISSION_DATE, DEVICEID, Q15_DISTRICT, "
			+ "Q15_BLOCK, Q15_GRAMPANCHAYAT, Q15_VILLAGE, MONITORING_DATE, COMPONENTS "
			+ "from training_checklist_26042018_v1_core where _IS_COMPLETE=false", nativeQuery=true)
	public List<Object[]> getTrainingChecklist();
	
}
