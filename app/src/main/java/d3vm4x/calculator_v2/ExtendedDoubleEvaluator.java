package d3vm4x.calculator_v2;

import android.content.Context;
import java.util.Iterator;
import java.lang.String;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;
import com.fathzer.soft.javaluator.Constant;
import com.fathzer.soft.javaluator.Operator;

public class ExtendedDoubleEvaluator extends DoubleEvaluator {
    private static Context main_context = Calculator.getContext();
//    private static String sqrt = main_context.getString(R.string.sqrt);
//    private static String cbrt = main_context.getString(R.string.cbrt);
    private static String pi = main_context.getString(R.string.pi);
    private static String expt = main_context.getString(R.string.root);

    private static final Function FACTORIAL = new Function("!", 1);
    private static final Function SQRT = new Function("sqrt", 1);
    private static final Function CBRT = new Function("cbrt", 1);
    private static final Function SIND = new Function("sind", 1);
    private static final Function COSD = new Function("cosd", 1);
    private static final Function TAND = new Function("tand", 1);
    private static final Function ASIND = new Function("asind", 1);
    private static final Function ACOSD = new Function("acosd", 1);
    private static final Function ATAND = new Function("atand", 1);
    private static final Constant PI = new Constant(pi);
    private static final Operator EXPT = new Operator(expt, 2, Operator.Associativity.LEFT, 3);
    private static final Operator COMBO = new Operator("C", 2, Operator.Associativity.LEFT, 3);
    private static final Operator PERM = new Operator("P", 2, Operator.Associativity.LEFT, 3);
    private static final Parameters PARAMS;
    static {
        // Gets the default DoubleEvaluator's parameters
        PARAMS = DoubleEvaluator.getDefaultParameters();
        // add the new sqrt function to these parameters
        PARAMS.add(FACTORIAL);
        PARAMS.add(SQRT);
        PARAMS.add(CBRT);
        PARAMS.add(SIND);
        PARAMS.add(COSD);
        PARAMS.add(TAND);
        PARAMS.add(ASIND);
        PARAMS.add(ACOSD);
        PARAMS.add(ATAND);
        PARAMS.add(PI);
        PARAMS.add(EXPT);
        PARAMS.add(COMBO);
        PARAMS.add(PERM);
    }
    public ExtendedDoubleEvaluator() {
        super(PARAMS);
    }
    @Override
    protected Double evaluate(Function function, Iterator<Double> arguments, Object evaluationContext) {
        if (function == SQRT) {
            // Implements the new function
            return Math.sqrt(arguments.next());
        }
        else if(function == CBRT) {
            return Math.cbrt(arguments.next());
        }
        else if (function == FACTORIAL) {
            return factorial(arguments.next());
        }
        else if (function == SIND) {
            return Math.sin(arguments.next() * 3.14159265358979323846 / 180);
        }
        else if (function == COSD) {
            return Math.cos(arguments.next() * 3.14159265358979323846 / 180);
        }
        else if (function == TAND) {
            return Math.tan(arguments.next() * 3.14159265358979323846 / 180);
        }
        else if (function == ASIND) {
            return Math.asin(arguments.next()) * 180 / 3.14159265358979323846;
        }
        else if (function == ACOSD) {
            return Math.acos(arguments.next()) * 180 / 3.14159265358979323846;
        }
        else if (function == ATAND) {
            return Math.atan(arguments.next()) * 180 / 3.14159265358979323846;
        }
        else {
            // If it's another function, pass it to DoubleEvaluator
            return super.evaluate(function, arguments, evaluationContext);
        }
    }
    @Override
    protected Double evaluate(Constant constant, Object evaluationContext) {
        if (constant == PI)
            return 3.14159265358979323846;
        else
            return super.evaluate(constant, evaluationContext);
    }

    @Override
    protected Double evaluate(Operator operator, Iterator<Double> operands, Object evaluationContext) {
        if (operator == EXPT) {
           Double exp = operands.next();
           Double base = operands.next();
           return Math.pow(base, 1 / exp);
        }
        else if(operator == COMBO) {
            Double n = operands.next();
            Double r = operands.next();
            return factorial(n)/(factorial(r) * factorial(n - r));
        }
        else if(operator == PERM) {
            Double n = operands.next();
            Double r = operands.next();
            return factorial(n)/factorial(n - r);
        }
        else
           return super.evaluate(operator, operands, evaluationContext);
    }
    private double factorial(Double n) {
        if(n != Math.floor(n) || n < 0)
            return Double.NaN;
        if (n > 1) {
            return n * factorial(n-1);
        }
        else {
            return 1;
        }
    }
}
