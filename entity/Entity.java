package inv.entity;

import java.lang.reflect.*;

import org.json.JSONObject;

public abstract class Entity {
	int startIndex = 1;
	public int getStartIndex() {
		return startIndex;
	}
	
	JSONObject valueJson = null;
	
	public void setValueJson(JSONObject json){
		valueJson = json;
	}
	
	public JSONObject getValueJson(){
		return valueJson;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	int count = 20;
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	boolean isExist = false;
	
	public abstract Object getId();                     
	
	public abstract void setId(long id);
	
	public boolean isExist() {
		return this.isExist;
	}
	
	public void setIsExist(boolean isEntityExist) {
		this.isExist = isEntityExist;
	}
	
	public Field[] getColumns() {
		return this.getClass().getDeclaredFields();
	}
	
	public void setValues(JSONObject json) throws IllegalArgumentException, IllegalAccessException {  
		this.valueJson = json;
		Field[] fields = getColumns();
		for(int i = 0; i < fields.length; i++) {
	         System.out.println("Field = " + fields[i].toString());
	         String fieldName = fields[i].getName().toString();
	         fieldName = fieldName.replace("set", "");
	         if(json.opt(fieldName) != null) {
	        	 fields[i].set(this, json.opt(fieldName));
	         }
	    }
	}
}