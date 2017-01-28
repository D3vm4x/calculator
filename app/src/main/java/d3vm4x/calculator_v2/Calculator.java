package d3vm4x.calculator_v2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import java.lang.String;
import android.content.Context;
import android.widget.Toast;

public class Calculator extends AppCompatActivity {
    String sqrt = "";
    String pi = "";
    String cbrt = "";
    String root = "";
    String expt = "";
    Boolean inverse = true;
    private TextView display;
    private TextView expression;
    String MODE = "";
    String onDisplay="";
    String expDisplay = "";
    public String last_answer = "";
//    String sign="";
    String deg_mode = "RAD";
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.textView);
        expression = (TextView) findViewById(R.id.expView);
        // set the resource values to the stylistic strings
        sqrt = getString(R.string.sqrt);
        cbrt = getString(R.string.cbrt);
        pi = getString(R.string.pi);
        root = getString(R.string.root);
        expt = getString(R.string.expt);
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    // appends value to display based on button clicks
    public void getValue(View v) {
        Button button = (Button)v;
        String str = button.getText().toString();
        switch(str) {
            case "1":
                onDisplay += "1";
                break;
            case "2":
                onDisplay += "2";
                break;
            case "3":
                onDisplay += "3";
                break;
            case "4":
                onDisplay += "4";
                break;
            case "5":
                onDisplay += "5";
                break;
            case "6":
                onDisplay += "6";
                break;
            case "7":
                onDisplay += "7";
                break;
            case "8":
                onDisplay += "8";
                break;
            case "9":
                onDisplay += "9";
                break;
            case "0":
                onDisplay += "0";
                break;
            case "(":
                onDisplay += "(";
                break;
            case ")":
                onDisplay += ")";
                break;
            case "e^x":
                onDisplay += "e^(";
                break;
            case "10^x":
                onDisplay += "10^(";
                break;
            case "log":
                onDisplay += "log(";
                break;
            case "ln":
                onDisplay += "ln(";
                break;
            case "x!":
                onDisplay += "!(";
                break;
            case "x^2":
                onDisplay += "^2";
                break;
            case "x^3":
                onDisplay += "^3";
                break;
            case "x^y":
                onDisplay += "^(";
                break;
            case ".":
                onDisplay += ".";
                break;
            case "ANS":
                if(!last_answer.equals(""))
                    onDisplay += last_answer;
                break;
        }
        // if calculator mode is set to degrees
        if(deg_mode.equals("DEG")) {
            switch (str) {
                case "asin":
                    onDisplay += "asind(";
                    break;
                case "acos":
                    onDisplay += "acosd(";
                    break;
                case "atan":
                    onDisplay += "atand(";
                    break;
                case "tan":
                    onDisplay += "tand(";
                    break;
                case "cos":
                    onDisplay += "cosd(";
                    break;
                case "sin":
                    onDisplay += "sind(";
                    break;
            }
        } // if calculator mode is set to radians
        else {
            switch (str) {
                case "asin":
                    onDisplay += "asin(";
                    break;
                case "acos":
                    onDisplay += "acos(";
                    break;
                case "atan":
                    onDisplay += "atan(";
                    break;
                case "tan":
                    onDisplay += "tan(";
                    break;
                case "cos":
                    onDisplay += "cos(";
                    break;
                case "sin":
                    onDisplay += "sin(";
                    break;
            }
        }
        if(str.equals(sqrt))
            onDisplay += "sqrt(";
        else if(str.equals(cbrt))
            onDisplay += "cbrt(";
        else if(str.equals(pi))
            onDisplay += pi;
        else if(str.equals(expt))
            onDisplay += root;
    }

    // appends given values to display based on button clicks
    public void OnClick(View v) {
        if(MODE.equals("ANS")) {
            onDisplay = "";
            expDisplay = "";
        }
        checkError();
        onDisplay = onDisplay.replaceAll("^0+","");
        if (onDisplay.startsWith("."))
            onDisplay = "0" + onDisplay;
        getValue(v);
        display.setText(onDisplay);
        MODE = "APPEND";
    }

    // appends the "^" power operator
    public void OnExpt(View v) {
        checkError();
        if(MODE != "OP" && !onDisplay.isEmpty()) {
            Button button = (Button) v;
            String power = button.getText().toString();
            if(power.equals("x^2"))
                power = "^2";
            else if(power.equals("x^3"))
                power = "^3";
            else if(power.equals("x^y"))
                power = "^";
            onDisplay += power;
            display.setText(onDisplay);
            MODE = "APPEND";
        }
        else {
            Context contxt = getApplicationContext();
            CharSequence text = "Missing Operand";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(contxt, text, duration);
            toast.show();
        }
    }

    // appends the binary operators +, -, *, / to display
    public void OnAdd(View v) {
        checkError();
        Button button = (Button)v;
        String sign = button.getText().toString();
        if(MODE.equals("OP")) {
            onDisplay = onDisplay.substring(0, onDisplay.length() - 1) + sign;
            display.setText(onDisplay);
        }
        else
            onDisplay += sign;
        display.setText(onDisplay);
        MODE = "OP";
    }

    // evaluates the given expression onDisplay and sets the answer to onDisplay
    // on buttonclick "="
    public void OnCalculate(View v) {
        try {
            expDisplay = onDisplay + "=";
            Double result = new ExtendedDoubleEvaluator().evaluate(onDisplay);
            onDisplay = format_answer(result);
            expression.setText(expDisplay);
            display.setText(onDisplay);
            MODE = "ANS";
        } catch(Exception e) {
            display.setText("ERROR");
            MODE = "ERROR";
        }
    }

    // formats the result to fit the screen of the display
    public String format_answer(Double result) {
        last_answer = result.toString();
        last_answer = removeZeros(last_answer);
        int length = last_answer.length();
        if (last_answer.contains("E")) {
            String partnum = last_answer.substring(0, 8);
            String partE = last_answer.substring(last_answer.indexOf("E"), length);
            last_answer = partnum + partE;
            last_answer = last_answer.replaceAll("E", "*10^(");
            last_answer += ")";
        }
        else if(length > 15) {
            last_answer = last_answer.substring(0, 15);
        }
        return last_answer;
    }

    // deletes the last character of onDisplay expression on buttonclick "C"
    public void OnClear(View v) {
        if(onDisplay.length() > 0) {
            onDisplay = onDisplay.substring(0, onDisplay.length() - 1);
        }
        display.setText(onDisplay);
        MODE = "ERASE";
    }

    // clears all expression and display values on buttonclick "AC"
    public void OnAllClear(View v) {
        onDisplay = "";
        expDisplay = "";
        last_answer = "";
        display.setText("");
        expression.setText("");
        MODE = "CLEAR";
    }

    // reveals the "2nd" option of function/operator buttons
    public void OnShift(View v) {
        Button sin = (Button) findViewById(R.id.buttonSin);
        Button cos = (Button) findViewById(R.id.buttonCos);
        Button tan = (Button) findViewById(R.id.buttonTan);
        Button log = (Button) findViewById(R.id.buttonLog);
        Button ln = (Button) findViewById(R.id.buttonLn);
        Button factorial = (Button) findViewById(R.id.buttonFactorial);
        Button sqr = (Button) findViewById(R.id.buttonSqr);
        Button cbr = (Button) findViewById(R.id.buttonCbr);
        Button exp = (Button) findViewById(R.id.buttonExp);
        if(inverse == false) {
            sin.setText("sin");
            cos.setText("cos");
            tan.setText("tan");
            log.setText("log");
            ln.setText("ln");
            factorial.setText("x!");
            sqr.setText("x^2");
            cbr.setText("x^3");
            exp.setText("x^y");
            inverse = true;
        }
        else {
            sin.setText("asin");
            cos.setText("acos");
            tan.setText("atan");
            log.setText("10^x");
            ln.setText("e^x");
            factorial.setText("x!");
            sqr.setText(sqrt);
            cbr.setText(cbrt);
            exp.setText(expt);
            inverse = false;
        }
    }

    // sets the calculator to either "DEG" or "RAD" mode
    public void OnDegree(View v) {
        Button btn = (Button) v;
        if (deg_mode.equals("RAD")) {
            btn.setText(deg_mode);
            deg_mode = "DEG";
        }
        else {
            btn.setText(deg_mode);
            deg_mode = "RAD";
        }
    }

    // removes trailing zeros of a decimal
    public String removeZeros(String s) {
        s = !s.contains(".") ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
        return s;
    }

    // clears the display if there is an error
    public void checkError() {
        if(MODE.equals("ERROR")) {
            onDisplay = "";

        }
    }
}
