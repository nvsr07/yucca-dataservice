package org.csi.yucca.dataservice.insertdataapi.dto;

import java.io.Serializable;

public class DatasetInfoKey implements Serializable {

	
	private Long idDataset;
	private Long datasetVersion;
	public Long getIdDataset() {
		return idDataset;
	}
	public Long getDatasetVersion() {
		return datasetVersion;
	}
	@Override
	public String toString() {
		return "DatasetInfoKey [idDataset=" + idDataset + ", datasetVersion="
				+ datasetVersion + "]";
	}
	public DatasetInfoKey(Long idDataset, Long datasetVersion) {
		super();
		this.idDataset = idDataset;
		this.datasetVersion = datasetVersion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datasetVersion == null) ? 0 : datasetVersion.hashCode());
		result = prime * result
				+ ((idDataset == null) ? 0 : idDataset.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatasetInfoKey other = (DatasetInfoKey) obj;
		if (datasetVersion == null) {
			if (other.datasetVersion != null)
				return false;
		} else if (!datasetVersion.equals(other.datasetVersion))
			return false;
		if (idDataset == null) {
			if (other.idDataset != null)
				return false;
		} else if (!idDataset.equals(other.idDataset))
			return false;
		return true;
	}
	
	
	
}
