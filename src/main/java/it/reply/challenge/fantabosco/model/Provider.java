package it.reply.challenge.fantabosco.model;

import java.util.ArrayList;
import java.util.List;

public class Provider {

	String name;
	List<DataCenter> listaRegionalDataCenter;

	public Provider() {
		listaRegionalDataCenter = new ArrayList<>();
	}

	public List<DataCenter> getListaRegionalDataCenter() {
		return listaRegionalDataCenter;
	}

	public void setListaRegionalDataCenter(
			List<DataCenter> listaRegionalDataCenter) {
		this.listaRegionalDataCenter = listaRegionalDataCenter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
