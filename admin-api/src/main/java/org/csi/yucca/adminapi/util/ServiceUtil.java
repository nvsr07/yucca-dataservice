package org.csi.yucca.adminapi.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.response.Response;
import org.springframework.beans.BeanUtils;

public class ServiceUtil {

	private static final String SORT_PROPERTIES_SEPARATOR = ",";
	private static final String DESC_CHAR = "-";
	
	public static final String UUID_PATTERN         = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	public static final String NOT_DEVICE_PATTERN   = "^[a-zA-Z0-9-]{5,100}$";
	public static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z0-9]*$";
	
	
	public static String getDefaultInternalSocode(String organizationcode){
		return "SOinternal" + organizationcode;
	}
	
	public static boolean isType(Type TYPE, SmartobjectRequest smartobjectRequest){
		return isType(TYPE, smartobjectRequest.getIdSoType());
	}

	public static boolean isType(Type TYPE, Integer idSoType){
		return TYPE.id() == idSoType;
	}

	public static boolean isType(Type TYPE, Smartobject smartobject){
		return TYPE.id() == smartobject.getIdSoType();
	}
	
	public static void checkCount(int count)throws NotFoundException{
		if (count == 0 ) {
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}

	public static void checkIfFoundRecord(Object object)throws NotFoundException{
		checkIfFoundRecord(object, null);
	}

	public static void checkIfFoundRecord(Object object, String arg)throws NotFoundException{
		if (object == null ) {
			
			if (arg != null) {
				throw new NotFoundException(Errors.RECORD_NOT_FOUND, arg);
			}
			
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}
	
	public static <T> List<Response> getResponseList(List<T> modelList, Class<?> responseClass) throws Exception {
		List<Response> responsesList = new ArrayList<Response>();

		for (T model : modelList) {
			Response response = (Response) responseClass.newInstance();
			BeanUtils.copyProperties(model, response);
			responsesList.add(response);
		}

		return responsesList;
	}

	public static void checkNullInteger(Object object, String fieldName) {
		try {

			Field fieldID = object.getClass().getField(fieldName);

			Integer value = (Integer) fieldID.get(object);

			if (value == null) {
				fieldID.set(object, 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static boolean isAlphaNumeric(String s){
	    return s.matches(ALPHANUMERIC_PATTERN);
	}

	public static boolean matchUUIDPattern(String s){
	    return s.matches(UUID_PATTERN);
	}
	
	public static boolean matchNotDevicePattern(String s){
	    return s.matches(NOT_DEVICE_PATTERN);
	}

	public static void checkAphanumeric(String s, String fieldName) throws BadRequestException{
		if (!isAlphaNumeric(s)){
			throw new BadRequestException(Errors.ALPHANUMERIC_VALUE_REQUIRED, "received " + fieldName + " [ " + s + " ]");
		}

	}
	
	public static boolean containsWhitespace(String s){
		
		Pattern pattern = Pattern.compile("\\s");
		
		Matcher matcher = pattern.matcher(s);
		
		return matcher.find();
		
	}
	
	public static void checkCode(String s, String parameterName) throws BadRequestException {
		checkMandatoryParameter(s, parameterName);
		checkWhitespace(s, parameterName);
		checkAphanumeric(s, parameterName);
	}
	
	public static void checkWhitespace(String s, String parameterName) throws BadRequestException {
		if(containsWhitespace(s)){
			throw new BadRequestException(Errors.WHITE_SPACES, parameterName);
		}
	}
	
	public static void checkMandatoryParameter(boolean isEmpty, String parameterName) throws BadRequestException {
		if (isEmpty) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}

	public static void checkMandatoryParameter(Object parameterObj, String parameterName) throws BadRequestException {
		if (parameterObj == null) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}

	public static void checkMandatoryParameter(String parameterObj, String parameterName) throws BadRequestException {
		if (parameterObj == null || parameterObj.isEmpty()) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}
	
	public static void checkList(List<?> list) throws NotFoundException {
		if (list == null || list.isEmpty()) {
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}

	public static List<String> getSortList(String sort, Class<?> clazz) throws BadRequestException {

		List<String> sortList = null;

		if (sort != null && !sort.isEmpty()) {

			if (sort.contains(SORT_PROPERTIES_SEPARATOR)) {
				sortList = Arrays.asList(sort.split(SORT_PROPERTIES_SEPARATOR));
			} else {
				sortList = Arrays.asList(sort);
			}

			validateSortParameter(sortList, clazz);
		}

		return sortList;

	}

	private static void validateSortParameter(List<String> sortList, Class<?> clazz) throws BadRequestException {
		for (String sortProperty : sortList) {

			if (propertyNotFound(sortProperty, clazz)) {
				throw new BadRequestException(Errors.PROPERTY_NOT_FOUND, sortProperty);
			}

		}
	}

	private static boolean propertyNotFound(String sortProperty, Class<?> clazz) {

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			String fieldName = field.getName();
			String fieldNameDesc = fieldName + DESC_CHAR;

			if (fieldName.equals(sortProperty) || fieldNameDesc.equals(sortProperty)) {
				return false;
			}
		}
		return true;
	}

}
