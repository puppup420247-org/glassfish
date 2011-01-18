/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.admingui.devtests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author anilam
 */
public class NodeTest extends BaseSeleniumTestClass {

    private static final String ID_NODES_TABLE = "propertyForm:nodesTable";
    private static final String ID_NODEHOST_FIELD = "propertyForm:propertySheet:propertSectionTextField:NodeHost:NodeHost";
    private static final String ID_NODEDIRECTORY_FIELD = "propertyForm:propertySheet:propertSectionTextField:NodeHome:NodeHome";
    private static final String ID_NEW_NODE_BUTTON = "propertyForm:nodesTable:topActionsGroup1:newButton";
    private static final String ID_DELETE_NODE_BUTTON = "propertyForm:nodesTable:topActionsGroup1:button1";
    private static final String ID_INSTALLDIR_FIELD = "propertyForm:propertySheet:propertSectionTextField:installdir-ssh:installDirssh";
    private static final String ID_FORCE_FIELD = "propertyForm:propertySheet:sshConnectorSection:force:force";
    private static final String ID_CREATE_NAME_FIELD = "propertyForm:propertySheet:propertSectionTextField:nameProp:name";
    private static final String ID_CONFIG_INSTALLDIR_FIELD = "propertyForm:propertySheet:propertSectionTextField:installdir-config:installDirConfig";
    private static final String ID_TYPE_FIELD = "propertyForm:propertySheet:propertSectionTextField:typeProp:type";
    private static final String ID_CREATE_NODE_BUTTON = "propertyForm:propertyContentPage:topButtons:newButton";
    private static final String ID_STANDALONE_TREE_LINK = "treeForm:tree:standaloneTreeNode:standaloneTreeNode_link";

    private static final String ID_NODE_TREE_LINK = "treeForm:tree:nodeTreeNode:nodeTreeNode_link";

    private static final String TRIGGER_DOMAIN_PAGE = "i18nc.domain.DomainAttrsPageTitleHelp";
    private static final String TRIGGER_PSWD_ALIASES_PAGE =  "i18nc.pswdAliases.titleHelp";
    private static final String TRIGGER_NEW_PSWD_ALIAS_PAGE = "i18nc.pswdAliasNew.titleHelp";
    private static final String TRIGGER_NODES_PAGE = "i18ncs.nodes.PageTitleHelp";
    private static final String TRIGGER_NEW_NODE_PAGE = "i18ncs.node.Keyfile";
    private static final String TRIGGER_EDIT_NODE = "i18ncs.node.InstallDir";
    private static final String TRIGGER_SAVE_SUCCESS = "New values successfully saved";
    private static final String TRIGGER_INSTANCES_PAGE = "i18ncs.standaloneInstances.PageTitleHelp";
    private static final String TRIGGER_NEW_INSTANCE_PAGE = "Configuration:";

    private static final String NODE_NAME_PREFIX = "testNode-";
    private static final String PSWD_ALIAS_NAME_PREFIX = "pswdalias-";


