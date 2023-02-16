package inv.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

import inv.constant.RequestType;
import inv.entity.Entity;

public class DBUtils {

	public static <T extends Entity> String getQuery(T entity, RequestType type) throws SecurityException, ClassNotFoundException {
		StringBuffer query = new StringBuffer();
		switch(type.name()) {
			case "GET":
			case "GETALL":
				query.append("select * from ").append(entity.getClass().getSimpleName().toLowerCase());
				  if( entity.getName() != null) { // select * query
					  query.append(" where "+ getColumnNameString(entity, false, true) + " like  ? ");
					  } else {
						  setRange(entity,query).toString(); 
					  }
				break;  
			case "UPDATE": 
				query.append("update ").append(entity.getClass().getSimpleName().toLowerCase()).append(" set");
				query.append("(").append(getPlaceHoderString(entity.getColumns(),entity.getColumns().length-1)).append(")");
				query.append(" where "+ getColumnNameString(entity, false, true) + " =  ? ");
				break;
			case "CREATE":
				query.append("insert into ").append(entity.getClass().getSimpleName().toLowerCase()).append("(").append(getColumnNameString(entity, false, false)).append(")").append(" values");
				query.append("(").append(getPlaceHoderString(entity.getColumns().length-1)).append(")");
				
				break;
			case "DELETE":
				break;
		}
		query.append(";");
		return query.toString();
	}
	
	public static <T extends Entity> String getResult(T entity, RequestType type)
			throws SecurityException, ClassNotFoundException {
		String query = getQuery(entity, type);
		System.out.println(query);
		HashMap rs = getResult(query, getEntityValues(entity, type));
		String result = null;
		if (Long.valueOf(rs.get("primary_id").toString()) != -1) {
			result = entity.getClass().getSimpleName() + " added sucessfully";
		} else if (rs.get("result_set") != null) {
			result =  (String) rs.get("result_set");
		}
      		return result;

	}
	
