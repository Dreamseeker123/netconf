/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.sal.restconf.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opendaylight.netconf.sal.restconf.impl.ControllerContext;
import org.opendaylight.restconf.common.context.InstanceIdentifierContext;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeIdentifierWithPredicates;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;

public class Bug3595Test {

    private static final QName CONT_QNAME = QName.create("leafref:module", "2014-04-17", "cont");
    private static final QName LST_WITH_LFREF_KEY_QNAME = QName.create(CONT_QNAME, "lst-with-lfref-key");
    private static final QName LFREF_KEY_QNAME = QName.create(CONT_QNAME, "lfref-key");


    private static ControllerContext controllerContext = ControllerContext.getInstance();

    @BeforeClass
    public static void initialize() throws FileNotFoundException {
        final SchemaContext schemaContext = TestUtils.loadSchemaContext("/leafref/yang");
        Module module = TestUtils.findModule(schemaContext.getModules(), "leafref-module");
        assertNotNull(module);
        module = TestUtils.findModule(schemaContext.getModules(), "referenced-module");
        assertNotNull(module);

        controllerContext.setGlobalSchema(schemaContext);
    }

    @Test
    public void testLeafrefListKeyDeserializtion() {
        final YangInstanceIdentifier node1IIexpected = YangInstanceIdentifier.of(CONT_QNAME)
                .node(LST_WITH_LFREF_KEY_QNAME).node(new NodeIdentifierWithPredicates(
                        LST_WITH_LFREF_KEY_QNAME, LFREF_KEY_QNAME, "node1"));
        final InstanceIdentifierContext<?> iiContext =
                controllerContext.toInstanceIdentifier("leafref-module:cont/lst-with-lfref-key/node1");
        iiContext.getInstanceIdentifier();
        assertEquals(node1IIexpected, iiContext.getInstanceIdentifier());
    }
}
