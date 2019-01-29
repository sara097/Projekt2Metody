package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Class Controller represents GUI control methods.
 * @author Julia Szymczak and Sara Strzalka
 * @version 1.0
 */
public class Controller {

    /**
     * Represents chart with potential in time values.
     */
    @FXML
    private LineChart<Number, Number> utChart;

    /**
     * Represents x axis of potential chart.
     */
    @FXML
    private NumberAxis uxAxis;

    /**
     * Represents y axis of potential chart.
     */
    @FXML
    private NumberAxis uyAxis;

    /**
     * Represents current in time chart.
     */
    @FXML
    private LineChart<Number, Number> itChart;

    /**
     * Represents x axis of current chart.
     */
    @FXML
    private NumberAxis ixAxis;

    /**
     * Represents y axis of current chart.
     */
    @FXML
    private NumberAxis iyAxis;

    /**
     * Represents text field with standard deviation
     */
    @FXML
    private TextField stdTextField;

    /**
     * Represents text field with average value of maximum potential.
     */
    @FXML
    private TextField avgTextField;

    /**
     * Represents textfield with value of maximum potential.
     */
    @FXML
    private TextField maxTextField;

    /**
     * Represents textfield with frequency value.
     */
    @FXML
    private TextField freqTextField;

    @FXML
    private TextField cTextField;

    @FXML
    private TextField uTextField;

    @FXML
    private TextField enaTextField;

    @FXML
    private TextField gNaTextField;

    @FXML
    private TextField ekTextField;

    @FXML
    private TextField elTextField;

    @FXML
    private TextField gKTextField;

    @FXML
    private TextField gLTextField;

    @FXML
    private LineChart<Number, Number> inaChart;

    @FXML
    private NumberAxis inaxAxis;

    @FXML
    private NumberAxis inayAxis;

    @FXML
    private LineChart<Number, Number> ikChart;

    @FXML
    private NumberAxis ikxAxis;

    @FXML
    private NumberAxis ikyAxis;

    @FXML
    private LineChart<Number, Number> ilChart;

    @FXML
    private NumberAxis ilxAxis;

    @FXML
    private NumberAxis ilyAxis;

    @FXML
    private LineChart<Number, Number> mChart;

    @FXML
    private NumberAxis mxAxis;

    @FXML
    private NumberAxis myAxis;

    @FXML
    private LineChart<Number, Number> hChart;

    @FXML
    private NumberAxis hxAxis;

    @FXML
    private NumberAxis hyAxis;

    @FXML
    private LineChart<Number, Number> nChart;

    @FXML
    private NumberAxis nxAxis;

    @FXML
    private NumberAxis nyAxis;

    /**
     * Represents time values.
     */
    private ArrayList<Double> time;
    /**
     * Represents potential values.
     */
    private ArrayList<Double> uValues;

