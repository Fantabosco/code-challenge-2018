package it.reply.challenge.fantabosco.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DataCenter {

	String region;
	Map<String, Integer> mappaLatencyCountries;
	private BigDecimal packageCost;
	private int packagesAvailable;
	private Map<String, Integer> servicesPerPackage;

	public DataCenter() {
		this.mappaLatencyCountries = new HashMap<>();
	}

	public Map<String, Integer> getMappaLatencyCountries() {
		return mappaLatencyCountries;
	}

	public void setMappaLatencyCountries(
			Map<String, Integer> mappaLatencyCountries) {
		this.mappaLatencyCountries = mappaLatencyCountries;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setPackageCost(BigDecimal packageCost) {
		this.packageCost = packageCost;
	}

	public BigDecimal getPackageCost() {
		return packageCost;
	}

	public void setPackagesAvailable(int packagesAvailable) {
		this.packagesAvailable = packagesAvailable;
	}

	public void setServicesPerPackage(
			Map<String, Integer> servicesPerPackage) {
		this.servicesPerPackage = servicesPerPackage;
	}

	public int getPackagesAvailable() {
		return packagesAvailable;
	}

	public Map<String, Integer> getServicesPerPackage() {
		return servicesPerPackage;
	}
	
}
