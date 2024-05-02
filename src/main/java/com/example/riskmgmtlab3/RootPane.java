package com.example.riskmgmtlab3;

import com.dlsc.formsfx.view.util.ViewMixin;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RootPane extends VBox implements ViewMixin {
    public static final MathContext MATH_CONTEXT = new MathContext(8);
    public static final String DOUBLE_REGEX = "[0-9]{1,13}(\\.[0-9]*)?";

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    private static final double[][] Sw_Hw = new double[][] {
                    new double[] { 1,0,0,1,1,1,0 },
                    new double[] { 0,1,0,1,0,0,1 },
                    new double[] { 0,0,1,0,1,1,0 },
                    new double[] { 1,0,1,1,0,0,1 },
                    new double[] { 0,1,0,1,1,1,0 },
                    new double[] { 0,0,1,1,0,0,1 },
                    new double[] { 0,1,1,0,1,1,1 },
                    new double[] { 1,0,1,1,0,1,0 },
                    new double[] { 0,1,0,1,1,0,1 },
                    new double[] { 1,0,1,1,0,1,0 },
                    new double[] { 0,0,1,0,1,0,1 },
                    new double[] { 0,0,0,1,0,1,0 },
                    new double[] { 0,1,1,0,1,0,1 },
                    new double[] { 1,0,0,1,0,1,0 },
                    new double[] { 0,1,1,0,1,0,1 },
            };
    private static final double[][] Mw_Sw = new double[][] {
                    new double[] { 1,0,1 },
                    new double[] { 1,1,0 },
                    new double[] { 0,0,1 },
                    new double[] { 1,1,0 },
                    new double[] { 0,0,1 },
                    new double[] { 1,1,0 },
                    new double[] { 1,0,1 },
                    new double[] { 1,1,0 },
                    new double[] { 0,1,1 },
                    new double[] { 0,0,1 },
                    new double[] { 1,1,0 },
                    new double[] { 1,0,1 },
                    new double[] { 0,1,0 },
                    new double[] { 1,1,0 },
                    new double[] { 1,0,1 },
            };

    private static final List<ParameterDescription> HARDWARE_PARAMETERS = List.of(
            createParamDescription("Тактова частота процесора", "fTp", "ГГц", "1.4", "1.5", "1.7"),
            createParamDescription("Кількість ядер процесора", "NCp", "шт.", "2", "2", "2"),
            createParamDescription("Розрядність процесора", "Cp", "біт", "32", "32", "64"),
            createParamDescription("Тактова частота ОЗП", "fTRAM", "ГГц", "0.3", "0.4", "0.5"),
            createParamDescription("Об'єм ОЗП", "VRAM", "Гбайт", "4", "6", "8"),
            createParamDescription("Швидкість доступу до жорсткого диска", "VHDD", "мс", "6", "8", "8"),
            createParamDescription("Об'єм жорсткого диска", "SHDD", "Гбайт", "320", "340", "470"),
            createParamDescription("Кількість портів", "NPT", "шт.", "8", "12", "12"),
            createParamDescription("Кількість протоколів", "NPR", "шт.", "8", "10", "12"),
            createParamDescription("Швидкість передачі", "VN", "Мбіт/с", "450", "520", "600"),
            createParamDescription("Розрядність даних, що передаються", "CNET", "біт", "32", "32", "64"),
            createParamDescription("Роздільна здатність", "RP", "піксел", "1200", "1960", "1960"),
            createParamDescription("Швидкість друку (сканування)", "VPR", "стор./хв.", "10", "15", "20"),
            createParamDescription("Швидкість обміну з ПК", "RE", "Мбіт/с", "15", "20", "24"),
            createParamDescription("Об'єм ОЗП", "VPRAM", "Гбайт", "0.256", "0.256", "0.512")
    );
    private static final List<ParameterDescription> SOFTWARE_PARAMETERS = List.of(
            createParamDescription("Розрядність ОС", "COS", "біт", "32", "64", "64"),
            createParamDescription("Кількість ядер процесора, що підтримується ОС", "NCOS", "шт.", "16", "16", "32"),
            createParamDescription("Кількість задач, що розв'язуються одночасно", "NTOS", "шт.", "4096", "5256", "6000"),
            createParamDescription("Кількість користувачів, які можуть працювати одночасно", "NUOS", "осіб", "2", "3", "3"),
            createParamDescription("Тривалість виконання однієї операції", "TOS", "сек", "0.05", "0.07", "0.1"),
            createParamDescription("Розрядність СУБД", "CDB", "біт", "32", "32", "32"),
            createParamDescription("Наявний розмір бази даних", "VDB", "Тбайт", "0.5", "0.7", "0.7"),
            createParamDescription("Наявний розмір таблиці БД", "VDBT", "Гбайт", "13", "14", "17"),
            createParamDescription("Наявна кількість стовпців у записі", "VDCR", "шт.", "8", "10", "13"),
            createParamDescription("Кількість типів даних, що підтримується", "VDBDT", "шт.", "32", "41", "45"),
            createParamDescription("Середня тривалість виконання запиту", "TDB", "сек", "5", "7", "9"),
            createParamDescription("Розрядність редактора", "CE", "біт", "32", "32", "32"),
            createParamDescription("Кількість вбудованих функцій", "NEF", "шт.", "92", "103", "127"),
            createParamDescription("Кількість форматів документів, що підтримуються", "NED", "шт.", "5", "7", "10"),
            createParamDescription("Наявний об'єм документу", "VED", "Гбайт", "0.2", "0.2", "0.3"),
            createParamDescription("Розрядність генератора звітів", "CRG", "біт", "32", "32", "32"),
            createParamDescription("Наявний об'єм початкових даних", "VRGIN", "Гбайт", "0.5", "0.5", "0.7"),
            createParamDescription("Кількість кодувань, що підтримуються", "VPRAM", "шт.", "15", "17", "20"),
            createParamDescription("Кількість форматів звітів, що підтримуються", "VPRAM", "шт.", "15", "18", "25"),
            createParamDescription("Кількість графічних форматів, що підтримуються", "VPRAM", "шт.", "5", "7", "11"),
            createParamDescription("Кількість форматів баз даних, що підтримуються", "VPRAM", "шт.", "2", "3", "4"),
            createParamDescription("Тривалість генерування звіту", "VPRAM", "кБайт/сек", "100", "111", "142")
    );
    private static final List<ParameterDescription> MATHWARE_PARAMETERS = List.of(
            createParamDescription("Кількість методів розв'язання задачі", "NNM", "шт.", "5", "6", "7"),
            createParamDescription("Точність виконання розрахунків", "PNM", "%", "3", "4", "5"),
            createParamDescription("Тривалість розв'язання задачі", "TNM", "с", "25", "35", "40"),
            createParamDescription("Тривалість підготовки вхідних даних", "TPD", "хв", "7", "8", "10"),
            createParamDescription("Тривалість поточної інтерпретації даних", "TPID", "хв", "4", "5", "7"),
            createParamDescription("Тривалість аналізу результатів розрахунку", "TARR", "хв", "15", "18", "21")
    );
    public static final Font FONT = new Font(20);

    private ScrollPane scrollContent;

    private TableView<ParameterDescription> hardwareParameters;
    private TableView<ParameterDescription> softwareParameters;
    private TableView<ParameterDescription> mathWareParameters;

    public RootPane() {
        init();
    }

    private BigDecimal sum(BigDecimal[][] matrix, Function<BigDecimal, BigDecimal> remapFunction) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal[] row : matrix) {
            for (BigDecimal v : row) {
                sum = sum.add(remapFunction.apply(v));
            }
        }
        return sum;
    }

    private Stream<BigDecimal> proportion(BigDecimal[] arr, BigDecimal part) {
        return Stream.of(arr).map(v -> part.multiply(v).divide(BigDecimal.valueOf(arr.length), MATH_CONTEXT));
    }

    private BigDecimal[][] transpose(BigDecimal[][] M) {
        int rows = M.length;
        int cols = M[0].length;
        BigDecimal[][] transposed = new BigDecimal[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = M[i][j];
            }
        }
        return transposed;
    }

    private BigDecimal[][] multiply(BigDecimal[][] a, BigDecimal[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int rowsB = b.length;
        if (colsA != rowsB) {
            throw new IllegalArgumentException("wrong dim");
        }
        int colsB = b[0].length;
        BigDecimal[][] res = new BigDecimal[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int k = 0; k < colsA; k++) {
                    sum = sum.add(a[i][k].multiply(b[k][j], MATH_CONTEXT), MATH_CONTEXT);
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    private BigDecimal[][] divideWithCoefficients(BigDecimal[][] a, BigDecimal[][] b, double[][] coeffs) {
        int n = a.length;
        int m = a[0].length;
        BigDecimal[][] res = new BigDecimal[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res[i][j] = a[i][j].divide(b[i][j], MATH_CONTEXT).multiply(BigDecimal.valueOf(coeffs[i][j]), MATH_CONTEXT);
            }
        }
        return res;
    }

    private BigDecimal[][] vectorMultiplication(BigDecimal[] a, BigDecimal[] b) {
        int n = a.length;
        int m = b.length;
        BigDecimal[][] res = new BigDecimal[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res[i][j] = a[i].multiply(b[j], MATH_CONTEXT);
            }
        }
        return res;
    }

    private BigDecimal avg(BigDecimal[] arr) {
        BigDecimal sum = Arrays.stream(arr).reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(arr.length), MATH_CONTEXT);
    }

    @Override
    public void initializeParts() {
        scrollContent = new ScrollPane();
        hardwareParameters = initTable(createColumns(() -> hardwareParameters));
        softwareParameters = initTable(createColumns(() -> softwareParameters));
        mathWareParameters = initTable(createColumns(() -> mathWareParameters));
        fillTables();
    }

    @Override
    public void layoutParts() {
        var vBox = new VBox();
        var calcResults = new VBox();
        vBox.getChildren().add(namedTable(hardwareParameters, "Параметри технічного забезпечення"));
        vBox.getChildren().add(namedTable(softwareParameters, "Параметри програмного забезпечення"));
        vBox.getChildren().add(namedTable(mathWareParameters, "Параметри математичного забезпечення"));
        vBox.getChildren().add(calcResults);
        scrollContent.setContent(vBox);
        scrollContent.setFitToWidth(true);
        getChildren().add(scrollContent);
        performCalculations(calcResults);
        addRecalcListeners(SOFTWARE_PARAMETERS, calcResults);
        addRecalcListeners(HARDWARE_PARAMETERS, calcResults);
        addRecalcListeners(MATHWARE_PARAMETERS, calcResults);
    }

    private void addRecalcListeners(List<ParameterDescription> params, VBox box) {
        params.forEach(v -> v.nominal().addListener((x, s, i) -> performCalculations(box)));
        params.forEach(v -> v.max().addListener((x, s, i) -> performCalculations(box)));
    }

    private void performCalculations(VBox mainBox) {
        mainBox.getChildren().clear();
        var PpcArr = new BigDecimal[]{
                getHardwareParameter(0).nomMaxProportion().multiply(getHardwareParameter(1).nomMaxProportion()),
                getHardwareParameter(3).nomMaxProportion().multiply(getHardwareParameter(4).nomMaxProportion()),
                getHardwareParameter(5).nomMaxProportion().multiply(getHardwareParameter(6).nomMaxProportion()),
        };
        var PnetArr = new BigDecimal[]{
                getHardwareParameter(7).nomMaxProportion(),
                getHardwareParameter(12).nomMaxProportion(),
        };
        var PpArr = new BigDecimal[]{
                getHardwareParameter(12).nomMaxProportion(),
                getHardwareParameter(13).nomMaxProportion()
        };
        var PosArr = new BigDecimal[]{
                getSoftwareParameter(1).nomMaxProportion(),
                getSoftwareParameter(2).nomMaxProportion(),
                getSoftwareParameter(3).nomMaxProportion()
        };
        var PdbArr = new BigDecimal[]{
                getSoftwareParameter(7).nomMaxProportion(),
                getSoftwareParameter(8).nomMaxProportion(),
                getSoftwareParameter(9).nomMaxProportion(),
                getSoftwareParameter(6).nomMaxProportion()
        };
        var PeArr = new BigDecimal[]{
                getSoftwareParameter(12).nomMaxProportion(),
                getSoftwareParameter(13).nomMaxProportion(),
                getSoftwareParameter(14).nomMaxProportion(),
        };
        var PrgArr = new BigDecimal[]{
                getSoftwareParameter(17).nomMaxProportion(),
                getSoftwareParameter(18).nomMaxProportion(),
                getSoftwareParameter(19).nomMaxProportion(),
                getSoftwareParameter(20).nomMaxProportion(),
                getSoftwareParameter(16).nomMaxProportion()
        };
        var PmArr = new BigDecimal[]{
                getMathwareParameter(3).nomMaxProportion(),
                getMathwareParameter(4).nomMaxProportion(),
                getMathwareParameter(5).nomMaxProportion()
        };
        var m = PosArr.length + PdbArr.length + PeArr.length + PrgArr.length;
        var PpcPart = getHardwareParameter(2).nomMaxProportion();
        var Ppc = avg(PpcArr).multiply(PpcPart, MATH_CONTEXT);
        var PnetPart = getHardwareParameter(3).nomMaxProportion();
        var Pnet = avg(PnetArr).multiply(PnetPart, MATH_CONTEXT);
        var PpPart = getHardwareParameter(11).nomMaxProportion().multiply(getHardwareParameter(14).nomMaxProportion());
        var Pp = avg(PpArr).multiply(PpPart, MATH_CONTEXT);
        var PosPart = getSoftwareParameter(0).nomMaxProportion();
        var Pos = avg(PosArr).multiply(PosPart, MATH_CONTEXT);
        var PdbPart = (getSoftwareParameter(5).nomMaxProportion()).multiply(getSoftwareParameter(10).nomMaxProportion());
        var Pdb = avg(PdbArr).multiply(PdbPart, MATH_CONTEXT);
        var PePart = getSoftwareParameter(4).nomMaxProportion().multiply(getSoftwareParameter(11).nomMaxProportion());
        var Pe = avg(PeArr).multiply(PePart, MATH_CONTEXT);
        var PrgPart = getSoftwareParameter(15).nomMaxProportion().multiply(getSoftwareParameter(21).nomMaxProportion());
        var Prg = avg(PrgArr).multiply(PrgPart, MATH_CONTEXT);
        var PmPart = getMathwareParameter(0).nomMaxProportion()
                .multiply(getMathwareParameter(1).nomMaxProportion())
                .multiply(getMathwareParameter(2).nomMaxProportion());
        var Pm = avg(PmArr).multiply(PmPart, MATH_CONTEXT);
        var Parr = new BigDecimal[] { Ppc, Pnet, Pp, Pos, Pdb, Pe, Prg, Pm };
        var P = Arrays.stream(Parr).map(v -> v.multiply(v)).reduce(BigDecimal.ZERO, BigDecimal::add).sqrt(MATH_CONTEXT);
        var Pts = Stream.concat(
                Stream.concat(proportion(PpcArr, PpcPart), proportion(PnetArr, PnetPart)),
                proportion(PpArr, PpPart)).toArray(BigDecimal[]::new);
        var Pss = Stream.concat(
                Stream.concat(proportion(PosArr, PosPart), proportion(PdbArr, PdbPart)),
                Stream.concat(proportion(PeArr, PePart), proportion(PrgArr, PrgPart))).toArray(BigDecimal[]::new);
        var Pms = proportion(PmArr, PmPart).toArray(BigDecimal[]::new);
        var RowArrMax = new BigDecimal[] {
                        getSoftwareParameter(1).maxValue(),
                        getSoftwareParameter(2).maxValue(),
                        getSoftwareParameter(3).maxValue(),
                        getSoftwareParameter(7).maxValue(),
                        getSoftwareParameter(8).maxValue(),
                        getSoftwareParameter(9).maxValue(),
                        getSoftwareParameter(10).maxValue(),
                        getSoftwareParameter(12).maxValue(),
                        getSoftwareParameter(13).maxValue(),
                        getSoftwareParameter(14).maxValue(),
                        getSoftwareParameter(17).maxValue(),
                        getSoftwareParameter(18).maxValue(),
                        getSoftwareParameter(19).maxValue(),
                        getSoftwareParameter(20).maxValue(),
                        getSoftwareParameter(16).maxValue(),
                };
        var RowArrNom = new BigDecimal[] {
                        getSoftwareParameter(1).nominalValue(),
                        getSoftwareParameter(2).nominalValue(),
                        getSoftwareParameter(3).nominalValue(),
                        getSoftwareParameter(7).nominalValue(),
                        getSoftwareParameter(8).nominalValue(),
                        getSoftwareParameter(9).nominalValue(),
                        getSoftwareParameter(10).nominalValue(),
                        getSoftwareParameter(12).nominalValue(),
                        getSoftwareParameter(13).nominalValue(),
                        getSoftwareParameter(14).nominalValue(),
                        getSoftwareParameter(17).nominalValue(),
                        getSoftwareParameter(18).nominalValue(),
                        getSoftwareParameter(19).nominalValue(),
                        getSoftwareParameter(20).nominalValue(),
                        getSoftwareParameter(16).nominalValue(),
                };
        var ZeqColArr = new BigDecimal[] {
                        getHardwareParameter(0).maxValue(),
                        getHardwareParameter(3).maxValue(),
                        getHardwareParameter(6).maxValue(),
                        getHardwareParameter(7).maxValue(),
                        getHardwareParameter(8).maxValue(),
                        getHardwareParameter(12).maxValue(),
                        getHardwareParameter(13).maxValue(),
                };
        var HeqColArr = new BigDecimal[] {
                        getMathwareParameter(3).maxValue(),
                        getMathwareParameter(4).maxValue(),
                        getMathwareParameter(5).maxValue(),
                };
        var ZusColArr = new BigDecimal[] {
                        getHardwareParameter(0).nominalValue(),
                        getHardwareParameter(3).nominalValue(),
                        getHardwareParameter(6).nominalValue(),
                        getHardwareParameter(7).nominalValue(),
                        getHardwareParameter(8).nominalValue(),
                        getHardwareParameter(12).nominalValue(),
                        getHardwareParameter(13).nominalValue(),
                };
        var HusColArr = new BigDecimal[] {
                        getMathwareParameter(3).nominalValue(),
                        getMathwareParameter(4).nominalValue(),
                        getMathwareParameter(5).nominalValue(),
                };

        var Zeq = vectorMultiplication(RowArrMax, ZeqColArr);
        var Heq = vectorMultiplication(RowArrMax, HeqColArr);
        var Zus = vectorMultiplication(RowArrNom, ZusColArr);
        var Hus = vectorMultiplication(RowArrNom, HusColArr);
        var Rts_ss = divideWithCoefficients(Zeq, Zus, Sw_Hw);
        var Rms_ss = divideWithCoefficients(Heq, Hus, Mw_Sw);
        var Rts_ss_Pts = transpose(multiply(Rts_ss, transpose(new BigDecimal[][]{Pts})));
        var Rms_ss_Pms = transpose(multiply(Rms_ss, transpose(new BigDecimal[][]{Pms})));
        var Rts_ss_sum_sq = sum(Rts_ss, v -> v.multiply(v, MATH_CONTEXT)).sqrt(MATH_CONTEXT);
        var Rms_ss_sum_sq = sum(Rms_ss, v -> v.multiply(v, MATH_CONTEXT)).sqrt(MATH_CONTEXT);
        var Rts_Pts_Rms_Pms = new BigDecimal[m];
        for (var i = 0; i < m; i++) {
            Rts_Pts_Rms_Pms[i] = Rts_ss_Pts[0][i].multiply(Rms_ss_Pms[0][i], MATH_CONTEXT);
        }
        var Q = transpose(multiply(new BigDecimal[][]{Rts_Pts_Rms_Pms}, transpose(new BigDecimal[][]{Pss})))[0][0];
        showCalculationResult("Ppc", Ppc, mainBox);
        showCalculationResult("Pnet", Pnet, mainBox);
        showCalculationResult("Pos", Pos, mainBox);
        showCalculationResult("Pdb", Pdb, mainBox);
        showCalculationResult("Pe", Pe, mainBox);
        showCalculationResult("Prg", Prg, mainBox);

        showMatrixMutliplication(
                Rts_ss,
                transpose(new BigDecimal[][]{Pts}),
                transpose(Rts_ss_Pts),
                mainBox,
                "R_tt_ss",
                "P_ts",
                "R_tt_ss * P_ts", " X "
        );
        showMatrixMutliplication(
                Rms_ss,
                transpose(new BigDecimal[][]{Pms}),
                transpose(Rms_ss_Pms),
                mainBox,
                "R_ms_ss",
                "P_ms",
                "R_ms_ss * P_ms", " X "
        );
        showMatrixMutliplication(
                transpose(Rts_ss_Pts),
                transpose(Rms_ss_Pms),
                transpose(new BigDecimal[][] {Rts_Pts_Rms_Pms}),
                mainBox,
                "R_tt_ss * P_ts",
                "R_ms_ss * P_ms",
                "(R_tt_ss * P_ts) * (R_ms_ss * P_ms)", " * "
        );
        showMatrixMutliplication(
                new BigDecimal[][] {Rts_Pts_Rms_Pms},
                transpose(new BigDecimal[][] {Pss}),
                new BigDecimal[][] {{Q}},
                mainBox,
                "(R_tt_ss * P_ts) * Rms_ss_Pms",
                "Pss",
                "Q",
                " X "
        );
        showCalculationResult("Rts_ss_sum_sq", Rts_ss_sum_sq, mainBox);
        showCalculationResult("Rms_ss_sum_sq", Rms_ss_sum_sq, mainBox);
        showCalculationResult("Q", Q, mainBox);
        showCalculationResult("P", P, mainBox);
    }

    private ParameterDescription getSoftwareParameter(int index) {
        return softwareParameters.getItems().get(index);
    }

    private ParameterDescription getMathwareParameter(int index) {
        return mathWareParameters.getItems().get(index);
    }

    private ParameterDescription getHardwareParameter(int index) {
        return hardwareParameters.getItems().get(index);
    }

    private void showCalculationResult(String variableName, BigDecimal variableValue, VBox mainBox) {
        mainBox.getChildren().add(centered(variableName + " = " + variableValue));
    }

    private void showMatrixMutliplication(BigDecimal[][] A, BigDecimal[][] B, BigDecimal[][] C, VBox mainBox, String aName, String bName, String cName, String operation) {
        var grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.add(centered(asNode(A, aName)), 0, 0);
        grid.add(centered(operation), 1, 0);
        grid.add(centered(asNode(B, bName)), 2, 0);
        grid.add(centered(" = "), 3, 0);
        grid.add(centered(asNode(C, cName)), 4, 0);
        mainBox.getChildren().add(grid);
    }

    private Node centered(String text) {
        Label node = new Label(text);
        node.setFont(FONT);
        return centered(node);
    }

    private Node centered(Node node) {
        BorderPane root = new BorderPane();
        BorderPane.setAlignment(node, Pos.CENTER);
        root.setCenter(node);
        return root;
    }

    private Node asNode(BigDecimal[][] M, String matrixName) {
        var container = new GridPane();
        var gridPane = new GridPane();
        gridPane.setPadding(new Insets(10D, 10D, 10D, 10D));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), new CornerRadii(2.5), new Insets(-1.0))));
        int rows = M.length;
        int cols = M[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Label label = new Label(" " + NUMBER_FORMAT.format(M[i][j]) + " ");
                label.setFont(Font.font(FONT.getFamily(), FontPosture.REGULAR, 15));
                gridPane.add(label, j, i);
            }
        }
        Label matrixNameLabel = new Label(matrixName);
        matrixNameLabel.setFont(Font.font(FONT.getFamily(), FontPosture.ITALIC, 20));
        container.add(matrixNameLabel, 0, 0);
        container.add(gridPane, 0, 1);
        return container;
    }

    private Node namedTable(TableView<?> tableView, String tableName) {
        var vBox = new VBox();
        var tableNameLabel = new Label(tableName);
        tableNameLabel.setPadding(new Insets(10));
        tableNameLabel.setFont(Font.font("Arial", 20));
        tableNameLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(tableNameLabel, 0.0);
        AnchorPane.setRightAnchor(tableNameLabel, 0.0);
        tableNameLabel.setAlignment(Pos.CENTER);
        vBox.getChildren().add(tableNameLabel);
        vBox.getChildren().add(tableView);
        return vBox;
    }

    @Override
    public void setupEventHandlers() {
        ViewMixin.super.setupEventHandlers();
    }

    private List<TableColumn<ParameterDescription, ?>> createColumns(
            Supplier<TableView<ParameterDescription>> tableView
    ) {
        return List.of(
                createColumn("Ім`я параметру", ParameterDescription::parameterName),
                createColumn("Позначення", ParameterDescription::notation),
                createColumn("Одиниця виміру", ParameterDescription::unitOfMeasurement),
                createColumn("Мінімальне значення", ParameterDescription::min),
                createEditableDoubleColumn(
                        "Номінальне значення",
                        ParameterDescription::nominal,
                        tableView,
                        new StringConverter<>() {
                            @Override
                            public String toString(String object) {
                                return object;
                            }

                            @Override
                            public String fromString(String string) {
                                if (string != null && string.matches(DOUBLE_REGEX)) {
                                    return string;
                                }
                                return null;
                            }
                        }
                ),
                createEditableDoubleColumn(
                        "Максимальне значення",
                        ParameterDescription::max,
                        tableView,
                        new StringConverter<>() {
                            @Override
                            public String toString(String object) {
                                return object == null ? null : object;
                            }

                            @Override
                            public String fromString(String string) {
                                if (string != null && string.matches(DOUBLE_REGEX)) {
                                    return string;
                                }
                                return null;
                            }
                        }
                )
        );
    }

    private void fillTables() {
        hardwareParameters.getItems().addAll(HARDWARE_PARAMETERS);
        softwareParameters.getItems().addAll(SOFTWARE_PARAMETERS);
        mathWareParameters.getItems().addAll(MATHWARE_PARAMETERS);
    }

    private static ParameterDescription createParamDescription(
            String name,
            String notation,
            String unitOfMeasurement,
            String min,
            String nominalValue,
            String max
    ) {
        return new ParameterDescription(
                new SimpleStringProperty(name),
                new SimpleStringProperty(notation),
                new SimpleStringProperty(unitOfMeasurement),
                new SimpleStringProperty(min),
                new SimpleStringProperty(nominalValue),
                new SimpleStringProperty(max)
        );
    }

    private <T> TableView<T> initTable(List<TableColumn<T, ?>> columns) {
        TableView<T> tableView = new TableView<>();
        tableView.setVisible(true);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableView.getColumns().addAll(columns);
        return tableView;
    }

    private <S, T> TableColumn<S, T> createColumn(String headerName, Function<S, ObservableValue<T>> function) {
        TableColumn<S, T> tableColumn = new TableColumn<>(headerName);
        tableColumn.setCellValueFactory(param -> function.apply(param.getValue()));
        return tableColumn;
    }

    private <S, T, K extends WritableValue<T> & Property<T>> TableColumn<S, T> createEditableDoubleColumn(
            String headerName,
            Function<S, K> function,
            Supplier<TableView<S>> tableView,
            StringConverter<T> stringConverter
    ) {
        TableColumn<S, T> tableColumn = new TableColumn<>(headerName);
        tableColumn.setEditable(true);
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringConverter));
        tableColumn.setCellValueFactory(param -> function.apply(param.getValue()));
        tableColumn.setOnEditCommit(event -> {
            T newValue = event.getNewValue();
            if (newValue == null) {
                return;
            }
            S objToUpdate = tableView.get().getItems().get(event.getTablePosition().getRow());
            function.apply(objToUpdate).setValue(newValue);
        });
        return tableColumn;
    }

}
