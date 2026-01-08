package in.co.greenwave.UserGroup.model;

import java.util.List;
import java.util.Objects;

public class NavigationDetails {
	// Label or name associated with the navigation item (e.g., "Home", "Settings")
	private String label;

	// Path or URL associated with the navigation item (e.g., "/home", "/settings")
	private String path;

	// List of child navigation details or items under this navigation (e.g., sub-menus or links)
	private List<NavigationDetails> items;

	// Parent navigation item or category (if this is a sub-item, this could represent its parent)
	private String parent;

	// Logo or icon associated with the navigation item (e.g., image file path or icon name)
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