    @Test
    public void testCreateAndDeleteSSHNodewithKeyFile() {
        final String nodeName = NODE_NAME_PREFIX + generateRandomString();

        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        clickAndWait(ID_NEW_NODE_BUTTON, TRIGGER_NEW_NODE_PAGE);

        selenium.type(ID_CREATE_NAME_FIELD, nodeName);
        selenium.type(ID_NODEHOST_FIELD, "NodeHOST");
        selenium.type(ID_NODEDIRECTORY_FIELD, "NodeDirectory");
        //selenium.type(ID_INSTALLDIR_FIELD, "${com.sun.aas.productRoot}");
        selenium.check(ID_FORCE_FIELD);
        selenium.type("propertyForm:propertySheet:sshConnectorSection:sshport:sshport", "24");
        selectDropdownOption("propertyForm:propertySheet:sshConnectorSection:psSelected:psSelected", "Key File");
        selenium.type("propertyForm:propertySheet:sshConnectorSection:Keyfile:Keyfile", "/sshKeyFileLocation");
        clickAndWait(ID_CREATE_NODE_BUTTON, TRIGGER_NODES_PAGE);
        // Verify nodes information in table
        String prefix = getTableRowByValue(ID_NODES_TABLE, nodeName, "col1");
        assertEquals(nodeName, selenium.getText(prefix + "col1:link"));
        assertEquals("NodeHOST", selenium.getText(prefix + "col2"));
        assertEquals("SSH", selenium.getText(prefix + "colType"));

        //Verify the node is created with the value specified.
        clickAndWait( getLinkIdByLinkText("propertyForm:nodesTable", nodeName), TRIGGER_EDIT_NODE) ;
        assertTrue(selenium.isTextPresent(nodeName));

        assertTrue(selenium.isTextPresent(nodeName));
        assertEquals( "SSH", selenium.getSelectedValue(ID_TYPE_FIELD));
        assertEquals("NodeHOST", selenium.getValue(ID_NODEHOST_FIELD));
        assertEquals("NodeDirectory", selenium.getValue(ID_NODEDIRECTORY_FIELD));
        assertEquals("${com.sun.aas.productRoot}", selenium.getValue(ID_INSTALLDIR_FIELD));

        assertEquals("24", selenium.getValue("propertyForm:propertySheet:sshConnectorSection:sshport:sshport"));
        assertEquals("/sshKeyFileLocation", selenium.getValue("propertyForm:propertySheet:sshConnectorSection:Keyfile:Keyfile"));
        clickAndWait("propertyForm:propertyContentPage:topButtons:cancelButton", TRIGGER_NODES_PAGE);

        //Test Delete Node
        deleteRow(ID_DELETE_NODE_BUTTON, "propertyForm:nodesTable", nodeName);
    }

    @Test
    public void testCreateAndDeleteSSHNodewithPassword() {
        final String nodeName = NODE_NAME_PREFIX + generateRandomString();

        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        clickAndWait(ID_NEW_NODE_BUTTON, TRIGGER_NEW_NODE_PAGE);

        selenium.type(ID_CREATE_NAME_FIELD, nodeName);
        selenium.type(ID_NODEHOST_FIELD, "NodeHOST2");
        selenium.type(ID_NODEDIRECTORY_FIELD, "NodeDirectory2");
        selenium.type(ID_INSTALLDIR_FIELD, "ProductRoot");
        selenium.check(ID_FORCE_FIELD);
        selenium.type("propertyForm:propertySheet:sshConnectorSection:sshport:sshport", "34");
        selectDropdownOption("propertyForm:propertySheet:sshConnectorSection:psSelected:psSelected", "Password");

        selenium.type("propertyForm:propertySheet:sshConnectorSection:newPasswordProp:NewPassword", "abcde");
        selenium.type("propertyForm:propertySheet:sshConnectorSection:confirmPasswordProp:ConfirmPassword", "abcde");
        clickAndWait(ID_CREATE_NODE_BUTTON, TRIGGER_NODES_PAGE);
        // Verify nodes information in table
        String prefix = getTableRowByValue(ID_NODES_TABLE, nodeName, "col1");
        assertEquals(nodeName, selenium.getText(prefix + "col1:link"));
        assertEquals("NodeHOST2", selenium.getText(prefix + "col2"));
        assertEquals("SSH", selenium.getText(prefix + "colType"));

        //Verify the node is created with the value specified.
        clickAndWait( getLinkIdByLinkText("propertyForm:nodesTable", nodeName), TRIGGER_EDIT_NODE) ;
        assertTrue(selenium.isTextPresent(nodeName));

        assertTrue(selenium.isTextPresent(nodeName));
        assertEquals( "SSH", selenium.getSelectedValue(ID_TYPE_FIELD));
        assertEquals("NodeHOST2", selenium.getValue(ID_NODEHOST_FIELD));
        assertEquals("NodeDirectory2", selenium.getValue(ID_NODEDIRECTORY_FIELD));
        assertEquals("ProductRoot", selenium.getValue(ID_INSTALLDIR_FIELD));

        assertEquals("34", selenium.getValue("propertyForm:propertySheet:sshConnectorSection:sshport:sshport"));
        assertEquals("abcde", selenium.getValue("propertyForm:propertySheet:sshConnectorSection:newPasswordProp:NewPassword"));
        assertEquals("abcde", selenium.getValue("propertyForm:propertySheet:sshConnectorSection:confirmPasswordProp:ConfirmPassword"));
        clickAndWait("propertyForm:propertyContentPage:topButtons:cancelButton", TRIGGER_NODES_PAGE);

        //Test Delete Node
        deleteRow(ID_DELETE_NODE_BUTTON, "propertyForm:nodesTable", nodeName);
    }

