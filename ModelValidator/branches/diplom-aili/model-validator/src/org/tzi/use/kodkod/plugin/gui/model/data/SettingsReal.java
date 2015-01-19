package org.tzi.use.kodkod.plugin.gui.model.data;

import org.tzi.kodkod.model.type.TypeConstants;

public class SettingsReal extends Settings{
	
	private String name = TypeConstants.REAL;
	private Double realStep = 0.5;
	
	public SettingsReal() {
		super();
	}
	
	public String name() {
		return this.name;
	}

	public Double getStep() {
		return realStep;
	}

	public void setStep(Object realStep) {
		if (realStep instanceof String) {
			this.realStep = Double.parseDouble((String)realStep);
		}
	}

}
