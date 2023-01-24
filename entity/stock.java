package inv.entity;

public class stock extends Entity {
	
	int stock_id;
	int quantity ;
	long date_of_purchase;
	int discount_rate;
	float unit_price;
	String category;
	
	

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub

	}

}
