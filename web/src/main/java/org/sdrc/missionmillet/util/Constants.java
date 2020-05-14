package org.sdrc.missionmillet.util;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Subrata
 * 
 */
@Component
public class Constants {
	public static final String REFERER = "referer";
	public static final String REDIRECT = "redirect:";
	public static final String ERROR_LIST = "errorList";

	public static final String AUTHENTICATION_USERID = "authentication.userid";
	public static final String AUTHENTICATION_PASSWORD = "authentication.password";

	public static final String WEB_MAINTENANCE = "site.under.maintenance";
	// status
	public static final String TEMPLATE_APPROVAL_STATUS = "Approved";

	// excel path
	public static final String EXCEL_READ_PATH = "excel.read.filepath";
	public static final String EXCEL_STORE_PATH = "excel.store.filepath";
	public static final String EXCEL_CREATE_PROJECT_SHEET = "excel.create.project.sheet";
	public static final String EXCEL_UNIQUE_KEY_SHEET = "excel.unique.key.sheet";
	public static final String EXCEL_UPLOAD_FILEPATH = "excel.upload.filepath";
	public static final String OPERATIONAL_EXCEL_READ_PATH = "operational.excel.read.filepath";

	public static final String USER_PRINCIPAL = "UserPrincipal";
	public static final String INVALID_CREDENTIALS = "invalid.credentials";

	public static final String INVALID_USERNAME_PASSWORD = "invalid.username.password";
	public static final String SUCCESS_LOGGED_OUT = "success.logged.out";
	
    public static final  String SMTP_HOST_KEY ="smtp.host.key";
    public static final  String SOCKETFACTORY_PORT_KEY ="socketFactory.port.key";
    public static final  String SOCKETFACTORY_CLASS_KEY ="socketFactory.class.key";
    public static final  String SMTP_AUTH_KEY ="smtp.auth.key";
    public static final  String SMTP_PORT_KEY ="smtp.port.key";
    
    public static final  String SMTP_HOST ="smtp.host";
    public static final  String SOCKETFACTORY_PORT ="socketFactory.port";
    public static final  String SOCKETFACTORY_CLASS ="socketFactory.class";
    public static final  String SMTP_AUTH ="smtp.auth";
    public static final  String SMTP_PORT ="smtp.port";

	public static final String SUBMISSION_PENDING = "status.pending";
	public static final String SUBMISSION_OLD_MESSAGE = "submission.old.message";
	public static final String PENDING_STATUS_TYPE_NAME = "pending.status.type.name";
	public static final String SUBMISSION_APPROVED_MESSAGE = "submission.approved.message";
	public static final String SUBMISSION_REJECTED_MESSAGE = "submission.rejected.message";

	public static final String ACCESS_DENIED = "accessDenied";

	public static final String LAST_DATE_SUP = "date.lastdate.sup";
	public static final String AFTER_DATE_15 = "date.after";

	public static final String FILE_PATH = "file.path.excel";
	public static final String EXT = "file.extension";
	public static final String RAW_DATA_XLS_TEMPLATE = "raw.data.xls.template";

	public static final String BOOTSTRAP_ALERT_SUCCESS = "bootstrap.alert.success";
	public static final String BOOTSTRAP_ALERT_DANGER = "bootstrap.alert.danger";

	// submission date crossed message
	public static final String LAST_DATE_SUBMISSION_CROSSED_MESSAGE = "last.date.submission.crossed.message";

	public static final String LOGIN_META_ID = "loginMetaId";

	public static final String MESSAGE_SETFORM = "message.setFrom";

	public static final class Odk {
		public static final String ODK_SERVER_DIRECTORY = "odk.directory";
		public static final String ODK_MEDIAFILE_DIRECTORY = "odk.default.upload.mediafile";
		public static final String ODK_DEFAULT_UPLOAD_FOLDER_NAME = "odk.default.upload.folder.name";
		public static final String ODK_DEFAULT_FORMS_FOLDER_NAME = "odk.default.forms.folder.name";
		public static final String ODK_DEFAULT_UPLOADFAILURE_FOLDER_NAME = "odk.default.uploadfailure.folder.name";
		public static final String ODK_DEFAULT_DESTINATIONFILE_FOLDER_NAME = "odk.default.definationfile.folder.name";
		public static final String ODK_DEFAULT_SUBMISSIONFILE_FOLDER_NAME = "odk.default.submissionfile.folder.name";
		public static final String DATE_FORMAT_ALL = "date.format.all";
		
		public static final String DATE_OF_VISIT_FORMAT = "date.of.visit.format";
		public static final String DATE_OF_VISIT_FORMAT_ALL = "date.format.all";

		public static final String MAX_POST_SIZE = "max.post.size";
		public static final String SUBMISSION_CREATE_FOLDER = "submission.create.folder";
		public static final String MEDIA_FILE_UPDATED_DATE = "media.file.updated.date";
	}

}
