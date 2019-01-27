package sample;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

/**
 * Class ODE represents computing core for given derivatives.
 * @author Julia Szymczak and Sara Strzalka
 * @version 1.0
 */
public class ODE implements FirstOrderDifferentialEquations {

    /**
     * Represents capacity.
     */
    private double C;
    /**
     * Represents sodium potential
     */
    private double ENa;
    /**
     * Represents potassium potential
     */
    private double EK;
    /**
     * Represents potential
     */
    private double EL;
    /**
     * Represents sodium conductance
     */
    private double gNa;
    /**
     * Represents sodium conductance
     */
    private double gK;
    /**
     * Represents conductance
     */
    private double gL;
    /**
     * Represents current
     */
    private double I;

    /**
     * Constructor creates object with given parameters.
     * @param c capacity
     * @param ENa sodium potential
     * @param EK potassium potential
     * @param EL potential
     * @param gNa sodium conductance
     * @param gK potassium conductance
     * @param gL conductance
     * @param i current
     */
    public ODE(double c, double ENa, double EK, double EL, double gNa, double gK, double gL, double i) {
        C = c;
        this.ENa = ENa;
        this.EK = EK;
        this.EL = EL;
        this.gNa = gNa;
        this.gK = gK;
        this.gL = gL;
        I = i;
    }

    /**
     * Method called for setting matrix's dimension.
     */
    @Override
    public int getDimension() {
        return 4;
    }

    /**
     * Method called for computing derivatives.
     * @param dxdt derivatives
     * @param t time
     * @param x variable to compute derivatives
     */
    @Override
    public void computeDerivatives(double t, double[] x, double[] dxdt) throws MaxCountExceededException, DimensionMismatchException {

        //dxdt[0] to pochodna m
        //dxdt[1] to pochodna n
        //dxdt[2] to pochodna h
        //dxdt[3] to pochodna u
        //x[0] to m
        //x[1] to n
        //x[2] to h
        //x[3] to u

        double am = (0.1 * (25 - x[3])) / (Math.exp((25 - x[3]) / 10) - 1);
        double bm = 4 * Math.exp(-x[3] / 18);
        double an = (0.01 * (10 - x[3])) / (Math.exp((10 - x[3]) / 10) - 1);
        double bn = 0.125 * Math.exp(-x[3] / 80);
        double ah = 0.07 * Math.exp(-x[3] / 20);
        double bh = 1 / (Math.exp((30 - x[3]) / 10) + 1);


        dxdt[0]= am *(1-x[0])- bm *x[0]; //16a
        dxdt[1]= an *(1-x[1])- bn *x[1]; //16b
        dxdt[2]= ah *(1-x[2])- bh *x[2]; //16c
        dxdt[3]=(-1*((gNa*Math.pow(x[0],3.0)*x[2]*(x[3]-ENa))+((gK*Math.pow(x[1],4.0)*(x[3]-EK))+(gL*(x[3]-EL))))+I)/C; //20



    }
}
