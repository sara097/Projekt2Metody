package sample;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class ODE implements FirstOrderDifferentialEquations {

    //klasa całkująca równania różniczkowe

    //atrybuty klasy
    private double am;
    private double an;
    private double ah;
    private double bm;
    private double bn;
    private double bh;
    private double C;
    private double ENa;
    private double EK;
    private double EL;
    private double gNa;
    private double gK;
    private double gL;
    private double I;

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

    //metoda zwracająca rząd równania różniczkowego
    @Override
    public int getDimension() {
        return 4;
    }

    //metoda obliczająca pochodne
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

        //skad Ik, INa i IL ????????????? to by czesci wyznaczania u, ale to mam robic dodatkowe rownania rozniczkowe?

        //i tu rownania na parametry chyba jeszcze
        am=(0.1*(25-x[3]))/(Math.exp((25-x[3])/10)-1); //17a
        bm=4*Math.exp(-x[3]/18); //17b
        an=(0.01*(10-x[3]))/(Math.exp((10-x[3])/10)-1); //18a
        bn=0.125*Math.exp(-x[3]/80); //18b
        ah=0.07*Math.exp(-x[3]/20); //19a
        bh=1/(Math.exp((30-x[3])/10)+1); //19b


        dxdt[0]=am*(1-x[0])-bm*x[0]; //16a
        dxdt[1]=an*(1-x[1])-bn*x[1]; //16b
        dxdt[2]=ah*(1-x[2])-bh*x[2]; //16c
        dxdt[3]=(-1*((gNa*Math.pow(x[0],3.0)*x[2]*(x[3]-ENa))+((gK*Math.pow(x[1],4.0)*(x[3]-EK))+(gL*(x[3]-EL))))+I)/C; //20



    }
}
