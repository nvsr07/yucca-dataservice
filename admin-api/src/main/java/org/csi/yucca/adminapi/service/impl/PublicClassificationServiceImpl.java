package org.csi.yucca.adminapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.DomainMapper;
import org.csi.yucca.adminapi.model.Domain;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.Response;
import org.csi.yucca.adminapi.service.PublicClassificationService;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.Languages;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class PublicClassificationServiceImpl implements PublicClassificationService{
	
	@Autowired
	private DomainMapper domainMapper;
	
	public List<Response> selectDomain(Integer ecosystemCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(ecosystemCode, "ecosystemCode");
		
		List<Domain> domainList = null;
		
		List<String> sortList = ServiceUtil.getSortList(sort, Domain.class);
		
		if(lang==null || lang.isEmpty()){
			domainList =  domainMapper.selectDomainAllLanguage(ecosystemCode, sortList);
		}
		else if(Languages.IT.value().equals(lang)){
			domainList =  domainMapper.selectDomainITLanguage(ecosystemCode, sortList);
		}
		else if(Languages.EN.value().equals(lang)){
			domainList =  domainMapper.selectDomainENLanguage(ecosystemCode, sortList);
		}
		else{
			throw new BadRequestException(Errors.LANGUAGE_NOT_SUPPORTED.arg(lang));
		}

		ServiceUtil.checkList(domainList);
		return getResponseList(domainList);
		
	}
	
	
	/******************************************************************************
	 * 
	 *				private methods
	 * 
	 *******************************************************************************/
	
	private List<Response> getResponseList(List<Domain> domainList)throws Exception{
		List<Response> domainResponsesList = new ArrayList<Response>();
			
		for (Domain domain : domainList) {
			Response obj = new DomainResponse();
			BeanUtils.copyProperties(domain, obj);
			domainResponsesList.add(obj);
		}
		
		return domainResponsesList;
	}
	
}
