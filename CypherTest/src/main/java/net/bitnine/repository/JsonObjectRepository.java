package net.bitnine.repository;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import org.postgresql.util.PGtokenizer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import net.bitnine.domain.DataMeta;
import net.bitnine.domain.Edge;
import net.bitnine.domain.Path;
import net.bitnine.domain.Vertex;
import net.bitnine.exception.QueryException;
import net.bitnine.parser.EdgeParser;
import net.bitnine.parser.PathParser;
import net.bitnine.parser.VertexParser;
import net.bitnine.utils.DomainParser;
import net.bitnine.utils.MetaDataUtils;
import net.bitnine.utils.TopCommaTokenizer;

@Repository
public class JsonObjectRepository {

	private JdbcTemplate jdbcTemplate;

	public JsonObjectRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public JSONObject getJson(String query) throws UnsupportedEncodingException {
//	    query = "match path=(a:production)-[]-(b:company) where id(a) = '4.7058' return nodes(path) as NODES, edges(path) as EDGES, id( (nodes(path))[1] ) as HEAD, id( (nodes(path))[length(path)+1] ) as TAIL";

//	    System.out.println("query: " + query);
	    
		return jdbcTemplate.query(query, new ResultSetExtractor<JSONObject>() {
			@Override
			public JSONObject extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				JSONObject mapRet = new JSONObject();

				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

				List<DataMeta> dataMetaList = MetaDataUtils.getMetaDataList(resultSetMetaData);

				mapRet.put("meta", dataMetaList);

				JSONArray nodeJsonArr = new JSONArray(); // 파싱된 값 전체를 담을 배열

				int colCnt = resultSetMetaData.getColumnCount();
				while (resultSet.next()) {
					// ResultSet의 결과를 LinkedHashMap에 저장 - put 순서대로 꺼내기 위해
					JSONObject rowJsonObject = new JSONObject();

					for (int count = 1; count <= colCnt; count++) { // i가 1부터
																	// 시작함에 유의
						try {
							setChangeType(resultSet, resultSetMetaData, rowJsonObject, count); // 컬럼의 타입별로 해당타입으로 변환하여 jsonObject에 put.
						} catch (ParseException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new QueryException (e.getMessage(), "or SQL_EXCEPTION");
                        }
					}
					nodeJsonArr.add(rowJsonObject);
				}

				mapRet.put("rows", nodeJsonArr);

				return mapRet;
			}
		});
	}

	private void setChangeType(ResultSet resultSet, ResultSetMetaData resultSetMetaData, JSONObject rowJsonObject,
			int cnt) throws ParseException, SQLException {
	    PathParser pathParser = new PathParser();
	    EdgeParser edgeParser = new EdgeParser();
	    VertexParser vertexParser = new VertexParser();
	    
		JSONParser parser = new JSONParser();
		String columnTypeName = resultSetMetaData.getColumnTypeName(cnt);

//		String columnName = resultSetMetaData.getColumnLabel(cnt).toUpperCase();
		String columnName = resultSetMetaData.getColumnLabel(cnt);
		String result = resultSet.getString(columnName);
		
//		System.out.println("columnTypeName: " + columnTypeName);

		switch (columnTypeName) {

		// number에는 int, long, double, float 등
		case "number":
			long longResult = Long.parseLong(result);
			rowJsonObject.put(columnName, longResult);
			break;

        case "int4":
            Integer integer4Result = Integer.parseInt(result);
            System.out.println("int4\n");
            rowJsonObject.put(columnName, integer4Result);
            break;

        case "int8":
            long int8Result = Long.parseLong(result);
            rowJsonObject.put(columnName, int8Result);
            break;
            
        case "decimal":
            BigDecimal decimalResult = new BigDecimal(result);
            rowJsonObject.put(columnName, decimalResult);
            break;
            
        case "numeric":
            BigDecimal numericResult = new BigDecimal(result);
            rowJsonObject.put(columnName, numericResult);
            break;
            
		case "varchar":
			rowJsonObject.put(columnName, result);
			break;

		case "graphid":
			Double doubleResult = resultSet.getDouble(columnName);

			rowJsonObject.put(columnName, doubleResult);
			break;

		case "text":
			rowJsonObject.put(columnName, result);
			break;

		case "jsonb":
			rowJsonObject.put(columnName, (JSONObject) parser.parse(result));
			break;

		case "graphpath":
		    Path path = pathParser.createParsedPath(result);
            rowJsonObject.put(columnName, path);
			break;

        case "vertex":
            Vertex vertext = vertexParser.createParsedVertext(result);
            rowJsonObject.put(columnName, vertext);
            break;
            
        case "edge":
            rowJsonObject.put(columnName, edgeParser.createParsedEdge(result));
            break;

        case "_vertex":
            List<Vertex> vertextList = vertexParser.createParsedVertextList(result);
            rowJsonObject.put(columnName, vertextList);
            break;
            
        case "_edge":
            rowJsonObject.put(columnName, edgeParser.createParsedVertextList(result));
            break;

		default:
			rowJsonObject.put(columnName, result);
			break;
		}
	}
	

	public void getJsonData(String node) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		
        jsonObject = (JSONObject) parser.parse(node);              
        
		List<String> arrNames = new ArrayList<>();
		
		Iterator<String> it = jsonObject.keySet().iterator();

		
		Map<String, Object> map = new HashMap<>();
		
		int i = 0;
		while (it.hasNext()) {
			String key = it.next();
			Object value = jsonObject.get(key);
			map.put(key, value);
			i++;
		}
	}

}
