package net.bitnine.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import net.bitnine.domain.DataMeta;

public class MetaDataUtils {


	public static List<DataMeta> getMetaDataList (ResultSetMetaData data) throws SQLException {
		List<DataMeta> dataMetaList = new ArrayList<>();
    	
        for(int i = 1 ; i <=data.getColumnCount() ; i++){
        	
        	String columnLabel = data.getColumnLabel(i);
        	String columnTypeName = data.getColumnTypeName(i);
//    		System.out.println("columnTypeName: " + columnTypeName);
    		
        	boolean isReadOnly = data.isReadOnly(i); 
        	
        	DataMeta dataMeta = new DataMeta (columnLabel, columnTypeName, isReadOnly);
        	
        	dataMetaList.add(dataMeta);
        }
		return dataMetaList;
	}
}
