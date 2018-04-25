/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ---------------
 * LayerTests.java
 * ---------------
 * (C) Copyright 2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: LayerTests.java,v 1.3 2007/11/02 17:50:37 taqua Exp $
 *
 * Changes
 * -------
 * 08-Jan-2004 : Version 1 (DG);
 *
 */

package org.jfree.ui.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.ui.Layer;

import java.io.*;

/**
 * Tests for the {@link Layer} class.
 */
public class LayerTests extends TestCase {

    /**
     * Constructs a new set of tests.
     *
     * @param name the name of the tests.
     */
    public LayerTests(final String name) {
        super(name);
    }

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(LayerTests.class);
    }

    /**
     * Tests the equals() method.
     */
    public void testEquals() {
        assertTrue(Layer.FOREGROUND.equals(Layer.FOREGROUND));
        assertTrue(Layer.BACKGROUND.equals(Layer.BACKGROUND));
        assertFalse(Layer.FOREGROUND.equals(Layer.BACKGROUND));
    }

    /**
     * Serialize an instance, restore it, and check for identity.
     */
    public void testSerialization() {

        final Layer l1 = Layer.FOREGROUND;
        Layer l2 = null;

        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(l1);
            out.close();

            final ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            l2 = (Layer) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(l1 == l2);

    }

}
