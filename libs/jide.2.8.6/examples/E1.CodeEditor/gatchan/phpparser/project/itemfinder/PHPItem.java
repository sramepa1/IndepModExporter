package gatchan.phpparser.project.itemfinder;

import javax.swing.*;

/**
 * @author Matthieu Casanova
 */
public interface PHPItem {
  int CLASS = 1;
  int METHOD = 2;
  int FIELD = 4;
  int INTERFACE = 8;
  int DOCUMENT = 16;// special type for document
  int DEFINE = 32;
  int GLOBAL = 64;
  int INCLUDE = 128;
  int VARIABLE = 256;
  int CLASS_CONSTANT = 512;

  int getItemType();

  String getName();

  /**
   * Returns the name of the item in lower case.
   * It will be used to quick find items
   *
   * @return the name in lower case
   */
  String getNameLowerCase();

  int getSourceStart();

  int getBeginLine();

  int getBeginColumn();

  String getPath();

  Icon getIcon();
}
