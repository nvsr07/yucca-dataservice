package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author gianfranco.stolfa
 *
 */
public interface SequenceMapper {
	
	String SEQ_PERSONAL_TENANTS = "yucca_seq_personaltenants_progressivo";

	String SEQ_TRIAL_TENANTS = "yucca_seq_trialtenants_progressivo";
	
	public static final String SEQ_PERSONAL_TENANTS_NEXT_VAL =  "select nextval('" + SEQ_PERSONAL_TENANTS + "')"; 
	@Select(SEQ_PERSONAL_TENANTS_NEXT_VAL)                      
	int selectPersonalTenantsSequence();

	public static final String SEQ_TRIAL_TENANTS_NEXT_VAL =  "select nextval('" + SEQ_TRIAL_TENANTS + "')"; 
	@Select(SEQ_TRIAL_TENANTS_NEXT_VAL)                      
	int selectTrialTenantsSequence();
	
}
