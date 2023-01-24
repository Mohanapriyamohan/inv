package inv.constant;

public enum EntityMapper {
	PRODUCT(1, "ProductOps", "inv.operation", "inv.entity.Product"),
	STOCK(2, "StockOps", "inv.operation", "inv.entity.Stock"),
	SALE(3, "SaleOps", "inv.operation", "inv.entity.Sale");
	
	int index = 0;
	RequestType rt = null;
	String mapper = null;
	String path = null;
	String entityPath = null;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public RequestType getRt() {
		return rt;
	}

	public void setRt(RequestType rt) {
		this.rt = rt;
	}

	public String getMapper() {
		return mapper;
	}

	public void setMapper(String mapper) {
		this.mapper = mapper;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getClassPath() {
		return path +"."+ mapper;
	}
	
	public String getEntityPath() {
		return entityPath;
	}
	
	private EntityMapper(int idx, String mapperClass, String mapperPackage, String entityPath) {
		this.index = idx;
		this.mapper = mapperClass;
		this.path = mapperPackage;
		this.entityPath = entityPath;
	}
	
	public static EntityMapper getInstance(int id) {
		for(EntityMapper em : EntityMapper.values()){
			if(em.getIndex() == id) {
				return em;
			}
		}
		return null;
	}
	
	/*public EntityMapper getInstance(RequestType rt) {
		for(EntityMapper em : EntityMapper.values()){
			if(em.getRt() == rt.getClass()) {
				return em;
			}
		}
		return null;
	}*/
}







