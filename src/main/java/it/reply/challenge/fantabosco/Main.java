package it.reply.challenge.fantabosco;

import it.reply.challenge.fantabosco.model.Provider;
import it.reply.challenge.fantabosco.model.Order;
import it.reply.challenge.fantabosco.model.Project;
import it.reply.challenge.fantabosco.model.DataCenter;
import it.reply.challenge.fantabosco.solutions.ISolver;
import it.reply.challenge.fantabosco.solutions.giova.SolutionGiova;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	private static String FILE_EXAMPLE = "zero_adventure.in";
	private static String FILE_1 = "first_adventure.in";
	private static String FILE_2 = "second_adventure.in";
	private static String FILE_3 = "third_adventure.in";
	private static String FILE_4 = "fourth_adventure.in";

	private static String inputFile;

	private static List<Provider> providers = new ArrayList<>();
	private static List<String> services = new ArrayList<>();
	private static List<String> countries = new ArrayList<>();
	private static List<Project> projects = new ArrayList<>();
	

	public static void main(String[] args) {
		readFile(FILE_2); // TODO Impostare il file da analizzare

		ISolver solver = new SolutionGiova(); // TODO Inserire implementazione soluzione
		List<Project> solution = solver.solve(new ArrayList<Provider>(providers),new ArrayList<String>(services),new ArrayList<String>(countries),new ArrayList<Project>(projects));

		try {
			evaluateSolution(solution);
			writeSolution(solution);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void readFile(String fileName) {
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try {
			inputFile = "src/main/resources/" + fileName;
			fileReader = new FileReader(inputFile);
			bufferedReader = new BufferedReader(fileReader);
			String currentLine = bufferedReader.readLine();
			String[] firstLine = currentLine.split(" ");

			// ES. 5 10 5 1000
			int numProviders = Integer.parseInt(firstLine[0]);
			int numServices = Integer.parseInt(firstLine[1]);
			int numCountries = Integer.parseInt(firstLine[2]);
			int numProjects = Integer.parseInt(firstLine[3]);

			// ES. cpu memory disk language_detection gpu chatbot database image_recognition network bigdata
			currentLine = bufferedReader.readLine();
			String[] splittedLine = currentLine.split(" ");
			for (String service : splittedLine) {
				services.add(service);
			}
			
			// ES. Slovakia Greece Italy Sweden Bulgaria 
			currentLine = bufferedReader.readLine();
			splittedLine = currentLine.split(" ");
			for (String country : splittedLine) {
				countries.add(country);
			}

			for(int i=0; i<numProviders; i++) {
				// ES. Microsoft 2
				Provider provider = new Provider();
				currentLine = bufferedReader.readLine();
				splittedLine = currentLine.split(" ");
				String providerName = splittedLine[0];
				provider.setName(providerName);
				
				
				int numRegions = Integer.parseInt(splittedLine[1]);
				
				List<DataCenter> regionalDC = new ArrayList<>();
				for(int j=0; j<numRegions; j++) {
					// ES: Mainz
					String region = bufferedReader.readLine();
					
					DataCenter rdc = new DataCenter();
					rdc.setRegion(region);
					
					// ES. 1841 0.21 10 7 10 16 8 5 0 7 4 3 
					currentLine = bufferedReader.readLine();
					splittedLine = currentLine.split(" ");

					int serviceUnitPackagesAvailable = Integer.parseInt(splittedLine[0]);
					BigDecimal packageCost = new BigDecimal(splittedLine[1]);
					Map<String,Integer> mappaServicesAvailablePerPackage = new HashMap<>();
					// ES. 1841 0.21 10 7 10 16 8 5 0 7 4 3
					int h=0;
					for(String service: services) {
						Integer cost = Integer.parseInt(splittedLine[2 + h]);
						mappaServicesAvailablePerPackage.put(service,cost);	
						h++;
					}
					rdc.setPackagesAvailable(serviceUnitPackagesAvailable);
					rdc.setPackageCost(packageCost);
					rdc.setServicesPerPackage(mappaServicesAvailablePerPackage);
					
					// ES. 1530 696 458 554 441
					currentLine = bufferedReader.readLine();
					splittedLine = currentLine.split(" ");
					Map<String, Integer> mappaLatencyCountries = new HashMap<>();
					int k=0;
					for(String country: countries) {
						int latencyPerCountry = Integer.parseInt(splittedLine[k]);
						mappaLatencyCountries.put(country, latencyPerCountry);
						k++;
					}
					rdc.setMappaLatencyCountries(mappaLatencyCountries);
					regionalDC.add(rdc);
				}
				provider.setListaRegionalDataCenter(regionalDC);
				providers.add(provider);
			}
			
			for(int i=0; i<numProjects; i++) {
				Project project = new Project();
				currentLine = bufferedReader.readLine();
				splittedLine = currentLine.split(" ");
				
				// ES. 997474735 Italy 45 59 46 32 93 18 36 35 61 33
				int penality = Integer.parseInt(splittedLine[0]);
				String country = splittedLine[1];
				
				project.setSlaPenality(penality);
				project.setTargetCounty(country);
				Map<String, Integer> unitPerDay = new HashMap<>();
				int j=0;
				for(String service: services) {
					Integer unitPerService = Integer.parseInt(splittedLine[2 + j]);
					unitPerDay.put(service, unitPerService);
					j++;
				}
				project.setUnitPerDay(unitPerDay);
				projects.add(project);
			}

			System.out.println("Loaded:" + projects.size() + " providers, " + projects.size() + " projects");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void writeSolution(List<Project> solvedProjects) {
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		try {
			String outputFile = inputFile.replace(".in", ".out");
			fileWriter = new FileWriter(outputFile);
			bufferedWriter = new BufferedWriter(fileWriter);

			StringBuilder solution = new StringBuilder();
			for(Project project: solvedProjects) {
				int j = 0;
				if(project.getOrderList() == null) {
					solution.append("\r\n");
					continue;
				}
				for (Order o: project.getOrderList()) {
					solution.append(providers.indexOf(o.getProvider()) + " ");
					solution.append(o.getProvider().getListaRegionalDataCenter().indexOf(o.getRegion()) + " ");
					solution.append(o.getQuantity());
					if (j != project.getOrderList().size() - 1) {
						solution.append(" ");
					}
					j++;
				}
				solution.append("\r\n");
			}
			bufferedWriter.append(solution);

			System.out.println("Soluzion wrote to: " + outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				fileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void evaluateSolution(List<Project> solution) throws Exception {
		List<Provider> l = new ArrayList<Provider>(providers);
		
		
		int totOrdiniMax = 0;
		for ( Provider c : providers) {
			for(DataCenter r: c.getListaRegionalDataCenter()) {
				totOrdiniMax ++;
			}
		}
		
		
		for ( Project i : solution) {
			int projectTotOrder = 0;
			
			if(i.getOrderList() == null) {
				continue;
			}
			if(i.getOrderList().size() > totOrdiniMax) {
				throw new Exception("ho ordini imprevisti");
			}
			
			for(Order o: i.getOrderList()) {
				projectTotOrder += o.getQuantity();
				
				
				Provider t = null;
				for(Provider p: l) {
					if(p.getName().equals(o.getProvider().getName())) {
						t = p;
						break;
					}
				}
				DataCenter t2 = null;
				for(DataCenter d: t.getListaRegionalDataCenter()) {
					if(d.getRegion().equals(o.getRegion().getRegion())) {
						t2 = d;
						break;
					}
				}
				int aval = t2.getPackagesAvailable();
				aval -= o.getQuantity();
				if(aval < 0) {
					new Exception("oridnati piu dei disponibili");
				}
				t2.setPackagesAvailable(aval);
			};
			
			if(projectTotOrder == 0) {
				throw new Exception("ordinati zero");
			}
		}
		
		for(Project p: solution) {
		
		 if(p.getOrderList() == null){
			 continue;
		 }
			// Ordina gli Order per coefficiente
			Collections.sort(p.getOrderList(), new Comparator<Order>() {
				public int compare(Order a, Order b) {
					if(a.getProvider().equals(b.getProvider()) && a.getRegion().equals(b.getRegion())) {
						new Exception("ordinati duplicati");

					}
					return a.hashCode() - b.hashCode();
				}
			});
		}
	}
}
