package kr.heegyu.wearablesample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener
{

    Button button;
    EditText editText;
    DataClient dataClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataClient = Wearable.getDataClient(this);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        // 텍스트를 워치로  전송
        String text = editText.getText().toString();

        // 보낼 데이터 생성
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/text");
        DataMap dataMap = putDataMapRequest.getDataMap();
        dataMap.putString("text", text);

        // 데이터 전송
        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        Task<DataItem> task = dataClient.putDataItem(putDataRequest);

        // 리스너 등록(안해도됨)
        task.addOnCompleteListener(this, new OnCompleteListener<DataItem>() {
            @Override
            public void onComplete(@NonNull Task<DataItem> task) {
                Toast.makeText(MainActivity.this, "전송 완료!", Toast.LENGTH_SHORT).show();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "전송 실패!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
