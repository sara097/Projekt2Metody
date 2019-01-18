package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

import java.util.ArrayList;

public class Controller {

    @FXML
    private Button doButton;

    @FXML
    private LineChart<Number, Number> uChart;

    @FXML
    private NumberAxis xAxisuChart;

    @FXML
    private NumberAxis yAxisUChart;

    @FXML
    private LineChart<Number, Number> vChart;

    @FXML
    private NumberAxis xAxisvChart;

    @FXML
    private NumberAxis yAxisvChart;

    @FXML
    private LineChart<Number, Number> IChart;

    @FXML
    private NumberAxis xAxisiChart;

    @FXML
    private NumberAxis yAxisiChart;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField aParamTxt;

    @FXML
    private TextField bParamTxt;

    @FXML
    private TextField cParamTxt;

    @FXML
    private TextField dParamTxt;

    @FXML
    void clicked(ActionEvent event) {

        double I=0;
        double C=1.0;
        double ENa=115.0;
        double Ek=-12.0;
        double El=10.6;
        double gNa=120.0;
        double gK=36.0;
        double gL=0.3;
        FirstOrderDifferentialEquations ode = new ODE(C,ENa,Ek,El,gNa,gK,gL,I);
        //utworzenie integratora Runge Kutta

        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01);
        //utworzneie obiektu klasy Path
        Path path = new Path();
        //dodanie do integratora step Handlera
        integrator.addStepHandler(path);

        //ustawienie czasu poczatkowego i koncowego
        double Tk = 0.001; //dokładnosc z jaka interesuje nasz czas (zeby wyznaczyc kiedy minie 15% pomiaru)
        double te = 50;
        double T15 = (0.15 * te);

        //wartosci poczatkowe
        double u0=0;
        double am=(0.1*(25-u0))/(Math.exp((25-u0)/10)-1); //17a
       double bm=4*Math.exp(-u0/18); //17b
        double an=(0.01*(10-u0))/(Math.exp((10-u0)/10)-1); //18a
        double bn=0.125*Math.exp(-u0/80); //18b
        double ah=0.07*Math.exp(-u0/20); //19a
        double bh=1/(Math.exp((30-u0)/30)-1); //19b

        double m0=am/(am+bm);
        double n0=an/(an+bn);
        double h0=ah/(ah/bh);

        double[] yStart = new double[]{m0, n0, h0, u0}; //warunki początkowe
        double[] yStop = new double[]{0, 1, 0 ,1};

        integrator.integrate(ode, 0, yStart, T15, yStop); //całkowanie równania

        //jesli mineło 15% pomiaru ( w granicy dokładnosci)
        if ((path.getTime() < (T15) + Tk) && (path.getTime() > (T15) - Tk)) {
            System.out.println("nowe i");
            //jesli tak to zmiana prądu
            I = 10;
            // ustawienie nowych wartosci początkowych
            m0 = path.getmValues().get(path.getmValues().size() - 1);
            n0= path.getnValues().get(path.getnValues().size() - 1);
            h0=path.gethValues().get(path.gethValues().size() - 1);
            u0 = path.getuValues().get(path.getuValues().size() - 1);

            ode = new ODE(C,ENa,Ek,El,gNa,gK,gL,I);
            yStart = new double[]{m0, n0, h0, u0}; //warunki początkowe
            yStop = new double[]{0, 1, 0 ,1};

            //ponowne całkowanie równania

            integrator.integrate(ode, T15, yStart, te, yStop);
        }

        ArrayList<Double> uValues = path.getuValues();
        ArrayList<Double> mValues = path.gethValues(); //to cos nie działa, wgl widać wikres dobrze dopiero po rozszerzeniu okna i wartości są z kosmosu
        ArrayList<Double> time = path.getTimes();


        //utowrzenie serii danych
        XYChart.Series<Number, Number> uSeries = new XYChart.Series();
        XYChart.Series<Number, Number> vSeries = new XYChart.Series();
        XYChart.Series<Number, Number> iSeries = new XYChart.Series();

        for (int i = 0; i < uValues.size(); i++) {
        //dodanie wartosci do serii danych
            if (time.get(i) > 7.5) iSeries.getData().add(new XYChart.Data<>(time.get(i), I));
            else iSeries.getData().add(new XYChart.Data<>(time.get(i), 0));

            uSeries.getData().add(new XYChart.Data<>(time.get(i), uValues.get(i)));
            vSeries.getData().add(new XYChart.Data<>(time.get(i), mValues.get(i)));

        }

        //dodanie serii do wykresu i opisanie osi
        vChart.getData().add(vSeries);
        uChart.getData().add(uSeries);
        IChart.getData().add(iSeries);

        yAxisiChart.setTickUnit(1);
        yAxisiChart.setAutoRanging(true);
        xAxisiChart.setTickUnit(1);
        xAxisiChart.setAutoRanging(true);


        yAxisUChart.setTickUnit(1);
        yAxisUChart.setAutoRanging(true);
        xAxisuChart.setTickUnit(1);
        xAxisuChart.setAutoRanging(true);


        yAxisvChart.setTickUnit(1);
        yAxisvChart.setAutoRanging(true);
        xAxisvChart.setTickUnit(1);
        xAxisvChart.setAutoRanging(true);


    }

}
