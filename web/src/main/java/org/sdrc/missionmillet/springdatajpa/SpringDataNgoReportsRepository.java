package org.sdrc.missionmillet.springdatajpa;

import java.util.List;

import org.sdrc.missionmillet.domain.NgoReports;
import org.sdrc.missionmillet.repository.NgoReportsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataNgoReportsRepository extends NgoReportsRepository,
		JpaRepository<NgoReports, Integer> {

	/**
	 * @Description fetching all the ngo details,NgoReports details and area details
	 */
	@Override
	@Query(value = "select nr,n,a from NgoReports nr LEFT JOIN nr.ngo as n LEFT JOIN n.districtId as a where nr.isLive=true ORDER BY a.areaName,n.name,nr.month,nr.createdDate asc")
	List<Object[]> findByIsLiveTrueOrderByDistNameNgoNameYearAndMinCreatedDate();
	
}
