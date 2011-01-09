/**
 * 
 */
package com.aslan.swing;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * Show all custom field objects support by the package in a single window.
 * 
 * @author snort
 *
 */
public class FieldGallery {

	public FieldGallery() {
		JFrame frame = new JFrame( "CAPSTORM Component Gallery");
		initUI( frame.getContentPane());
		frame.pack();
		
		
		frame.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent ev) {
	                System.exit(0);
	            }
	        });
		frame.setVisible(true);
	}
	
	private GridBagConstraints getLabelGBC( int x, int y ) {
		GridBagConstraints gbc = new GridBagConstraints();
		Insets insets = new Insets(0,0,10,10);

		
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.insets = insets;
		
		return gbc;
	}
	
	private GridBagConstraints getControlGBC( int x, int y ) {
		GridBagConstraints gbc = new GridBagConstraints();
		Insets insets = new Insets(0,0,10,10);
		
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.insets = insets;
		gbc.anchor = GridBagConstraints.LINE_START;
		
		return gbc;
	}
	
	private JLabel makeLabel( String value ) {
		JLabel label = new JLabel( value );
		return label;
	}
	
	private JPanel createNumericPanel() {
		JPanel root = new JPanel();
		root.setLayout( new GridBagLayout());
		int nextRow = 0;
		
		root.add( makeLabel("Integer"), getLabelGBC( 0,nextRow ));
		root.add( new BoundedIntegerField( (Integer)null, (Integer)null, (Integer)null, 8), getControlGBC( 1, nextRow++));
		
		root.add( makeLabel("Natural"), getLabelGBC( 0,nextRow ));
		root.add( new BoundedIntegerField( (Integer)0, (Integer)null, (Integer)null, 8), getControlGBC( 1, nextRow++));
		
		root.add( makeLabel("Integer Range (1 thru 100)"), getLabelGBC( 0,nextRow ));
		root.add( new BoundedIntegerField( 1, 100, 50, 8), getControlGBC( 1, nextRow++));
		
		root.add( makeLabel("Integer Range (-10 thru 10)"), getLabelGBC( 0, nextRow ));
		root.add( new BoundedIntegerField( -10, 10, 0, 8), getControlGBC( 1, nextRow++));
		
		root.add( makeLabel("Integer Range (-20 thru -10)"), getLabelGBC( 0, nextRow ));
		root.add( new BoundedIntegerField( -20, -10, (Integer)null, 8), getControlGBC( 1, nextRow++));
		
		
		root.add( makeLabel("Double"), getLabelGBC( 0,nextRow ));
		root.add( new BoundedDoubleField( (Double)null, (Double)null, (Double)null, 8), getControlGBC( 1, nextRow++));
		
		root.add( makeLabel("Double Range (1.1 thru 100.3)"), getLabelGBC( 0,nextRow ));
		root.add( new BoundedDoubleField( 1.1, 100.3, 50.0, 8), getControlGBC( 1, nextRow++));
		
		root.add( makeLabel("Double Range (-10 thru 10)"), getLabelGBC( 0, nextRow ));
		root.add( new BoundedDoubleField( -10.0, 10.0, 0.0, 8), getControlGBC( 1, nextRow++));
		
		root.add( makeLabel("Double Range (-20 thru -10)"), getLabelGBC( 0, nextRow ));
		root.add( new BoundedDoubleField( -20.0, -10.0, (Double)null, 8), getControlGBC( 1, nextRow++));
		
		return root;
	}
	
	private JPanel createDateTimePanel() {
		JPanel root = new JPanel();
		root.setLayout( new GridBagLayout());
		int nextRow = 0;
		
		root.add( makeLabel("Date"), getLabelGBC( 0,nextRow ));
		root.add( new  BoundedDateField((Date) null, (Date) null, "23-Mar-1960"), getControlGBC( 1, nextRow++));
		
		BoundedDateField bdf = new BoundedDateField( (Date) null, (Date) null, "yesterday");
		bdf.addNamedDate("Birthday", new Date());
		root.add( makeLabel("Date with Birthday"), getLabelGBC( 0,nextRow ));
		root.add(bdf, getControlGBC( 1, nextRow++));
		
		
		return root;
	}
	private void initUI(Container masterRoot ) {
		JPanel root = new JPanel();
		GridBagConstraints gbc;
		root.setLayout( new GridBagLayout());
		
		JPanel numberPanel = createNumericPanel();
		numberPanel.setBorder( BorderFactory.createTitledBorder("Numeric Fields"));
		
		JPanel datePanel = createDateTimePanel();
		datePanel.setBorder( BorderFactory.createTitledBorder("Date/Time Fields"));
		
		gbc = getControlGBC(0,0);
		root.add( numberPanel, gbc );
		
		gbc = getControlGBC(1,0);
		root.add( datePanel, gbc );
		
		masterRoot.add(root);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FieldGallery();

	}

}
