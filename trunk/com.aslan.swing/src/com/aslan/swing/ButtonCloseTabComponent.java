/*******************************************************************************
 * Copyright (c) 2011 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package com.aslan.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Modify a tab on a JTabbedPane to include an icon for closing the tab.
 * 
 * A close icon will be appened to the text on the tab (even if the text changes).
 * If the user clicks on the icon then the tab will be closed.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class ButtonCloseTabComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	private static ImageIcon closeIcon;

    public ButtonCloseTabComponent(final JTabbedPane pane) {
        super(new GridBagLayout());
 
        setOpaque(false);
        
        JLabel label = new JLabel() {
			private static final long serialVersionUID = 1L;

			public String getText() {
                int i = pane.indexOfTabComponent(ButtonCloseTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        
        GridBagConstraints gbc;
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy=0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0,0,0,5);
        add(label, gbc );
  
        
        if( null == closeIcon) {
        	URL url = getClass().getResource("CloseTabIcon.png");
    		
    		closeIcon = new ImageIcon(url);
        }
  
        JButton closeButton = new JButton(closeIcon);
        closeButton.setFocusable(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				 int index = pane.indexOfTabComponent(ButtonCloseTabComponent.this);
		         if (index != -1) {
		                pane.remove(index);
		            }
			}
        	
        });
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy=0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0,0,0,0);
        add( closeButton, gbc);
 
    }

 
}
