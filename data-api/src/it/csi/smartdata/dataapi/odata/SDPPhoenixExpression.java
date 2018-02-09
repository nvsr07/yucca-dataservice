package it.csi.smartdata.dataapi.odata;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;

public class SDPPhoenixExpression {
	private String preparedWhere;
	private List<Object> parameters;
	private BinaryOperator operator;

	public SDPPhoenixExpression(final BinaryOperator operator) {
		preparedWhere = "";
		parameters = new ArrayList<Object>();
		this.operator = operator;
	}

	public void addParameter(final Object parameter) {
		parameters.add(parameter);
	}

	public void setPrepeparedWhere(final String where) {
		preparedWhere = where;
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public BinaryOperator getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return preparedWhere;
	}
}

