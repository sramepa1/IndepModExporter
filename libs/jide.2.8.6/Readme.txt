JIDE Docking Framework, JIDE Action Framework, JIDE Components, JIDE Grids, JIDE Dialogs, JIDE Shortcut Editor, JIDE Pivot Grid, JIDE Code Editor, JIDE Feed Reader, JIDE Dashboard and JIDE Data Grids

*************
   Version
*************
2.8.6 build on 20100406

************************
   Directory Structure
************************
Readme.txt		            this file
doc\			            documents directory
    *.pdf                   developer guides
javadoc\		            javadoc of all public classes
lib\                        jar files directory
    jide-common.jar         the common jar used by all other JIDE products. JIDE common layer is part of this jar.
    jide-action.jar         the jar for JIDE Action Framework
    jide-dock.jar           the jar for JIDE Docking Framework
    jide-components.jar     the jar for JIDE Components
    jide-grids.jar          the jar for JIDE Grids
    jide-dialogs.jar        the jar for JIDE Dialogs
    jide-shortcut.jar       the jar for JIDE Shortcut Editor
    jide-pivot.jar          the jar for JIDE Pivot Grid
    jide-editor.jar         the jar for JIDE Code Editor
    jide-rss.jar            the jar for JIDE Feed Reader
    jide-dashboard.jar      the jar for JIDE Dashboard
    jide-data.jar           the jar for JIDE Data Grids
    jide-synthetica.jar     the jar for the integration of Synthetica L&F. You only need it if you are using Synthetica L&F.
    jide-designer.jar       the jar for Visual Designer - do not need to include in your application's class path
    jide-beaninfo.jar       the jar for any 3rd party GUI Builders - do not need to include in your application's class path
    velocity-dep-1.4.jar    3rd party jar used by Visual Designer - do not need to include in your application's class path
    xerces.jar              3rd party jar used by Visual Designer - do not need to include in your application's class path
    hssf.jar                3rd party jar used by JIDE Grids and JIDE Pivot Grid - you only need it if you use HssfTableUtils and HssfPivotTableUtils to export a table to Excel file.
    lucene-core-xxx.jar     3rd party jar used by JIDE Grids - you only need it if you use LuceneFilterableTableModel and LuceneQuickTableFilterField.
src-stub\                   source code stub directory
    src-stub.zip       source code stub zip file. In the Java IDEs, you add this zip to JIDE jars as source code and the IDE will provide better support in auto-completing the parameter names.
examples\                   examples directory
src\                        source code directory (only if you purchased source code license)
    src-oss.zip             source code zip file for common layer (this project is open sourced)
    src-action.zip          source code zip file for JIDE Action Framework
    src-dock.zip            source code zip file for JIDE Docking Framework
    src-components.zip      source code zip file for JIDE Components
    src-dialogs.zip         source code zip file for JIDE Dialogs
    src-grids.zip           source code zip file for JIDE Grids
    src-shortcut.zip        source code zip file for JIDE Shortcut Editor
    src-pivot.zip           source code zip file for JIDE Pivot Grid
    src-editor.zip          source code zip file for JIDE Code Editor
    src-rss.zip             source code zip file for JIDE Feed Reader
    src-dashboard.zip       source code zip file for JIDE Dashboard
    src-data.zip            source code zip file for JIDE Data Grids
license\
    SLA.htm	            end user license agreement

************************
   System Requirements
*************************

1. Any Java-enabled OS
2. j2sdk 1.5.0 or above. If you are running JDK 1.4.2, please download another package from a link 
called "Release for JDK1.4" on the same page where you downloaded this package.

**********************
   JIDE Software
**********************

JIDE Software is a privately held software company focusing on providing rich client solutions and services
using Java technology. Founded in 2002, JIDE Software has developed 11 products and over 100 professional
Swing components that cover almost every aspect of Java/Swing development.

With more than 1000 customers in over 40 countries worldwide, JIDE Software is the clear market leader.
Many companies are depending on technologies JIDE provides to build their applications or frameworks,
deliver their customers a polished user interface, and simplify their in-house development.

For further information, please contact support@jidesoft.com or visit our website at http://www.jidesoft.com.
