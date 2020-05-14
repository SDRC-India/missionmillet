package org.sdrc.missionmillet.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bushe.swing.event.EventBus;
import org.opendatakit.briefcase.model.BriefcaseFormDefinition;
import org.opendatakit.briefcase.model.DocumentDescription;
import org.opendatakit.briefcase.model.FormStatus;
import org.opendatakit.briefcase.model.FormStatusEvent;
import org.opendatakit.briefcase.model.ServerConnectionInfo;
import org.opendatakit.briefcase.model.TerminationFuture;
import org.opendatakit.briefcase.util.AggregateUtils;
import org.opendatakit.briefcase.util.ServerUploader.SubmissionResponseAction;
import org.sdrc.missionmillet.domain.Program_XForm_Mapping;
import org.sdrc.missionmillet.domain.User_Program_XForm_Mapping;
import org.sdrc.missionmillet.domain.XForm;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.XFormModel;
import org.sdrc.missionmillet.repository.AreaRepository;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.sdrc.missionmillet.repository.User_Program_XForm_MappingRepository;
import org.sdrc.missionmillet.repository.XFormRepository;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.DomainToModelConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itextpdf.text.pdf.codec.Base64;

/**
 * This class is responsible for the submission of forms
 * 
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Sarita
 * @since version 1.0.0
 *
 */
@Service
public class SubmissionServiceImpl implements SubmissionService {

	@Autowired
	private XFormRepository xFormRepository;

	@Autowired
	private CollectUserRepository collectUserRepository;

	@Autowired
	private User_Program_XForm_MappingRepository user_Program_XForm_MappingRepository;

	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private DomainToModelConverter domainToModelConverter;

	@Autowired
	private ServletContext context;

	@Autowired
	private ResourceBundleMessageSource applicationMessageSource;

	private static final Logger logger = LoggerFactory.getLogger("LOGGER");

