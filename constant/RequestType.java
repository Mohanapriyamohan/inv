package inv.constant;

public enum RequestType {
	GET(1,"GET", "get", true),
	GETALL(2,"GET", "get", false),
	CREATE(3,"POST", "create", true),
	UPDATE(4,"PUT", "update", true),
	DELETE(5,"DELETE", "delete", true);
	
	int index = 0;
	String type = null;
	String method = null;
	boolean param = false;
	
	RequestType(int idx, String type, String method, boolean flag){
		this.index = idx;
		this.type = type;
		this.method = method;
		this.param = flag;
	}
	
	public int getIndex() {
		return this.index;
	} 

	public String getType() {
		return this.type;
	} 
	
	public String getMethod() {
		return this.method;
	}
	
	public static RequestType getInstance(int idx) {
		for(RequestType rt : RequestType.values()){
			if(rt.getIndex() == idx) {
				return rt;
			}
		}
		return null;
	}
	
	public RequestType getInstance(String type) {
		for(RequestType rt : RequestType.values()){
			if(rt.getType().equals(type)) {
				return rt;
			}
		}
		return null;
	}
}