    @Test
    public void testCreateAndDeleteSSHNodewithPswdAlias() {
        final String nodeName = NODE_NAME_PREFIX + generateRandomString();
        final String pswdAliasName = PSWD_ALIAS_NAME_PREFIX + generateRandomString();
        //create PasswordAlias
        clickAndWait("treeForm:tree:nodes:nodes_link", TRIGGER_DOMAIN_PAGE);
        clickAndWait("propertyForm:domainTabs:pswdAliases", TRIGGER_PSWD_ALIASES_PAGE);
        clickAndWait("propertyForm:aliases:topActionsGroup1:newButton", TRIGGER_NEW_PSWD_ALIAS_PAGE);
        selenium.type("propertyForm:propertySheet:propertSectionTextField:aliasNameNew:aliasNameNew", pswdAliasName);
        selenium.type("propertyForm:propertySheet:propertSectionTextField:newPasswordProp:NewPassword", "abcde");
        selenium.type("propertyForm:propertySheet:propertSectionTextField:confirmPasswordProp:ConfirmPassword", "abcde");
        clickAndWait(ID_CREATE_NODE_BUTTON, TRIGGER_PSWD_ALIASES_PAGE);

        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        clickAndWait(ID_NEW_NODE_BUTTON, TRIGGER_NEW_NODE_PAGE);
        selenium.type(ID_CREATE_NAME_FIELD, nodeName);
        selenium.type(ID_NODEHOST_FIELD, "NodeHOST3");
        selenium.type(ID_NODEDIRECTORY_FIELD, "NodeDirectory3");
        selenium.type(ID_INSTALLDIR_FIELD, "ProductRoot3");
        selenium.check(ID_FORCE_FIELD);
        selenium.type("propertyForm:propertySheet:sshConnectorSection:sshport:sshport", "34");
        selectDropdownOption("propertyForm:propertySheet:sshConnectorSection:psSelected:psSelected", "Password Alias");
        selectDropdownOption("propertyForm:propertySheet:sshConnectorSection:pswdAlias:pswdAlias", pswdAliasName);
        clickAndWait(ID_CREATE_NODE_BUTTON, TRIGGER_NODES_PAGE);


        // Verify nodes information in table
        String prefix = getTableRowByValue(ID_NODES_TABLE, nodeName, "col1");
        assertEquals(nodeName, selenium.getText(prefix + "col1:link"));
        assertEquals("NodeHOST3", selenium.getText(prefix + "col2"));
        assertEquals("SSH", selenium.getText(prefix + "colType"));

        //Verify the node is created with the value specified.
        clickAndWait( getLinkIdByLinkText("propertyForm:nodesTable", nodeName), TRIGGER_EDIT_NODE) ;
        assertTrue(selenium.isTextPresent(nodeName));

        assertTrue(selenium.isTextPresent(nodeName));
        assertEquals( "SSH", selenium.getSelectedValue(ID_TYPE_FIELD));
        assertEquals("NodeHOST3", selenium.getValue(ID_NODEHOST_FIELD));
        assertEquals("NodeDirectory3", selenium.getValue(ID_NODEDIRECTORY_FIELD));
        assertEquals("ProductRoot3", selenium.getValue(ID_INSTALLDIR_FIELD));

        assertEquals("34", selenium.getValue("propertyForm:propertySheet:sshConnectorSection:sshport:sshport"));
        assertTrue(selenium.isElementPresent("propertyForm:propertySheet:sshConnectorSection:psSelected:psSelected"));
        assertTrue(selenium.isTextPresent(pswdAliasName));
        clickAndWait("propertyForm:propertyContentPage:topButtons:cancelButton", TRIGGER_NODES_PAGE);

        //Test Delete Node
        deleteRow(ID_DELETE_NODE_BUTTON, "propertyForm:nodesTable", nodeName);

        //Delete Pswd Alias created
        clickAndWait("treeForm:tree:nodes:nodes_link", TRIGGER_DOMAIN_PAGE);
        clickAndWait("propertyForm:domainTabs:pswdAliases", TRIGGER_PSWD_ALIASES_PAGE);
        deleteRow("propertyForm:aliases:topActionsGroup1:button1", "propertyForm:aliases", pswdAliasName);
    }


