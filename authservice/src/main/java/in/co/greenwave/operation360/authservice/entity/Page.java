package in.co.greenwave.operation360.authservice.entity;

/**
 * The Page class represents a web page or application module with properties such as its URL,
 * logo, sequence order, and tenant information. This entity is useful for organizing and 
 * displaying pages for different tenants in a multi-tenant system.
 */
public class Page {

    // The name of the page
    private String page;
    
    // The source of the page (could indicate its origin or category)
    private String source;
    
    // The URL of the page, used for navigation
    private String pageUrl;
    
    // The logo of the page, typically displayed in the UI
    private String pageLogo;
    
    // The sequence in which this page appears (e.g., order in a menu or list)
    private String sequence;
    
    // The tenant ID to identify which tenant (organization) the page belongs to
    private String tenantId;
    
    // An alternative name or alias for the page, used for customization purposes
    private String aliasName;

    /**
     * Default constructor that creates an empty Page object.
     */
    public Page() {
        super();
    }

    /**
     * Constructor with parameters to initialize all fields of the Page class.
     * 
     * @param page the name of the page.
     * @param source the source of the page.
     * @param pageUrl the URL of the page.
     * @param pageLogo the logo of the page.
     * @param sequence the sequence/order of the page.
     * @param tenantId the ID of the tenant who owns the page.
     * @param aliasName an alias or alternative name for the page.
     */
    public Page(String page, String source, String pageUrl, String pageLogo, String sequence, String tenantId, String aliasName) {
        super();
        this.page = page;
        this.source = source;
        this.pageUrl = pageUrl;
        this.pageLogo = pageLogo;
        this.sequence = sequence;
        this.tenantId = tenantId;
        this.aliasName = aliasName;
    }

    /**
     * Gets the name of the page.
     * 
     * @return the page name.
     */
    public String getPage() {
        return page;
    }

    /**
     * Sets the name of the page.
     * 
     * @param page the name to be set for the page.
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Gets the source of the page.
     * 
     * @return the source of the page.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source of the page.
     * 
     * @param source the source to be set for the page.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the URL of the page.
     * 
     * @return the page's URL.
     */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * Sets the URL of the page.
     * 
     * @param pageUrl the URL to be set for the page.
     */
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * Gets the logo of the page.
     * 
     * @return the page logo.
     */
    public String getPageLogo() {
        return pageLogo;
    }

    /**
     * Sets the logo of the page.
     * 
     * @param pageLogo the logo to be set for the page.
     */
    public void setPageLogo(String pageLogo) {
        this.pageLogo = pageLogo;
    }

    /**
     * Gets the sequence/order of the page in a list or menu.
     * 
     * @return the page's sequence.
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the sequence/order of the page.
     * 
     * @param sequence the sequence to be set for the page.
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Gets the tenant ID associated with the page.
     * 
     * @return the tenant ID.
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Sets the tenant ID for the page.
     * 
     * @param tenantId the tenant ID to be set.
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Gets the alias or alternative name for the page.
     * 
     * @return the alias name of the page.
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Sets an alias or alternative name for the page.
     * 
     * @param aliasName the alias name to be set.
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * Returns a string representation of the Page object, helpful for logging or debugging.
     * 
     * @return a string that represents the Page object.
     */
    @Override
    public String toString() {
        return "Page [page=" + page + ", source=" + source + ", pageUrl=" + pageUrl + ", pageLogo=" + pageLogo
                + ", sequence=" + sequence + ", tenantId=" + tenantId + ", aliasName=" + aliasName + "]";
    }
}
