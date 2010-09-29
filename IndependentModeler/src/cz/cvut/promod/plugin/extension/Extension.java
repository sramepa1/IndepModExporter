package cz.cvut.promod.plugin.extension;

import cz.cvut.promod.plugin.Plugin;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:30:53, 16.10.2009
 */

/**
 * Extension interface has no specialized methods. Is not a Notation and it is not a module.
 * The reason why there are separated interfaces for Module and Extension is, that the Extension
 * never has an identifier of related notation. Module does.
 */
public interface Extension extends Plugin {}
