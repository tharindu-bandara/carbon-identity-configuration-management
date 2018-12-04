package org.wso2.carbon.identity.configuration.mgt.core.util;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementRuntimeException;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.securevault.SecretResolver;
import org.wso2.securevault.SecretResolverFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import javax.xml.stream.XMLStreamException;

import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_BUILDING_CONFIG;

/**
 * This class parses the configurations required for the configuration management.
 */
public class ConfigurationManagementConfigParser {

    private static final String DATA_SOURCE_NAME = "DataSource.Name";
    private static Map<String, Object> configuration = new HashMap<>();
    private OMElement rootElement;
    private static SecretResolver secretResolver;
    private static Log log = LogFactory.getLog(ConfigurationManagementConfigParser.class);

    public ConfigurationManagementConfigParser() {

        buildConfiguration();
    }

    public Map<String, Object> getConfiguration() {

        return configuration;
    }

    public String getConfigDataSource() {

        return configuration.get(DATA_SOURCE_NAME) == null ? null : configuration.get(DATA_SOURCE_NAME).toString();
    }

    private void buildConfiguration() {

        InputStream inputStream = null;
        StAXOMBuilder builder;

        try {
            String configurationFilePath = CarbonUtils.getCarbonConfigDirPath() + File.separator +
                    ConfigurationConstants.CONFIG_MANAGEMENT_CONFIG_XML;
            File configMgtConfigXml = new File(configurationFilePath);
            if (configMgtConfigXml.exists()) {
                inputStream = new FileInputStream(configMgtConfigXml);
            }
            if (inputStream == null) {
                String message = "Configuration management configs not found";
                if (log.isDebugEnabled()) {
                    log.debug(message);
                }
                throw new FileNotFoundException(message);
            }
            builder = new StAXOMBuilder(inputStream);
            rootElement = builder.getDocumentElement();
            Stack<String> nameStack = new Stack<>();
            secretResolver = SecretResolverFactory.create(rootElement, true);
            readChildElements(rootElement, nameStack);
        } catch (IOException | XMLStreamException e) {
            throw new ConfigurationManagementRuntimeException(ERROR_CODE_BUILDING_CONFIG.getMessage(),
                    ERROR_CODE_BUILDING_CONFIG.getCode(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("Error closing the input stream for "
                        + ConfigurationConstants.CONFIG_MANAGEMENT_CONFIG_XML, e);
            }
        }
    }

    private void readChildElements(OMElement serverConfig, Stack<String> nameStack) {

        for (Iterator childElements = serverConfig.getChildElements(); childElements.hasNext(); ) {
            OMElement element = (OMElement) childElements.next();
            nameStack.push(element.getLocalName());
            if (elementHasText(element)) {
                String key = getKey(nameStack);
                Object currentObject = configuration.get(key);
                String value = replaceSystemProperty(element.getText());
                if (secretResolver != null && secretResolver.isInitialized() &&
                        secretResolver.isTokenProtected(key)) {
                    value = secretResolver.resolve(key);
                }
                if (currentObject == null) {
                    configuration.put(key, value);
                } else if (currentObject instanceof ArrayList) {
                    ArrayList list = (ArrayList) currentObject;
                    if (!list.contains(value)) {
                        list.add(value);
                        configuration.put(key, list);
                    }
                } else {
                    if (!value.equals(currentObject)) {
                        ArrayList arrayList = new ArrayList(2);
                        arrayList.add(currentObject);
                        arrayList.add(value);
                        configuration.put(key, arrayList);
                    }
                }
            }
            readChildElements(element, nameStack);
            nameStack.pop();
        }
    }

    private boolean elementHasText(OMElement element) {

        String text = element.getText();
        return text != null && text.trim().length() != 0;

    }

    private String getKey(Stack<String> nameStack) {

        StringBuilder key = new StringBuilder();
        for (int i = 0; i < nameStack.size(); i++) {
            String name = nameStack.elementAt(i);
            key.append(name).append(".");
        }
        key.deleteCharAt(key.lastIndexOf("."));

        return key.toString();
    }

    private String replaceSystemProperty(String text) {

        int indexOfStartingChars = -1;
        int indexOfClosingBrace;

        // The following condition deals with properties.
        // Properties are specified as ${system.property},
        // and are assumed to be System properties
        StringBuilder textBuilder = new StringBuilder(text);
        while (indexOfStartingChars < textBuilder.indexOf("${")
                && (indexOfStartingChars = textBuilder.indexOf("${")) != -1
                && (indexOfClosingBrace = textBuilder.indexOf("}")) != -1) { // Is a property used?
            String sysProp = textBuilder.substring(indexOfStartingChars + 2, indexOfClosingBrace);
            String propValue = System.getProperty(sysProp);
            if (propValue != null) {
                textBuilder = new StringBuilder(textBuilder.substring(0, indexOfStartingChars) + propValue
                        + textBuilder.substring(indexOfClosingBrace + 1));
            }
            if (sysProp.equals(ServerConstants.CARBON_HOME)) {
                if (System.getProperty(ServerConstants.CARBON_HOME).equals(".")) {
                    textBuilder.insert(0, new File(".").getAbsolutePath() + File.separator);
                }
            }
        }
        text = textBuilder.toString();
        return text;
    }
}