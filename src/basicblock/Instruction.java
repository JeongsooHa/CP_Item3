package basicblock;

public class Instruction {
	private String label; 
	private String functionName;
	private String code;
	private String targetLabel;
	
	public Instruction(String label, String functionName, String code, String targetLabel) {
		super();
		this.label = label;
		this.functionName = functionName;
		this.code = code;
		this.targetLabel = targetLabel;
	}

	@Override
	public String toString() {
		return this.label+"\t"+this.code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTargetLabel() {
		return targetLabel;
	}

	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}
	
	
}
