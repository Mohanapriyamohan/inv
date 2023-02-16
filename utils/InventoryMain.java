package inv.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


import org.json.JSONArray;
import org.json.JSONObject;

import inv.constant.EntityMapper;
import inv.constant.RequestType;
import inv.entity.Column;
import inv.entity.Entity;
import inv.entity.Product;
import inv.utils.Util;

class InventoryMain {
	public static void main(String args[]) {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("select below option (ex: press 1 for product)");

			EntityMapper[] enumValues = EntityMapper.values();
			for (EntityMapper value : enumValues) {
				System.out.println(value.getIndex() + "  " + value.name());
			}
			int selectedEntity = sc.nextInt();
			EntityMapper entityMapperObj = EntityMapper.getInstance(selectedEntity);
			
			RequestType[] reqTypeValues = RequestType.values();
			for (RequestType value : reqTypeValues) {
				System.out.println(value.getIndex() + "  " + value.name());
			}
			int selectedMethod = sc.nextInt();
			RequestType requestTypeObj = RequestType.getInstance(selectedMethod);
			entityMapperObj.setRt(requestTypeObj);
			String classPath = entityMapperObj.getClassPath();
			String entityPath = entityMapperObj.getEntityPath();
			JSONArray entityColumns = Util.getEntityColumnObject(entityPath);


			JSONObject inputJson = new JSONObject();
			String tempVal = null;
			for (Object columnObj : entityColumns) {
				JSONObject columnMeta = (JSONObject) columnObj;
				if(requestTypeObj != RequestType.GETALL) {
				if (requestTypeObj == RequestType.GET || requestTypeObj == RequestType.DELETE) {
					if (columnMeta.optBoolean("is_search")) {
						System.out.println("Enter " + columnMeta.optString("diplay_name"));
						tempVal = sc.next();
						if (tempVal != null) {
							inputJson.put(columnMeta.optString("name"), tempVal);
						}
					}
				} else {
					if (!columnMeta.optBoolean("is_primary")) {
						System.out.println("Enter " + columnMeta.optString("diplay_name"));
						tempVal = sc.next();
						if (tempVal != null) {
							inputJson.put(columnMeta.optString("name"), tempVal);
						}
					} else {
						inputJson.put(columnMeta.optString("name"), -1);
					}
				}
			}
			}


			Class<?> c = Class.forName(entityMapperObj.getClassPath());
			Method method = null;
			Object obj = null;
			if (requestTypeObj == RequestType.GET) {
				method = c.getDeclaredMethod(requestTypeObj.getMethod().toLowerCase(), String.class);
				obj = method.invoke(c.newInstance(), tempVal);
				view(obj);
			} else if(requestTypeObj == RequestType.GETALL) {
				method = c.getDeclaredMethod(requestTypeObj.getMethod().toLowerCase());
				obj = method.invoke(c.newInstance());
				view(obj);
				
			} else {
				method = c.getDeclaredMethod(requestTypeObj.getMethod().toLowerCase(), JSONObject.class);
				obj = method.invoke(c.newInstance(), inputJson);
				System.out.println(obj);
			}

			//System.out.println("FINAL OUTPUT :: " + obj.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getName() {
		return null;
	}
	
	public static void view(Object obj) {

		JSONArray data = (JSONArray) obj;
		if (data != null) {
			Iterator<Object> iterator = data.iterator();
			int i =0;
	
		while (iterator.hasNext()) {
			JSONObject Value = (JSONObject) iterator.next();
			Set<String> keyValue = Value.keySet();
			if(i == 0) {
			for (String key : keyValue) {
				System.out.print(key + "\t\t\t\t\t");
			}
			System.out.println();
			i++;
			}
			for (String key : keyValue) {
				System.out.print(Value.get(key) + "\t\t\t\t\t");
			}
			System.out.println();
		}
		}
		// Iterating JSON array
	}
	
	

} //class ends here 
