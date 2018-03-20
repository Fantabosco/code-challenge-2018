package it.reply.challenge.fantabosco.model;

import java.util.List;
import java.util.Map;

public class Project {
	String targetCounty;
	Map<String, Integer> unitPerServices;
	int slaPenality;
	List<Order> orderList;
	
	public String getTargetCounty() {
		return targetCounty;
	}

	public void setTargetCounty(String targetCounty) {
		this.targetCounty = targetCounty;
	}

	public Map<String,Integer> getUnitPerServices() {
		return unitPerServices;
	}

	public void setUnitPerDay(Map<String,Integer> unitPerServices) {
		this.unitPerServices = unitPerServices;
	}

	public int getSlaPenality() {
		return slaPenality;
	}

	public void setSlaPenality(int slaPenality) {
		this.slaPenality = slaPenality;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
}
