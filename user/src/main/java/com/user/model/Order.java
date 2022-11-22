package com.user.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;




import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	private User user;
	private double orderTotal;
	
	@ManyToOne
	private Shipping shippingDetails;
	
	@ManyToOne
	private Coupon coupon;
	
	@ManyToMany
	@JoinTable(name = "orders_products", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
	private List<Product> products;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	@CreatedDate
	private Date orderDate;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIME)
	@CreatedDate
	private Date orderTime;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;
	
}
