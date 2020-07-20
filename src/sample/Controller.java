package sample;

import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.util.Precision;

public class Controller {

    @FXML TextField factorA; @FXML TextField factorL; @FXML TextField factorN; @FXML TextField factorLbd;
    @FXML TextField xMin; @FXML TextField xMax;
    @FXML Button button; @FXML Button reset_btn;
    @FXML LineChart<Number, Number> chart;
    @FXML NumberAxis xAxis; @FXML NumberAxis yAxis;
    @FXML Slider limitSlider; @FXML Slider stepSlider; @FXML Slider LSlider; @FXML Slider NSlider; @FXML Slider LbdSlider;
    @FXML Label lbl_step; @FXML Label lbl_L; @FXML Label lbl_N; @FXML Label lbl_Lbd; @FXML Label lbl_Min; @FXML Label lbl_Max; @FXML Label monitor;
    @FXML CheckBox cb_symb; @FXML CheckBox cb_pol; @FXML CheckBox cb_anim; @FXML CheckBox cb_line_anim;
    @FXML Rectangle rect_anim;
    @FXML Polyline polyline;
    PathTransition transition;

    UnivariateIntegrator integrator;
    double result;

    @FXML
    public void runAnimation() {

        //transition.setPath(polyline) is raping me

    }

    @FXML
    public void updateChart() {

        if (cb_anim.isSelected()) { chart.setAnimated(false); }
        else chart.setAnimated(cb_line_anim.isSelected());

        if (cb_symb.isSelected()) chart.setCreateSymbols(true); else if (!cb_symb.isSelected()) chart.setCreateSymbols(false);

        cb_anim.setDisable(false);

        XYChart.Series<Number, Number> series = getSeries();
        chart.getData().clear();
        chart.getData().add(series);

        polyline.getPoints().clear();

    }

    public void initialize(){

        monitor.toFront();

        initChartProperties();
        initInputControls();
        XYChart.Series<Number, Number> series = getSeries();
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        polyline.getPoints().clear();

        rect_anim.setVisible(false);

        // Alternate // 5B5B5B // Elephant 16 //
        lbl_step.setText("Step:"); lbl_step.setTextFill(Color.web("#757575")); lbl_step.setFont(new Font("Arial Bold", 16));
        lbl_L.setText("L:"); lbl_L.setTextFill(Color.web("#757575")); lbl_L.setFont(new Font("Arial Bold", 16));
        lbl_N.setText("N:"); lbl_N.setTextFill(Color.web("#757575")); lbl_N.setFont(new Font("Arial Bold", 16));
        lbl_Lbd.setText("λ:"); lbl_Lbd.setTextFill(Color.web("#757575")); lbl_Lbd.setFont(new Font("Arial Bold", 16));
        lbl_Min.setText("Mn:"); lbl_Min.setTextFill(Color.web("#757575")); lbl_Min.setFont(new Font("Arial Bold", 16));
        lbl_Max.setText("Mx:"); lbl_Max.setTextFill(Color.web("#757575")); lbl_Max.setFont(new Font("Arial Bold", 16));

        monitor.setTextFill(Color.web("#757575")); monitor.setFont(new Font("Arial Bold", 12));

//        chart.setOnMouseMoved(new EventHandler<MouseEvent>() {
//            //Bounds boundsInScene = chart.getNode().localToScene(point.getNode().getBoundsInLocal(), true);
//            @Override public void handle(MouseEvent event) {
//
//                String msg =
//                    //"X: "  + (chart.screenToLocal(event.getX() * xAxis.getScaleX(), event.getY() * yAxis.getScaleY())) +
//                    "X: "  + xAxis.getZeroPosition() + event.getX();
//
//                monitor.setText(msg);
//
//            }
//        });

        factorA.textProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });
        factorL.textProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });
        factorN.textProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });
        factorLbd.textProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });
        xMin.textProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });
        xMax.textProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });
        cb_pol.selectedProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });
        cb_symb.selectedProperty().addListener((observable, oldValue, newValue) -> {
            cb_anim.setSelected(false); cb_anim.setDisable(true);
        });

        limitSlider.setOrientation(Orientation.VERTICAL); limitSlider.setShowTickLabels(true); limitSlider.setShowTickMarks(true); limitSlider.setMajorTickUnit(10); limitSlider.setBlockIncrement(0.1); limitSlider.setMax(8.0); limitSlider.setValue(Double.parseDouble(xMax.getText())); limitSlider.setMin(6.0);
        limitSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
