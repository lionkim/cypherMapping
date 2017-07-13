package net.bitnine.domain;

import java.util.List;

public class Jsonb implements Meta {

	private String prodYear;
	
	private String jsons;
	
	private List<DataMeta> dataMetaList;

	public Jsonb() {
	}

	public Jsonb(String prodYear, String jsons, List<DataMeta> dataMetaList) {
		this.prodYear = prodYear;
		this.jsons = jsons;
		this.dataMetaList = dataMetaList;
	}

	public String getProdYear() {
		return prodYear;
	}

	public void setProdYear(String prodYear) {
		this.prodYear = prodYear;
	}

	public String getJsons() {
		return jsons;
	}

	public void setJsons(String jsons) {
		this.jsons = jsons;
	}

	public List<DataMeta> getDataMetaList() {
		return dataMetaList;
	}

	public void setDataMetaList(List<DataMeta> dataMetaList) {
		this.dataMetaList = dataMetaList;
	}


	@Override
	public String toString() {
		return "[prodYear: " + prodYear + ", jsons=" + jsons + "]";
	}
	
}
