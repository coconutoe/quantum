package ast;

import java.util.ArrayList;
import java.util.List;

import Quantum.Qubit;
import interp.Environment;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import operater.CnotsGateOperator;
import util.AmpToBra;
import util.FindSource;
import util.Function;

public class List_CnotsOP extends ASTList {

	public List_CnotsOP(List<ASTree> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	//第一个是操作门变量
	public ASTree cnotsOp() {
		return child(0);
	}

	//第i个变量
	public ASTree argument(int i) {
		return child(i);
	}
		
	public String toString() {
		String str = "(";
		for(int i = 1; i < size(); i++) {
			str = str + argument(i) + " ";
		}
		
		str = str.trim();
		str = str + ")";
		return str;
		
	}
	
	public List<Integer> allArguments(Environment env) {
		List<Integer> arguments = new ArrayList<Integer>();
		for(int i = 1; i < size(); i++) {
			arguments.add(FindSource.findSource(env,argument(i).toString()));
		}
		return arguments;
	}
	
	@Override
	public Object eval(Environment env) {
		List<Integer> arguments = allArguments(env);
		
		//获取操作对象，在这个实验里是QuantumRegisterDX，正常应该解析Array得到对象，偷懒中。。。
		Qubit q = (Qubit) env.get("QuantumRegisterDX");
		//执行操作
		CnotsGateOperator.excute(q,arguments);
		//因为量子状态是全局的，所以都不要更新
		return null;
	}
	
	public Object eval(Environment env, Pane circuitpane, Pane outcomepane, int num) {
		eval(env);
		Qubit q = (Qubit)env.get("QuantumRegisterDX");

		int changeNum = (int)env.get("changeDx");

		int startX = beginLineX + 20+  changeNum*rowspace;
		Group g = new Group();

		//三条横线
		for (int i = 0; i < q.number(); i++) {
			Line l = new Line(startX, rowspace * i + beginLineY, startX + rowspace, rowspace * i + beginLineY);
			g.getChildren().add(l);
		}
		
		List<Integer> arguments = allArguments(env);
		
		int maxLocation = Function.maxElement(arguments);
        int minLocation = Function.minElement(arguments);

        int targetLocation = arguments.get(arguments.size() - 1);

        Circle targetCircle = new Circle(startX + 15, rowspace * targetLocation + beginLineY, 13);
        targetCircle.setFill(Color.DARKSEAGREEN);
        
        for(int i = 0; i < arguments.size() - 1; i++) {
        	Circle circle = new Circle(startX + 15, rowspace * arguments.get(i) + beginLineY, 6);
            circle.setFill(Color.DARKSEAGREEN);
            g.getChildren().addAll(circle);
        }
        Line line = new Line(startX + 15, rowspace * minLocation + beginLineY, startX + 15, rowspace * maxLocation + beginLineY);
        line.setStroke(Color.DARKSEAGREEN);
        g.getChildren().addAll(line, targetCircle);
        circuitpane.getChildren().add(g);
        

		outcomepane.getChildren().clear();
        if(num == 1){
            Text outcome = new Text(10,20,"current state : "+ AmpToBra.translate(q.getPossibles()));
            outcomepane.getChildren().add(outcome);
        }

        if( num == 2){
            Text outcome = new Text(10,20,"当前的状态的密度矩阵为："+ q.matrix());
            outcomepane.getChildren().add(outcome);
        }

		env.put("changeDx",changeNum+1);
		
		
		return null;
	}

}
