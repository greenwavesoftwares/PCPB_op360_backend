package in.co.greenwave.taskapi.model;

import java.util.List;
import java.util.Objects;

public class NavigationDetails {
	private String label;
	private String path;
	private List<NavigationDetails> items;
	private String parent;
	private String logo;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<NavigationDetails> getItems() {
		return items;
	}
	public void setItems(List<NavigationDetails> items) {
		this.items = items;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public NavigationDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public NavigationDetails(String label, String path, List<NavigationDetails> items, String parent, String logo) {
		super();
		this.label = label;
		this.path = path;
		this.items = items;
		this.parent = parent;
		this.logo = logo;
	}
}

