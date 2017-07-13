package net.bitnine.domain;

import java.math.BigDecimal;
import java.util.List;

public class Edge  {

	private BigDecimal rId;
	private BigDecimal rHead;
	private BigDecimal rTail;
	private String rProps;
	private BigDecimal pId;
	private String pName;
	private String pProps;
	private List<DataMeta> dataMetaList;
	
	public Edge() {
	}

	public Edge(BigDecimal rId, BigDecimal rHead, BigDecimal rTail, String rProps, BigDecimal pId, String pName,
			String pProps, List<DataMeta> dataMetaList) {
		this.rId = rId;
		this.rHead = rHead;
		this.rTail = rTail;
		this.rProps = rProps;
		this.pId = pId;
		this.pName = pName;
		this.pProps = pProps;
		this.dataMetaList = dataMetaList;
	}

	public BigDecimal getrId() {
		return rId;
	}

	public void setrId(BigDecimal rId) {
		this.rId = rId;
	}

	public BigDecimal getrHead() {
		return rHead;
	}

	public void setrHead(BigDecimal rHead) {
		this.rHead = rHead;
	}

	public BigDecimal getrTail() {
		return rTail;
	}

	public void setrTail(BigDecimal rTail) {
		this.rTail = rTail;
	}

	public String getrProps() {
		return rProps;
	}

	public void setrProps(String rProps) {
		this.rProps = rProps;
	}

	public BigDecimal getpId() {
		return pId;
	}

	public void setpId(BigDecimal pId) {
		this.pId = pId;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpProps() {
		return pProps;
	}

	public void setpProps(String pProps) {
		this.pProps = pProps;
	}

	public List<DataMeta> getDataMetaList() {
		return dataMetaList;
	}

	public void setDataMetaList(List<DataMeta> dataMetaList) {
		this.dataMetaList = dataMetaList;
	}

	
}
