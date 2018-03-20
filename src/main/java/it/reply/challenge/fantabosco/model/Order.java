package it.reply.challenge.fantabosco.model;

public class Order {
	
	private Provider provider;
	private DataCenter region;
	private int quantity;
	
	public Provider getProvider() {
		return provider;
	}
	
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	public DataCenter getRegion() {
		return region;
	}
	
	public void setRegion(DataCenter region) {
		this.region = region;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
