package org.sdrc.missionmillet.odk.springdatajparepository;

import org.sdrc.missionmillet.odk.domain.CRPRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Subham Ashish(subham@sdrc.co.in)
 * 
 * 	This is added due to limited future of spring data jpa
 */
public interface BaseCRPRegistrationRepository extends JpaRepository<CRPRegistration, Integer>,CustomCRPRegistrationRepository {

}
