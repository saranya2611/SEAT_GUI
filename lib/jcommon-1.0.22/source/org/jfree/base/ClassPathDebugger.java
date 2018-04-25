/**
 * ========================================
 * JCommon : a free Java report library
 * ========================================
 * <p>
 * Project Info:  http://www.jfree.org/jcommon/
 * <p>
 * (C) Copyright 2000-2006, by Object Refinery Limited and Contributors.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * <p>
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 * <p>
 * ------------
 * $Id: ClassPathDebugger.java,v 1.2 2008/09/10 09:23:34 mungady Exp $
 * ------------
 * (C) Copyright 2002-2006, by Object Refinery Limited.
 */

package org.jfree.base;

import org.jfree.util.ObjectUtilities;

import java.util.Enumeration;

/**
 * Creation-Date: 02.11.2007, 18:42:27
 *
 * @author Thomas Morgner
 */
public class ClassPathDebugger {
    /**
     * Entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        System.out.println("Listing the various classloaders:");
        System.out.println("Defined classloader source: " + ObjectUtilities.getClassLoaderSource());
        System.out.println("User classloader: " + ObjectUtilities.getClassLoader());
        System.out.println("Classloader for ObjectUtilities.class: " + ObjectUtilities.getClassLoader(ObjectUtilities.class));
        System.out.println("Classloader for String.class: " + ObjectUtilities.getClassLoader(String.class));
        System.out.println("Thread-Context Classloader: " + Thread.currentThread().getContextClassLoader());
        System.out.println("Defined System classloader: " + ClassLoader.getSystemClassLoader());
        System.out.println();
        try {
            System.out.println("Listing sources for '/jcommon.properties':");
            Enumeration resources = ObjectUtilities.getClassLoader
                    (ObjectUtilities.class).getResources("jcommon.properties");
            while (resources.hasMoreElements()) {
                System.out.println(" " + resources.nextElement());
            }
            System.out.println();
            System.out.println("Listing sources for 'org/jfree/JCommonInfo.class':");
            resources = ObjectUtilities.getClassLoader
                    (ObjectUtilities.class).getResources("org/jfree/JCommonInfo.class");
            while (resources.hasMoreElements()) {
                System.out.println(" " + resources.nextElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
