package inv.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

			// System.out.println(entityColumns);

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

			//System.out.println("Input JSON :: " + inputJson.toString());

			Class<?> c = Class.forName(entityMapperObj.getClassPath());
			Method method = null;
			Object obj = null;
			if (requestTypeObj == RequestType.GET) {
				method = c.getDeclaredMethod(requestTypeObj.getMethod().toLowerCase(), String.class);
				obj = method.invoke(c.newInstance(), tempVal);
			} else if(requestTypeObj == RequestType.GETALL) {
				System.out.println("hhhh");
				method = c.getDeclaredMethod(requestTypeObj.getMethod().toLowerCase());
				obj = method.invoke(c.newInstance());
			} else {
				method = c.getDeclaredMethod(requestTypeObj.getMethod().toLowerCase(), JSONObject.class);
				obj = method.invoke(c.newInstance(), inputJson);
			}

			System.out.println("FINAL OUTPUT :: " + obj.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getName() {
		return null;
	}

}
