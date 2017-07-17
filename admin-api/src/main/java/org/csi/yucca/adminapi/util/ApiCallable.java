package org.csi.yucca.adminapi.util;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;

public interface ApiCallable {
	
	Object call() throws BadRequestException, NotFoundException, Exception;
}
