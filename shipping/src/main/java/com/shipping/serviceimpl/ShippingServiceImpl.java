package com.shipping.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shipping.model.Shipping;
import com.shipping.model.User;
import com.shipping.repository.ShippingRepository;
import com.shipping.service.ShippingService;

@Service
public class ShippingServiceImpl implements ShippingService{

	@Autowired
	private ShippingRepository shippingRepository;
	
	@Override
	public Shipping save(Shipping shipping) {
		// TODO Auto-generated method stub
		return shippingRepository.save(shipping);
	}

	@Override
	public List<Shipping> findAll() {
		// TODO Auto-generated method stub
		return shippingRepository.findAll();
	}

	@Override
	public Optional<Shipping> findById(int id) {
		// TODO Auto-generated method stub
		return shippingRepository.findById(id);
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		shippingRepository.deleteById(id);
	}

	@Override
	public List<Shipping> findByUser(User user) {
		// TODO Auto-generated method stub
		return shippingRepository.findByUser(user);
	}

}