//                if (cb_pol.isSelected()) {
//                    xMax.setText(String.valueOf(Precision.round((double) newValue, 3)));
//                    xMin.setText(String.valueOf(0));
//                } else {
                    xMax.setText(String.valueOf(Precision.round((double) newValue, 1)));
                    //xMin.setText(String.valueOf(Precision.round(-(double) newValue, 1)));
                    //xMin.setText(String.valueOf(6.7));
//                }
            }
        });

        stepSlider.setShowTickLabels(true); stepSlider.setShowTickMarks(true); stepSlider.setMajorTickUnit(0.4); stepSlider.setBlockIncrement(0.0001); stepSlider.setMax(0.001); stepSlider.setValue(Double.parseDouble(factorA.getText())); stepSlider.setMin(0.0002);
        stepSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorA.setText(String.valueOf(Precision.round((double)newValue, 5)));
            }
        });

        LSlider.setShowTickLabels(true); LSlider.setShowTickMarks(true); LSlider.setMajorTickUnit(4); LSlider.setBlockIncrement(1); LSlider.setMax(10); LSlider.setValue(Double.parseDouble(factorL.getText())); LSlider.setMin(0.1);
        LSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorL.setText(String.valueOf(Precision.round((double)newValue, 2)));
            }
        });

        NSlider.setShowTickLabels(true); NSlider.setShowTickMarks(true); NSlider.setMajorTickUnit(4); NSlider.setBlockIncrement(0.5); NSlider.setMax(10); NSlider.setValue(Double.parseDouble(factorN.getText())); NSlider.setMin(0.1);
        NSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorN.setText(String.valueOf(Precision.round((double)newValue, 2)));
            }
        });

        LbdSlider.setShowTickLabels(true); LbdSlider.setShowTickMarks(true); LbdSlider.setMajorTickUnit(4); LbdSlider.setBlockIncrement(0.5); LbdSlider.setMax(10); LbdSlider.setValue(Double.parseDouble(factorLbd.getText())); LbdSlider.setMin(1.0);
        LbdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorLbd.setText(String.valueOf(Precision.round((double)newValue, 2)));
            }
        });

        cb_symb.setSelected(false); cb_symb.setText("Draw symbols");
        cb_pol.setSelected(false); cb_pol.setText("Draw polar");
        cb_anim.setSelected(false); cb_anim.setText("Animate object");
        cb_line_anim.setSelected(true); cb_line_anim.setText("Animate chart");

        rect_anim.setVisible(true); rect_anim.setFill(Color.DODGERBLUE);
        rect_anim.setWidth(20.0); rect_anim.setHeight(20.0);
        rect_anim.setVisible(false);

        series.setNode(rect_anim);
        chart.getData().add(series);

        transition = new PathTransition();
        transition.setNode(rect_anim);
        transition.setDuration(Duration.seconds(3));

        transition.setCycleCount(2);
        //transition.setCycleCount(PathTransition.INDEFINITE);
        transition.setAutoReverse(true);

        reset_btn.setText("Reset"); reset_btn.setTextFill(Color.web("#757575")); reset_btn.setFont(new Font("Arial Bold", 12));
        button.setText("Proceed"); button.setTextFill(Color.web("#757575")); button.setFont(new Font("Arial Bold", 14));

    }

    private double iterate_x(double a, double b, double c) {

        //(Math.pow(S, 3) / (6 * C)) - (Math.pow(S, 7) / (336 * Math.pow(C, 3)));
        //Math.signum(a) * Math.abs(a) * Math.pow(b, 1.0/2.0) * Math.cos(b);
        return c * (a - Math.pow(a, 5) / (1 * 2 * 5 * Math.pow(b, 4)) + Math.pow(a, 9) / (1 * 2 * 3 * 4 * 9 * Math.pow(b, 8)) - Math.pow(a, 13) / (1 * 2 * 3 * 4 * 5 * 6 * 13 * Math.pow(b, 12)));
        //return (Math.pow(a, 2) - 1) / (Math.pow(a, 2) + 1);

    }

    private double iterate_y(double a, double b, double d) {

        //S - (Math.pow(S, 5) / (40 * Math.pow(C, 2)));
        //Math.signum(a) * Math.abs(a) * Math.pow(b, 1.0/2.0) * Math.sin(b);
        return d * (Math.pow(a, 3) / (1 * 3 * Math.pow(b, 2)) - Math.pow(a, 7) / (1 * 2 * 3 * 7 * Math.pow(b, 6)) + Math.pow(a, 11) / (1 * 2 * 3 * 4 * 5 * 11 * Math.pow(b, 10)) - Math.pow(a, 15) / (1 * 2 * 3 * 4 * 5 * 6 * 7 * 15 * Math.pow(b, 14)));
        //return (2 * a * Math.pow(a, 2) - 1) / Math.pow((Math.pow(a, 2) + 1), 2);

    }

    private XYChart.Series<Number, Number> getSeries() {

        double xMax1 = Double.parseDouble(xMax.getText());
        double xMin1 = Double.parseDouble(xMin.getText());

        double step = Double.parseDouble(factorA.getText());
        double L = Double.parseDouble(factorL.getText());
        double N = Double.parseDouble(factorN.getText());
        double Lbd = Double.parseDouble(factorLbd.getText());

        double function_body = 0;

        XYChart.Series<Number,Number> series = new XYChart.Series<Number, Number>();
//        XYChart.Series<Number,Number> ersatz = new XYChart.Series<Number, Number>();
        series.setName("Chart");

        double x_temp = 0, y_temp = 0; int i = 0;
        double pol_rds = 0, pol_angle = 10;

        for (double a = xMin1; a < xMax1; a += step) {

            i++;

            if (cb_pol.isSelected()) {// && a != 0) {

//                integrator = new SimpsonIntegrator(1.0e-12, 1.0e-8, 1, 32);
//
//                double finalA = a, finalS = b;
//                x_temp = integrator.integrate(100, new UnivariateFunction() {
//                    @Override
//                    public double value(double v) {
//                        v = finalA;
//                        //return Math.sin(Math.PI * Math.pow(v, 2));
//                        return Math.cos(Math.PI * Math.pow(v, 2) / 2);
//                        //return Math.cos(3 * (Math.pow(v, 2) / 2 * Math.pow(3, 2)) + 1);
//                    }
//                }, -10, 10);
//
//                y_temp = integrator.integrate(100, new UnivariateFunction() {
//                    @Override
//                    public double value(double v) {
//                        v = finalA;
//                        //return Math.cos(Math.PI * Math.pow(v, 2));
//                        return Math.sin(Math.PI * Math.pow(v, 2) / 2);
//                        //return Math.sin(3 * (Math.pow(v, 2) / 2 * Math.pow(3, 2)) + 1);
//                    }
//                }, -10, 10);


                //limitSlider.setMajorTickUnit(Math.PI); limitSlider.setBlockIncrement(0.1);
                //limitSlider.setMajorTickUnit(50); limitSlider.setBlockIncrement(1); limitSlider.setMax(100); //limitSlider.setMin(0);

                //if (a < 0) {
                //    x_temp = iterate_x(a, b, c); y_temp = -iterate_y(a, b, d);
                //} else {
                //x_temp = iterate_x(a, b, c); y_temp = iterate_y(a, b, d);
                //}

                //if (cb_pol.isSelected()) {

//                pol_rds = Math.sqrt(Math.pow(x_temp, 2) + Math.pow(y_temp, 2));
                pol_angle = a;

                //pol_rds = Math.pow(1.0 / 2.0, pol_angle); // логарифмическая спираль
                //pol_rds = Math.pow(3, pol_angle); // логарифмическая спираль
                //pol_rds = 5 * Math.cos(pol_angle / 2); // спираль [0; 3]
                //pol_rds = 2 * Math.sin(2 * pol_angle); // ??? [0; 6.3]
                //pol_rds = Math.sqrt(pol_angle); //
                //pol_rds = Math.sin(pol_angle) / pol_angle; // спираль

                //pol_rds = 3 * Math.sqrt(Math.cos(7 * pol_angle)); // [x] роза
                //pol_rds = 3 * Math.sqrt(Math.cos(3 * pol_angle)); // [x] 3

                if (a > 1)
                    //pol_rds = Math.sqrt(a * 3); // ??? [0; 6.3]  <-----------------
                    pol_rds = 10 / Math.sqrt(a); // ??? [0; 6.3]  <-----------------
                //pol_rds = -3 * Math.pow(Math.cos(2 * pol_angle), 3); // ??? [0; 6.3]  <-----------------
                //x_temp = pol_rds * Math.cos(pol_angle); y_temp = pol_rds * Math.sin(pol_angle); // <-----------------
                //x_temp = Math.cos(Math.pow(a, 2) / 2); y_temp = Math.sin(Math.pow(a, 2) / 2); // <-----------------
                //x_temp += 1.0 / 2.0 * Math.cos(Math.pow(Math.pow(2, a), 2) / 2 * Math.pow(2, 2));
                //y_temp += 1.0 / 2.0 * Math.sin(Math.pow(Math.pow(2, a), 2) / 2 * Math.pow(2, 2));

                //x_temp = 3 * Math.sin(a); y_temp = 3 * (1 - Math.cos(a));

                x_temp = iterate_x(a, L, N); y_temp = iterate_y(a, L, Lbd);

//                ub = 0;
//                lb = 10;
//                n = 1000;
//
//                y_temp = IntSimpson(ub, lb, n);

                //}

            } else {
                //limitSlider.setMajorTickUnit(10); limitSlider.setBlockIncrement(1); limitSlider.setMax(25); limitSlider.setMin(5);

                function_body = Lbd * (Math.pow(Math.pow(2, a), 2) / 2 * Math.pow(2, 2));
                //System.out.println("Lbd is " + Lbd);

                x_temp += L / N * Math.cos(function_body);
                y_temp += L / N * Math.sin(function_body);
            }

            System.out.println("[" + i + "]: " + "x_temp is " + x_temp + "; y_temp is " + y_temp);

            polyline.getPoints().addAll(xAxis.getDisplayPosition(x_temp) + 70.0, yAxis.getDisplayPosition(y_temp) + 20.0);

            if(cb_symb.isSelected()) {

                XYChart.Data chartData;
                chartData = new XYChart.Data(x_temp, y_temp);
                chartData.setNode(new ShowDotCoordinates(x_temp, y_temp));
                series.getData().add(chartData);

            } else {

                series.getData().add(new XYChart.Data(x_temp, y_temp));

            }

        }

        monitor.setText("Dots: " + i);

//        double pre_last_x = 0, pre_last_y = 0, last_x = 0, last_y = 0, rate_x, rate_y;
//
//        ersatz.getData().add(new XYChart.Data(0, 0));
//
//        pre_last_x = Double.parseDouble(String.valueOf(ersatz.getData().get(ersatz.getData().size() - 1).getXValue()));
//        pre_last_y = Double.parseDouble(String.valueOf(ersatz.getData().get(ersatz.getData().size() - 1).getYValue()));
//        System.out.println("[Ersatz] Pre-last; x: " + pre_last_x + "; y: " + pre_last_y);
//
//        ersatz.getData().add(new XYChart.Data(1, 1));
//
//        last_x = Double.parseDouble(String.valueOf(ersatz.getData().get(ersatz.getData().size() - 1).getXValue()));
//        last_y = Double.parseDouble(String.valueOf(ersatz.getData().get(ersatz.getData().size() - 1).getYValue()));
//        System.out.println("[Ersatz] The last; x: " + last_x + "; y: " + last_y);


        rect_anim.toFront();

        if (cb_anim.isSelected()) {

//            try {
//                System.out.println("Waiting of 1000...");
//                Thread.sleep(1000);
//                System.out.println("Done...");
//            } catch (java.lang.InterruptedException ie) { System.out.println(ie); }

            transition.setPath(polyline);

            button.setDisable(true);
            transition.setOnFinished(e -> { button.setDisable(false); rect_anim.setVisible(false); } );

            transition.play();
            rect_anim.setVisible(true);

        }
        else { // if (!cb_anim.isSelected() && transition.getStatus() == STOPPED) {

            //transition.stop();
            rect_anim.setVisible(false);

        }

        return series;
    }

    private void initInputControls() {
        xMax.setText("6.77");
        xMin.setText("6.7");
        factorA.setText("2.0E-4");
        factorL.setText("0.1");
        factorN.setText("0.1");
        factorLbd.setText("1.0");
        chart.setCreateSymbols(false);
    }

    private void initChartProperties() {
        xAxis.setLabel("X");
        yAxis.setLabel("Y");
    }

    public void resetParameters() {

        xMax.setText("6.77");
        xMin.setText("6.7");
        factorA.setText("2.0E-4");
        factorL.setText("0.1");
        factorN.setText("0.1");
        factorLbd.setText("1.0");

        limitSlider.setValue(Double.parseDouble(xMax.getText()));
        stepSlider.setValue(Double.parseDouble(factorA.getText()));
        LSlider.setValue(Double.parseDouble(factorL.getText()));
        NSlider.setValue(Double.parseDouble(factorN.getText()));
        LbdSlider.setValue(Double.parseDouble(factorLbd.getText()));

        xMax.setText("6.77");
        xMin.setText("6.7");

    }
}