	public static String getPlaceHoderString(int len) { 
		StringBuffer buf = new StringBuffer();
		for(int i=0;i<len;i++) {
			buf.append("?");
			if(i != len-1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}
	
	public static <T extends Entity> String getColumnNameString(T entity, boolean isPrimary, boolean isSearch) throws SecurityException, ClassNotFoundException {
		StringBuffer buf = new StringBuffer();
		JSONArray entityColumns = Util.getEntityColumnObject(entity.getClass().getName());
		int len = entityColumns.length();
		for(int i = 0; i < len ;i++) {
			JSONObject jsonObj = (JSONObject) entityColumns.getJSONObject(i);
			if (jsonObj.getBoolean("is_search") && isSearch) {
				buf.append(jsonObj.optString("name")).append(",");
			} else if(jsonObj.getBoolean("is_primary") && isPrimary) {
				buf.append(jsonObj.optString("name")).append(",");
			} else if(!jsonObj.getBoolean("is_primary") && !isSearch) {
				buf.append(jsonObj.optString("name")).append(",");
			}
		}
		String result = buf.toString();
		return result.substring(0,result.length()-1);
	     }
	
	public static String getPlaceHoderString(Field[] columns, int len) {
		StringBuffer buf = new StringBuffer();
		for(int i=0;i<len;i++) {
			buf.append("?");
			if(i != len-1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}
		
	public <T extends Entity> String deleteQuery(T entity, long id) {
		return null;
	}
	
	public static <T extends Entity> StringBuffer setRange(T entity, StringBuffer query) {
	
		return query.append(" limit ").append(entity.getStartIndex()).append(",").append(entity.getCount());
	}
	
	public static HashMap getResult(String query, Object... args) {
		Connection con = null;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory?autoReconnect=true&useSSL=false","root","Root@123");  
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 
			if (args != null) {
				mapParams(stmt, args);
			}
			System.out.println(stmt);
			ResultSet rs = null;
			ResultSet rsNew = null;
			int status = -1;
			String result = null;
			Long primaryId = -1L; 
			if(query.contains("select")) {
				rs=stmt.executeQuery();
				result = convertJsonObj(rs).toString();
			}else {  
				status = stmt.executeUpdate();
				if(query.contains("insert")) {
					rsNew = stmt.executeQuery("select last_insert_id();");
					//select *from LastInsertedRow where Id=(SELECT LAST_INSERT_ID()); this can be used to get whole row;
					rsNew.first();
					primaryId = rsNew.getLong("last_insert_id()")+1;
				}
			}
			//System.out.println("==========="+rs.toString());
			HashMap map = new HashMap();
			map.put("result_set",result);
			map.put("primary_id",primaryId);
			return map;  
		}catch(Exception e){ 
			System.out.println(e);
		}finally{
			if(con != null) {
				try {
				con.close();
				}catch(Exception ex) {}
			}
		}
		return null;
	}
	
	
	
	public static boolean mapParams(PreparedStatement ps, Object... args) throws SQLException {
		int i = 1;
		for (Object arg : args) {
			if (arg instanceof Date) {
				ps.setTimestamp(i++, new Timestamp(((Date) arg).getTime()));
			} else if (arg instanceof Integer) {
				ps.setInt(i++, (Integer) arg);
			} else if (arg instanceof Long) {
				ps.setLong(i++, (Long) arg);
			} else if (arg instanceof Double) {
				ps.setDouble(i++, (Double) arg);
			} else if (arg instanceof Float) {
				ps.setFloat(i++, (Float) arg);
			} else {
				ps.setString(i++, (String) arg);
			}
		}
		return true;
	}
	
	public static <T extends Entity> Object[] getEntityValues(T entity, RequestType type) {
		Object[] arr = null; 
		if (type != RequestType.GETALL) {
		JSONObject json = entity.getValueJson();
		if(type == RequestType.GET) {
			arr = new Object[1];
			arr[0] = entity.getName();
		}else if(type == RequestType.CREATE || type == RequestType.UPDATE) {
			JSONArray jsonArr = Util.getEntityColumns(entity.getClass());
			int len = jsonArr.length();
			if(type == RequestType.CREATE) {
				len = len -1;
			}
			arr = new Object[len];
			int i = 0;
			for(Object column : jsonArr) {
				JSONObject clm = (JSONObject)column;
				System.out.println(clm+"name::::: "+clm.optString("name"));
				if(type == RequestType.UPDATE) {
					arr[i] = json.opt(clm.optString("name"));
					System.out.println("ENTITY VALUE ::: "+arr[i]);
					i++;
				}else {
					if(!clm.optBoolean("is_primary")) {
						arr[i] = json.opt(clm.optString("name"));
						System.out.println("ENTITY VALUE ::: "+arr[i]);
						i++;
					}
				}				
			}
		}
	}
		return arr;
	}	



public static String convertString(ResultSet rs) {

    StringBuffer buf = new StringBuffer();
    buf.append("[");
    try {
        ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
        int nColumns = metaData.getColumnCount();
        while (rs.next()) {
        for (int i = 1; i <= nColumns; ++i) {
            buf.append(metaData.getColumnName(i));
            buf.append(" = ");
            buf.append(rs.getString(i));
            if (i < nColumns)
                buf.append(" , ");
        }
        }
    } catch (SQLException e) {
        buf.append(e.getMessage());
        e.printStackTrace();
    }
    buf.append("]");

    return buf.toString();
}


public static JSONArray convertJsonObj(ResultSet rs) {
	JSONArray json = new JSONArray();
	ResultSetMetaData rsmd;
	try {
		rsmd = (ResultSetMetaData) rs.getMetaData();
	
	while(rs.next()) {
	  int numColumns = rsmd.getColumnCount();
	  JSONObject obj = new JSONObject();
	  for (int i=1; i<=numColumns; i++) {
	    String column_name = rsmd.getColumnName(i);
	    obj.put(column_name, rs.getObject(column_name));
	  }
	  json.put(obj);
	}

} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	return json;
}


} // class end here








