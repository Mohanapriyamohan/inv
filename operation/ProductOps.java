package inv.operation;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import inv.constant.RequestType;
import inv.entity.Product;
import inv.utils.DBUtils;

public class ProductOps implements Operations {

	@Override
	public JSONArray get(String Name) throws Exception {
		
		// TODO Auto-generated method stub
		Product product = new Product();
		product.setName(Name);
		try {
			return new JSONArray(DBUtils.getResult(product, RequestType.GET));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}      

	@Override
	public List<JSONObject> get(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public JSONArray get() throws Exception{
		try {
			Product product = new Product();
			return new JSONArray(DBUtils.getResult(product, RequestType.GETALL));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	@Override
	public JSONObject create(JSONObject json) throws Exception {
		System.out.println("inside Create");
		Product product = new Product();
		product.setValues(json);
		String result = DBUtils.getResult(product, RequestType.CREATE);
		// TODO Auto-generated method stub
		return new JSONObject().put("result", result);
	}

	@Override
	public JSONObject update(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inside update");
		Product product = new Product();
		product.setValues(json);
		String result = DBUtils.getResult(product, RequestType.UPDATE);
		return null;
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
