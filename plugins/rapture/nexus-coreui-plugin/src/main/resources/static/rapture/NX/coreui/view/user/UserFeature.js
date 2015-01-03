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
 * User feature panel.
 *
 * @since 3.0
 */
Ext.define('NX.coreui.view.user.UserFeature', {
  extend: 'NX.view.drilldown.Drilldown',
  alias: 'widget.nx-coreui-user-feature',
  requires: [
    'NX.I18n'
  ],

  iconName: 'user-default',

  masters: { xtype: 'nx-coreui-user-list' },

  tabs: [
    { xtype: 'nx-coreui-user-settings', title: NX.I18n.get('ADMIN_USERS_DETAILS_SETTINGS_TAB'), weight: 10 },
    { xtype: 'nx-coreui-privilege-trace', title: NX.I18n.get('ADMIN_USERS_DETAILS_PRIVILEGE_TAB'), weight: 20 },
    { xtype: 'nx-coreui-role-tree', title: NX.I18n.get('ADMIN_USERS_DETAILS_ROLE_TAB'), weight: 30 }
  ],

  actions: [
    { xtype: 'button', text: NX.I18n.get('ADMIN_USERS_DETAILS_DELETE_BUTTON'), glyph: 'xf056@FontAwesome' /* fa-minus-circle */, action: 'delete', disabled: true },
    { xtype: 'button', text: NX.I18n.get('ADMIN_USERS_DETAILS_MORE_BUTTON'), glyph: 'xf0ae@FontAwesome' /* fa-tasks */, action: 'more', disabled: true,
      menu: [
        { text: NX.I18n.get('ADMIN_USERS_DETAILS_RESET_ITEM'), glyph: 'xf084@FontAwesome' /* fa-key */, action: 'resetpassword' },
        { text: NX.I18n.get('ADMIN_USERS_DETAILS_CHANGE_ITEM'), glyph: 'xf084@FontAwesome' /* fa-key */, action: 'setpassword' }
      ]
    }
  ]
});
