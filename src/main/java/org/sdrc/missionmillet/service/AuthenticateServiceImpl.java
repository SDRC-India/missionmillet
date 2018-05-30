package org.sdrc.missionmillet.service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.Program;
import org.sdrc.missionmillet.domain.User_Program_XForm_Mapping;
import org.sdrc.missionmillet.domain.XForm;
import org.sdrc.missionmillet.model.FormsToDownloadMediafiles;
import org.sdrc.missionmillet.model.MediaFilesToUpdate;
import org.sdrc.missionmillet.model.ModelToCollectApplication;
import org.sdrc.missionmillet.model.ProgramModel;
import org.sdrc.missionmillet.model.ProgramXFormsModel;
import org.sdrc.missionmillet.model.XFormModel;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.sdrc.missionmillet.repository.User_Program_XForm_MappingRepository;
import org.sdrc.missionmillet.repository.XFormRepository;
import org.sdrc.missionmillet.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Azar
 * 
 */
@Service
public class AuthenticateServiceImpl implements AuthenticateService {

	private static final Logger logger = LoggerFactory.getLogger("LOGGER");

	@Autowired
	private User_Program_XForm_MappingRepository user_Program_XForm_MappingRepository;

	@Autowired
	private CollectUserRepository collectUserRepository;
	@Autowired
	private XFormRepository xFormRepository;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	@Override
	public List<ProgramXFormsModel> getProgramWithXFormsList(
			String username, String password) {

		// the user is present in the database or not. if present, live or not
		CollectUser user = collectUserRepository.findByUsernameAndPassword(
				username, password);
		if (user != null) {

			// this variable will return
			List<ProgramXFormsModel> programWithXFormsList = new ArrayList<ProgramXFormsModel>();

			// fetching user-program-xForm mappings
			for (User_Program_XForm_Mapping user_Program_XForm_Mapping : user_Program_XForm_MappingRepository
					.findByUser(username)) {

				List<XFormModel> xFormModels = new ArrayList<XFormModel>();

				/*
				 * Checking whether the program and it's xForms are present or
				 * not in the programWithXFormsList. If present, get the XForm
				 * list, add one, remove the list and restore the latest list If
				 * not present add new program and xForm list
				 */
				boolean programPresent = false;
				for (ProgramXFormsModel programXFormsModel : programWithXFormsList) {
					if (programXFormsModel.getProgramModel().getProgramId() == user_Program_XForm_Mapping
							.getProgram_XForm_Mapping().getProgram()
							.getProgramId()) {
						programPresent = true;
						// get the XForm list
						xFormModels = programXFormsModel.getxFormsModel();
					}
				}

				if (programPresent) {
					// program and it's xForms are present in the
					// programWithXFormsList
					XForm xForm = user_Program_XForm_Mapping
							.getProgram_XForm_Mapping().getxForm();

					XFormModel xFormModel = new XFormModel();

					xFormModel.setxFormId(xForm.getxFormId().trim());
					xFormModel.setOdkServerURL(xForm.getOdkServerURL().trim());
					xFormModel.setUsername(xForm.getUsername().trim());
					xFormModel.setPassword(xForm.getPassword().trim());

					// add one
					xFormModels.add(xFormModel);

					// remove the list
					ProgramModel programModel = null;
					for (int i = 0; i < programWithXFormsList.size(); i++) {
						if (programWithXFormsList.get(i).getProgramModel()
								.getProgramId() == user_Program_XForm_Mapping
								.getProgram_XForm_Mapping().getProgram()
								.getProgramId()) {
							programModel = programWithXFormsList.get(i)
									.getProgramModel();
							programWithXFormsList.remove(i);
						}
					}

					// restore the latest list
					ProgramXFormsModel programWithXFormsModelChild = new ProgramXFormsModel();

					programWithXFormsModelChild.setProgramModel(programModel);
					programWithXFormsModelChild.setxFormsModel(xFormModels);

					programWithXFormsList.add(programWithXFormsModelChild);

				} else {
					ProgramXFormsModel programWithXFormsModelChild = new ProgramXFormsModel();

					Program program = user_Program_XForm_Mapping
							.getProgram_XForm_Mapping().getProgram();

					ProgramModel programModel = new ProgramModel();
					programModel.setProgramId(program.getProgramId());
					programModel.setProgramName(program.getProgramName());
					programWithXFormsModelChild.setProgramModel(programModel);

					XForm xForm = user_Program_XForm_Mapping
							.getProgram_XForm_Mapping().getxForm();

					XFormModel xFormModel = new XFormModel();

					xFormModel.setxFormId(xForm.getxFormId().trim());
					xFormModel.setOdkServerURL(xForm.getOdkServerURL().trim());
					xFormModel.setUsername(xForm.getUsername().trim());
					xFormModel.setPassword(xForm.getPassword().trim());
					xFormModels.add(xFormModel);

					programWithXFormsModelChild.setxFormsModel(xFormModels);

					programWithXFormsList.add(programWithXFormsModelChild);
				}
			}
			logger.info("Data sent for username : " + username);
			return programWithXFormsList;
		} else {
			logger.warn("Username : " + username + " authentication failed!");
			return null;
		}
	}

