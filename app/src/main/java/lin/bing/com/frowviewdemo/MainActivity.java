package lin.bing.com.frowviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;


public class MainActivity extends Activity {
    private TagAdapter mAdapter;
    private FlowLayout mFlowLayout;
    private String[] datas={"working","studying","sleeping and play game","coding","go shopping ","play basecetball"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        mFlowLayout.setClickable(true);
        final LayoutInflater inflater = LayoutInflater.from(this);
        mFlowLayout.setAdapter(new TagAdapter(datas) {
            @Override
            public View getView(FlowLayout layout, int postion, Object o) {
                View itemView = inflater.inflate(R.layout.item, layout, false);
                TextView text = (TextView) itemView.findViewById(R.id.text);
                text.setText(String.valueOf(o));
                return itemView;
            }
        });
        mFlowLayout.setOnSelectListener(new FlowLayout.OnSelectListener() {
            @Override
            public void onSelect(View view, int postion) {
                Toast.makeText(MainActivity.this, "click " + datas[postion], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
