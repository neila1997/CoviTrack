package com.neilinc.covitrack.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.neilinc.covitrack.models.User;

@Repository
@Transactional
public class DaoImpl {
	
	@Autowired
	EntityManager em;
	
	public boolean registerForAlerts(User user) {
		
		try {
		TypedQuery<User> query = em.createQuery("select user from User user where user.email = :email", User.class);
		List<User>  list = query.setParameter("email", user.getEmail()).getResultList();
		if (list.size() > 0) {
			System.out.println("User Already Exists");
			return false;
		}
		else
			em.persist(user);
		
		return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public List<Integer> retrieveDistinctDistricts(){
		
		TypedQuery<Integer> query = em.createQuery("select distinct user.city_id from User user", Integer.class);
		List<Integer> districts = query.getResultList();
		
		return districts;
	}
	
	public List<User> retrieveUserByDistrict(int district){

		TypedQuery<User> query = em.createQuery("select user from User user where user.city_id = :city_id", User.class).setParameter("city_id", district);
		List<User> users = query.getResultList();
		
		return users;
	}
 
}
