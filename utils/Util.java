package inv.utils;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import inv.entity.Column;

public class Util {
	
	public static JSONArray getEntityColumnObject(String classPath) throws SecurityException, ClassNotFoundException {
		return getEntityColumns(Class.forName(classPath));
	}
	
	public static JSONArray getEntityColumns(Class<?> cls) {
		Field[] fields = cls.getDeclaredFields();
		System.out.println(Arrays.toString(fields));
		JSONArray columnList = new JSONArray();
		for(Field fd : fields)  {
			JSONObject property = new JSONObject();
			JSONObject columnName = new JSONObject();
			Column cl = fd.getAnnotation(Column.class);
			//System.out.println(cl);
			property.putOpt("name", cl.name());
			property.putOpt("diplay_name", cl.display_name());
			property.putOpt("is_primary", cl.is_primary());
			property.putOpt("is_search", cl.is_search());
			//columnName.putOpt(cl.name(), property);
			//System.out.println(columnName.toString());
			columnList.put(property);
			//System.out.println("###########"+columnList.toString());
			//System.out.println(fd.getName()+" ===== "+cl.name()+"===="+cl.display_name());
		}
		return columnList;
	}
}
