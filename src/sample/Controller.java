package sample;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javafx.scene.shape.Box;
import javafx.util.Duration;

import javax.swing.*;

import static javafx.animation.Animation.Status.STOPPED;

public class Controller {
    @FXML TextField factorA; @FXML TextField factorB; @FXML TextField factorC; @FXML TextField factorD;
    @FXML TextField xMin; @FXML TextField xMax;

    @FXML
    Button button;
    @FXML
    LineChart<Number, Number> chart;
    @FXML
    NumberAxis xAxis;
    @FXML
    NumberAxis yAxis;
    @FXML Slider limitSlider; @FXML Slider stepSlider; @FXML Slider SSlider; @FXML Slider CSlider; @FXML Slider DSlider;
    @FXML Label lbl_step; @FXML Label lbl_S; @FXML Label lbl_C; @FXML Label lbl_D; @FXML Label lbl_Min; @FXML Label lbl_Max;
    @FXML CheckBox cb_symb; @FXML CheckBox cb_pol; @FXML CheckBox cb_anim;
    @FXML Rectangle rect_anim;

    @FXML Polyline polyline;
    PathTransition transition;

    @FXML Label monitor;

    @FXML CheckBox cb_line_anim;

//    // final?
//    NumberAxis xAxis = new NumberAxis();
//    NumberAxis yAxis = new NumberAxis();
//    LineChart<Number,Number> chart = new LineChart<Number,Number>(xAxis,yAxis);
//    TextField factorA = new TextField("0.1");
//    TextField factorB = new TextField("10.0");
//    TextField factorC = new TextField("");
//    TextField xMin = new TextField("-10.0");
//    TextField xMax = new TextField("10.0");
//    //

    @FXML
    public void updateChart() {

        if (cb_anim.isSelected()) { chart.setAnimated(false); }
        else { chart.setAnimated(true); }

        XYChart.Series<Number, Number> series = getSeries();
        chart.getData().clear();
        chart.getData().add(series);

        polyline.getPoints().clear();
        //polyline.setVisible(true);

        if (cb_symb.isSelected()) chart.setCreateSymbols(true); else if (!cb_symb.isSelected()) chart.setCreateSymbols(false);
    }

