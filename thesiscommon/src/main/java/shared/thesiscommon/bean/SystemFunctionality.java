package shared.thesiscommon.bean;

public enum SystemFunctionality {
	
	EVENTS_MANAGEMENT("Events Management"),
	VIEWER("Events Visualization");
	
	
	private final String label;

	private SystemFunctionality(final String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

}
