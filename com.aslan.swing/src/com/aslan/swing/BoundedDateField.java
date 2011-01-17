package com.aslan.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * Swing component that accepts a date within a specified range.
 * 
 * The range can be finite (e.g. 1-Jan-2010 thru 31-Jan-2010) or unbounded on either end. An unbounded range
 * is specified by giving null as a bound.
 * 
 * @author gsmithfarmer@gmail.com
 */
public class BoundedDateField extends JPanel {

	private static final long serialVersionUID = 1;
	protected Color errorColor = Color.PINK; // displays if the user enters something invalid
	protected Color okColor;
	protected Color incompleteColor = Color.YELLOW; // displays if the user is typing something invalid
	private boolean forceKeepFocus = false;
	private boolean isRequired = false;
	
	private JComboBox dateEditor;

	
	private List<SimpleDateFormat> allDateFormats = new ArrayList<SimpleDateFormat>();
	{
		allDateFormats.add(new SimpleDateFormat( "d-MMM-yy"));
		allDateFormats.add(new SimpleDateFormat( "M/d/yy"));
		allDateFormats.add(new SimpleDateFormat( "dd.MM.yy"));
		allDateFormats.add(new SimpleDateFormat( "dd-MM-yy"));
		allDateFormats.add(new SimpleDateFormat( "yyyyMMdd"));
		
	}
	
