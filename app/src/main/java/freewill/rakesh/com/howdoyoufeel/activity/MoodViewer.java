package freewill.rakesh.com.howdoyoufeel.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import freewill.rakesh.com.howdoyoufeel.R;
import freewill.rakesh.com.howdoyoufeel.adapter.MoodTableViewAdapter;
import freewill.rakesh.com.howdoyoufeel.database.DaoHelper;
import freewill.rakesh.com.howdoyoufeel.model.DaoSession;
import freewill.rakesh.com.howdoyoufeel.model.Mood;
import freewill.rakesh.com.howdoyoufeel.model.MoodDao;

public class MoodViewer extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String selectedMood;
    DaoSession daoSession;
    MoodDao moodDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession = DaoHelper.getDaoSession(this);
        moodDao = daoSession.getMoodDao();
        setContentView(R.layout.activity_mood_viewer);

        adapter = new MoodTableViewAdapter(this,moodDao);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.mood_viewer_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void deleteAll(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm dialog demo !");
        builder.setMessage("Do you really want to kill the mood?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                daoSession.deleteAll(Mood.class);
                Toast.makeText(getApplicationContext(), "All records have been deleted Successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mood_logger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_savedValue) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
