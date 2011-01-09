package com.aslan.swing;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * Abstract implementation of a JTextField that validates data as it is entered
 * and visually informs the user if they enter invalid data.
 * 
 * Color is used to indicate when a the value of the field is correct. By default, initial color of the
 * field is used for correct data, yellow is used if data is incorrect while the field is in focus, and
 * red is used if the data is incorrect and the field is not in focus.
 *<p>
 * At minimum, concrete implementations must implement {@link #isDataValid(String)}. This function determines
 * if entered data is valid.
 * <p>
 * If only certain characters are allowable, an concrete class will also want to implement
 * {@link #onKeyTyped(KeyEvent)}.
 *
 * onFocusLost(FocusEvent).
 * 
 * @author gsmithfarmer@gmail.com
 * @param <T> the type of data that this field will contain. The toString() method of the type MUST return a value suitable for the {@link JTextField#setText(String)}.
 */
public abstract class ValidatedJTextField<T> extends JTextField {

	private static final long serialVersionUID = 1L;
	protected Color errorColor = Color.PINK; // displays if the user enters something invalid
	protected Color okColor;
	protected Color incompleteColor = Color.yellow; // displays if the user is typing something invalid
	private boolean forceKeepFocus = false;
	private boolean isRequired = false;

	/**
	 * Make a new ValidatedJTextField with a blank default value,
	 * defaulting the okColor to the original background color.
	 * 
	 * @param length the number of characters that this field is sized for
	 */
	public ValidatedJTextField(int length) {
		this("", length);
	}

	/**
	 * Make a new ValidatedJTextField with the given value,
	 * defaulting the okColor to the original background color.
	 * 
	 * @param startingValue the ValidatedJTextField's starting value
	 * @param length the number of characters that this field is sized for
	 */
	public ValidatedJTextField(String startingValue, int length) {
		super((null == startingValue?"":startingValue), length);
		this.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				onFocusLost(e);
				highLightFocusLost(e);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(isDataValid()) {
					setBackground(okColor);
				}
				else {
					setBackground(incompleteColor);
				}
			}
			
		});
		this.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()!=KeyEvent.VK_BACK_SPACE && e.getKeyChar()!=KeyEvent.VK_DELETE) {
					onKeyTyped(e);
				}
				highLightKeyTyped(e);
			}
		});
		okColor = this.getBackground();
	}

	/**
	 * Override to change the behavior whenever focus is lost.
	 * 
	 * If focus is lost while the user has entered invalid data and forceToKeepFocus
	 * is set, then request focus.
	 * 
	 * @param e the FocusEvent that is occurring
	 */
	protected FocusEvent onFocusLost(FocusEvent e) {
		if(forceKeepFocus && !isDataValid()) {
			this.requestFocus();
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
	protected FocusEvent highLightFocusLost(FocusEvent e) {
		if(isDataValid()) {
			this.setBackground(okColor);
		}
		else if(forceKeepFocus){
			this.setBackground(incompleteColor);
		}
		else {
			this.setBackground(errorColor);
		}
		return e;
	}

	
	/**
	 * Override to change the behavior whenever a key is typed.
	 * 
	 * @param key the character typed
	 */
	protected void onKeyTyped(KeyEvent key) {
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

		String s = this.getText() + (Character.isISOControl(key.getKeyChar())?"":key.getKeyChar());
		if(	(!isRequired && (null == s || 0==s.trim().length())) || isDataValid(s) ) {
			this.setBackground(okColor);
		}
		else {
			this.setBackground(incompleteColor);
		}
	}

	/**
	 * Get the non-text value of the current value of the field.
	 * 
	 * If the text is invalid (cannot be converted to type T) or blank then the method will
	 * should return null (or a default value).
	 * <p>
	 * A caller may want to use {@link #isDataValid()} to ensure that the field contains good data
	 * before relying on the return value of this method.
	 * 
	 * @return the data in the ValidatedJTextField
	 */
	abstract public T getData();

	/**
	 * Update the field to a new value WITHOUT any error checks.
	 * 
	 * @param data the new value for the field. If null then the field will be reset.
	 */
	public void setData(T data) {
		this.setText(null==data?"":data.toString());
	}

	/**
	 * Determine if a string is valid data for the field.
	 * 
	 * @param s a String that was entered
	 * @return whether or not s is a valid entry
	 */
	abstract protected boolean isDataValid(String s);

	/**
	 * Set the behavior when a user tries to change focus and the field contains invalid non-blank data.
	 * 
	 * @param keepsFocus true to not let the user change focus with a incorrect value
	 */
	public void setForceKeepFocus(boolean keepsFocus) {
		forceKeepFocus = keepsFocus;
	}

	/**
	 * Determine if the current value of the field is valid.
	 * 
	 * @return true if the value is OK, else false.
	 */
	public final boolean isDataValid() {
		String s = this.getText();
		
		if( !isRequired && (null == s || 0==s.trim().length())) { return true; }
		
		return isDataValid(s);
	}
	
	/**
	 * Will reset the background color of text field to the originial color.
	 */
	public void resetHighlightColor() {
		this.setBackground(okColor);
	}
	public void setRequired( boolean newState ) {
		isRequired = newState;
	}
}