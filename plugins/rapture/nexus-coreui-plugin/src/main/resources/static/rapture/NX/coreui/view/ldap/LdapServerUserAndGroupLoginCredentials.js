/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2015 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
/*global Ext, NX*/

/**
 * LDAP Server User & Group login credentials window.
 *
 * @since 3.0
 */
Ext.define('NX.coreui.view.ldap.LdapServerUserAndGroupLoginCredentials', {
  extend: 'Ext.window.Window',
  alias: 'widget.nx-coreui-ldapserver-userandgroup-login-credentials',
  requires: [
    'NX.Icons',
    'NX.I18n'
  ],

  title: NX.I18n.get('ADMIN_LDAP_CREDENTIALS_TITLE'),

  layout: 'fit',
  autoShow: true,
  modal: true,
  constrain: true,
  width: 320,
  defaultFocus: 'username',

  /**
   * @protected
   */
  initComponent: function () {
    var me = this;

    if (!me.message) {
      me.message = NX.I18n.get('ADMIN_LDAP_CREDENTIALS_VALIDATION');
    }

    Ext.apply(this, {
      items: {
        xtype: 'form',
        bodyPadding: 10,
        defaultType: 'textfield',
        defaults: {
          labelAlign: 'left',
          labelWidth: 100,
          anchor: '100%'
        },
        items: [
          {
            xtype: 'panel',
            layout: 'hbox',
            style: {
              marginBottom: '10px'
            },
            items: [
              { xtype: 'component', html: NX.Icons.img('authenticate', 'x32') },
              { xtype: 'component', html: NX.I18n.get('ADMIN_LDAP_CREDENTIALS_INPUT') }
            ]
          },
          {
            name: 'username',
            itemId: 'username',
            fieldLabel: NX.I18n.get('ADMIN_LDAP_CREDENTIALS_USERNAME'),
            allowBlank: false
          },
          {
            name: 'password',
            itemId: 'password',
            fieldLabel: NX.I18n.get('ADMIN_LDAP_CREDENTIALS_PASSWORD'),
            inputType: 'password',
            allowBlank: false,
            validateOnBlur: false // allow cancel to be clicked w/o validating this to be non-blank
          }
        ],

        buttonAlign: 'left',
        buttons: [
          { text: NX.I18n.get('ADMIN_LDAP_CREDENTIALS_SUBMIT_BUTTON'), action: 'verifylogin', formBind: true, bindToEnter: true, ui: 'primary' },
          { text: NX.I18n.get('ADMIN_LDAP_CREDENTIALS_CANCEL_BUTTON'), handler: me.close, scope: me }
        ]
      }
    });

    me.callParent();
  }

});
