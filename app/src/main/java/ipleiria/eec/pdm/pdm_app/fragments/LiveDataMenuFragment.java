package ipleiria.eec.pdm.pdm_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;

public class LiveDataMenuFragment extends Fragment {
    private LineChart lineChart;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<Entry> dataEntries;
    private TextView currentValueText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_data_menu, container, false);

        // Initialize chart components
        lineChart = view.findViewById(R.id.live_data_chart);
        currentValueText = view.findViewById(R.id.current_value_text);
        EditText inputValue = view.findViewById(R.id.input_value);
        Button addValueButton = view.findViewById(R.id.add_value_button);

        // Initialize chart data
        dataEntries = new ArrayList<>();
        lineDataSet = new LineDataSet(dataEntries, "Live Data");
        lineDataSet.setLineWidth(2f);
        lineDataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
        lineDataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        lineDataSet.setCircleColor(getResources().getColor(android.R.color.holo_blue_dark));
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Configure chart appearance
        configureChartAppearance();

        // Add button click listener
        addValueButton.setOnClickListener(v -> {
            String valueString = inputValue.getText().toString();
            if (!valueString.isEmpty()) {
                float value = Float.parseFloat(valueString);
                addValueToChart(value);
                currentValueText.setText("Current Value: " + value);
                inputValue.setText("");
            }
        });

        return view;
    }

    /**
     * configura a aparência do gráfico
     */
    private void configureChartAppearance() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
    }

    /**
     * adiciona um valor ao gráfico
     * @param value valor a ser adicionado
     */
    private void addValueToChart(float value) {
        dataEntries.add(new Entry(dataEntries.size(), value));
        lineDataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate(); // Refresh the chart
    }
}
