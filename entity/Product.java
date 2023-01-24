package inv.entity;

import org.json.JSONObject;

public class Product extends Entity {
	@Column(name = "PRODUCT_ID", display_name = "Product ID", is_primary = true)
	long productId = -1;
	@Column(name = "PRODUCT_NAME", display_name = "Product Name", is_search = true)
	String productName;
	@Column(name = "PRODUCT_CODE", display_name = "Product Code")
	String productCode;
	@Column(name = "PRODUCT_DESC", display_name = "Product Description")
	String productDesc;
	@Column(name = "PRODUCT_MAX_DISCOUNT", display_name = "Product Max Discount")
	double maxDiscount = -1;
	@Column(name = "PRODUCT_MIN_DISCOUNT", display_name = "Product Min Discount")
	double minDiscount = -1;
	@Column(name = "CATEGORY_ID", display_name = "Product Category")
	long categoryId = -1;

	public Product() {

	}

	public Product(JSONObject json) throws IllegalArgumentException, IllegalAccessException {
		this.setValues(json);
	}

	@Override
	public Object getId() {
		return productId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long product_id) {   
		this.productId = product_id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String product_name) {
		this.productName = product_name;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String product_code) {
		this.productCode = product_code;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String product_desc) {
		this.productDesc = product_desc;
	}

	public double getMaxDiscount() {
		return maxDiscount;
	}

	public void setMaxDiscount(double max_discount) {
		this.maxDiscount = max_discount;
	}

	public double getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(double min_dicount) {
		this.minDiscount = min_dicount;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long category_id) {
		this.categoryId = category_id;
	}

	@Override
	public void setId(long productId) {
		this.productId = productId;
	}

}
