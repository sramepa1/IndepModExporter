/*
 * @(#)PHPTest.java 9/15/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

package gatchan.phpparser.parser;

import net.sourceforge.phpdt.internal.compiler.ast.*;
import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;

import java.io.FileReader;
import java.io.File;
import java.util.List;

import gatchan.phpparser.project.itemfinder.PHPItem;

public class PHPTest {
    public static void main(String[] args) throws Exception {
        PHPParser parser = new PHPParser();
        parser.setPhp5Enabled(true);
        parser.addParserListener(new PHPParserListener() {
            public void parseError(PHPParseErrorEvent e) {
                System.out.println("Error: "+e.getMessage() +  " " + e.getBeginLine() + "," + e.getBeginColumn());
            }

            public void parseMessage(PHPParseMessageEvent e) {
                System.out.println("Message: "+e.getMessage() +  " " + e.getBeginLine() + "," + e.getBeginColumn());
            }
        });
        long start = System.currentTimeMillis();
        parser.setPath("C:\\Dev\\trunk\\examples_working\\E1.CodeEditor\\sample.php");
        parser.parse(new FileReader(new File("C:\\\\Dev\\\\trunk\\\\examples_working\\\\E1.CodeEditor\\\\sample.php")));
                //new File("D:\\download\\prado-3.0.3.r1331\\prado-3.0.3.r1331\\framework\\TApplication.php")));
        PHPDocument doc = parser.getPHPDocument();
        System.out.println("Parsing took "+(System.currentTimeMillis() - start)+" ms");
        Statement stmt = doc.getStatementAt(30, 21);
        if(stmt instanceof Assignment) {
            Expression expr = stmt.expressionAt(30, 21);
            if(expr != null) {
                System.out.println("Type: "+expr.getType().getClassName());
            }
        }
//        // Note: took around 380ms for 214kb - quite nice
//        PHPIndex.getInstance().setDocument((VFSFile) FileSystem.getEntry("C:\\Dev\\trunk\\examples_working\\E1.CodeEditor\\sample.php"), doc);
//        ClassDeclaration clazz = PHPIndex.getInstance().getClass("MySecondObject");
//        System.out.println("Methods of class "+clazz.getName());
//        List<MethodHeader> headers = PHPSupport.getMethods(clazz);
//        for (MethodHeader header : headers) {
//            System.out.println("header.toString() = " + header.toString());
//        }
//        for(Outlineable o : PHPIndex.getInstance().getTopLevelElements()) {
//            System.out.println("o = " + o);
//            if(o instanceof PHPItem) {
//                System.out.println("path = "+(((PHPItem)o).getPath()));
//            } else if(o instanceof ClassDeclaration) {
//                System.out.println("path = "+(((ClassDeclaration)o).getClassHeader().getPath()));
//            }
//        }

        /*PHP parser = new PHP(new FileReader(
                  new File("/home/pago/projects/SimpleEdit/call_test.php")));
          parser.token_source.SwitchTo(PHPTokenManager.HTML_STATE);
          SimpleNode node = parser.PhpPage();
          System.out.println("Visiting");
          node.childrenAccept(new PHPVisitorAdapter() {
              public Object visit(ASTString node, Object data) {
                  System.out.println("A string"+node.toString());
                  return super.visit(node, data);
              }

              public Object visit(ASTVariable node, Object data) {
                  System.out.println("Variable: "+node.toString());
                  return super.visit(node, data);
              }
          }, null);*/
    }
}
