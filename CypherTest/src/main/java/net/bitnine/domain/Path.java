package net.bitnine.domain;

import java.util.List;

public class Path {

	private String head;
	private String tail;
	private String nodes;
	private String edges;
	
	private List<DataMeta> dataMetaList;
	

	public Path() {
	}


	public Path(String head, String tail, String nodes, String edges, List<DataMeta> dataMetaList) {
		this.head = head;
		this.tail = tail;
		this.nodes = nodes;
		this.edges = edges;
		this.dataMetaList = dataMetaList;
	}


	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}

	public String getTail() {
		return tail;
	}
	public void setTail(String tail) {
		this.tail = tail;
	}

	public String getNodes() {
		return nodes;
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getEdges() {
		return edges;
	}
	public void setEdges(String edges) {
		this.edges = edges;
	}

	public List<DataMeta> getDataMetaList() {
		return dataMetaList;
	}
	public void setDataMetaList(List<DataMeta> dataMetaList) {
		this.dataMetaList = dataMetaList;
	}
	
	
}
