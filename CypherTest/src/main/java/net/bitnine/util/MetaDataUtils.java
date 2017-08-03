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
//        	String columnTypeName = (data.getColumnTypeName(i));
            String columnTypeName = getChangedType(data.getColumnTypeName(i));
//    		System.out.println("columnTypeName: " + columnTypeName);
    		
        	boolean isReadOnly = data.isReadOnly(i); 
        	
        	DataMeta dataMeta = new DataMeta (columnLabel, columnTypeName, isReadOnly);
        	
        	dataMetaList.add(dataMeta);
        }
		return dataMetaList;
	}

    private static String getChangedType(String columnTypeName) {
        String changedType = "";
        if ( columnTypeName.equals("int2") || columnTypeName.equals("int4") ) {
            changedType = "int";
        }
        else if ( columnTypeName.equals("int8") ) {
            changedType = "long";
        }
        else if ( columnTypeName.equals("numeric") || columnTypeName.equals("float4")  || columnTypeName.equals("float8")  || columnTypeName.equals("serial")  || columnTypeName.equals("bigserial") ) {
            changedType = "float";
        }
        else {
            changedType = columnTypeName;
        }
        return changedType;
    }
}



























