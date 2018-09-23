package kr.heegyu.wearablesample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends WearableActivity
    implements DataClient.OnDataChangedListener
{
    private static final String TAG = "MainActivity";
    private TextView mTextView;
    private DataClient dataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        dataClient = Wearable.getDataClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataClient.addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataClient.removeListener(this);
    }

    /**
     *
     * @param dataEventBuffer
     *
     * 폰으로부터 데이터를 받았을 때 호출되는 부분!!
     */
    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        // 받은 데이터가 1개 이상일 때
        if(dataEventBuffer.getCount() > 0) {
            // 첫번째 데이터를 가져옴
            DataEvent event = dataEventBuffer.get(0);
            DataItem item = event.getDataItem();

            // 데이터가 /text 일 때 문자열 데이터를 가져옴
            // uri로 데이터를 구분할 수 있음
            if(item.getUri().getPath().equals("/text")) {
                DataMap dataMap = DataMap.fromByteArray(item.getData());
                String text = dataMap.getString("text");
                mTextView.setText(text);
            }
        }
        dataEventBuffer.release();
    }
}
