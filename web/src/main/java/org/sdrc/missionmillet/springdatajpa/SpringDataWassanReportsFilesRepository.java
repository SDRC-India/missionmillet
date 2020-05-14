package org.sdrc.missionmillet.springdatajpa;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 * 	
 */

import java.util.List;

import org.sdrc.missionmillet.domain.WASSANReportsFile;
import org.sdrc.missionmillet.repository.WassanReportsFileRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass = WASSANReportsFile.class, idClass = Integer.class)
public interface SpringDataWassanReportsFilesRepository extends
		WassanReportsFileRepository {
	
	/**
	 * @Description This query fetches the record whose reportId and TypeId matches with the parameter,
	 * 
	 * @param reportId
	 * @param typeId
	 */
	@Override
	@Query(value = "select file from WASSANReportsFile file where wassanReports.reportsId =:reportId and status.typeDetailId=:typeId")
	WASSANReportsFile getFile(@Param("reportId") Integer reportId,@Param("typeId") Integer typeId);
	
	

	/**
	 * @Description  This query fetches the record whose WASSANSoEReportsId and userId matches with the parameter,
	 * 
	 * @param id
	 * @param userid
	 */
	@Override
	@Query(value = "select file from WASSANReportsFile file where wassanSoeReport.WASSANSoEReportsId=:id and collectUser.userId=:userid")
	WASSANReportsFile findFileByWassanSoeReportId(@Param("id") Integer id, @Param("userid") Integer userid);
	
	
	/**
	 * @Description This query  executes to find last updated record present 
	 * 
	 * @param id
	 */
	@Override
	@Query(value = "select file from WASSANReportsFile file where wassanSoeReport.WASSANSoEReportsId=:id")
	WASSANReportsFile findBywassanSoeId(@Param("id") Integer id);

	/**
	 * @Description This query executes to find all the records whose wassanReportsId matches with the passed 
	 * parameter.
	 * 
	 * @param reportIdList
	 */
	
	@Override
	@Query(value = "select file from WASSANReportsFile file where file.wassanReports.reportsId IN (:reportIdList)")
	List<WASSANReportsFile> findByReportId(@Param("reportIdList") List<Integer> reportIdList);
	
}
