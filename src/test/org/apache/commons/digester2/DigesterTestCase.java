/* $Id: $
 *
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.commons.digester2;

import java.math.BigDecimal;
import java.net.URL;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.EmptyStackException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.xml.sax.ErrorHandler;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import org.apache.commons.logging.Log;


/**
 * <p>Test Case for the Digester class.  These tests exercise the individual
 * methods of a Digester, but do not attempt to process complete documents.
 * </p>
 */

public class DigesterTestCase extends TestCase {

    private static class AppenderAction extends AbstractAction {
        private List list;
        private String str;
        
        public AppenderAction(List list, String str) {
            this.list = list;
            this.str = str;
        }
        
        public void begin(
        Context context,
        String namespace, String name, 
        org.xml.sax.Attributes attrs) {
            list.add(str);
        }
    }
    
    // ----------------------------------------------------- Instance Variables

    /**
     * The digester instance we will be processing.
     */
    protected Digester digester = null;

    /**
     * The set of public identifiers, and corresponding resource names,
     * for the versions of the DTDs that we know about.  There
     * <strong>MUST</strong> be an even number of Strings in this array.
     */
    protected static final String registrations[] = {
        "-//Netscape Communications//DTD RSS 0.9//EN",
        "/org/apache/commons/digester/rss/rss-0.9.dtd",
        "-//Netscape Communications//DTD RSS 0.91//EN",
        "/org/apache/commons/digester/rss/rss-0.91.dtd",
    };

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public DigesterTestCase(String name) {
        super(name);
    }

    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
        digester = new Digester();
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(DigesterTestCase.class));
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        digester = null;
    }

    // ------------------------------------------------ Individual Test Methods

    /**
     * Test the basic constructor functionality.
     */
    public void testConstructor1() {
        Digester d = new Digester();

        assertNotNull("Default constructor", d.getSAXHandler());
    }

    /**
     * Test the basic constructor functionality.
     */
    public void testConstructor2() {
        SAXHandler h = new SAXHandler();
        Digester d = new Digester(h);

        assertEquals("Constructor with SAXHandler", d.getSAXHandler(), h);
    }

    /**
     * Test that digester auto-creates an XMLReader if needed,
     * and that parsing works ok with that reader.
     */
    public void testXMLReaderAuto() throws Exception {
        String inputText = "<root/>";
        InputSource source = new InputSource(new StringReader(inputText));
        
        Digester d = new Digester();

        XMLReader reader = d.getXMLReader();
        assertNotNull("getXMLReader", reader);

        ArrayList list = new ArrayList();
        d.addRule("/root", new AppenderAction(list, "action1"));

        d.parse(source);

        assertEquals("Parse works with auto-created parser", 1, list.size());
        assertEquals("Parse works with auto-created parser", "action1", list.get(0));
    }

    /**
     * Same as testXMLReaderAuto except that getXMLReader is not called.
     * This is just to make sure getXMLReader hasn't had some necessary
     * side-effect that causes a parse to fail without it.
     */
    public void testXMLReaderAuto2() throws Exception {
        String inputText = "<root/>";
        InputSource source = new InputSource(new StringReader(inputText));
        
        Digester d = new Digester();

        ArrayList list = new ArrayList();
        d.addRule("/root", new AppenderAction(list, "action1"));

        d.parse(source);

        assertEquals("Parse works with auto-created parser", 1, list.size());
        assertEquals("Parse works with auto-created parser", "action1", list.get(0));
    }

    /**
     * Test that digester works if an XMLReader has been explicitly created
     * and passed in to the digester.
     */
    public void testXMLReaderManual() throws Exception {
        // test that digester auto-creates an XMLReader if needed,
        // and that parsing works ok with that reader.
        String inputText = "<root/>";
        InputSource source = new InputSource(new StringReader(inputText));
        
        // create XMLReader
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();

        // Create the digester
        Digester d = new Digester();

        // connect XMLReader to saxHandler
        d.setXMLReader(reader, true);

        ArrayList list = new ArrayList();
        d.addRule("/root", new AppenderAction(list, "action1"));

        d.parse(source);

        assertEquals("Parse works with manual-created parser", 1, list.size());
        assertEquals("Parse works with manual-created parser", "action1", list.get(0));
    }

    // TODO: add test for setValidating/getValidating

    // TODO: add test for get/set explicit classloader

    // TODO: add test for get/set logger. This should probably wait until
    // we figure out whether to revamp the logging approach though.
    
    /**
     * Test the basic property getters and setters.
     */
    public void testProperties() {
        DefaultHandler defaultHandler = new org.xml.sax.helpers.DefaultHandler();

        // check we can set and get a custom error handler
        assertNull("Initial error handler is null",
                digester.getErrorHandler());
        digester.setErrorHandler(defaultHandler);
        assertTrue("Set/get error handler failed",
                digester.getErrorHandler() == defaultHandler);
        digester.setErrorHandler(null);
        assertNull("Reset error handler failed",
                digester.getErrorHandler());

         // check the validation property
        assertTrue("Initial validating is false",
                !digester.getValidating());
        digester.setValidating(true);
        assertTrue("Set validating is true",
                digester.getValidating());
        digester.setValidating(false);
        assertTrue("Reset validating is false",
                !digester.getValidating());

        // set and get classloader, and useContextClassLoader
        // get and set saxlogger
        // get and set RuleManager
        // get and set Substitutor
    }


    /**
     * Test registration of URLs for specified public identifiers.
     */
    public void testRegistrations() {

        Map map = digester.getKnownEntities();
        assertEquals("Initially zero registrations", 0, map.size());
        int n = 0;
        for (int i = 0; i < registrations.length; i += 2) {
            URL url = this.getClass().getResource(registrations[i + 1]);
            if (url != null) {
                digester.registerKnownEntity(registrations[i], url.toString());
                n++;
            }
        }

        assertEquals("Registered two URLs", n, map.size());

        int count[] = new int[n];
        for (int i = 0; i < n; i++) {
            count[i] = 0;
        }
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            for (int i = 0; i < n; i++) {
                if (key.equals(registrations[i * 2])) {
                    count[i]++;
                    break;
                }
            }
        }
        for (int i = 0; i < n; i++)
            assertEquals("Count for key " + registrations[i * 2],
                    1, count[i]);
    }
}
