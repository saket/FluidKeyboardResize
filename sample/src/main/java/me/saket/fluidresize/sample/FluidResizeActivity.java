package me.saket.fluidresize.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import me.saket.fluidresize.FluidContentResizer;
import me.saket.fluidresize.R;

public class FluidResizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fluid_resize);

        new FluidContentResizer().listen(this);
    }
}