    @Test
    public void testCreateAndDeleteCONFIGNodes() {
        final String nodeName = NODE_NAME_PREFIX + generateRandomString();

        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        clickAndWait(ID_NEW_NODE_BUTTON, TRIGGER_NEW_NODE_PAGE);

        selectDropdownOption(ID_TYPE_FIELD, "CONFIG");
        assertTrue(!selenium.isTextPresent("Force:"));
        assertTrue(!selenium.isTextPresent("SSH Port:"));
        assertTrue(!selenium.isTextPresent("SSH User Name:"));
        assertTrue(!selenium.isTextPresent("SSH User Authentication:"));
        assertTrue(!selenium.isTextPresent("SSH Password:"));
        assertTrue(!selenium.isTextPresent("Confirm SSH Password:"));
        assertTrue(!selenium.isTextPresent("Password Alias:"));
        assertTrue(!selenium.isTextPresent("Key File:"));

        selenium.type(ID_CREATE_NAME_FIELD, nodeName);
        selenium.type(ID_NODEHOST_FIELD, "NodeHOSTCC");
        selenium.type(ID_NODEDIRECTORY_FIELD, "NodeDirectoryCC");
        selenium.type(ID_CONFIG_INSTALLDIR_FIELD, "/ProductRoot");
        clickAndWait(ID_CREATE_NODE_BUTTON, TRIGGER_NODES_PAGE);

        // Verify nodes information in table
        String prefix = getTableRowByValue(ID_NODES_TABLE, nodeName, "col1");
        assertEquals(nodeName, selenium.getText(prefix + "col1:link"));
        assertEquals("NodeHOSTCC", selenium.getText(prefix + "col2"));
        assertEquals("CONFIG", selenium.getText(prefix + "colType"));

        //Verify the node is created with the value specified.
        clickAndWait( getLinkIdByLinkText("propertyForm:nodesTable", nodeName), TRIGGER_EDIT_NODE) ;
        assertTrue(selenium.isTextPresent(nodeName));
        assertEquals( "CONFIG", selenium.getSelectedValue(ID_TYPE_FIELD));
        assertEquals("NodeHOSTCC", selenium.getValue(ID_NODEHOST_FIELD));
        assertEquals("NodeDirectoryCC", selenium.getValue(ID_NODEDIRECTORY_FIELD));
        assertEquals("/ProductRoot", selenium.getValue(ID_CONFIG_INSTALLDIR_FIELD));

        clickAndWait("propertyForm:propertyContentPage:topButtons:cancelButton", TRIGGER_NODES_PAGE);

        //Test Delete Node
        deleteRow(ID_DELETE_NODE_BUTTON, "propertyForm:nodesTable", nodeName);
    }