	/**
	 * This method will be responsible for returning all file names that are
	 * sent from the mobile app
	 * 
	 * @return files All file names that are sent from the mobile app
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 * @since version 1.0.0.0
	 */
	public List<String> allFilesFromMobileApp(String folderName) {

		List<String> files = new ArrayList<String>();
		File folder = new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
				+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files.add(listOfFiles[i].getName());
			}
		}
		return files;
	}

	private Integer validateUserFormMapping(XForm xForm, CollectUserModel collectUserModel) {
		// see whether the user has this particular form assigned or not
		List<User_Program_XForm_Mapping> User_Program_XForm_Mappings = user_Program_XForm_MappingRepository
				.findByUser(collectUserModel.getUsername());

		for (User_Program_XForm_Mapping user_Program_XForm_Mapping : User_Program_XForm_Mappings) {
			// get program with xForm mapping
			Program_XForm_Mapping program_XForm_Mapping = user_Program_XForm_Mapping.getProgram_XForm_Mapping();
			if (program_XForm_Mapping != null) {
				if (program_XForm_Mapping.getxForm().getFormId() == xForm.getFormId()) {

					return user_Program_XForm_Mapping.getUserProgramXFomrMappingId();
				}
			}
		}
		return null;

	}

	/**
	 * This method is responsible for extracting form id from the xml file.
	 * 
	 * @return formId
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 * @since version 1.0.0.0
	 */
	private Map<String, String> getFormIdFromXML(String folderName, String fileName) {
		Map<String, String> map = new HashMap<>(1);
		try {
			String formId = null;
			File inputFile = new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName
					+ "\\" + fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList2 = doc.getChildNodes();
			String xFormId = nList2.item(0).getNodeName();
			formId = nList2.item(0).getAttributes().item(0).getTextContent();
			map.put(formId, xFormId);
			return map;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error("Exception in getting form id from XML, Exception message : " , e );
			return null;
		}
	}

	/**
	 * This method will persist the record in the database
	 * 
	 * @param areaId
	 *            Area id for which we are submitting the data
	 * @param fileName
	 *            The submission file name
	 */

	/**
	 * This method is responsible for extracting form id from the xml file.
	 * 
	 * @return formId
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 * @since version 1.0.0.0
	 */


	public boolean upload(String folderName, String fileName, String formId, Map<String, List<File>> formFilesMap,
			ServerConnectionInfo serverConnectionInfo, DocumentDescription documentDescription,
			TerminationFuture terminationFuture, XFormModel xFormModel, CollectUserModel collectUserModel,
			int userProgramXFormMappingId, String xFormIdFromOdk) {
		try {

			URI uri = null;

			// validate the URI from ui otherwise exception will occur
			uri = AggregateUtils.testServerConnectionWithHeadRequest(serverConnectionInfo, "formUpload");

			File file = new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName
					+ "\\" + fileName);
			File dFile = new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME, null, null) + "\\" + formId
					+ "\\" + messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_DESTINATIONFILE_FOLDER_NAME, null, null)
					+ "\\" + formId + ".xml");

			BriefcaseFormDefinition lfd = null;
			File f = new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME, null, null) + "\\" + formId
					+ "\\" + messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_DESTINATIONFILE_FOLDER_NAME, null, null));
			lfd = new BriefcaseFormDefinition(f, dFile);

			FormStatus formToTransfer = new FormStatus(FormStatus.TransferType.UPLOAD, lfd);
			SubmissionResponseAction action = null;

			if (!file.exists()) {
				String msg = "Submission file not found: " + file.getAbsolutePath();
				formToTransfer.setStatusString(msg, false);
				EventBus.publish(new FormStatusEvent(formToTransfer));
			}

			boolean uploadSuccess = AggregateUtils.uploadFilesToServer(serverConnectionInfo, uri, "xml_submission_file",
					file, formFilesMap.get(fileName), documentDescription, action, terminationFuture, formToTransfer);

			return uploadSuccess;
		} catch (Exception e) {
			logger.error("Exception in uploading process, Exception message : " , e);
			return false;
		}
	}


	/**
	 * The following method will extract the media file names from the xml file
	 * and convert it into File then return them
	 * 
	 * @param xmlFileName2
	 * @return media files
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 * @since version 1.0.0.0
	 */
	private Map<String, List<File>> extractMediaFiles(String folderName, String xmlFileName) {
		List<File> files = new ArrayList<File>();
		Map<String, List<File>> formFilesMap = new HashMap<String, List<File>>();

		try {

			File inputFile = new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName
					+ "\\" + xmlFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			String xmlString = "";

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(inputFile);
				int content;
				while ((content = fis.read()) != -1) {
					// convert to char and display it
					xmlString += (char) content;
				}

			} catch (IOException e) {
				logger.error("Exception while creating file : ",e);
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException ex) {
					logger.error("Exception while creating file : ",ex);
				}
			}

			List<String> imgList = getImageList(xmlString);

			for (String imageFileName : imgList) {
				try {
					File imageFile = new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
							+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\"
							+ folderName + "\\" + imageFileName);
					if (imageFile.exists())
						files.add(imageFile);
				} catch (NullPointerException e) {
					logger.error("Exception while creating imageFile file : ",e);
				}
			}
			formFilesMap.put(xmlFileName, files);
			return formFilesMap;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error("Exception while creating imageFile file : ",e);
			return formFilesMap;
		}
	}

	/**
	 * This method will take xml file content as string and extract the jpg
	 * images and return as list of strings
	 * 
	 * @param xmlString
	 *            This parameter will bring the xml file content as string
	 * @return List of image file name
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 * @since version 1.0.0.0
	 */

	private List<String> getImageList(String xmlString) {
		List<String> imageFileNames = new ArrayList<String>();

		String array[] = xmlString.split(".jpg<");

		for (int i = 0; i < array.length - 1; i++) {
			String imgFile = null;
			for (int j = array[i].length() - 1; j >= 0; j--) {
				if (array[i].charAt(j) == '>') {
					imgFile = array[i].substring(j + 1) + ".jpg";
					break;
				}
			}
			imageFileNames.add(imgFile);
		}
		return imageFileNames;
	}

	@Override
	public int uploadForm(HttpServletRequest request, String deviceID, CollectUserModel collectUserModel) {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat(
					messageSourceNotification.getMessage(Constants.Odk.SUBMISSION_CREATE_FOLDER, null, null));
			String folderName = collectUserModel.getUserId().toString() + "_" + sdf.format(new Date());
			

			if ((new File(messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "/"
					+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "/"
					+ folderName)).mkdir()) {
				logger.info("Folder created");
				// cast request
			    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			    // You can get your file from request
			    CommonsMultipartFile multipartFile =  null; // multipart file class depends on which class you use assuming you are using org.springframework.web.multipart.commons.CommonsMultipartFile

			    Iterator<String> iterator = multipartRequest.getFileNames();

			    while (iterator.hasNext()) {
			        String key = (String) iterator.next();
			        // create multipartFile array if you upload multiple files
			        multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);			        
			        String path = (messageSourceNotification.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "/"
							+ messageSourceNotification.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "/"
							+ folderName).toString();	

			        String originalFilename = multipartFile.getOriginalFilename();
			        
			        File convFile = new File(path, originalFilename.contains(".xml")?
			        		(originalFilename.split("_")[1]+"_"+originalFilename.split("_")[2]):
			        		originalFilename);
			        multipartFile.transferTo(convFile);
			        
			    }
			    
				// Set respective files in respective places
				List<String> xmlFileNames = new ArrayList<>();
				List<String> mediaFileNames = new ArrayList<>();

				for (String fileName : allFilesFromMobileApp(folderName)) {
					if (fileName.substring(fileName.length() - 4, fileName.length()).equals(".xml"))
						xmlFileNames.add(fileName);
					else
						mediaFileNames.add(fileName);
				}

				// Do the upload work
				for (String fileName : xmlFileNames) {
					Map<String, String> map = getFormIdFromXML(folderName, fileName);

					if (map != null) {

						String formId = null;
						String xFormIdFromODK = null;

						for (Map.Entry<String, String> entry : map.entrySet()) {
							formId = entry.getKey();
							xFormIdFromODK = entry.getValue();
							break;
						}

						if (formId != null || xFormIdFromODK != null) {

							XForm xForm = xFormRepository.findByXFormIdAndIsLiveTrue(formId);
							XFormModel xFormModel = domainToModelConverter.toXFormModel(xForm);
							if (xForm != null) {
								
								String decodedPasswordString = new String (Base64.decode(xForm.getPassword()));//xForm.getPassword() can not be null
								//because the password field in xForm table will not allow null value entry
								TerminationFuture terminationFuture = new TerminationFuture();
								DocumentDescription description = new DocumentDescription(
										"Submission upload failed.  Detailed error: ", "Submission upload failed.",
										"submission (" + 0 + " of " + 1 + ")", terminationFuture);
								ServerConnectionInfo serverConnectionInfo = new ServerConnectionInfo(
										xForm.getOdkServerURL() + "submission?deviceID=" + deviceID,
										collectUserModel.getUsername(), decodedPasswordString.toCharArray());

								Map<String, List<File>> formFilesMap = extractMediaFiles(folderName, fileName);
								Integer upxm_id = validateUserFormMapping(xForm, collectUserModel);
								if (upxm_id != null) {
									if (upload(folderName, fileName, formId, formFilesMap, serverConnectionInfo,
											description, terminationFuture, xFormModel, collectUserModel, upxm_id,
											xFormIdFromODK)) {
										logger.info(
												"Submission success for filename : " + folderName + "\\" + fileName);
										return 0;
									} else {
										logger.error(
												"Form upload failure for filename : " + folderName + "\\" + fileName);
										return 500;
									}
								} else {
									logger.warn(
											"Submission failure, unassigned form : " + folderName + "\\" + fileName);
									return 406;
								}
							} else {
								logger.warn("No xForm found in the database in this file name : " + folderName + "\\"
										+ fileName);
								return 500;
							}
						} else {
							logger.warn("Could not extract form id from this file : " + folderName + "\\" + fileName);
							return 500;
						}

					} else {
						logger.warn("Could not extract form id from this file : " + folderName + "\\" + fileName);
						return 500;
					}
				}

				return 0;

			} else {
				
				logger.error("Could not create folder name " + folderName);
				return 500;
			}

		} catch (Exception e) {
			logger.error("Exception while creating folder : ",e);
			return 500;
		}

	}

	

}