package it.csi.smartdata.dataapi.exception;

import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.MessageReference;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;

public class SDPPageSizeException extends ODataApplicationException {

	  public SDPPageSizeException(String message, Locale locale) {
		super(message, locale);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;


}
