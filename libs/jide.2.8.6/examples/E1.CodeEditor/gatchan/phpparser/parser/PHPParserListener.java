package gatchan.phpparser.parser;

import java.util.EventListener;

/**
 * The listener to listen to the PHPParserListener
 *
 * @author Matthieu Casanova
 */
public interface PHPParserListener extends EventListener {

    void parseError(PHPParseErrorEvent e);
    void parseMessage(PHPParseMessageEvent e);
}
