package org.sdrc.missionmillet.service;

import java.util.List;

import org.sdrc.missionmillet.model.FormsToDownloadMediafiles;
import org.sdrc.missionmillet.model.MediaFilesToUpdate;
import org.sdrc.missionmillet.model.ModelToCollectApplication;
import org.sdrc.missionmillet.model.ProgramXFormsModel;

/**
 * 
 * @author Azar
 * 
 */
public interface AuthenticateService {

	List<ProgramXFormsModel> getProgramWithXFormsList(String username, String password);
	
	ModelToCollectApplication getModelToCollectApplication(List<FormsToDownloadMediafiles> list,String username,String password);
	
	List<MediaFilesToUpdate> getMediaFilesToUpdate(List<FormsToDownloadMediafiles> list);
	
}