    public void initialize(){
        initChartProperties();
        initInputControls();
        XYChart.Series<Number, Number> series = getSeries();
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        //chart.getData().add(series);

        polyline.getPoints().clear();
        //polyline.setVisible(true);

        rect_anim.setVisible(false);

        // 5B5B5B // Elephant 16 //
        lbl_step.setText("Step:"); lbl_step.setTextFill(Color.web("#757575")); lbl_step.setFont(new Font("Arial Bold", 16));
        lbl_S.setText("S:"); lbl_S.setTextFill(Color.web("#757575")); lbl_S.setFont(new Font("Arial Bold", 16));
        lbl_C.setText("C:"); lbl_C.setTextFill(Color.web("#757575")); lbl_C.setFont(new Font("Arial Bold", 16));
        lbl_D.setText("D:"); lbl_D.setTextFill(Color.web("#757575")); lbl_D.setFont(new Font("Arial Bold", 16));
        lbl_Min.setText("Mn:"); lbl_Min.setTextFill(Color.web("#757575")); lbl_Min.setFont(new Font("Arial Bold", 16));
        lbl_Max.setText("Mx:"); lbl_Max.setTextFill(Color.web("#757575")); lbl_Max.setFont(new Font("Arial Bold", 16));

        monitor.setText("X = "); monitor.setTextFill(Color.web("#757575")); monitor.setFont(new Font("Arial Bold", 16));

        chart.setOnMouseMoved(new EventHandler<MouseEvent>() {
            //Bounds boundsInScene = chart.getNode().localToScene(point.getNode().getBoundsInLocal(), true);
            @Override public void handle(MouseEvent event) {
                String msg =
                        //"(x: "       + event.getX()      + ", y: "       + event.getY()       + ") -- " +
                                        //"X: "  + (xAxis.getDisplayPosition(0) + event.getX() - 1375.0) / 100 +
                                        "X: "  + (chart.screenToLocal(event.getX() * xAxis.getScaleX(), event.getY() * yAxis.getScaleY())) +
                                                "\nX: "  + (xAxis.getDisplayPosition(0) + event.getX() - 1375.0) / 100 * xAxis.getScaleX() +
                                                "\nY: "  + (yAxis.getDisplayPosition(0) + event.getY() - 695.0) / 100 * yAxis.getScaleY();

                monitor.setText(msg);
            }
        });

        limitSlider.setOrientation(Orientation.VERTICAL); limitSlider.setShowTickLabels(true); limitSlider.setShowTickMarks(true); limitSlider.setMajorTickUnit(10); limitSlider.setBlockIncrement(1); limitSlider.setMax(25); limitSlider.setValue(Double.parseDouble(xMax.getText())); limitSlider.setMin(5);
        limitSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                xMin.setText(String.valueOf(-(double)newValue)); xMax.setText(String.valueOf(newValue));
            }
        });
        stepSlider.setShowTickLabels(true); stepSlider.setShowTickMarks(true); stepSlider.setMajorTickUnit(0.4); stepSlider.setBlockIncrement(0.05); stepSlider.setMax(1); stepSlider.setValue(Double.parseDouble(factorA.getText())); stepSlider.setMin(0.1);
        stepSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorA.setText(String.valueOf(newValue));
            }
        });
        SSlider.setShowTickLabels(true); SSlider.setShowTickMarks(true); SSlider.setMajorTickUnit(25); SSlider.setBlockIncrement(5); SSlider.setMax(50); SSlider.setValue(Double.parseDouble(factorB.getText())); SSlider.setMin(0.1);
        SSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorB.setText(String.valueOf(newValue));
            }
        });
        CSlider.setShowTickLabels(true); CSlider.setShowTickMarks(true); CSlider.setMajorTickUnit(4); CSlider.setBlockIncrement(0.5); CSlider.setMax(10); CSlider.setValue(Double.parseDouble(factorC.getText())); CSlider.setMin(0.1);
        CSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorC.setText(String.valueOf(newValue));
            }
        });
        DSlider.setShowTickLabels(true); DSlider.setShowTickMarks(true); DSlider.setMajorTickUnit(4); DSlider.setBlockIncrement(0.5); DSlider.setMax(10); DSlider.setValue(Double.parseDouble(factorD.getText())); DSlider.setMin(0.1);
        DSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorD.setText(String.valueOf(newValue));
            }
        });

        cb_symb.setSelected(true); cb_symb.setText("Draw symbols");
        cb_pol.setSelected(false); cb_pol.setText("Draw polar");
        cb_anim.setSelected(false); cb_anim.setText("Animate object");
        cb_line_anim.setSelected(true); cb_line_anim.setText("Animate line");

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DODGERBLUE);
        //redMaterial.setSpecularColor(Color.BLUE);

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

    }

    double f (double x) {                                              //DEFINIR la fonction à intégrer.
        return Math.sin(Math.PI * Math.pow(x, 2) / 2);
    }

    double IntSimpson(double a, double b, int n){                       //Methode de Simpson pour calcul intégrale
        int i,z;                                                       //a= borne inférieure et b, borne supérieure d'intégration
        double h,s;                                                    //n = nombre de pas

        n=n+n;
        s = f(a)*f(b);
        h = (b-a)/n;
        z = 4;

        for(i = 1; i<n; i++){
            s = s + z * f(a+i*h);
            z = 6 - z;
        }
        return (s * h)/3;
    }

    double ub = 0, lb = 0; int n = 0;

    private double iterate_x(double a, double b, double c) {

        //(Math.pow(S, 3) / (6 * C)) - (Math.pow(S, 7) / (336 * Math.pow(C, 3)));
        //Math.signum(a) * Math.abs(a) * Math.pow(b, 1.0/2.0) * Math.cos(b);
        return c * (a - Math.pow(a, 5) / (1 * 2 * 5 * Math.pow(b, 4)) + Math.pow(a, 9) / (1 * 2 * 3 * 4 * 9 * Math.pow(b, 8)) - Math.pow(a, 13) / (1 * 2 * 3 * 4 * 5 * 6 * 13 * Math.pow(b, 12)));

    }

    private double iterate_y(double a, double b, double d) {

        //S - (Math.pow(S, 5) / (40 * Math.pow(C, 2)));
        //Math.signum(a) * Math.abs(a) * Math.pow(b, 1.0/2.0) * Math.sin(b);
        return d * (Math.pow(a, 3) / (1 * 3 * Math.pow(b, 2)) - Math.pow(a, 7) / (1 * 2 * 3 * 7 * Math.pow(b, 6)) + Math.pow(a, 11) / (1 * 2 * 3 * 4 * 5 * 11 * Math.pow(b, 10)) - Math.pow(a, 15) / (1 * 2 * 3 * 4 * 5 * 6 * 7 * 15 * Math.pow(b, 14)));

    }

    private XYChart.Series<Number, Number> getSeries() {

        double xMax1 = Double.parseDouble(xMax.getText());
        double xMin1 = Double.parseDouble(xMin.getText());
        double step = Double.parseDouble(factorA.getText());
        double b = Double.parseDouble(factorB.getText());
        double c = Double.parseDouble(factorC.getText());
        double d = Double.parseDouble(factorD.getText());

        XYChart.Series<Number,Number> series = new XYChart.Series<Number, Number>();
        XYChart.Series<Number,Number> ersatz = new XYChart.Series<Number, Number>();
        series.setName("Chart");

        double x_temp = 0, y_temp = 0; int i = 0;
        double pol_sin = 0, pol_cos = 0, pol_rds = 0, pol_angle = 10;

        for (double a = xMin1; a < xMax1; a += step) {

            i++;

            if (cb_pol.isSelected() && a!=0) {

                x_temp = iterate_x(a, b, c);
                y_temp = iterate_y(a, b, d);

                if (cb_pol.isSelected()) {

                    pol_rds = Math.sqrt(Math.pow(x_temp, 2) + Math.pow(y_temp, 2));
                    pol_angle = Math.atan(x_temp / y_temp);

                    x_temp = pol_rds * Math.cos(pol_angle);
                    y_temp = pol_rds * Math.sin(pol_angle);

//                ub = 0;
//                lb = 10;
//                n = 1000;
//
//                y_temp = IntSimpson(ub, lb, n);

                }

            } else {

                x_temp = iterate_x(a, b, c);
                y_temp = iterate_y(a, b, d);

            }

            System.out.println("[" + i + "]: " + "x_temp is " + x_temp + "; y_temp is " + y_temp);

            polyline.getPoints().addAll(xAxis.getDisplayPosition(x_temp) + 75.0, yAxis.getDisplayPosition(y_temp) + 20.0);
            series.getData().add(new XYChart.Data(x_temp, y_temp));

        }

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

        polyline.setVisible(true);
        rect_anim.toFront();
        //polyline.getPoints().remove(0, 30);
        //rect_anim.setX(polyline.getPoints().get(0)); rect_anim.setY(polyline.getPoints().get(1));

        if (cb_anim.isSelected()) {

//            try {
//                System.out.println("Waiting of 1000...");
//                Thread.sleep(1000);
//                System.out.println("Done...");
//            } catch (java.lang.InterruptedException ie) { System.out.println(ie); }

            transition.setPath(polyline);
            transition.play();
            rect_anim.setVisible(true);

        }
        else if (!cb_anim.isSelected()) {// && transition.getStatus() == STOPPED) {

            //transition.stop();
            rect_anim.setVisible(false);

        }

        return series;
    }

    private void initInputControls() {
        xMax.setText("20.0");
        xMin.setText("-20.0");
        factorA.setText("0.25");
        factorB.setText("10.0");
        factorC.setText("1.0");
        factorD.setText("1.0");
    }

    private void initChartProperties() {
        chart.setAnimated(true);
        xAxis.setLabel("X");
        yAxis.setLabel("Y");
    }

}