The usage, dependency, redistributable status of each jar file under lib folder 

--- redistributable jars -----

    jide-common.jar
        the common jar used by all other JIDE products.
        This is redistributable if you purchased any of JIDE products.

    jide-action.jar
        the jar for JIDE Action Framework. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Action Framework product.

    jide-dock.jar
        the jar for JIDE Docking Framework. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Docking Framework product.

    jide-components.jar
        the jar for JIDE Components. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Components product.

    jide-grids.jar
        the jar for JIDE Grids. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Grids product.

    jide-dialogs.jar
        the jar for JIDE Dialogs. It depends on jide-common.jar.
        This is redistributable if you purchased JIDE Dialogs product.

    jide-shortcut.jar
        the jar for JIDE Shortcut Editor. It depends on jide-common.jar, jide-components.jar and jide-grids.jar.
        This is redistributable if you purchased JIDE Shortcut Editor product.

    jide-pivot.jar
        the jar for JIDE Pivot Grid. It depends on jide-common.jar and jide-grids.jar.
        This is redistributable if you purchased JIDE Pivot Grid product.

    jide-editor.jar
        the jar for JIDE Code Editor. It depends on jide-common.jar jide-grids.jar jide-components.jar and jide-shortcut.jar.
        This is redistributable if you purchased JIDE Code Editor product.

    jide-rss.jar
        the jar for JIDE Feed Reader. It depends on jide-common.jar jide-grids.jar jide-components.jar informa.jar hsqldb.jar and jdom.jar.
        This is redistributable if you purchased JIDE Feed Reader product.

    jide-dashboard.jar
        the jar for JIDE Dashboard. It depends on jide-common.jar and jide-components.jar.
        This is redistributable if you purchased JIDE Feed Reader product.

    jide-data.jar
        the jar for JIDE Data Grids. It depends on jide-common.jar, jide-grids.jar and jide-action.jar (only for
        CommandBar as PageNavigationBar extends it).
        This is redistributable if you purchased JIDE Data Grids product.

    jide-charts.jar
        the jar for JIDE Charts. It depends on jide-common.jar, commons-math-1.2.jar, servlet-api.jar and batik-awt-util.jar.
        This is redistributable if you purchased JIDE Charts product.

    jide-synthetica.jar
        the jar contains a few extra classes to support Synthetica L&F. If you use Synthetica L&f in your application,
        you should include this jar in your class path. Otherwise no need to include.

    jide-properties.jar
        the jar contains all the properties files (localized strings) for different languages. If your application
        will run on different locales, you should include this jar file in the class path. Please note, this jar file
        contains as many as 48 languages for some of the properties file. So feel free to edit this jar to remove
        those properties files that your application won't need to reduce the jar file size.

-- for JIDE Grids and JIDE Pivot Grid only and only if you use export to Excel feature --
    poi-3.5-FINAL-20090928.jar and poi-ooxml-3.5-FINAL-20090928.jar(http://poi.apache.org/download.html, Apache license, version 3.5-FINAL-20090928)
        a 3rd party jar used by JIDE Grids and JIDE Pivot Grid. This is only used to implement Excel(TM) export feature.
        If you don't use HssfTableUtils and HssfPivotTableUtils, you don't need to include this jar.
        You can get it from http://poi.apache.org/download.html. Refer to their side for licensing information.

    lucene-core-2.4.1.jar (http://lucene.apache.org/java/docs/index.html, Apache license, version 2.4.1)
        a 3rd party jar used by JIDE Grids. This is only used in LuceneFilterableTableModel and LuceneQuickTableFilterField.
        If you don't use these two classes, you don't need to include this jar.
       

-- For JIDE Feed Reader only --
    informa.jar (http://informa.sourceforge.net/, LGPL, version 0.7.0-alpha2)
    commons-logging.jar (http://commons.apache.org/logging/, Apache license, version 1.0.4)
    jdom.jar (http://www.jdom.org/, Apache license, version 1.0)
        3rd party jars used by JIDE Feed Reader. Informa project is a news aggregation library based on the Java Platform.
        It depends on commons-logging.jar and jdom.jar. JIDE has no direct dependency on these two jars

    hsqldb.jar (http://hsqldb.org/, BSD license, version 1.8.0)
        a 3rd party jar used by JIDE Feed Reader. It is a "Lightweight 100% Java SQL Database Engine". This depends is
        optional if you provide your own PersistenceManagerIF other than the default FeedDatabasePersistenceManager.

-- For JIDE Data Grids only and only if you use HibernateTableModel and HibernatePageTableModel --
    hibernate3.jar (Hibernate, http://hibernate.org, version 3.2.6.ga)
    dom4j-1.6.1.jar commons-collections-2.1.1.jar cglib-2.1.3.jar asm.jar jta.jar
        3rd party jars used by JIDE Data Grids. JIDE Data Grids only depends on hibernate3.jar and the additional jars on
        the next line are used by hibernate3.jar. You may use all the jars in the hibernate release instead of taking them
        from here. 
        If you don't use any package under com.jidesoft.hibernate, you don't need those jars.


-- For JIDE Charts only --
    batik-awt-util.jar (http://xmlgraphics.apache.org/batik/license.html, Apache license, version 2.0)
    commons-math-1.2.jar (http://commons.apache.org/math/, Apache license, version 1.2)
    servlet-api.jar (part of Java Servlet API)
      3rd party jars used by JIDE Charts.
      If you are using JDK6 and above, there is no need to include batik-awt-util.jar.
      The common-math-1.2.jar is only used by classes from com.jidesoft.chart.fit package.
      The servlet-api.jar is only used by classes from com.jidesoft.chart.servlet package.

--- non-redistributable jars -----

    jide-designer.jar
        the jar for Visual Designer. It depends on jide-common.jar, jide-dock.jar, jide-action.jar, jide-dialogs.jar,
        jide-components.jar, jide-grids.jar, xerces.jar, velocity-dep-1.4.jar.
        This jar is NOT redistributable. Do not include it in your application's class path.

    jide-beaninfo.jar
        the jar for any 3rd party GUI Builders. It depends on jide-common.jar, jide-dock.jar, jide-action.jar, jide-dialogs.jar,
        jide-components.jar, jide-grids.jar.
        This jar is NOT redistributable. Do not include it in your application's class path.

    velocity-dep-1.4.jar
        a 3rd party jar used by Visual Designer.
        This jar is NOT redistributable. Do not include it in your application's class path.
        If you need to use velocity for your own application, please get it from http://jakarta.apache.org/velocity/.

    xerces.jar
        a 3rd party jar used by Visual Designer.
        This jar is NOT redistributable. Do not include it in your application's class path.
        If you need to use xerces for your own application, please get it from http://xml.apache.org/.