	/**
	 * This method will get all the programXFormModelList and MediaFilesToUpdateList.
	 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
	 * @since version 1.0.0.0
	 *
	 */
	@Override
	public ModelToCollectApplication getModelToCollectApplication(
			List<FormsToDownloadMediafiles> list,String username,String password) {
		ModelToCollectApplication modelToCollectApplication = new ModelToCollectApplication();
		if(getProgramWithXFormsList(username,password)!=null){
			modelToCollectApplication.setProgramXFormModelList(getProgramWithXFormsList(username,password));
		}else{
			return null;
		}
		if(getMediaFilesToUpdate(list)!=null){
			modelToCollectApplication.setListOfMediaFilesToUpdate(getMediaFilesToUpdate(list));
		}else{
			return null;
		}
		return modelToCollectApplication;
	}
	/**
	 * This method will get all the MediaFiles To UpdateList.
	 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
	 * @since version 1.0.0.0
	 *
	 */
	@Override
	public List<MediaFilesToUpdate> getMediaFilesToUpdate(
			List<FormsToDownloadMediafiles> formToDownloadMediaList) {
		List<MediaFilesToUpdate> mediaFilesToUpdatesList = new ArrayList<MediaFilesToUpdate>();
		List<XForm> xForms = xFormRepository.findAllByIsLiveTrue();
		
		formToDownloadMediaList.forEach(formFromMobile->{
			MediaFilesToUpdate mediaFilesToUpdate = new MediaFilesToUpdate();
			XForm xForm = getValidatedXForm(xForms, formFromMobile);
			if(xForm!=null){
				mediaFilesToUpdate.setxFormId(xForm.getxFormId());
				Path path = Paths.get(xForm.getMediaPath());
				byte[] data;
				try {
					data = Files.readAllBytes(path);
					String encodedString = org.apache.commons.codec.binary.Base64.encodeBase64String(data);
					mediaFilesToUpdate.setMediaFile(encodedString);
				} catch (Exception e) {
					logger.error("Media file not present in the specified path");					
				}
				
			}else{				
				mediaFilesToUpdate.setxFormId(formFromMobile.getFormId());
				mediaFilesToUpdate.setMediaFile(null);
			}
			mediaFilesToUpdatesList.add(mediaFilesToUpdate);
		});
		return mediaFilesToUpdatesList;
	}

	/**
	 * This method is used to filter all the xForms whose updated date is greater than the date of all downloaded forms coming from mobile
	 * @author subhadarshani patra<subhadarshani@sdrc.co.in>
	 * @since version 1.0.0
	 * 
	 * 
	 */
	
	private XForm getValidatedXForm(List<XForm> xForms, FormsToDownloadMediafiles formFromMobile) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(messageSourceNotification.getMessage(Constants.Odk.MEDIA_FILE_UPDATED_DATE, null, null));
		
		String xFormUpdatedDate = null;
		
		for(XForm form : xForms){
			Date date = new Date();
			if(form.getUpdatedDate()!=null){
				date.setTime(form.getUpdatedDate().getTime());
				 xFormUpdatedDate = new SimpleDateFormat(messageSourceNotification.getMessage(Constants.Odk.MEDIA_FILE_UPDATED_DATE, null, null)).format(date);
				 try {
						if(formFromMobile.getFormId().equals(form.getxFormId())
								&& sdf.parse(formFromMobile.getDownloadOrUpdateDate()).before(sdf.parse(xFormUpdatedDate))
								&& form.getMediaPath() != null){
							return form;
						}
					} catch (ParseException e) {
						return null;
					}
			}		
		}
		return null;
	}
	
}