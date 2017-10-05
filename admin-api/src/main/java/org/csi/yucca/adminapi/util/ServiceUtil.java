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
import org.springframework.util.StringUtils;

public class ServiceUtil {

	private static final String SORT_PROPERTIES_SEPARATOR = ",";
	private static final String DESC_CHAR = "-";
	public static final String UUID_PATTERN         = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	public static final String NOT_DEVICE_PATTERN   = "^[a-zA-Z0-9-]{5,100}$";
	public static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z0-9]*$";
	
	/**
	 * 
	 * @param organizationcode
	 * @return
	 */
	public static String getDefaultInternalSocode(String organizationcode){
		return "SOinternal" + organizationcode;
	}
	
	/**
	 * 
	 * @param TYPE
	 * @param smartobjectRequest
	 * @return
	 */
	public static boolean isType(Type TYPE, SmartobjectRequest smartobjectRequest){
		return isType(TYPE, smartobjectRequest.getIdSoType());
	}

	/**
	 * 
	 * @param TYPE
	 * @param idSoType
	 * @return
	 */
	public static boolean isType(Type TYPE, Integer idSoType){
		return TYPE.id() == idSoType;
	}

	/**
	 * 
	 * @param TYPE
	 * @param smartobject
	 * @return
	 */
	public static boolean isType(Type TYPE, Smartobject smartobject){
		return TYPE.id() == smartobject.getIdSoType();
	}
	
	/**
	 * 
	 * @param count
	 * @throws NotFoundException
	 */
	public static void checkCount(int count)throws NotFoundException{
		if (count == 0 ) {
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}

	/**
	 * 
	 * @param object
	 * @throws NotFoundException
	 */
	public static void checkIfFoundRecord(Object object)throws NotFoundException{
		checkIfFoundRecord(object, null);
	}

	/**
	 * 
	 * @param object
	 * @param arg
	 * @throws NotFoundException
	 */
	public static void checkIfFoundRecord(Object object, String arg)throws NotFoundException{
		if (object == null ) {
			
			if (arg != null) {
				throw new NotFoundException(Errors.RECORD_NOT_FOUND, arg);
			}
			
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}
	
	/**
	 * 
	 * @param modelList
	 * @param responseClass
	 * @return
	 * @throws Exception
	 */
	public static <T> List<Response> getResponseList(List<T> modelList, Class<?> responseClass) throws Exception {
		List<Response> responsesList = new ArrayList<Response>();

		for (T model : modelList) {
			Response response = (Response) responseClass.newInstance();
			BeanUtils.copyProperties(model, response);
			responsesList.add(response);
		}

		return responsesList;
	}

	/**
	 * 
	 * @param object
	 * @param fieldName
	 */
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
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isAlphaNumeric(String s){
	    return s.matches(ALPHANUMERIC_PATTERN);
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean matchUUIDPattern(String s){
	    return s.matches(UUID_PATTERN);
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean matchNotDevicePattern(String s){
	    return s.matches(NOT_DEVICE_PATTERN);
	}

	/**
	 * 
	 * @param s
	 * @param fieldName
	 * @throws BadRequestException
	 */
	public static void checkAphanumeric(String s, String fieldName) throws BadRequestException{
		if (!isAlphaNumeric(s)){
			throw new BadRequestException(Errors.ALPHANUMERIC_VALUE_REQUIRED, "received " + fieldName + " [ " + s + " ]");
		}

	}
	
	/**
	 * 
	 * @param codeTenantStatus
	 * @throws BadRequestException
	 */
	public static void checkCodeTenantStatus(String codeTenantStatus) throws BadRequestException{
		
		for (Status status : Status.values()) {
			if(status.code().equals(codeTenantStatus))return;
		}
		
		List<String> listCodeTenantStatus = new ArrayList<>();
		for (Status status : Status.values()) {
			listCodeTenantStatus.add(status.code());
		}
		
		String message = "received " + "codeTenantStatus" + " [ " + codeTenantStatus + " ]. Possible values are: " 
				+ StringUtils.collectionToCommaDelimitedString(listCodeTenantStatus);
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, message);
	}
	
	/**
	 * 
	 * @param idTenantType
	 * @throws BadRequestException
	 */
	public static void checkIdTenantType(Integer idTenantType) throws BadRequestException{
		
		for (TenantType type : TenantType.values()) {
			if(type.id() == idTenantType)return;
		}
		
		List<Integer> listIdTenantType = new ArrayList<>();
		for (TenantType type : TenantType.values()) {
			listIdTenantType.add(type.id());
		}
		
		String message = "received " + "idTenantType" + " [ " + idTenantType + " ]. Possible values are: " 
				+ StringUtils.collectionToCommaDelimitedString(listIdTenantType);
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, message);
	}
	
	/**
	 * 
	 * @param userTypeAuth
	 * @param idTenantType
	 * @throws BadRequestException
	 */
	public static void checkTenantTypeAndUserTypeAuth(String userTypeAuth, Integer idTenantType) throws BadRequestException{
		
		if ( ( TenantType.DEFAULT.id() == idTenantType || TenantType.PLUS.id()    == idTenantType || 
			   TenantType.ZERO.id()    == idTenantType || TenantType.DEVELOP.id() == idTenantType  ) && 
				!UserTypeAuth.ADMIN.description().equals(userTypeAuth)) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "tenant type " + tenantTypeDescription(idTenantType) + " [ " + idTenantType + " ] permitted only for " + UserTypeAuth.ADMIN.description() + " user");
		}
		
		if (UserTypeAuth.SOCIAL.description().equals(userTypeAuth) && TenantType.TRIAL.id() != idTenantType) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, 
		"user type [ " + UserTypeAuth.SOCIAL.description() + " ] permitted only for " + TenantType.TRIAL.description() + " [ " + TenantType.TRIAL.id() + " ] " + " idTenantType");
		}
	}
	
	/**
	 * 
	 * @param userTypeAuth
	 * @throws BadRequestException
	 */
	public static void checkUserTypeAuth(String userTypeAuth) throws BadRequestException{
		
		for (UserTypeAuth type : UserTypeAuth.values()) {
			if(type.description().equals(userTypeAuth))return;
		}

		List<String> listUserTypeAuth = new ArrayList<>();
		for (UserTypeAuth type : UserTypeAuth.values()) {
			listUserTypeAuth.add(type.description());
		}
		
		String message = "received " + "userTypeAuth" + " [ " + userTypeAuth + " ]. Possible values are: " 
				+ StringUtils.collectionToCommaDelimitedString(listUserTypeAuth);
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, message);
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean containsWhitespace(String s){
		
		Pattern pattern = Pattern.compile("\\s");
		
		Matcher matcher = pattern.matcher(s);
		
		return matcher.find();
		
	}
	
	/**
	 * 
	 * @param s
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkCode(String s, String parameterName) throws BadRequestException {
		checkMandatoryParameter(s, parameterName);
		checkWhitespace(s, parameterName);
		checkAphanumeric(s, parameterName);
	}
	
	/**
	 * 
	 * @param s
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkWhitespace(String s, String parameterName) throws BadRequestException {
		if(containsWhitespace(s)){
			throw new BadRequestException(Errors.WHITE_SPACES, parameterName);
		}
	}
	
	/**
	 * 
	 * @param isEmpty
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkMandatoryParameter(boolean isEmpty, String parameterName) throws BadRequestException {
		if (isEmpty) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}

	/**
	 * 
	 * @param parameterObj
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkMandatoryParameter(Object parameterObj, String parameterName) throws BadRequestException {
		if (parameterObj == null) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}
	
	/**
	 * 
	 * @param parameterObj
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkMandatoryParameter(String parameterObj, String parameterName) throws BadRequestException {
		if (parameterObj == null || parameterObj.isEmpty()) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}
	
	/**
	 * 
	 * @param list
	 * @throws NotFoundException
	 */
	public static void checkList(List<?> list) throws NotFoundException {
		if (list == null || list.isEmpty()) {
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}

	/**
	 * 
	 * @param sort
	 * @param clazz
	 * @return
	 * @throws BadRequestException
	 */
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

	/**
	 * 
	 * @param sortList
	 * @param clazz
	 * @throws BadRequestException
	 */
	private static void validateSortParameter(List<String> sortList, Class<?> clazz) throws BadRequestException {
		for (String sortProperty : sortList) {

			if (propertyNotFound(sortProperty, clazz)) {
				throw new BadRequestException(Errors.PROPERTY_NOT_FOUND, sortProperty);
			}

		}
	}

	/**
	 * 
	 * @param sortProperty
	 * @param clazz
	 * @return
	 */
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
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String tenantTypeDescription(int id){
		
		for (TenantType type : TenantType.values()) {
			if(type.id() == id){
				return type.description();
			}
		}
		return null;
		
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String codeTenantStatus(int id){
		
		for (Status status : Status.values()) {
			if(status.id() == id){
				return status.code();
			}
		}
		return null;
		
	}

}
