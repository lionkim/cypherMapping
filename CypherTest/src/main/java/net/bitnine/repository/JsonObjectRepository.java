package net.bitnine.repository;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.postgresql.jdbc.PgStatement;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.jdbc.PgResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.bitnine.domain.DataMeta;
import net.bitnine.domain.Path;
import net.bitnine.domain.Vertex;
import net.bitnine.exception.QueryException;
import net.bitnine.parser.EdgeParser;
import net.bitnine.parser.PathParser;
import net.bitnine.parser.VertexParser;
import net.bitnine.service.PropertiesService;
import net.bitnine.util.JDBCTutorialUtilities;
import net.bitnine.util.MetaDataUtils;

@Repository
public class JsonObjectRepository {

    @Autowired private PropertiesService propertiesService;
    
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public JSONObject getJson(String query) throws QueryException {
        PgConnection pgConnection  = null;
        PgResultSet pgResultSet = null;
        PgStatement pgstmt = null;
        
        JSONObject mapRet = new JSONObject();

        String maxRow = propertiesService.getSetMax();
        
        int maxRows = Integer.parseInt(maxRow);
        
	    try {
            if (dataSource.getConnection().isWrapperFor(PgConnection.class)) {
                pgConnection = dataSource.getConnection().unwrap(PgConnection.class);
            }
	        
	        pgstmt =  (PgStatement) pgConnection.createStatement();
	        
	        pgstmt.setMaxRows(maxRows);			// 반환하는 row 수를 maxRows 값으로 설정함. maxRows값은 application.properties의 setMax 값. 관리자가 런타임 수정가능

	        pgResultSet = (PgResultSet) pgstmt.executeQuery(query);
            
            ResultSetMetaData resultSetMetaData = pgResultSet.getMetaData();

            List<DataMeta> dataMetaList = MetaDataUtils.getMetaDataList(resultSetMetaData);


            JSONArray nodeJsonArr = new JSONArray(); // 파싱된 값 전체를 담을 배열

            while (pgResultSet.next()) {
                // ResultSet의 결과를 LinkedHashMap에 저장 - put 순서대로 꺼내기 위해
                JSONObject rowJsonObject = new JSONObject();

                for (int count = 1; count <= resultSetMetaData.getColumnCount(); count++) { // i가 1부터 시작함에 유의
                    setChangeType(pgResultSet, resultSetMetaData, rowJsonObject, count); // 컬럼의 타입별로 해당타입으로 변환하여 jsonObject에 put.                    
                }
                nodeJsonArr.add(rowJsonObject);
	        }

            mapRet.put("meta", "success");
            mapRet.put("meta", dataMetaList);
            mapRet.put("rows", nodeJsonArr);

	    } catch (ParseException ex) {
	        ex.printStackTrace();
            
        } catch (SQLException ex) {
            JDBCTutorialUtilities.printSQLException(ex);
            ex.printStackTrace();
            throw new QueryException (JDBCTutorialUtilities.getSQLState(ex), ex);
            
        } finally {
            if (pgConnection != null) try { pgConnection.close(); } catch (SQLException e) {}
            if (pgResultSet != null) try { pgResultSet.close(); } catch (SQLException e) {}
            if (pgstmt != null) try { pgstmt.close(); } catch (SQLException e) {}
	    }
        return mapRet;
	}

	private void setChangeType(PgResultSet pgResultSet, ResultSetMetaData resultSetMetaData, JSONObject rowJsonObject,
			int cnt) throws ParseException, SQLException {
	    PathParser pathParser = new PathParser();
	    EdgeParser edgeParser = new EdgeParser();
	    VertexParser vertexParser = new VertexParser();
	    
		JSONParser parser = new JSONParser();
		String columnTypeName = resultSetMetaData.getColumnTypeName(cnt);
		String columnName = resultSetMetaData.getColumnLabel(cnt);
		
		System.out.println("columnTypeName: " + columnTypeName);

		switch (columnTypeName) {

		// number에는 int, long, double, float 등
		case "number":
		    int result = pgResultSet.getInt(columnName);
			rowJsonObject.put(columnName, result);
			break;

        case "int4":
            int int4Result = pgResultSet.getInt(columnName);
            rowJsonObject.put(columnName, int4Result);
            break;

        case "int8":
            int int8Result = pgResultSet.getInt(columnName);
            rowJsonObject.put(columnName, int8Result);
            break;
            
        case "decimal":
            Double decimalResult = pgResultSet.getDouble(columnName);
            rowJsonObject.put(columnName, decimalResult);
            break;
            
        case "numeric":
            Double numericResult = pgResultSet.getDouble(columnName);
            rowJsonObject.put(columnName, numericResult);
            break;
            
		case "varchar":
            String varcharResult = pgResultSet.getString(columnName);
			rowJsonObject.put(columnName, varcharResult);
			break;

		case "graphid":
		    Double doubleResult = pgResultSet.getDouble(columnName);

			rowJsonObject.put(columnName, doubleResult);
			break;

		case "text":
            String textResult = pgResultSet.getString(columnName);
			rowJsonObject.put(columnName, textResult);
			break;

		case "jsonb":
            String jsonbResult = pgResultSet.getString(columnName);
			rowJsonObject.put(columnName, (JSONObject) parser.parse(jsonbResult));
			break;

		case "graphpath":
            String graphpathResult = pgResultSet.getString(columnName);
		    Path path = pathParser.createParsedPath(graphpathResult);
            rowJsonObject.put(columnName, path);
			break;

        case "vertex":
            String vertexResult = pgResultSet.getString(columnName);
            Vertex vertext = vertexParser.createParsedVertext(vertexResult);
            rowJsonObject.put(columnName, vertext);
            break;
            
        case "edge":
            String edgeResult = pgResultSet.getString(columnName);
            rowJsonObject.put(columnName, edgeParser.createParsedEdge(edgeResult));
            break;

        case "_vertex":
            String vertexListResult = pgResultSet.getString(columnName);
            List<Vertex> vertextList = vertexParser.createParsedVertextList(vertexListResult);
            rowJsonObject.put(columnName, vertextList);
            break;
            
        case "_edge":
            String edgeListResult = pgResultSet.getString(columnName);
            rowJsonObject.put(columnName, edgeParser.createParsedVertextList(edgeListResult));
            break;

		default:
            Object objectResult = pgResultSet.getObject(columnName);
			rowJsonObject.put(columnName, objectResult);
			break;
		}
	}

}
