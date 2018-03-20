package it.reply.challenge.fantabosco.common;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.reply.challenge.fantabosco.model.Provider;
import it.reply.challenge.fantabosco.model.Order;
import it.reply.challenge.fantabosco.model.Project;
import it.reply.challenge.fantabosco.model.DataCenter;

public class Utils {
	
	private Utils() {
	}
	
	public static BigDecimal calculateAverageProjectLatency(Project project) {
		BigDecimal unitSum = BigDecimal.ZERO;
		BigDecimal unitSumLatency = BigDecimal.ZERO;
		for (Order order : project.getOrderList()) {
			String projectCountry = project.getTargetCounty();
			Integer latency = order.getRegion().getMappaLatencyCountries().get(projectCountry);
			Integer unitsInPackage = getTotalNumberOfUnitsPerPackage(order.getRegion().getServicesPerPackage());
			Integer quantity = order.getQuantity();
			unitSum = unitSum.add(new BigDecimal(quantity * unitsInPackage));
			unitSumLatency = unitSumLatency.add(new BigDecimal(latency * unitsInPackage));
		}
		return unitSumLatency.divide(unitSum);
	}
	
	public static BigDecimal calculateOverallAvailabilityIndex(List<String> serviceList, List<Project> projectList) {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal squaredSum = BigDecimal.ZERO;
		
		for (String serviceMain : serviceList) {
			BigDecimal qi = BigDecimal.ZERO;
			for (Project project : projectList) {
				for (Order order : project.getOrderList()) {
					Integer boughtUnits = order.getRegion().getServicesPerPackage().get(serviceMain);
					qi = qi.add(new BigDecimal(boughtUnits));
				}
			}
			total = total.add(qi);
			squaredSum = squaredSum.add(qi.multiply(qi));
		}
		
		return total.multiply(total).divide(squaredSum);
	}
	
	public static BigDecimal calculateOperationalProjectCost(Project project) {
		BigDecimal totalCost = BigDecimal.ZERO;
		for (Order order : project.getOrderList()) {
			Integer packageQuantity = order.getQuantity();
			BigDecimal packageCost = order.getRegion().getPackageCost();
			totalCost = totalCost.add(packageCost.multiply(new BigDecimal(packageQuantity)));
		}
		return totalCost;
	}
	
	private static Integer getTotalNumberOfUnitsPerPackage(Map<String, Integer> mapServicePerPackage) {
		Integer total = 0;
		
		for (Integer serviceNumber : mapServicePerPackage.values()) {
			total += serviceNumber;
		}
		
		return total;
	}
	
	public boolean areOrdersWithinAvailability(List<Project> projects){
		HashMap<DataCenter, Integer> orderedPackages = new HashMap<>();
		for (Project x : projects){
			for(Order y : x.getOrderList()){
				if (orderedPackages.containsKey(y.getRegion())) {
					Integer temp = orderedPackages.get(y.getRegion());
					temp = temp + y.getQuantity();
					orderedPackages.put(y.getRegion(), temp);
				} else {
					orderedPackages.put(y.getRegion(), y.getQuantity());
				}
			}
		}
		for (Map.Entry<DataCenter, Integer> entry : orderedPackages.entrySet()) {
			if (entry.getKey().getPackagesAvailable()<entry.getValue()) {
				return false;
			}
		}
		return true;
	}
	
	public static List<Provider> orderProvidersByServiceAvailability(List<Provider> providerList) {
		providerList.sort(new Comparator<Provider>() {

			@Override
			public int compare(Provider provider1, Provider provider2) {
				Integer totalProvider1 = 0;
				Integer totalProvider2 = 0;
				for (DataCenter region : provider1.getListaRegionalDataCenter()) {
					Integer totalNumberOfUnitsPerPackage = getTotalNumberOfUnitsPerPackage(region.getServicesPerPackage());
					Integer numberOfPackages = region.getPackagesAvailable();
					Integer totalUnitsPerRegion = totalNumberOfUnitsPerPackage * numberOfPackages;
					totalProvider1 += totalUnitsPerRegion;
				}
				for (DataCenter region : provider2.getListaRegionalDataCenter()) {
					Integer totalNumberOfUnitsPerPackage = getTotalNumberOfUnitsPerPackage(region.getServicesPerPackage());
					Integer numberOfPackages = region.getPackagesAvailable();
					Integer totalUnitsPerRegion = totalNumberOfUnitsPerPackage * numberOfPackages;
					totalProvider2 += totalUnitsPerRegion;
				}
				
				return totalProvider1.compareTo(totalProvider2);
			}
			
		});
		return providerList;
	}
}
