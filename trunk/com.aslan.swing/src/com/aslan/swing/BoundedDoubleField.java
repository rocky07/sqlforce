package com.aslan.swing;


import java.awt.event.KeyEvent;

/**
 * Swing component that accepts a double within a specified range.
 * 
 * The range can be finite (e.g. 5.0 thru 10.0) or unbounded on either end. An unbounded range
 * is specified by giving null as a bound.
 * 
 * @author gsmithfarmer@gmail.com
 */
public class BoundedDoubleField extends ValidatedJTextField<Double> {

	private static final long serialVersionUID = 5546759358163716484L;
	
	Double lowerBound;
	Double upperBound;
	
	/**
	 * Make a BoundedDoubleJTextField with a given lower and upper bound, and a default or
	 * starting value. If the lower bound is larger than the upper bound then they will be
	 * swapped.
	 * 
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @param startingVal the starting value
	 * @param length the number of characters that this field is sized for
	 */
	public BoundedDoubleField(Double lower, Double upper, Double startingVal, int length) {
		super((startingVal == null)?"":startingVal.toString(), length);
		
		//
		// Force lower to be less than or equal to upper.
		//
		if(upper != null && lower != null && upper < lower) {
			Double temp = lower;
			lower = upper;
			upper = temp;
		}
		
		lowerBound = lower;
		upperBound = upper;
	}
	
	/**
	 * If the data is valid, return it. Otherwise returns null.
	 */
	public Double getData() {
		try {
			if(isDataValid()) {
				return Double.parseDouble(getText());
			}
			return null;
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Get the lower bound, or null if there is no bound.
	 * 
	 * @return the lower bound, or null if there is no bound.
	 */
	public Double getLowerBound() {
		return lowerBound;
	}
	
	/**
	 * Get the upper bound, or null if there is no bound.
	 * 
	 * @return the upper bound, or null if there is no bound.
	 */
	public Double getUpperBound() {
		return upperBound;
	}
	
	private boolean isNegativeAllowed() {
		if( null != upperBound && upperBound < 0 ) { return true; }
		if( null != lowerBound && lowerBound < 0 ) { return true; }
		if( null == lowerBound ) { return true; }
		
		return false;
	}
	
	/**
	 * Force the user to enter a double value.
	 * 
	 * @param e the triggering KeyEvent
	 */
	@Override
	protected void onKeyTyped(KeyEvent key) {
		
		char thisChar = key.getKeyChar();
		
		if( Character.isDigit(thisChar)) { return; }
		if( isNegativeAllowed() && 0==this.getText().trim().length() && '-'==thisChar) { return; }
	
		if( '.'==thisChar && this.getText().indexOf('.')<0) { return; }
		
		key.consume();
		
	}
	

	@Override
	public boolean isDataValid(String s) {
		try {
			double t = Double.parseDouble(s);
			if(upperBound == null && lowerBound == null) { return true; }
			if(upperBound == null && t >= lowerBound) { return true; }
			if(lowerBound == null && t <= upperBound) { return true; }
			if(t <= upperBound && t >= lowerBound) { return true; }

			return false;
		}
		catch(Exception e) {
			return false;
		}
	}
}

