package org.csi.yucca.adminapi.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.response.Response;
import org.springframework.beans.BeanUtils;

public class ServiceUtil {

	private static final String SORT_PROPERTIES_SEPARATOR = ",";
	private static final String DESC_CHAR = "-";
	
	public static <T> List<Response> getResponseList(List<T> modelList, Class<?> responseClass)throws Exception{
		List<Response> responsesList = new ArrayList<Response>();
			
		for (T model : modelList) {
			Response response = (Response)responseClass.newInstance();
			BeanUtils.copyProperties(model, response);
			responsesList.add(response);
		}
		
		return responsesList;
	}
	
	public static  void checkMandatoryParameter(Object parameterObj, String parameterName)throws BadRequestException{
		if(parameterObj == null){
			throw new BadRequestException(Errors.MANDATORY_PARAMETER.arg(parameterName));
		}
	}	
	
	public static void checkList(List<?> list)throws NotFoundException{
		if(list == null || list.isEmpty()){
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}	
	
	public static List<String> getSortList(String sort, Class<?> clazz) throws BadRequestException{
		
		List<String> sortList = null;
		
		if (sort != null && !sort.isEmpty()) {
			
			if (sort.contains(SORT_PROPERTIES_SEPARATOR)) {
				sortList = Arrays.asList(sort.split(SORT_PROPERTIES_SEPARATOR));
			}
			else{
				sortList = Arrays.asList(sort);
			}
			
			validateSortParameter(sortList, clazz);
		}

		return sortList;
		
	}
	
	private static void validateSortParameter(List<String> sortList, Class<?> clazz) throws BadRequestException{
		for (String sortProperty : sortList) {
				
			if(propertyNotFound(sortProperty, clazz)){
				throw new BadRequestException(Errors.PROPERTY_NOT_FOUND.arg(sortProperty));
			}
				
		}
	}
	
	private static boolean propertyNotFound(String sortProperty, Class<?> clazz){
		
		Field [] fields = clazz.getDeclaredFields();
		
		for(Field field: fields){
			
			String fieldName = field.getName();
			String fieldNameDesc = fieldName + DESC_CHAR;
			
			if (fieldName.equals(sortProperty) || fieldNameDesc.equals(sortProperty)) {
				return false;
			}
		}
		return true;
	}

	
	
}
