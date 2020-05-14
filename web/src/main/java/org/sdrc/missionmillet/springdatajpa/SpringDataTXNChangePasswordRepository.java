package org.sdrc.missionmillet.springdatajpa;

import org.sdrc.missionmillet.domain.TXNChangePassword;
import org.sdrc.missionmillet.repository.TXNChangePasswordRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataTXNChangePasswordRepository extends
		TXNChangePasswordRepository, Repository<TXNChangePassword, Integer> {

}
