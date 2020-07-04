package gui.model;

import java.util.List;

public class CnotsOperator extends Operator {
	int target;
	List<Integer> controlsl;

    public CnotsOperator(int target, List<Integer> controlsl, String name) {
        
    	super(name);
        this.target = target;
        this.controlsl = controlsl;
        type = "Cnots";
    }

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public List<Integer> getControlsl() {
		return controlsl;
	}

	public void setControlsl(List<Integer> controlsl) {
		this.controlsl = controlsl;
	}

	
    
}