    /**
     * Returns way of text format to write parameters.
     *
     * @return TextFormatter
     */
    private TextFormatter format() { //prywatna metoda (może ją użyc tylko metoda z klasy) zwracająca obiekt typu TextFormatter
        //ustawienie formatowania tekstu w polach tekstowych (zeby nie wpisywać niedozwolonych wartości
        Pattern pattern = Pattern.compile("[\\-]?\\d{0,10}([\\.]\\d{0,2})?"); //ustawienie wzoru formatowania tekstu

        //ustawienie formatowania tekstu z użyciem interfejsu UnaryOperator
        // Tworze obiekt klasy TextFormatter, w którego konstruktorze używam operatora lambda
        //jesli wyrazenie wpisywane nie pasuje do wzoru formatowania nie pojawia się w polu tekstowym
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null; //wyrazenie lambda zwraca null jesli wpisywany tekst nie pasuje do wzrou formatowania
        });
        return formatter; //metoda zwraca obiekt typu TextFormatter
    }

    /**
     * Initialize application after launching.
     */
    @FXML
    void initialize() {

        cTextField.setTextFormatter(format());
        enaTextField.setTextFormatter(format());
        ekTextField.setTextFormatter(format());
        elTextField.setTextFormatter(format());
        gNaTextField.setTextFormatter(format());
        gKTextField.setTextFormatter(format());
        gLTextField.setTextFormatter(format());
        uTextField.setTextFormatter(format());

        cTextField.setText("1.0");
        enaTextField.setText("115.0");
        ekTextField.setText("-12.0");
        elTextField.setText("10.6");
        gNaTextField.setText("120");
        gKTextField.setText("36.0");
        gLTextField.setText("0.3");
        uTextField.setText("0");
    }

    /**
     * Method called after button clicked. Draws plots.
     * @param event Button pressed.
     */
    @FXML
    void rysujClicked(ActionEvent event) {

        utChart.getData().removeAll(utChart.getData());
        itChart.getData().removeAll(itChart.getData());

        mChart.getData().removeAll(mChart.getData());
        nChart.getData().removeAll(nChart.getData());
        hChart.getData().removeAll(hChart.getData());

        inaChart.getData().removeAll(inaChart.getData());
        ikChart.getData().removeAll(ikChart.getData());
        ilChart.getData().removeAll(ilChart.getData());

        double I = 0;
        double C = Double.valueOf(cTextField.getText());
        double ENa = Double.valueOf(enaTextField.getText());
        double Ek = Double.valueOf(ekTextField.getText());
        double El = Double.valueOf(elTextField.getText());
        double gNa = Double.valueOf(gNaTextField.getText());
        double gK = Double.valueOf(gKTextField.getText());
        double gL = Double.valueOf(gLTextField.getText());

        FirstOrderDifferentialEquations ode = new ODE(C, ENa, Ek, El, gNa, gK, gL, I);
        //utworzenie integratora Runge Kutta

        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01);
        //utworzneie obiektu klasy Path
        Path path = new Path(ENa, Ek, El, gNa, gK, gL);
        //dodanie do integratora step Handlera
        integrator.addStepHandler(path);

        //ustawienie czasu poczatkowego i koncowego
        double Tk = 0.001; //dokładnosc z jaka interesuje nasz czas (zeby wyznaczyc kiedy minie 15% pomiaru)
        double te = 50;
        double T15 = (0.15 * te);

        //wartosci poczatkowe
        double u0 = Double.valueOf(uTextField.getText());
        double am = (0.1 * (25 - u0)) / (Math.exp((25 - u0) / 10) - 1); //17a
        double bm = 4 * Math.exp(-u0 / 18); //17b
        double an = (0.01 * (10 - u0)) / (Math.exp((10 - u0) / 10) - 1); //18a
        double bn = 0.125 * Math.exp(-u0 / 80); //18b
        double ah = 0.07 * Math.exp(-u0 / 20); //19a
        double bh = 1 / (Math.exp((30 - u0) / 10) + 1); //19b

        double m0 = am / (am + bm);
        double n0 = an / (an + bn);
        double h0 = ah / (ah + bh);

        double[] yStart = new double[]{m0, n0, h0, u0}; //warunki początkowe
        double[] yStop = new double[]{0, 1, 0, 1};

        integrator.integrate(ode, 0, yStart, T15, yStop); //całkowanie równania

        //jesli mineło 15% pomiaru ( w granicy dokładnosci)
        if ((path.getTime() < (T15) + Tk) && (path.getTime() > (T15) - Tk)) {
            System.out.println("nowe i");
            //jesli tak to zmiana prądu
            I = 15;
            // ustawienie nowych wartosci początkowych
            m0 = path.getmValues().get(path.getmValues().size() - 1);
            n0 = path.getnValues().get(path.getnValues().size() - 1);
            h0 = path.gethValues().get(path.gethValues().size() - 1);
            u0 = path.getuValues().get(path.getuValues().size() - 1);

            ode = new ODE(C, ENa, Ek, El, gNa, gK, gL, I);
            yStart = new double[]{m0, n0, h0, u0}; //warunki początkowe
            System.out.println(Arrays.toString(yStart));
            yStop = new double[]{0, 1, 0, 1};

            //ponowne całkowanie równania

            integrator.integrate(ode, T15, yStart, te, yStop);
        }

        time = path.getTimes();
        uValues = path.getuValues();

        ArrayList<Double> INaValues = path.getInas();
        ArrayList<Double> IKValues = path.getIks();
        ArrayList<Double> ILValues = path.getIls();

        ArrayList<Double> mValues = path.getmValues();
        ArrayList<Double> nValues = path.getnValues();
        ArrayList<Double> hValues = path.gethValues();

        //utowrzenie serii danych
        XYChart.Series<Number, Number> uSeries = new XYChart.Series();
        XYChart.Series<Number, Number> ISeries = new XYChart.Series();

        XYChart.Series<Number, Number> INaSeries = new XYChart.Series();
        XYChart.Series<Number, Number> IKSeries = new XYChart.Series();
        XYChart.Series<Number, Number> ILSeries = new XYChart.Series();

        XYChart.Series<Number, Number> mSeries = new XYChart.Series();
        XYChart.Series<Number, Number> nSeries = new XYChart.Series();
        XYChart.Series<Number, Number> hSeries = new XYChart.Series();

        for (int i = 0; i < uValues.size(); i++) {
            //dodanie wartosci do serii danych
            if (time.get(i) > 7.5) ISeries.getData().add(new XYChart.Data<>(time.get(i), I));
            else ISeries.getData().add(new XYChart.Data<>(time.get(i), 0));

            uSeries.getData().add(new XYChart.Data<>(time.get(i), uValues.get(i)));

            INaSeries.getData().add(new XYChart.Data<>(time.get(i), INaValues.get(i)));
            IKSeries.getData().add(new XYChart.Data<>(time.get(i), IKValues.get(i)));
            ILSeries.getData().add(new XYChart.Data<>(time.get(i), ILValues.get(i)));

            mSeries.getData().add(new XYChart.Data<>(time.get(i), mValues.get(i)));
            nSeries.getData().add(new XYChart.Data<>(time.get(i), nValues.get(i)));
            hSeries.getData().add(new XYChart.Data<>(time.get(i), hValues.get(i)));

        }

        //dodanie serii do wykresu i opisanie osi
        utChart.getData().add(uSeries);
        itChart.getData().add(ISeries);

        mChart.getData().add(mSeries);
        nChart.getData().add(nSeries);
        hChart.getData().add(hSeries);

        inaChart.getData().add(INaSeries);
        ikChart.getData().add(IKSeries);
        ilChart.getData().add(ILSeries);

        //hmn
        hxAxis.setTickUnit(1);
        hxAxis.setAutoRanging(true);
        mxAxis.setTickUnit(1);
        mxAxis.setAutoRanging(true);
        nxAxis.setTickUnit(1);
        nxAxis.setAutoRanging(true);
        hyAxis.setTickUnit(1);
        hyAxis.setAutoRanging(true);
        myAxis.setTickUnit(1);
        myAxis.setAutoRanging(true);
        nyAxis.setTickUnit(1);
        nyAxis.setAutoRanging(true);

        uxAxis.setTickUnit(1);
        uxAxis.setAutoRanging(true);
        uyAxis.setTickUnit(1);
        uyAxis.setAutoRanging(true);

        ixAxis.setTickUnit(1);
        ixAxis.setAutoRanging(true);
        iyAxis.setTickUnit(1);
        iyAxis.setAutoRanging(true);

        ikxAxis.setTickUnit(1);
        ikxAxis.setAutoRanging(true);
        ikyAxis.setTickUnit(1);
        ikyAxis.setAutoRanging(true);

        inaxAxis.setTickUnit(1);
        inaxAxis.setAutoRanging(true);
        inayAxis.setTickUnit(1);
        inayAxis.setAutoRanging(true);

        ilxAxis.setTickUnit(1);
        ilxAxis.setAutoRanging(true);
        ilyAxis.setTickUnit(1);
        ilyAxis.setAutoRanging(true);

        calculateStats(uValues);


    }

    /**
     * Method that calculates statistics of simulation.
     * @param u potential values.
     */
    private void calculateStats(ArrayList<Double> u) {

        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Double> maxU = new ArrayList<>();

        for (int i = 1; i < u.size() - 1; i++) {
            if (u.get(i) > u.get(i - 1) && u.get(i) > u.get(i + 1) && u.get(i) > 1) {
                times.add(time.get(i));
                maxU.add(uValues.get(i));
            }
        }

        System.out.println(Arrays.toString(maxU.toArray()));
        double mean = 0;
        double sum = 0;
        double sumstd = 0;
        double std = 0;
        double max = 0;
        double fs = 0;
        double sumTime = 0;

        for (int i = 0; i < times.size(); i++) {
            if (max < maxU.get(i)) max = maxU.get(i);

            sum += maxU.get(i);
            if (i > 0) sumTime += times.get(i) - times.get(i - 1);
            else sumTime = time.get(i);
        }

        mean = sum / maxU.size();
        fs = 1 / (sumTime / (times.size() - 1));

        for (int i = 0; i < times.size(); i++) {
            sumstd += (Math.pow(maxU.get(i) - mean, 2) / (times.size() - 1));
        }

        std = Math.pow(sumstd, 0.5);

        freqTextField.setText(String.format("%.4f", fs));
        maxTextField.setText(String.format("%.2f", max));
        avgTextField.setText(String.format("%.2f", mean));
        stdTextField.setText(String.format("%.2f", std));


    }

}

