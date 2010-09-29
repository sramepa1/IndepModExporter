package cz.cvut.promod.services.projectService.localIO;

import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import cz.cvut.promod.services.pluginLoaderService.errorHandling.PluginLoaderErrorHandler;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 2:13:43, 9.2.2010
 */
public class ProjectFileXmlErrorHandler implements ErrorHandler {

    private static final Logger LOG = Logger.getLogger(PluginLoaderErrorHandler.class);

    /** {@inheritDoc} */
    public void warning(SAXParseException exception) throws SAXException {
        LOG.warn("Project loader XSD validating and parsing warning.", exception);
    }

    /** {@inheritDoc} */
    public void error(SAXParseException exception) throws SAXException {
        LOG.warn("Project loader XSD validating and parsing error.", exception);
        throw new SAXException(exception);
    }

    /** {@inheritDoc} */
    public void fatalError(SAXParseException exception) throws SAXException {
        LOG.warn("Project loader XSD validating and parsing fatal error.", exception);
        throw new SAXException(exception);
    }

}
