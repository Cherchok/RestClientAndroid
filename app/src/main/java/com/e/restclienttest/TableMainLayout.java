package com.e.restclienttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@SuppressLint("ViewConstructor")
public class TableMainLayout extends RelativeLayout {
    // список заголовков таблицы
    String[] headers;
    // список данных для заполнения таблицы
    LinkedHashMap<String, LinkedList<String>> datamap;
    // статическая таблица для заголовка A
    TableLayout tableA;
    // динамическая таблица для заголовков B
    TableLayout tableB;
    // статическая таблица для значений C
    TableLayout tableC;
    // динамическая таблица для значений D
    TableLayout tableD;
    // горизонтальная прокрутка для таблицы В
    HorizontalScrollView horizontalScrollViewB;
    // горизонтальная прокрутка для таблицы D
    HorizontalScrollView horizontalScrollViewD;
    // вертикальная прокрутка для таблицы С
    ScrollView scrollViewC;
    // вертикальная прокрутка для таблицы D
    ScrollView scrollViewD;
    // контекст
    Context context;
    // ширина заголовков
    int[] headerCellsWidth;

    // конструктор передачи SAP данных
    public TableMainLayout(Context context, LinkedHashMap<String, LinkedList<String>> dataMap) {
        super(context);

        this.context = context;
        setTableData(dataMap);
        setHeaders();

        // инициализация основных компонентов (TableLayouts, HorizontalScrollView, ScrollView)
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        this.setBackgroundColor(Color.RED);


        // add some table rows
        this.addTableRowToTableA();
        this.addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();

        this.generateTableC_AndTable_D();

        this.resizeBodyTableRowHeight();
    }

    // заполнение списка значениями
    private void setTableData(LinkedHashMap<String, LinkedList<String>> dataMap) {
        LinkedHashMap<String, LinkedList<String>> tableMap = new LinkedHashMap<>();
        for (String name : dataMap.keySet()) {
            boolean flag = true;
            if (name.equals("columnLeng")) {
                flag = false;
            }
            if (name.equals("fieldName")) {
                flag = false;
            }
            if (name.equals("dataType")) {
                flag = false;
            }
            if (name.equals("repText")) {
                flag = false;
            }
            if (name.equals("domName")) {
                flag = false;
            }
            if (name.equals("outputLen")) {
                flag = false;
            }
            if (name.equals("decimals")) {
                flag = false;
            }
            if (name.equals("clientNumber")) {
                flag = false;
            }
            if (flag) {
                tableMap.put(name, dataMap.get(name));
            }
        }
        this.datamap = tableMap;
    }

    // заполнение списка заголовками
    private void setHeaders() {
        this.headers = new String[datamap.keySet().size()];
        int i = 0;
        for (String name : datamap.keySet()) {
            headers[i] = name;
            i++;
        }
        this.headerCellsWidth = new int[headers.length];
    }

    // инициализация компонентов
    private void initComponents() {

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);

        this.tableA.setBackgroundColor(Color.GREEN);
        this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);

    }

    // установка основных идентификаторов компонентов
    @SuppressLint("ResourceType")
    private void setComponentsId() {
        this.tableA.setId(1);
        this.horizontalScrollViewB.setId(2);
        this.scrollViewC.setId(3);
        this.scrollViewD.setId(4);
    }

    // установка названий тэгов для прокруток
    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // добавление компонентов в TableMainLayout
    private void addComponentToMainLayout() {

        // RelativeLayout params очень полезны здесь
        // метод addRule является ключом для правильного расположения компонентов
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' - это относительный макет,
        // мы расширяем этот макет таблицы как относительный макет, как видно при создании этого класса
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);
    }

    // добавление строк в таблицу А
    private void addTableRowToTableA() {
        this.tableA.addView(this.componentATableRow());
    }

    // добавление строк в таблицу В
    private void addTableRowToTableB() {
        this.tableB.addView(this.componentBTableRow());
    }

    // заполнение ячеек таблицы A
    TableRow componentATableRow() {
        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView(this.headers[0]);
        textView.setTextColor(Color.BLACK);
        componentATableRow.setBackgroundColor(Color.BLACK);
        componentATableRow.addView(textView);
        return componentATableRow;
    }

    // заполнение ячеек таблицы B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount - 1); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.BLACK);
            componentBTableRow.setBackgroundColor(Color.BLACK);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    // заполнение  ячеек таблиц С и D
    private void generateTableC_AndTable_D() {
        // заполнение таблицы С
        for (String name : datamap.keySet()) {
            if (headers[0].equals(name)) {
                for (int columnNum2 = 0; columnNum2 < datamap.get(name).size(); columnNum2++) {
                    TableRow tableRowForTableC = this.tableRowForTableC(datamap.get(name).get(columnNum2));
                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    this.tableC.addView(tableRowForTableC);
                }
            }
        }

        // заполнение таблицы D
        for (int columnNum = 0; columnNum < datamap.get(headers[0]).size(); columnNum++) {
            TableRow tableRowForTableD = this.tableRowForTableD(columnNum);
            tableRowForTableD.setBackgroundColor(Color.LTGRAY);
            this.tableD.addView(tableRowForTableD);
        }
    }

    // генерирование ячеек для C
    TableRow tableRowForTableC(String column) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.bodyTextView(column);
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;
    }

    //генерирование ячеек для D
    TableRow tableRowForTableD(int columnNum) {
        TableRow taleRowForTableD = new TableRow(this.context);

        int size = datamap.keySet().size();
        String[] info = new String[(size - 1)];
        int col = 0;
        for (String name : datamap.keySet()) {
            if (!name.equals(headers[0])) {
                info[col] = datamap.get(name).get(columnNum).trim();
                col++;
            }
        }

        for (int i = 0; i < col; i++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[i + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(info[i]);
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;
    }

    // стандартный текстовый вид ячейки таблицы
    TextView bodyTextView(String label) {
        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // стандартный текстовый вид заголовков таблицы
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.LTGRAY);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // изменение размера высоты TableRow
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    // ширина заголовков
    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();

        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableA.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // изменить размер высоты строки таблицы
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableC.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // сопоставление всех высот в строке таблицы
    // чтобы сделать стандартную высоту TableRow
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // проверка, имеет ли вид наибольшую высоту в TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // считывание высот вьюх
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // считывание ширины вьюх
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // горизонтальный вид прокрутки пользовательский класс
    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // верикальный вид прокрутки пользовательский класс
    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }
}