    @Test
    public void testUpdateCONFIGNode() {
        final String nodeName = NODE_NAME_PREFIX + generateRandomString();

        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        clickAndWait(ID_NEW_NODE_BUTTON, TRIGGER_NEW_NODE_PAGE);

        //create the config node
        selectDropdownOption(ID_TYPE_FIELD, "CONFIG");
        selenium.type(ID_CREATE_NAME_FIELD, nodeName);
        selenium.type(ID_NODEHOST_FIELD, "NodeHOSTCC");
        selenium.type(ID_NODEDIRECTORY_FIELD, "NodeDirectoryCC");
        selenium.type(ID_CONFIG_INSTALLDIR_FIELD, "/ProductRoot");
        clickAndWait(ID_CREATE_NODE_BUTTON, TRIGGER_NODES_PAGE);

        //edit the node
        clickAndWait( getLinkIdByLinkText("propertyForm:nodesTable", nodeName), TRIGGER_EDIT_NODE) ;
        assertTrue(selenium.isTextPresent(nodeName));


        assertEquals( "CONFIG", selenium.getSelectedValue(ID_TYPE_FIELD));
        assertEquals("NodeHOSTCC", selenium.getValue(ID_NODEHOST_FIELD));
        selenium.type(ID_NODEHOST_FIELD, "new-NodeHOSTCC");
        
        assertEquals("NodeDirectoryCC", selenium.getValue(ID_NODEDIRECTORY_FIELD));
        selenium.type(ID_NODEDIRECTORY_FIELD, "new-NodeDirectoryCC");
        
        assertEquals("/ProductRoot", selenium.getValue(ID_CONFIG_INSTALLDIR_FIELD));
        selenium.type(ID_CONFIG_INSTALLDIR_FIELD, "/new-ProductRoot");
        clickAndWait("propertyForm:propertyContentPage:topButtons:saveButton", TRIGGER_SAVE_SUCCESS );

        assertEquals("new-NodeHOSTCC", selenium.getValue(ID_NODEHOST_FIELD ));
        assertEquals("new-NodeDirectoryCC", selenium.getValue(ID_NODEDIRECTORY_FIELD));
        assertEquals("/new-ProductRoot", selenium.getValue(ID_CONFIG_INSTALLDIR_FIELD));

        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        deleteRow(ID_DELETE_NODE_BUTTON, "propertyForm:nodesTable", nodeName);
    }

    /* Create a Node,  create an instance with this node,  delete this node will cause error */
    @Test
    public void testDeleteWithInstance(){
        final String nodeName = NODE_NAME_PREFIX + generateRandomString();
        final String instanceName = "testInstance" + generateRandomString();

        createSSHNode(nodeName);
        createInstance(instanceName, nodeName);
        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        // This part shoudl fail?
        rowActionWithConfirm(ID_DELETE_NODE_BUTTON, "propertyForm:nodesTable", nodeName);
        waitForCondition("document.getElementById('propertyForm:nodesTable:topActionsGroup1:button1').value != 'Processing...'", 50000);
        assertTrue(selenium.isTextPresent("An error has occurred"));

        //cleanup
        clickAndWait(ID_STANDALONE_TREE_LINK, TRIGGER_INSTANCES_PAGE);
        deleteRow("propertyForm:instancesTable:topActionsGroup1:button1", "propertyForm:instancesTable", instanceName);

        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        deleteRow(ID_DELETE_NODE_BUTTON, "propertyForm:nodesTable", nodeName);
    }

    private void createSSHNode(String nodeName){
        clickAndWait(ID_NODE_TREE_LINK, TRIGGER_NODES_PAGE);
        clickAndWait(ID_NEW_NODE_BUTTON, TRIGGER_NEW_NODE_PAGE);

        selenium.type(ID_CREATE_NAME_FIELD, nodeName);
        selenium.type(ID_NODEHOST_FIELD, "localhost");
        selenium.check(ID_FORCE_FIELD);
        clickAndWait(ID_CREATE_NODE_BUTTON, TRIGGER_NODES_PAGE);
        assertTrue(tableContainsRow("propertyForm:nodesTable", "col1", nodeName));
    }

    private void createInstance(String instanceName, String nodeName){
        clickAndWait(ID_STANDALONE_TREE_LINK, TRIGGER_INSTANCES_PAGE);
        clickAndWait("propertyForm:instancesTable:topActionsGroup1:newButton", TRIGGER_NEW_INSTANCE_PAGE );
        selenium.type("propertyForm:propertySheet:propertSectionTextField:NameTextProp:NameText", instanceName);
        selectDropdownOption("propertyForm:propertySheet:propertSectionTextField:node:node", nodeName);
        selectDropdownOption("propertyForm:propertySheet:propertSectionTextField:configProp:Config", "default-config");
        selenium.click("propertyForm:propertySheet:propertSectionTextField:configOptionProp:optC");
        clickAndWait("propertyForm:propertyContentPage:topButtons:newButton", TRIGGER_INSTANCES_PAGE);
        assertTrue(tableContainsRow("propertyForm:instancesTable", "col1", instanceName));
    }
}
