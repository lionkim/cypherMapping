package net.bitnine.domain;

import java.util.List;

public class General implements Meta {

	private String idA;
	private String labelA;
	private String title;
	private String idB;
	private String labelB;
	private String name;
	private String labelC;
	private String idC;
	
	private List<DataMeta> dataMetaList;
	
	public String getIdA() {
		return idA;
	}
	public void setIdA(String idA) {
		this.idA = idA;
	}
	public String getLabelA() {
		return labelA;
	}
	public void setLabelA(String labelA) {
		this.labelA = labelA;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIdB() {
		return idB;
	}
	public void setIdB(String idB) {
		this.idB = idB;
	}
	public String getLabelB() {
		return labelB;
	}
	public void setLabelB(String labelB) {
		this.labelB = labelB;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabelC() {
		return labelC;
	}
	public void setLabelC(String labelC) {
		this.labelC = labelC;
	}
	public String getIdC() {
		return idC;
	}
	public void setIdC(String idC) {
		this.idC = idC;
	}
	public List<DataMeta> getDataMetaList() {
		return dataMetaList;
	}
	public void setDataMetaList(List<DataMeta> dataMetaList) {
		this.dataMetaList = dataMetaList;
	}
	@Override
	public String toString() {
		return "[idA: " + idA + ", labelA: " + labelA + ", title: " + title + ", idB: " + idB + ", labelB: " + labelB
				+ ", name: " + name + ", labelC: " + labelC + ", idC: " + idC + "]";
	}

	
	
	
}
