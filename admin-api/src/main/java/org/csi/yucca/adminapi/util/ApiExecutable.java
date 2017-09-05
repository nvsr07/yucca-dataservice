package org.csi.yucca.adminapi.util;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;

public interface ApiExecutable {
	
	void call() throws BadRequestException, NotFoundException, Exception;
	
}
