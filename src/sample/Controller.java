package sample;

import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.Precision;

public class Controller {

    @FXML TextField factorA; @FXML TextField factorL; @FXML TextField factorN; @FXML TextField factorLbd; @FXML TextField xMin; @FXML TextField xMax;
    @FXML LineChart<Number, Number> chart; @FXML NumberAxis xAxis; @FXML NumberAxis yAxis;
    @FXML Slider limitSlider; @FXML Slider stepSlider; @FXML Slider LSlider; @FXML Slider NSlider; @FXML Slider LbdSlider;
    @FXML Label lbl_step; @FXML Label lbl_L; @FXML Label lbl_N; @FXML Label lbl_Lbd; @FXML Label lbl_Min; @FXML Label lbl_Max; @FXML Label monitor;
    @FXML CheckBox cb_symb; @FXML CheckBox cb_pol; @FXML CheckBox cb_anim; @FXML CheckBox cb_line_anim;
    @FXML Rectangle rect_anim; @FXML Polyline polyline; PathTransition transition;
    @FXML Button button; @FXML Button reset_btn;

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

        initChartProperties(); //вызов функции инициализации осей координат графика
        initInputControls(); //вызов функции инициализации полей ввода
        XYChart.Series<Number, Number> series = getSeries(); //вызов функции расчета точек графика
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE); //отключение сортировки точек
                                        // (обеспечивает соединение точек по порядку их расчета)

        polyline.getPoints().clear();
        rect_anim.setVisible(false);
        monitor.toFront();

        // Alternate : 5B5B5B - Elephant 16 //
        lbl_step.setText("Step:"); lbl_step.setTextFill(Color.web("#757575")); lbl_step.setFont(new Font("Arial Bold", 16));
        lbl_L.setText("L:"); lbl_L.setTextFill(Color.web("#757575")); lbl_L.setFont(new Font("Arial Bold", 16));
        lbl_N.setText("N:"); lbl_N.setTextFill(Color.web("#757575")); lbl_N.setFont(new Font("Arial Bold", 16));
        lbl_Lbd.setText("λ:"); lbl_Lbd.setTextFill(Color.web("#757575")); lbl_Lbd.setFont(new Font("Arial Bold", 16));
        lbl_Min.setText("Mn:"); lbl_Min.setTextFill(Color.web("#757575")); lbl_Min.setFont(new Font("Arial Bold", 16));
        lbl_Max.setText("Mx:"); lbl_Max.setTextFill(Color.web("#757575")); lbl_Max.setFont(new Font("Arial Bold", 16));

        monitor.setTextFill(Color.web("#757575")); monitor.setFont(new Font("Arial", 16));

