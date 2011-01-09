package com.aslan.swing;

import java.awt.event.KeyEvent;

/**
 * Swing component that accepts an integer within a specified range.
 * 
 * The range can be finite (e.g. 5 thru 10) or unbounded on either end. An unbounded range
 * is specified by giving null as a bound.
 * 
 * @author gsmithfarmer@gmail.com
 */
public class BoundedIntegerField extends ValidatedJTextField<Integer> {

	private static final long serialVersionUID = 1;
	
	private Integer lowerBound, upperBound;

	/**
	 * Make a new BoundedIntegerJTextField with given bounds, default value, and length.
	 * If the lower bound is greater than the upper then they will be switched.
	 * 
	 * Entering a null value for one or both of the bounds will cause them to become
	 * unbounded.
	 * 
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @param startingValue a default value
	 * @param length the number of characters that this field is sized for
	 */
	public BoundedIntegerField(Integer lower, Integer upper, Integer startingValue, int length) {
		super((startingValue == null)?"":startingValue.toString(), length);
		
		//
		// If the lower value is greater than the upper value, switch them so that
		// there is always an acceptable range of input.
		//
		if(lower != null && upper != null && lower > upper) {
			int t = lower;
			lower = upper;
			upper = t;
		}
		
		lowerBound = lower;
		upperBound = upper;
	}
	
	
	/**
	 * Gets the lower bound of the bounded text field, or null if there is no lower bound.
	 * 
	 * @return the lower bound
	 */
	public Integer getLowerBound() {
		return lowerBound;
	}
	
	/**
	 * Gets the upper bound of the bounded text field, or null if there is no upper bound.
	 * 
	 * @return the upper bound
	 */
	public Integer getUpperBound() {
		return upperBound;
	}
	
	private boolean isNegativeAllowed() {
		if( null != upperBound && upperBound < 0 ) { return true; }
		if( null != lowerBound && lowerBound < 0 ) { return true; }
		if( null == lowerBound ) { return true; }
		
		return false;
	}
	/**
	 * Force the user to only enter digits.
	 */
	@Override
	protected void onKeyTyped(KeyEvent key) {
		
		char thisChar = key.getKeyChar();
		if( Character.isDigit(thisChar)) { return; }
		if( isNegativeAllowed() && 0==this.getText().trim().length() && '-'==thisChar) { return; }
		
		//
		// Invalid Character.
		//
		key.consume();

	}
	
	/**
	 * {@inheritDoc}
	 */
	public Integer getData() {
		if(this.isDataValid()) {
			return Integer.parseInt(getText());
		}
		return null;
	}
	
	/**
	 * Returns true if the text field contains an integer within the bounds.
	 * 
	 * @param s the input being tested
	 */
	@Override
	public boolean isDataValid(String s) {
		try {
			int t = Integer.parseInt(s);
			
			if(upperBound == null && lowerBound == null) { return true; }
			if(upperBound == null && t >= lowerBound) { return true; }
			if(lowerBound == null && t <= upperBound) { return true; }
			if(t <= upperBound && t >= lowerBound) { return true; }
			
			return false;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
}
