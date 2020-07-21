package sample;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.text.DecimalFormat;

public class ShowDotCoordinates extends StackPane {

    public ShowDotCoordinates(double x, double y) {

        final Label label = createDataThresholdLabel(x, y); //вызов функции формирования надписи

        //обработчик события наведения курсора мыши на точку
        setOnMouseEntered(mouseEvent -> {
            getChildren().setAll(label); //установка надписи с координатами точки
            setCursor(Cursor.NONE); //отключение курсора
            toFront(); //вывод надписи и точки на передний план
        });
        //обработчик события отведения курсора с точки
        setOnMouseExited(mouseEvent -> {
            getChildren().clear(); //удаление надписи
            setCursor(Cursor.CROSSHAIR); //установка стандартного курсора
        });
    }

    private Label createDataThresholdLabel(double x, double y) {

        //создание экземпляра класса для округления координат точки
        DecimalFormat df = new DecimalFormat("0.##");

        //создание надписи с текстом, включающим в себя координаты точки
        final Label label = new Label("(" + df.format(x) + "; " + df.format(y) + ")");

        //настройка внешнего вида надписи
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

        //возвращаемое значение - надпись
        return label;

    }
}