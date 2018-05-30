package org.sdrc.missionmillet.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Subrata
 *
 */
@Component
public class MissionMilletUtil {
	
	private static final Logger	LOGGER	= LoggerFactory.getLogger( "LOGGER" );
	/**
	 * 
	 * @param byteArray
	 * @return ByteArrayInputStream
	 * 
	 * @Description This method will take byte array and return byteArrayStream.
	 */
	public static ByteArrayInputStream getByteArrayStream(byte[] byteArray) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		return byteArrayInputStream;
	}
	
	/**
	 * @author Subrata
	 * @param fileArray
	 * @param dir
	 * @param fileName
	 * 
	 * @return filepath
	 * 
	 * @Description Saving PDF file and returning the path.
	 */

	public static String saveFile(byte[] fileArray, String dir, String fileName) {
		File file = null;
		file = new File(dir);
		String fullPath = dir+""+fileName;
		try {
			if (!file.exists()) 
				file.mkdirs();
			file = new File(fullPath);
			FileUtils.writeByteArrayToFile(file, fileArray);
			
			return fullPath;
		} catch (IOException e) {
			LOGGER.info("File not found");
			return "fail";
		}
	}
	
	
	/**
	 * @Description get the PDF file for download
	 * 
	 * @author Subrata
	 * 
	 * @param pdfFilePath
	 * @param directory
	 * 
	 * @return List
	 * @throws IOException
	 * 
	 
	 */
	@SuppressWarnings("deprecation")
	public static String getPdfFile(String pdfFilePath, String dir) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmssSSSSSSS");
		
		File filePath = null;
		try {
			filePath = new File(pdfFilePath);
			
			String[] bits = pdfFilePath.split("/");
			String originalFileName = bits[bits.length-1];
			String fileName = dir+sdf.format(new Date())+"_"+originalFileName;
			PDDocument pdfDocument = PDDocument.load(filePath);
			PDStream stream= new PDStream(pdfDocument);
		    stream.addCompression();
		    pdfDocument.setAllSecurityToBeRemoved(true);
			pdfDocument.save(fileName);
	        pdfDocument.close();
			return fileName;
		} catch (Exception e) {
			LOGGER.info("Unable to load file.");
			return "failed";
		}
	}
	/**
	 * @author Subrata
	 * @param string
	 * @return List
	 * @throws IOException
	 * 
	 * @Description split a string and adding in a list  
	 */
	public static List<Integer> getLockedList(String str) throws IOException {
		List<Integer> list = new ArrayList<Integer>();
		String[] rowList = str.split(",");
		for (String string : rowList) {
			list.add(Integer.valueOf(string));
		}
		return list;
	}
	
	/**
	 * @author Subrata
	 * @param inputStream
	 * @return map
	 * @throws IOException
	 * 
	 * @Description Reading values from properties file and returning Map<Integer, String>. 
	 */
	
	public static Map<Integer, String> getPropertiesValues(InputStream inputStream) throws IOException {
		Properties properties = new Properties();
		properties.load(inputStream);
		Map<Integer, String> map = new HashMap<Integer, String>();
		Set<Object> keys = properties.keySet();
		for (Object object : keys) {
			map.put(Integer.valueOf(object.toString()), (String)properties.get(object.toString()));
		}
		return map;
	}
	
}
