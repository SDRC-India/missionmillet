package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.XForm;

public interface XFormRepository {

	XForm findByXFormIdAndIsLiveTrue(String getxFormId);

	List<XForm> findAllByIsLiveTrue();

}
