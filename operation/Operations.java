package inv.operation;

import java.util.List;

import org.json.JSONObject;

public interface Operations {
	public JSONObject get(String id)throws Exception;
	public List<JSONObject> get(String[] ids)throws Exception;
	public JSONObject create(JSONObject json)throws Exception;
	public JSONObject update(JSONObject json)throws Exception;
	public boolean delete(String id);
}
