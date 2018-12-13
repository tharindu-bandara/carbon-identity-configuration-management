package org.wso2.carbon.identity.configuration.mgt.core.model.search;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.util.ConfigurationUtils;

import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_SEARCH_QUERY_PROPERTY_DOES_NOT_EXISTS;

public class ResourceSearchBean {

    private int tenantId;
    private String tenantDomain;
    private String resourceTypeId;
    private String resourceTypeName;
    private String resourceId;
    private String resourceName;
    private String attributeKey;
    private String attributeValue;

    public String getTenantDomain() {

        return tenantDomain;
    }

    public void setTenantDomain(String tenantDomain) {

        this.tenantDomain = tenantDomain;
    }

    public int getTenantId() {

        return tenantId;
    }

    public void setTenantId(int tenantId) {

        this.tenantId = tenantId;
    }

    public String getResourceTypeId() {

        return resourceTypeId;
    }

    public void setResourceTypeId(String resourceTypeId) {

        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceTypeName() {

        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {

        this.resourceTypeName = resourceTypeName;
    }

    public String getResourceId() {

        return resourceId;
    }

    public void setResourceId(String resourceId) {

        this.resourceId = resourceId;
    }

    public String getResourceName() {

        return resourceName;
    }

    public void setResourceName(String resourceName) {

        this.resourceName = resourceName;
    }

    public String getAttributeKey() {

        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {

        this.attributeKey = attributeKey;
    }

    public String getAttributeValue() {

        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {

        this.attributeValue = attributeValue;
    }

    /**
     * Map field name to the DB table identifier.
     *
     * @return
     */
    public static String getDBQualifiedFieldName(String fieldName) throws ConfigurationManagementException {

        String dbQualifiedFieldName = null;
        switch (fieldName) {
            case "tenantId":
                dbQualifiedFieldName = "R.TENANT_ID";
                break;
            case "resourceTypeId":
                dbQualifiedFieldName = "T.ID";
                break;
            case "resourceTypeName":
                dbQualifiedFieldName = "T.NAME";
                break;
            case "resourceId":
                dbQualifiedFieldName = "R.ID";
                break;
            case "resourceName":
                dbQualifiedFieldName = "R.NAME";
                break;
            case "attributeKey":
                dbQualifiedFieldName = "A.ATTR_KEY";
                break;
            case "attributeValue":
                dbQualifiedFieldName = "A.ATTR_VALUE";
                break;
        }
        if (StringUtils.isEmpty(dbQualifiedFieldName)) {
            throw ConfigurationUtils.handleClientException(ERROR_CODE_SEARCH_QUERY_PROPERTY_DOES_NOT_EXISTS, fieldName);
        }
        return dbQualifiedFieldName;
    }
}
