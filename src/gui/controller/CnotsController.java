package gui.controller;

import java.util.ArrayList;
import java.util.List;

import gui.model.CcnotOperator;
import gui.model.CnotsOperator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class CnotsController extends Controller {
	@FXML
    private TextField target;

    @FXML
    private TextField controls;

    @FXML
    private TextField step;

    int addClicked = -1;

    // 操作的位置
    int targetL;

    // 控制的位置
    List<Integer> controlsl = new ArrayList<Integer>();

    // 第幾部操作
    int stepL;
    // 寄存器
    int register;



    public int isAdd() {
        return addClicked;
    }

    @FXML
    private void handleAdd() {
        if (isInputValid()) {
            addClicked = stepL;
            circuit.addOperator(new CnotsOperator(targetL, controlsl, id), stepL);
            dialogStage.close();
        }
    }


    private boolean isInputValid() {
        String errorMessage = "";
        try {
            targetL = Integer.parseInt(target.getText());
            parse(controls.getText());
            
            stepL = Integer.parseInt(step.getText());
        } catch (Exception e) {
            errorMessage = "Enter Integer values ";
        }
        register = circuit.getRegister();

        

        if (stepL < 0 || stepL > circuit.getNumberOfOperators()) {
            errorMessage = "Enter valid index for gate step, should be between 0 and " + circuit.getNumberOfOperators();
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errorMessage);
            alert.setTitle("Error");
            alert.showAndWait();
            return false;
        }
    }
    
    public void parse(String str) {
    	String[] strArray = null;   
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray 
        for(int i = 0; i < strArray.length; i++) {
        	controlsl.add(Integer.parseInt(strArray[i]));
        }
    }
}
