package sample;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class PathODE represents path for parameters integration.
 * @author Julia Szymczak and Sara Strzalka
 * @version 1.0
 */
public class Path implements StepHandler {

    /**
     * Represents array with m parameter values.
     */
    private ArrayList<Double> mValues = new ArrayList<>();
    /**
     * Represents array with n parameter values.
     */
    private ArrayList<Double> nValues = new ArrayList<>();
    /**
     * Represents array with h parameter values.
     */
    private ArrayList<Double> hValues = new ArrayList<>();
    /**
     * Represents array with potential values.
     */
    private ArrayList<Double> uValues = new ArrayList<>();
    /**
     * Represents array with time values.
     */
    private ArrayList<Double> times = new ArrayList<>();
    /**
     * Represents time value.
     */
    private double time = 0;
    /**
     * Represents array with sodium ion current values.
     */
    private ArrayList<Double> Inas=new ArrayList<>();
    /**
     * Represents array with potassium ion current values.
     */
    private ArrayList<Double> Iks=new ArrayList<>();
    /**
     * Represents array with ion current values.
     */
    private ArrayList<Double> Ils=new ArrayList<>();
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
     * Constructor creates object with given parameters.
     * @param ENa sodium potential
     * @param EK potassium potential
     * @param EL potential
     * @param gNa sodium conductance
     * @param gK potassium conductance
     * @param gL conductance
     */
    public Path(double ENa, double EK, double EL, double gNa, double gK, double gL) {
        this.ENa = ENa;
        this.EK = EK;
        this.EL = EL;
        this.gNa = gNa;
        this.gK = gK;
        this.gL = gL;
    }


    /**
     * Returns sodium ion current array.
     * @return sodium ion current array
     */
    public ArrayList<Double> getInas() {
        return Inas;
    }

    /**
     * Returns potassium ion current array.
     * @return potassium ion current array
     */
    public ArrayList<Double> getIks() {
        return Iks;
    }

    /**
     * Returns ion current array.
     * @return ion current array
     */
    public ArrayList<Double> getIls() {
        return Ils;
    }

    /**
     * Returns parameter m array.
     * @return parameter m array.
     */
    public ArrayList<Double> getmValues() {
        return mValues;
    }

    /**
     * Returns parameter n array.
     * @return parameter n array.
     */
    public ArrayList<Double> getnValues() {
        return nValues;
    }

    /**
     * Returns parameter h array.
     * @return parameter h array.
     */
    public ArrayList<Double> gethValues() {
        return hValues;
    }

    /**
     * Returns potential array.
     * @return potential array.
     */
    public ArrayList<Double> getuValues() {
        return uValues;
    }

    /**
     * Returns time array.
     * @return time array.
     */
    public ArrayList<Double> getTimes() {
        return times;
    }

    /**
     * Returns time value.
     * @return time value.
     */
    public double getTime() {
        return time;
    }

    /**
     * Not used init metod.
     * @param v
     * @param doubles
     * @param v1
     */
    @Override
    public void init(double v, double[] doubles, double v1) {

    }


    /**
     * Method called for handling integration steps
     * Responsible for setting parameters for integration.
     * @param stepInterpolator
     * @param b
     */
    @Override
    public void handleStep(StepInterpolator stepInterpolator, boolean b) throws MaxCountExceededException {

        double t = stepInterpolator.getCurrentTime(); //pobranie obecnego

        double[] x = stepInterpolator.getInterpolatedState();
        time = t;

        double ina = gNa * Math.pow(x[0], 3) * x[2] * (x[3] - ENa);
        double ik = gK * Math.pow(x[1], 4) * (x[3] - EK);
        double il = gL * (x[3] - EL);

        Inas.add(ina);
        Iks.add(ik);
        Ils.add(il);

        mValues.add(x[0]);
        nValues.add(x[1]);
        hValues.add(x[2]);
        uValues.add(x[3]);
        times.add(time);

        System.out.println("t= " + t + " " + Arrays.toString(x));


    }
}
