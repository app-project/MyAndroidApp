package com.app.myapp.model;

import com.app.myapp.ahibernate.Column;
import com.app.myapp.ahibernate.Table;

/**
 * Cigarette entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Table(name = "CartInfo")
public class CartInfo implements java.io.Serializable {

	// Fields
	
	//@Id
	@Column(name = "id", length = 11)
	private Integer id;
	
	@Column(name = "name",type="TEXT", length = 20)
	private String name;
	
	@Column(name = "type",type="TEXT", length = 10)
	private String type;
	
	@Column(name = "pic",type="TEXT", length = 100)
	private String pic;
	
	@Column(name = "status",type="INTEGER", length = 11)
	private Integer status;
	
	@Column(name = "packagcolor",type="TEXT", length = 30)
	private String packagcolor;
	
	@Column(name = "catrame",type="NUMERIC", length = 11)
	private Double catrame;
	
	@Column(name = "lengthen",type="NUMERIC", length = 11)
	private Double lengthen;
	
	@Column(name = "price",type="NUMERIC", length = 11)
	private Double price;
	
	@Column(name = "packageform",type="TEXT", length = 50)
	private String packageform;
	
	@Column(name = "num",type="INTEGER", length = 11)
	private Integer num;

	// Constructors

	/** default constructor */
	public CartInfo() {
	}

	/** full constructor */
	public CartInfo(String name, String type, String pic, Integer status,
			String packagcolor, Double catrame, Double lengthen, Double price,
			String packageform, Integer num) {
		this.name = name;
		this.type = type;
		this.pic = pic;
		this.status = status;
		this.packagcolor = packagcolor;
		this.catrame = catrame;
		this.lengthen = lengthen;
		this.price = price;
		this.packageform = packageform;
		this.num = num;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPackagcolor() {
		return this.packagcolor;
	}

	public void setPackagcolor(String packagcolor) {
		this.packagcolor = packagcolor;
	}

	public Double getCatrame() {
		return this.catrame;
	}

	public void setCatrame(Double catrame) {
		this.catrame = catrame;
	}

	public Double getLengthen() {
		return this.lengthen;
	}

	public void setLengthen(Double lengthen) {
		this.lengthen = lengthen;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPackageform() {
		return this.packageform;
	}

	public void setPackageform(String packageform) {
		this.packageform = packageform;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}