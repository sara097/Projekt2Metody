package sample;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.util.ArrayList;
import java.util.Arrays;

public class Path implements StepHandler {

    //klasa zapisujaca nam kolejne kroki wycałkowane z rownan różniczkowych

    //atrubuty klasy ( listy tablicowe do których zapisujemy uzyskane wartosci
    private ArrayList<Double> mValues = new ArrayList<>();
    private ArrayList<Double> nValues = new ArrayList<>();
    private ArrayList<Double> hValues = new ArrayList<>();
    private ArrayList<Double> uValues = new ArrayList<>();
    private ArrayList<Double> times = new ArrayList<>();
    private double time = 0;

    public ArrayList<Double> getmValues() {
        return mValues;
    }

    public void setmValues(ArrayList<Double> mValues) {
        this.mValues = mValues;
    }

    public ArrayList<Double> getnValues() {
        return nValues;
    }

    public void setnValues(ArrayList<Double> nValues) {
        this.nValues = nValues;
    }

    public ArrayList<Double> gethValues() {
        return hValues;
    }

    public void sethValues(ArrayList<Double> hValues) {
        this.hValues = hValues;
    }

    public ArrayList<Double> getuValues() {
        return uValues;
    }

    public void setuValues(ArrayList<Double> uValues) {
        this.uValues = uValues;
    }

    public ArrayList<Double> getTimes() {
        return times;
    }



    public double getTime() {
        return time;
    }

    //nieuzywana metoda inicjalizująca
    @Override
    public void init(double v, double[] doubles, double v1) {

    }

    //pobieranie kroku i zapisywanie go do list tablicowych oraz wyswietlanie
    @Override
    public void handleStep(StepInterpolator stepInterpolator, boolean b) throws MaxCountExceededException {

        double t = stepInterpolator.getCurrentTime(); //pobranie obecnego

        double[] x = stepInterpolator.getInterpolatedState();
        time = t;

        mValues.add(x[0]);
        nValues.add(x[1]);
        hValues.add(x[2]);
        uValues.add(x[3]);
        times.add(time);

        System.out.println("t= " + t + " " + Arrays.toString(x));


    }
}
