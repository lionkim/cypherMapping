package net.bitnine.utils;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import net.bitnine.domain.DataMeta;

public class MetaDataUtils {


	public static List<DataMeta> setMetaData(ResultSetMetaData data) throws SQLException {
		List<DataMeta> dataMetaList = new ArrayList<>();
    	
        for(int i = 1 ; i <=data.getColumnCount() ; i++){
        	
        	String columnLabel = data.getColumnLabel(i);
        	String columnTypeName = data.getColumnTypeName(i);
        	boolean isReadOnly = data.isReadOnly(i); 
        	
        	DataMeta dataMeta = new DataMeta (columnLabel, columnTypeName, isReadOnly);
        	
        	dataMetaList.add(dataMeta);
        }
		return dataMetaList;
	}

	public static List<DataMeta> setSqlRowSetMetaData(SqlRowSetMetaData data) {
		List<DataMeta> dataMetaList = new ArrayList<>();

		for (int i = 1; i <= data.getColumnCount(); i++) {

			String columnLabel = data.getColumnLabel(i);
			String columnTypeName = data.getColumnTypeName(i);
			//boolean isReadOnly = data.isReadOnly(i);

//			DataMeta dataMeta = new DataMeta(columnLabel, columnTypeName, isReadOnly);
			DataMeta dataMeta = new DataMeta(columnLabel, columnTypeName, true);

			dataMetaList.add(dataMeta);
		}
		return dataMetaList;
	}
}
