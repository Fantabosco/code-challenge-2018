package it.reply.challenge.fantabosco.solutions;

import it.reply.challenge.fantabosco.model.Provider;
import it.reply.challenge.fantabosco.model.Order;
import it.reply.challenge.fantabosco.model.Project;
import it.reply.challenge.fantabosco.model.DataCenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SolutionGreedy implements ISolver {

	@Override
	public List<Project> solve(List<Provider> providers,List<String> services,
			List<String> countries,List<Project> projects) {
		// per ogni progetto...
		Iterator projectIter = projects.iterator();
		while (projectIter.hasNext()) {
			Project p = (Project) projectIter.next();
			p.setOrderList(new ArrayList<>());
			// per ogni servizio necessario...
//			Iterator serviceIter = p.getUnitPerServices().entrySet().iterator();
//			while (serviceIter.hasNext()) {
//				Map.Entry<String, Integer> service = (Map.Entry<String, Integer>) serviceIter.next();
			for (Map.Entry<String, Integer> service : p.getUnitPerServices().entrySet()) {
				// se non ho comprato tutte le unità...
				if (service.getValue() > 0) {
					for (Provider csp : providers) {
						if (p.getUnitPerServices().get(service.getKey()) <= 0) {
							break;
						}
						Collections.sort(csp.getListaRegionalDataCenter(), new LatencyComparator(p.getTargetCounty()));
						for (DataCenter rdc : csp.getListaRegionalDataCenter()) {
							if (p.getUnitPerServices().get(service.getKey()) <= 0) {
								break;
							}
							Order order = new Order();
							order.setQuantity(0);
							order.setProvider(csp);
							order.setRegion(rdc);
							while (rdc.getPackagesAvailable() > 0
									&& rdc.getServicesPerPackage().get(service.getKey()) > 0
									&& p.getUnitPerServices().get(service.getKey()) > 0) {
								// compro un package
								rdc.setPackagesAvailable(rdc.getPackagesAvailable() - 1);
								Map<String, Integer> servicesCopy = new HashMap<>();
								servicesCopy.putAll(p.getUnitPerServices());
								Iterator serviceIter = servicesCopy.entrySet().iterator();
								while (serviceIter.hasNext()) {
									Map.Entry<String, Integer> s = (Map.Entry<String, Integer>) serviceIter.next();
									for (Map.Entry<String, Integer> servPackage : rdc.getServicesPerPackage().entrySet()) {
										if (s.getKey().equals(servPackage.getKey())) {
											s.setValue(s.getValue() - servPackage.getValue());
											break;
										}
									}
								}
								p.setUnitPerDay(servicesCopy);
//									for (Map.Entry<String, Integer> s : rdc.getServicesPerPackage().entrySet()) {
//										p.getUnitPerServices().get(s.getKey())
//									}
//								service.setValue(service.getValue() - rdc.getServicesPerPackage().get(service.getKey()));
								order.setQuantity(order.getQuantity() + 1);
							}
							if (order.getQuantity() > 0) {
								boolean trovato = false;
								for (Order o : p.getOrderList()) {
									if (o.getProvider().getName().equals(csp.getName())
											&& o.getRegion().getRegion().equals(rdc.getRegion())) {
										o.setQuantity(o.getQuantity() + order.getQuantity());
										trovato = true;
										break;
									}
								}
								if (!trovato) {
									p.getOrderList().add(order);
								}
							}
						}
					}
				}
			}
		}
		return projects;
	}

}

class LatencyComparator implements Comparator<DataCenter>
{
   public String c;
   public LatencyComparator(String c){
       this.c=c;
   }
    public int compare(DataCenter c1, DataCenter c2)
    {
        return c1.getMappaLatencyCountries().get(c).compareTo(c2.getMappaLatencyCountries().get(c));
    }
}
