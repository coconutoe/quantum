package gui.model;

public class CcnotOperator extends Operator {
	int target;
    int control1;
    int control2;

    public CcnotOperator(int target, int control1, int control2, String name) {
        super(name);
        this.target = target;
        this.control1 = control1;
        this.control2 = control2;
        type = "Ccnot";
    }

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getControl1() {
		return control1;
	}

	public void setControl1(int control1) {
		this.control1 = control1;
	}

	public int getControl2() {
		return control2;
	}

	public void setControl2(int control2) {
		this.control2 = control2;
	}
    
    
}
