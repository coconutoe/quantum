package ast;

import interp.Environment;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import util.AmpToBra;

import java.util.List;

import Quantum.Qubit;

public class List_Statements extends ASTList {

	public List_Statements(List<ASTree> list) {
		super(list);
	}

	//返回所有的Statement列表
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ASTree ast : children) {
			sb.append(ast.toString() + "\r\n");
		}
		return sb.toString();
	}

	public Object eval(Environment env) {
		for (ASTree ast : children) {
			ast.eval(env);
		}
		return "";
	}

	public Object eval(Environment env, Pane circuitpane, Pane outcomepane, int num){
        Text ind = new Text(15, 20, "");
        circuitpane.getChildren().add(ind);
        
        //在这里i=0时得到（1,0；0,0）；i=1时得到（0.707,0；0,0.707）。
        for (int i = 0; i < numChildren(); i++) {
        	//long startTime = System.currentTimeMillis();
        	
            Object o = children.get(i).eval(env,circuitpane,outcomepane,num);
            if(o instanceof String){
            	if(o.toString().equals("buyunxuceliangde")){
            		break;
				}
			}
            ind.setText("Step:" + Integer.toString(i + 1) );
            
            //long endTime = System.currentTimeMillis();
            //System.out.println(endTime - startTime);
        }
        
        //最后输出向量
        Qubit q = (Qubit) env.get("QuantumRegisterDX");
        outcomepane.getChildren().clear();
        if(num == 1){
            Text outcome = new Text(10,20,"current state : " + AmpToBra.translate(q.getPossibles()));
        	outcomepane.getChildren().add(outcome);
        }
        if(num == 2){
            Text outcome = new Text(10,20,"current state : "+ q.matrix());
            outcomepane.getChildren().add(outcome);
        }
        
	    return null;
    }

    public Object eval(Environment env, Pane circuitpane, Pane outcomepane, int num , int step){
        Text ind = new Text(15, 20, "");
        circuitpane.getChildren().add(ind);

        for (int i = 0; i < step; i++) {
			Object o = children.get(i).eval(env,circuitpane,outcomepane,num);
			if(o instanceof String){
				if(o.toString().equals("buyunxuceliangde")){
					break;
				}
			}
            ind.setText("Step:" + Integer.toString(i + 1) );
        }
        return null;
    }


}
