package cz.cvut.promod.services.pluginLoaderService.errorHandling;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:56:47, 5.2.2010
 */

/**
 * Deals with SAX parsing warnings & errors.
 */
public class PluginLoaderErrorHandler implements ErrorHandler {

    private static final Logger LOG = Logger.getLogger(PluginLoaderErrorHandler.class);

    /** {@inheritDoc} */
    public void warning(SAXParseException exception) throws SAXException {
        LOG.warn("Plugin loader XSD validating and parsing warning.", exception);
    }

    /** {@inheritDoc} */
    public void error(SAXParseException exception) throws SAXException {
        LOG.warn("Plugin loader XSD validating and parsing error.", exception);
        throw new SAXException(exception);
    }

    /** {@inheritDoc} */
    public void fatalError(SAXParseException exception) throws SAXException {
        LOG.warn("Plugin loader XSD validating and parsing fatal error.", exception);
        throw new SAXException(exception);
    }
}
