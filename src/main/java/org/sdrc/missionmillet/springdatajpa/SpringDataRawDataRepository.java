package org.sdrc.missionmillet.springdatajpa;

import java.util.List;

import org.sdrc.missionmillet.domain.RawData;
import org.sdrc.missionmillet.repository.RawDataRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataRawDataRepository extends Repository<RawData, Integer>, RawDataRepository {

	@Override
	@Query(value="SELECT rd.* "
			+ "FROM raw_data_soe rd WHERE rd.timeperiod_id_fk=:monthId ORDER BY rd.ngo_id_fk", nativeQuery=true)
	public List<RawData> getMonthlyData(@Param("monthId") int monthId);
	
	@Override
	@Query(value="select sum(rd.finance) as finance from raw_data_soe rd where rd.timeperiod_id_fk=:monthId", nativeQuery=true)
	public double getAllNgoMonthlyData(@Param("monthId")int monthId);
	
	@Override
	@Query(value="select rd.ngo_id_fk, sum(finance) as finance "
			+ "from raw_data_soe rd, time_period tp where rd.timeperiod_id_fk = tp.timeperiod_id_pk "
			+ "and tp.timeperiod_id_pk in(select tp.timeperiod_id_pk from time_period tp "
			+ "where tp.periodicity=:period  and tp.start_date >= (select tp.start_date from time_period tp "
			+ "where tp.timeperiod_id_pk=:monthId )\\:\\:date and (tp.end_date <= (select tp.end_date "
			+ "from time_period tp where tp.timeperiod_id_pk=:monthId)\\:\\:date)) group by rd.ngo_id_fk order by rd.ngo_id_fk", nativeQuery=true)
	public List<Object[]> getRawData(@Param("monthId") int monthId, @Param("period")String period);
	
	@Override
	@Query(value="select  sum(finance) as finance from raw_data_soe rd, time_period tp "
			+ "where rd.timeperiod_id_fk = tp.timeperiod_id_pk and tp.timeperiod_id_pk in(select tp.timeperiod_id_pk "
			+ "from time_period tp where  tp.periodicity=:period and tp.start_date >= (select tp.start_date "
			+ "from time_period tp where tp.timeperiod_id_pk=:monthId )\\:\\:date and (tp.end_date <= "
			+ "(select tp.end_date from time_period tp where tp.timeperiod_id_pk=:monthId)\\:\\:date))", nativeQuery=true)
	public double getAllNgoData(@Param("monthId") int monthId, @Param("period")String period);
}
