package com.trabajo.carlos.fireball.utils;

import com.dd.processbutton.ProcessButton;
import android.os.Handler;
import java.util.Random;

/**
 * Created by Carlos Prieto on 14/02/2017.
 *
 * Clase para la animacion de carga de los botones de logueo y registro
 *
 */

public class ProgressGenerator {

    public interface OnCompleteListener {

        public void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mProgress += 10;
                button.setProgress(mProgress);

                if (mProgress < 100) {

                    handler.postDelayed(this, generateDelay());

                } else {

                    mListener.onComplete();
                }

            }
        }, generateDelay());

    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(500);
    }
}
