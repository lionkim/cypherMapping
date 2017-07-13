package net.bitnine.domain;

import java.math.BigDecimal;
import java.util.List;

public class Vertex  {

	private BigDecimal id;
	private String type;
	private String title;
	private String props;
	
	private List<DataMeta> dataMetaList;

	public Vertex() {
	}

	public Vertex(BigDecimal id, String type, String title, String props, List<DataMeta> dataMetaList) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.props = props;
		this.dataMetaList = dataMetaList;
	}

	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getProps() {
		return props;
	}
	public void setProps(String props) {
		this.props = props;
	}

	public List<DataMeta> getDataMetaList() {
		return dataMetaList;
	}
	public void setDataMetaList(List<DataMeta> dataMetaList) {
		this.dataMetaList = dataMetaList;
	}
	
	
}
