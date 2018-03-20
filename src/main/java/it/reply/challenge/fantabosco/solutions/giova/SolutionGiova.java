package it.reply.challenge.fantabosco.solutions.giova;

import it.reply.challenge.fantabosco.model.DataCenter;
import it.reply.challenge.fantabosco.model.Order;
import it.reply.challenge.fantabosco.model.Project;
import it.reply.challenge.fantabosco.model.Provider;
import it.reply.challenge.fantabosco.solutions.ISolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SolutionGiova implements ISolver {

	@Override
	public List<Project> solve(List<Provider> providers,
			List<String> services, List<String> countries,
			List<Project> projects) {

		// TODO Ottimizzazione: prova a cambiare l'ordine dei progetti

		for (Project p : projects) {
			List<Order> orderList = new ArrayList<>();
			List<OrderWeighted> listaCoefficienti = new ArrayList<>();

			for (Provider provider : providers) {
				for (DataCenter region : provider
						.getListaRegionalDataCenter()) {
					// Calcola il coefficiente del package
					int coefficienteMagicoPessiva;
					// TODO ottimizzazione: cambia i pesi
					int pesoLatency = 1;
					int pesoQualitaPrezzo = 1;

					// Quanta latency ha?
					int coeffLat = pesoLatency
							* region.getMappaLatencyCountries().get(
									p.getTargetCounty());

					// Quanto mi costerebbe servirmi solo da lui e
					// raggiungere il target?
					int maxPackage = 0;
					for (Entry<String, Integer> entry : region
							.getServicesPerPackage().entrySet()) {
						// se il servizio mi serve ed è disponibile
						if (p.getUnitPerServices().containsKey(entry.getKey()) && entry.getValue() > 0) {
							int unitNecessarie = p.getUnitPerServices().get(
									entry.getKey());
							int packageRichiesti = Double
									.valueOf(
											Math.ceil(unitNecessarie
													/ entry.getValue()))
									.intValue();
							if (packageRichiesti > maxPackage) {
								maxPackage = packageRichiesti;
							}
						}
					}
					int coeffQualita = pesoQualitaPrezzo * maxPackage
							* region.getPackageCost().intValue();

					//TODO Quanto mi è utile discosta in difetto dalla mia
					// necessità (se mi servissi solo con lui)?

					//TODO qual è la penalità?
					
					coefficienteMagicoPessiva = coeffLat + coeffQualita;

					// Inserisci il valore del coefficiente
					OrderWeighted key = new OrderWeighted();
					key.setProvider(provider);
					key.setRegion(region);
					key.setQuantity(region.getPackagesAvailable());
					key.setCoeff(coefficienteMagicoPessiva);
					listaCoefficienti.add(key);

				}
			}

			// Ordina gli Order per coefficiente
			Collections.sort(listaCoefficienti, new Comparator<OrderWeighted>() {
				public int compare(OrderWeighted a, OrderWeighted b) {
					return a.getCoeff() - b.getCoeff();
				}
			});
			
			// Genera gli ordini, sottraendo i packagesAvailable
			for(Order order : listaCoefficienti) {
				int available = order.getRegion().getPackagesAvailable();
				
				// TODO quanto prendo? per ora la metà dei disponibili più uno, per distribuire gli acquisti
				int taken = Math.min(available,  order.getQuantity() / 2) + 1;
				if(taken <= 0) {
					continue;
				}
				order.setQuantity(taken);
				order.getRegion().setPackagesAvailable(available - taken);
					
				orderList.add(order);
				if(isProjectResolved(p, orderList)) {
					break;
				};
			}
			if(!orderList.isEmpty()) {
				p.setOrderList(orderList);
			}
		}
		return projects;
	}

	private boolean isProjectResolved(Project p, List<Order> orderList) {
		Map<String,Boolean> mappaOk = new HashMap<>();
		for(Entry<String, Integer> i: p.getUnitPerServices().entrySet()) {
			mappaOk.put(i.getKey(), Boolean.FALSE);
		}
		for(Order o: orderList) {
			for(Entry<String, Integer> i : o.getRegion().getServicesPerPackage().entrySet()) {
				int servonoAncora = p.getUnitPerServices().get(i.getKey());
				servonoAncora -= i.getValue();
				// Aggiorna quanti servono ancora
				p.getUnitPerServices().put(i.getKey(), servonoAncora);
				if(servonoAncora <= 0) {
					mappaOk.put(i.getKey(), Boolean.TRUE);
				}
			}
		}
		for(Boolean i: mappaOk.values()) {
			if(!i) {
				return false;
			}
		}
		return true;
	}

}
