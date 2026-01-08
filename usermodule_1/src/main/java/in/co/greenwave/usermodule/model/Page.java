package in.co.greenwave.usermodule.model;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)

public class Page {

    /**
     * The unique name of the page
     */
    private String page;

    /**
     * The source or category of the page (e.g., module or feature it belongs to).
     */
    private String source;

    /**
     * The URL of the page.
     */
    private String pageUrl;

    /**
     * The logo or icon associated with the page.
     */
    private String pageLogo;

    /**
     * The sequence number for ordering the page, typically used for UI rendering.
     */
    private String sequence;

    /**
     * The identifier of the tenant to which the page belongs.
     */
    private String tenantId;

    /**
     * An alias or alternative name for the page, used for display or customization purposes.
     */
    private String aliasName;


	public Page() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Page(String page, String source, String pageUrl, String pageLogo, String sequence, String tenantId,
			String aliasName) {
		super();
		this.page = page;
		this.source = source;
		this.pageUrl = pageUrl;
		this.pageLogo = pageLogo;
		this.sequence = sequence;
		this.tenantId = tenantId;
		this.aliasName = aliasName;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getPageLogo() {
		return pageLogo;
	}

	public void setPageLogo(String pageLogo) {
		this.pageLogo = pageLogo;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	@Override
	public String toString() {
		return "Page [page=" + page + ", source=" + source + ", pageUrl=" + pageUrl + ", pageLogo=" + pageLogo
				+ ", sequence=" + sequence + ", tenantId=" + tenantId + ", aliasName=" + aliasName + "]";
	}
     
     
}