//        // Cursor-to-chart coordinates monitor // doesn't work properly
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

        factorA.textProperty().addListener((observable, oldValue, newValue) -> { cb_anim.setSelected(false); cb_anim.setDisable(true); });
        factorL.textProperty().addListener((observable, oldValue, newValue) -> { cb_anim.setSelected(false); cb_anim.setDisable(true); });
        factorN.textProperty().addListener((observable, oldValue, newValue) -> { cb_anim.setSelected(false); cb_anim.setDisable(true); });
        factorLbd.textProperty().addListener((observable, oldValue, newValue) -> { cb_anim.setSelected(false); cb_anim.setDisable(true); });
        xMin.textProperty().addListener((observable, oldValue, newValue) -> { cb_anim.setSelected(false); cb_anim.setDisable(true); });
        xMax.textProperty().addListener((observable, oldValue, newValue) -> { cb_anim.setSelected(false); cb_anim.setDisable(true); });
        cb_symb.selectedProperty().addListener((observable, oldValue, newValue) -> { cb_anim.setSelected(false); cb_anim.setDisable(true); });
        cb_pol.selectedProperty().addListener((observable, oldValue, newValue) -> {

            cb_anim.setSelected(false); cb_anim.setDisable(true);

            if (!cb_pol.isSelected()) {
                stepSlider.setMax(0.01); stepSlider.setMin(0.0002); stepSlider.setMajorTickUnit(100.0); stepSlider.setBlockIncrement(0.0001);
                limitSlider.setMax(6.9); limitSlider.setMin(6.5); limitSlider.setMajorTickUnit(0.2); limitSlider.setBlockIncrement(0.1);
                factorA.setText("2.0E-4"); xMax.setText("6.77"); xMin.setText("6.7");
                stepSlider.setValue(Double.parseDouble(factorA.getText())); limitSlider.setValue(Double.parseDouble(xMax.getText()));
            } else {
                stepSlider.setMax(1.0); stepSlider.setMin(0.01); stepSlider.setMajorTickUnit(0.49); stepSlider.setBlockIncrement(0.0001);
                limitSlider.setMax(25.0); limitSlider.setMin(1.0); limitSlider.setMajorTickUnit(11.5); limitSlider.setBlockIncrement(1.0);
                factorA.setText("0.1"); xMax.setText("17.0"); xMin.setText("-17.0");
                stepSlider.setValue(Double.parseDouble(factorA.getText())); limitSlider.setValue(Double.parseDouble(xMax.getText()));
            }

        });

        limitSlider.setOrientation(Orientation.VERTICAL); limitSlider.setShowTickLabels(true); limitSlider.setShowTickMarks(true); limitSlider.setValue(Double.parseDouble(xMax.getText()));
        limitSlider.setMax(6.9); limitSlider.setMin(6.5); limitSlider.setMajorTickUnit(0.2); limitSlider.setBlockIncrement(0.1);
        limitSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){

                if (!cb_pol.isSelected()) {
                    xMax.setText(String.valueOf(Precision.round((double) newValue, 3)));
                    //xMin.setText(String.valueOf(0));
                } else {
                    xMax.setText(String.valueOf(Precision.round((double) newValue, 1)));
                    xMin.setText(String.valueOf(Precision.round(-(double) newValue, 1)));
                }

            }
        });

        stepSlider.setShowTickLabels(true); stepSlider.setShowTickMarks(true); stepSlider.setValue(Double.parseDouble(factorA.getText()));
        stepSlider.setMax(0.01); stepSlider.setMin(0.0002); stepSlider.setMajorTickUnit(100.0); stepSlider.setBlockIncrement(0.0001);
        stepSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorA.setText(String.valueOf(Precision.round((double)newValue, 5)));
            }
        });

        LSlider.setShowTickLabels(true); LSlider.setShowTickMarks(true); LSlider.setValue(Double.parseDouble(factorL.getText()));
        LSlider.setMax(10); LSlider.setMin(1.0); LSlider.setMajorTickUnit(4); LSlider.setBlockIncrement(1.0);
        LSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorL.setText(String.valueOf(Precision.round((double)newValue, 0)));
            }
        });

        NSlider.setShowTickLabels(true); NSlider.setShowTickMarks(true); NSlider.setValue(Double.parseDouble(factorN.getText()));
        NSlider.setMax(10); NSlider.setMin(1.0); NSlider.setMajorTickUnit(4); NSlider.setBlockIncrement(1.0);
        NSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorN.setText(String.valueOf(Precision.round((double)newValue, 0)));
            }
        });

        LbdSlider.setShowTickLabels(true); LbdSlider.setShowTickMarks(true); LbdSlider.setMajorTickUnit(4); LbdSlider.setBlockIncrement(0.5); LbdSlider.setMax(10); LbdSlider.setValue(Double.parseDouble(factorLbd.getText())); LbdSlider.setMin(1.0);
        LbdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                factorLbd.setText(String.valueOf(Precision.round((double)newValue, 2)));
            }
        });

        cb_symb.setSelected(false); cb_symb.setText("Draw symbols");
        cb_pol.setSelected(false); cb_pol.setText("Draw transition");
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

        return c * (a -
                Math.pow(a, 5) / (ArithmeticUtils.factorialDouble(2) * 5 * Math.pow(Math.sqrt(2) * b, 4)) +
                Math.pow(a, 9) / (ArithmeticUtils.factorialDouble(4) * 9 * Math.pow(Math.sqrt(2) * b, 8)) -
                Math.pow(a, 13) / (ArithmeticUtils.factorialDouble(6) * 13 * Math.pow(Math.sqrt(2) * b, 12)) +
                Math.pow(a, 17) / (ArithmeticUtils.factorialDouble(8) * 17 * Math.pow(Math.sqrt(2) * b, 16)) -
                Math.pow(a, 21) / (ArithmeticUtils.factorialDouble(10) * 21 * Math.pow(Math.sqrt(2) * b, 20))
        ); // формула для расчета координаты x (переходная кривая)

    }

    private double iterate_y(double a, double b, double d) {

        return d * (Math.pow(a, 3) / (1 * 3 * Math.pow(Math.sqrt(2) * b, 2)) -
                Math.pow(a, 7) / (ArithmeticUtils.factorialDouble(3) * 7 * Math.pow(Math.sqrt(2) * b, 6)) +
                Math.pow(a, 11) / (ArithmeticUtils.factorialDouble(5) * 11 * Math.pow(Math.sqrt(2) * b, 10)) -
                Math.pow(a, 15) / (ArithmeticUtils.factorialDouble(7) * 15 * Math.pow(Math.sqrt(2) * b, 14)) +
                Math.pow(a, 19) / (ArithmeticUtils.factorialDouble(9) * 19 * Math.pow(Math.sqrt(2) * b, 18)) -
                Math.pow(a, 23) / (ArithmeticUtils.factorialDouble(11) * 23 * Math.pow(Math.sqrt(2) * b, 22))
        ); // формула для расчета координаты y (переходная кривая)

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
        series.setName("Chart");

        double x_temp = 0, y_temp = 0; int i = 0;
        double pol_rds = 0, pol_angle = 10;

        for (double a = xMin1; a < xMax1; a += step) {

            i++;
            if (cb_pol.isSelected()) {
                //расчет координат точки для переходной линии
                x_temp = iterate_x(a, L, N); y_temp = iterate_y(a, L, Lbd);
            }
            else {
                //расчет координат точки по исходной формуле
                function_body = Lbd * (Math.pow(Math.pow(2, a), 2) / 2 * Math.pow(2, 2));
                x_temp += L / N * Math.cos(function_body);
                y_temp += L / N * Math.sin(function_body);
            }
            //добавление точек в объект Polyline - путь для анимации движения примитива
            polyline.getPoints().
                    addAll(xAxis.getDisplayPosition(x_temp) + 70.0,
                                    yAxis.getDisplayPosition(y_temp) + 20.0);

            if (cb_symb.isSelected()) {
                //создание альтернативного графика (необходимо для реализации функции ShowDotCoordinates)
                XYChart.Data chartData;
                chartData = new XYChart.Data(x_temp, y_temp);
                chartData.setNode(new ShowDotCoordinates(x_temp, y_temp));
                series.getData().add(chartData);
            } else {
                //добавление последовательности точек в исходный график
                series.getData().add(new XYChart.Data(x_temp, y_temp));
            }

        } monitor.setText("Dots: " + i); //присвоение

        rect_anim.toFront(); //установка примитива на передний план

        if (cb_anim.isSelected()) {

            transition.setPath(polyline); //установка пути анимации
            button.setDisable(true); //отключение кнопки Proceed
            rect_anim.setVisible(true); //включение видимости примитива
            transition.play(); //запуск анимации

            //включение кнопки Proceed и отключение видимости примитива по завершении анимации
            transition.setOnFinished(e -> { button.setDisable(false); rect_anim.setVisible(false); } );

        } else rect_anim.setVisible(false); //отключение видимости примитива (скрыт, если анимация не включена)

        return series; //возвращаемое значение - последовательность точек
    }

    private void initInputControls() {
        //ввод значений по умолчанию в поля ввода
        xMax.setText("6.77");
        xMin.setText("6.7");
        factorA.setText("2.0E-4");
        factorL.setText("5.0");
        factorN.setText("5.0");
        factorLbd.setText("1.0");

        //отключение функции рисования точек по умолчанию
        chart.setCreateSymbols(false);
    }

    private void initChartProperties() {
        //присвоение имен координатным осям графика
        xAxis.setLabel("X");
        yAxis.setLabel("Y");
    }

    public void resetParameters() {

        if (!cb_pol.isSelected()) {

            xMax.setText("6.77");
            xMin.setText("6.7");
            factorA.setText("2.0E-4");
            factorL.setText("5.0");
            factorN.setText("5.0");
            factorLbd.setText("1.0");

            limitSlider.setValue(Double.parseDouble(xMax.getText()));
            stepSlider.setValue(Double.parseDouble(factorA.getText()));
            LSlider.setValue(Double.parseDouble(factorL.getText()));
            NSlider.setValue(Double.parseDouble(factorN.getText()));
            LbdSlider.setValue(Double.parseDouble(factorLbd.getText()));

        } else {

            xMax.setText("17.0");
            xMin.setText("-17.0");
            factorA.setText("0.1");
            factorL.setText("5.0");
            factorN.setText("5.0");
            factorLbd.setText("1.0");

            limitSlider.setValue(Double.parseDouble(xMax.getText()));
            stepSlider.setValue(Double.parseDouble(factorA.getText()));
            LSlider.setValue(Double.parseDouble(factorL.getText()));
            NSlider.setValue(Double.parseDouble(factorN.getText()));
            LbdSlider.setValue(Double.parseDouble(factorLbd.getText()));

        }

    }
}