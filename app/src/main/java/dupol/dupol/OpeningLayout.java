package dupol.dupol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class OpeningLayout extends AppCompatActivity {
    private ImageView ii;
    private TextView uu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_layout);

        uu = (TextView)findViewById(R.id.pol);
        ii = (ImageView) findViewById(R.id.dut);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        ii.startAnimation(myanim);
        uu.startAnimation(myanim);


        final Intent i = new Intent(this, MainActivity.class);


        Thread timer = new Thread(){
            public void run (){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
