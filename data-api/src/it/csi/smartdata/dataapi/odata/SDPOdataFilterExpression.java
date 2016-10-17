package it.csi.smartdata.dataapi.odata;

public class SDPOdataFilterExpression {
	private String clause=null;

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}
	
	public SDPOdataFilterExpression(String clause) {
		this.clause = clause;
	}
	public SDPOdataFilterExpression() {
	}
	
	public String toString() {
		return this.clause;
	}
}
