package com.ggt.cardetosample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ggt.cardeto.CardetoService;
import com.ggt.cardetosample.database.CardetoSampleDatabaseDataSource;

/**
 * A simple sample activity showing usage of cardeto.
 *
 * @author guiguito
 */
public class TestActivity extends Activity {

    private ScrollView mLogsScrollView;
    private TextView mLogsTextView;
    private CardetoSampleDatabaseDataSource mCardetoSampleDatabaseDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mLogsScrollView = (ScrollView) findViewById(R.id.logsScrollView);
        mLogsTextView = (TextView) findViewById(R.id.logsTextView);
        mCardetoSampleDatabaseDataSource = new CardetoSampleDatabaseDataSource(
                getApplicationContext());
        mCardetoSampleDatabaseDataSource.open();
    }

    private void addLog(String log) {
        mLogsTextView.setText(mLogsTextView.getText() + "\n" + log);
        mLogsScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void onStartCardetoServiceButtonClicked(View view) {
        Intent intent = new Intent(this, CardetoService.class);
        intent.putExtra(CardetoService.CARDETO_PORT, 2000);
        startService(intent);
        addLog("Service started");
    }

    public void onStopCardetoServiceButtonClicked(View view) {
        stopService(new Intent(this, CardetoService.class));
        addLog("Service stopped");
    }

    public void onAddRandomLogButtonClicked(View view) {
        mCardetoSampleDatabaseDataSource.createLog(System.currentTimeMillis() + "");
        addLog("Log added");
    }

    public void onClearLogsButtonClicked(View view) {
        mCardetoSampleDatabaseDataSource.clearLogs();
        addLog("Logs cleared");
    }

}