	private Map<String,Date> namedDates = new HashMap<String,Date>();
	{
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1 );
		
//		namedDates.put("today", new Date());
//		namedDates.put("now", new Date());
//		
//		namedDates.put("yesterday", yesterday.getTime());
	}
	private Date lowerBound, upperBound;

	/**
	 * Create a new bounded date field.
	 * 
	 * Entering a null value for one or both of the bounds will cause them to become
	 * unbounded.
	 * 
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @param startingValue a default value
	 * @param length the number of characters that this field is sized for
	 */
	public BoundedDateField(Date lower, Date upper, String startingValue) {
		
		this.setLayout( new GridBagLayout());
		this.setOpaque(false);
		dateEditor = new JComboBox(namedDates.keySet().toArray(new String[0]));
		dateEditor.setEditable(true);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		
		this.add( dateEditor, gbc );
		
		dateEditor.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				onFocusLost(e);
				highLightFocusLost(e);
			}


			@Override
			public void focusGained(FocusEvent e) {
				if(isDataValid()) {
					dateEditor.getEditor().getEditorComponent().setBackground(okColor);
				}
				else {
					dateEditor.getEditor().getEditorComponent().setBackground(incompleteColor);
				}
			}



		});
		
		dateEditor.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				highLightKeyTyped(e);
			}
			
		});
		
		dateEditor.addItemListener( new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent ev) {
			
				if( ItemEvent.SELECTED == ev.getStateChange()) {
					hightLightValueChanged();
				}
			}
			
		});
		okColor = dateEditor.getEditor().getEditorComponent().getBackground();
		//
		// If the lower value is greater than the upper value, switch them so that
		// there is always an acceptable range of input.
		//
		if(lower != null && upper != null && lower.after(upper)) {
			Date t = lower;
			lower = upper;
			upper = t;
		}
		
		lowerBound = lower;
		upperBound = upper;
		
		//
		// Initialize the combo.
		//
		dateEditor.setSelectedItem( null==startingValue?"":startingValue);
		
	}
	
	
	/**
	 * Gets the lower bound of the bounded text field, or null if there is no lower bound.
	 * 
	 * @return the lower bound
	 */
	public Date getLowerBound() {
		return lowerBound;
	}
	
	/**
	 * Gets the upper bound of the bounded text field, or null if there is no upper bound.
	 * 
	 * @return the upper bound
	 */
	public Date getUpperBound() {
		return upperBound;
	}
	
	private Date parseDate( String s ) {
		
		if( null == s || 0==s.trim().length()) { return null; }
		
		s = s.trim();
		
		//
		// Try named dates.
		//
		for( String name : namedDates.keySet()) {
			if( name.equalsIgnoreCase(s)) {
				return namedDates.get(name);
			}
		}
		
		//
		// All registered name formats.
		//
		for( SimpleDateFormat df : allDateFormats) {
			try {
				Date d = df.parse(s);
				// System.err.println("Match " + df.toPattern() + " is " + d );
				return d;
			} catch (ParseException e) {
				; // Not the right format.
			}
		}
		

	
		return null;
	}
	/**
	 * Get the current date selected in the editor.
	 * 
	 * @return selected date or null if no date is specified.
	 */
	public Date getDate() {
		String s = (String)dateEditor.getSelectedItem();
		return parseDate(s);
	}
	
	/**
	 * Return the current data selected as a string.
	 * 
	 * If the date is one of the registered "named" dates, then this string will be returned.
	 * 
	 * @return selected data or null if the date is not valid.
	 */
	public String getNamedDate() {
		String s = (String)dateEditor.getSelectedItem();
		
		if( isDataValid(s)) {
			return s;
		}
		return  null;
	}
	public void setRequired( boolean newState ) {
		isRequired = newState;
	}
	/**
	 * Returns true if the text field contains a date within the bounds.
	 * 
	 * @param s the input being tested
	 */
	
	private boolean isDataValid(String s) {
		
		if( !isRequired && (null == s || 0==s.trim().length())) { return true; }
		
		Date t = parseDate(s);
		if( null ==t ) {return false; }
		
		if(upperBound == null && lowerBound == null) { return true; }
		if(upperBound == null && (t.equals(lowerBound) || t.after(lowerBound))) { return true; }
		if(lowerBound == null && (t.equals(upperBound) || t.before(upperBound))) { return true; }
		if( t.equals(upperBound) || t.equals(lowerBound)) { return true; }
		if(t.before(upperBound) && t.after(lowerBound)) { return true; }
		
		return false;
	}
	
	private boolean isDataValid() {
		return isDataValid( (String)dateEditor.getSelectedItem());
	}
	/**
	 * Override to change the behavior whenever focus is lost.
	 * 
	 * If focus is lost while the user has entered invalid data and forceToKeepFocus
	 * is set, then request focus.
	 * 
	 * @param e the FocusEvent that is occurring
	 */
	private FocusEvent onFocusLost(FocusEvent e) {
		if(forceKeepFocus && !isDataValid()) {
			dateEditor.requestFocus();
		}
		return e;
	}

	/**
	 * Handles highlighting when focus is lost, if the field is
	 * in an error state this changes the background to an error color,
	 * otherwise it changes the background to the ok color.
	 * 
	 * @param the FocusEvent that is occurring
	 */
	private FocusEvent highLightFocusLost(FocusEvent e) {
		if(isDataValid()) {
			dateEditor.getEditor().getEditorComponent().setBackground(okColor);
		}
		else if(forceKeepFocus){
			dateEditor.getEditor().getEditorComponent().setBackground(incompleteColor);
		}
		else {
			dateEditor.getEditor().getEditorComponent().setBackground(errorColor);
		}
		return e;
	}
	
	private void hightLightValueChanged() {
		if(isDataValid()) {
			dateEditor.getEditor().getEditorComponent().setBackground(okColor);
		}
		else if(!forceKeepFocus){
			dateEditor.getEditor().getEditorComponent().setBackground(incompleteColor);
		}
		else {
			dateEditor.getEditor().getEditorComponent().setBackground(errorColor);
		}
	}

	/**
	 * Handles highlighting when a key is typed. If that key puts the field
	 * into an error state then it changes the background to an error color,
	 * otherwise it changes the background to the ok color.
	 * 
	 * @param key the character typed
	 */
	protected void highLightKeyTyped(KeyEvent key) {
		
		//
		// ignore null events, and events that have already been taken care of
		//
		if(key == null) {return;}
		if(key.isConsumed()) {return;}

	
		String myItem = (String)dateEditor.getEditor().getItem();

		if(isDataValid( myItem + (Character.isISOControl(key.getKeyChar())?"":key.getKeyChar()))) {
			dateEditor.getEditor().getEditorComponent().setBackground(okColor);
		}
		else {
			dateEditor.getEditor().getEditorComponent().setBackground(incompleteColor);
		}
	}
	
	/**
	 * Add a new nameed date (like yesterday) to the list of pre-defined dates.
	 * 
	 * @param name a nice name.
	 * @param date the date corresponding to the nice name.
	 */
	public void addNamedDate( String name, Date date ) {
		if( namedDates.containsKey(name)) {
			namedDates.put(name, date);
			return;
		}
		
		namedDates.put(name, date);
		dateEditor.addItem(name);
	}
	
	/**
	 * Determine if the currently selected date is a registered named date.
	 * 
	 */
	public boolean isNamedDate() {
		String value = (String)dateEditor.getSelectedItem();
		return namedDates.containsKey(value);
	}
}
