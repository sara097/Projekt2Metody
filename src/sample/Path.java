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
    private ArrayList<Double> Inas=new ArrayList<>();
    private ArrayList<Double> Iks=new ArrayList<>();
    private ArrayList<Double> Ils=new ArrayList<>();
    private double Ina;
    private double Ik;
    private double Il;
    private double C;
    private double ENa;
    private double EK;
    private double EL;
    private double gNa;
    private double gK;
    private double gL;
    private double I;

    public Path(double c, double ENa, double EK, double EL, double gNa, double gK, double gL, double i) {
        C = c;
        this.ENa = ENa;
        this.EK = EK;
        this.EL = EL;
        this.gNa = gNa;
        this.gK = gK;
        this.gL = gL;
        I = i;
    }

    public void setTimes(ArrayList<Double> times) {
        this.times = times;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public ArrayList<Double> getInas() {
        return Inas;
    }

    public void setInas(ArrayList<Double> inas) {
        Inas = inas;
    }

    public ArrayList<Double> getIks() {
        return Iks;
    }

    public void setIks(ArrayList<Double> iks) {
        Iks = iks;
    }

    public ArrayList<Double> getIls() {
        return Ils;
    }

    public void setIls(ArrayList<Double> ils) {
        Ils = ils;
    }

    public double getIna() {
        return Ina;
    }

    public void setIna(double ina) {
        Ina = ina;
    }

    public double getIk() {
        return Ik;
    }

    public void setIk(double ik) {
        Ik = ik;
    }

    public double getIl() {
        return Il;
    }

    public void setIl(double il) {
        Il = il;
    }

    public double getC() {
        return C;
    }

    public void setC(double c) {
        C = c;
    }

    public double getENa() {
        return ENa;
    }

    public void setENa(double ENa) {
        this.ENa = ENa;
    }

    public double getEK() {
        return EK;
    }

    public void setEK(double EK) {
        this.EK = EK;
    }

    public double getEL() {
        return EL;
    }

    public void setEL(double EL) {
        this.EL = EL;
    }

    public double getgNa() {
        return gNa;
    }

    public void setgNa(double gNa) {
        this.gNa = gNa;
    }

    public double getgK() {
        return gK;
    }

    public void setgK(double gK) {
        this.gK = gK;
    }

    public double getgL() {
        return gL;
    }

    public void setgL(double gL) {
        this.gL = gL;
    }

    public double getI() {
        return I;
    }

    public void setI(double i) {
        I = i;
    }

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

        Ina=gNa*Math.pow(x[0],3)*x[2]*(x[3]-ENa);
        Ik=gK*Math.pow(x[1],4)*(x[3]-EK);
        Il=gL*(x[3]-EL);

        Inas.add(Ina);
        Iks.add(Ik);
        Ils.add(Il);

        mValues.add(x[0]);
        nValues.add(x[1]);
        hValues.add(x[2]);
        uValues.add(x[3]);
        times.add(time);

        System.out.println("t= " + t + " " + Arrays.toString(x));


    }
}
