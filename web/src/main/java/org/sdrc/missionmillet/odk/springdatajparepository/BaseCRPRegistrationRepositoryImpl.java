package org.sdrc.missionmillet.odk.springdatajparepository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Subham Ashish(subham@sdrc.co.in)
 *
 *Overriding EnityManager to take table name dynamically and excute query accordingly
 */
@Repository
@Transactional
public class BaseCRPRegistrationRepositoryImpl implements CustomCRPRegistrationRepository {

	@PersistenceContext(unitName="odkEntityManagerFactory")
	private EntityManager entityManager;

	@Override
	public List<Object[]> findAllRecords(String tableName) {

		String queryString = "select DISTRICT, BLOCK from "+tableName+" where _IS_COMPLETE=true";
		
		Query query = entityManager.createNativeQuery(queryString);
//		query.setParameter("date", date);
		
		return (List<Object[]>) query.getResultList();

	}

	
}
