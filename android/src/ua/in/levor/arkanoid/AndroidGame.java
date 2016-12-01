package ua.in.levor.arkanoid;


import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class AndroidGame extends Arkanoid {
    private Context context;

    public AndroidGame(Context context) {
        this.context = context;
    }

    @Override
    public void showMessageDialog(final String message) {
        // TODO: 12/1/16 show custom message dialog
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
