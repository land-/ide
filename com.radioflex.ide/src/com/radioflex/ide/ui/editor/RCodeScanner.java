package com.radioflex.ide.ui.editor;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Device;

import com.radioflex.ide.ui.Activator;
import com.radioflex.ide.ui.Constants;
import com.radioflex.ide.ui.preference.TextAttributeConverter;

public class RCodeScanner extends RuleBasedScanner implements
		IPropertyChangeListener {
	private Token instructionToken;
	private Token segmentToken;
	private Token macrosToken;
	private Token derivativeToken;
	private Token registerToken;

	private RadioflexEditor editor;

	public RCodeScanner(final RadioflexEditor editor) {
		this.editor = editor;

		ArrayList<IRule> rules = new ArrayList<IRule>();
		createTokens(editor.getSite().getShell().getDisplay());

		Activator.getDefault().getPreferenceStore()
				.addPropertyChangeListener(this);

		WordRuleCaseInsensitive wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> instructions = SyntaxKeywords.getInstructions();

		if (instructions != null) {
			for (String instruction : instructions.keySet()) {
				wordRule.addWord(instruction, instructionToken);
			}
		}
		rules.add(wordRule);

		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> segments = SyntaxKeywords.getSegments();
		if (segments != null) {
			for (String segment : segments.keySet()) {
				wordRule.addWord(segment, segmentToken);
			}
		}
		rules.add(wordRule);
		
		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> macros = SyntaxKeywords.getMacros();
		if (segments != null) {
			for (String macro : macros.keySet()) {
				wordRule.addWord(macro, macrosToken);
			}
		}
		rules.add(wordRule);
		
		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> derivatives = SyntaxKeywords.getDerivative();
		if(segments != null){
			for (String derivative : derivatives.keySet()){
				wordRule.addWord(derivative, derivativeToken);
			}
		}
		rules.add(wordRule);
		
		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> registers = SyntaxKeywords.getRegister();
		if(segments != null){
			for (String register : registers.keySet()){
				wordRule.addWord(register, registerToken);
			}
		}
		rules.add(wordRule);
		
		setRules(rules.toArray(new IRule[] {}));
	}

	/**
	 * Disposes the PropertyChangeListener from the PreferenceStore.
	 */
	public void dispose() {
		Activator.getDefault().getPreferenceStore()
				.removePropertyChangeListener(this);
	}

	/**
	 * Create all Tokens.
	 * 
	 * @param device
	 *            The device is needed for the color of the Tokens.
	 */
	private void createTokens(Device device) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		instructionToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_INSTRUCTION)));

		segmentToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_SEGMENT)));

		macrosToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_MACROS)));

		derivativeToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_DERIVATIVES)));

		registerToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_REGISTER)));
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_INSTRUCTION)) {
			instructionToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		} else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_SEGMENT)) {
			segmentToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		}else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_MACROS)) {
			macrosToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		}else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_DERIVATIVES)) {
			derivativeToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		}else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_REGISTER)) {
			registerToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		}

		editor.refreshSourceViewer();
	}
}
