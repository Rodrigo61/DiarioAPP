package categoryPieChart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rodrigo.myapplication.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PieChartActivity extends Activity {

    private PieChart mChart;
    // we're going to display pie chart for smartphones martket shares

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        createPieChart();

    }

    private void createPieChart() {
        mChart = (PieChart) findViewById(R.id.chart);

        // configure pie chart
        mChart.setUsePercentValues(true);
        mChart.setDrawSliceText(false);
        mChart.setDescription("Estátisticas da semana");

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);


        // add data
        addData();

        // customize legends
        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }


    private void addData() {
        HashMap<String, Integer> categorySumHashMap = getSlicesFromSumCategory();

        ArrayList<Entry> categoryTitleList = new ArrayList<>();
        Iterator<String> keySetIterator = categorySumHashMap.keySet().iterator();
        ArrayList<String> categoryPercentList = new ArrayList<>();
        int iteratorCount = 0;
        while(keySetIterator.hasNext()){
            String currentCategory = keySetIterator.next();
            categoryPercentList.add(currentCategory);
            categoryTitleList.add(new Entry(categorySumHashMap.get(currentCategory), iteratorCount));
            ++iteratorCount;
        }

        // create pie data set
        PieDataSet dataSet = new PieDataSet(categoryTitleList, null);
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(categoryPercentList, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }


    @NonNull
    private HashMap<String, Integer> getSlicesFromSumCategory() {

        int categoryCount = 0;
        HashMap<String, Integer> categorySumHashMap = new HashMap<>();
        Intent intent = getIntent();

        while(intent.getStringExtra("category"+categoryCount) != null){
            String currentCategory = intent.getStringExtra("category"+categoryCount);
            int currentCategorySum = intent.getIntExtra(currentCategory, 0);

            categorySumHashMap.put(currentCategory, currentCategorySum);
            ++categoryCount;
        }

        return categorySumHashMap;
    }

}
