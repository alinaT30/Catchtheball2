package com.example.andreeamanole.catchtheball;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;




import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView pacman;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    // Dimensiune
    private int frameHeight;
    private int pacmanSize;
    private int screenWidth;
    private int screenHeight;

    // Pozitie
    private int pacmanY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    // Scor
    private int score = 0;

    // Initializare clasa
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;



    // Verificare status
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);



        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        pacman = (ImageView) findViewById(R.id.pacman);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);

        // Obtinerea dimensiunii ecranului
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Deplasare spre exteriorul ecranului
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score : 0" );

    }

    public void changePos() {

        int min = 50;
        int Level = 1;
        int q;
        q = score / min;

        hitCheck();

        // Orange
        orangeX -= 8 + 2 * q; // Viteza de deplasare pe axa OX
        if (orangeX < 0) {
            orangeX = screenWidth + 35;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // Black
        blackX -= 10 + 2 * q; // Viteza de deplasare pe axa OX
        if (blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        // Pink
        pinkX -= 12 + 2 * q; // Viteza de deplasare pe axa OX
        if (pinkX < 0) {
            pinkX = screenWidth + 4500 - 500 * q;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        // Deplasare pacman

        if (action_flg == true) {
            // Daca ecranul e atins, pacman se duce spre partea superioara a ecranului.
            pacmanY -= 10 + q;
        } else {
            // Daca nu se mai atinge ecranul, pacman se duce spre partea inferioara a ecranului.
            pacmanY += 10 + q;
        }

        // Verificare pozitie pacman
        // Se seteaza limitele pentru pacman, astfel incat sa nu mai iasa din ecran.
        if (pacmanY < 0)
            pacmanY = 0;
        if (pacmanY > frameHeight - pacmanSize)
            pacmanY = frameHeight - pacmanSize;
        pacman.setY(pacmanY);

        Level = q + 1;

        scoreLabel.setText("Score : " + score + " Level " + Level);


    }

    public void hitCheck(){
        // Daca centrul mingii este in interiorul lui pacman, se contorizeaza ca un punct.

        // Orange
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        // 0 <= orangeCenterX <= pacmanWidth
        // pacmanY <= orangeCenterY <= pacmanY + pacmanHeight

        if(0 <= orangeCenterX && orangeCenterX <= pacmanSize && pacmanY <= orangeCenterY && orangeCenterY <= pacmanY + pacmanSize) {

            score += 10;
            orangeX = -10; // face ca bila portocalie sa dispara din ecran, pacman a adunat puncte
            sound.playHitSound();
        }

        // Pink
        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        // 0 <= pinkCenterX <= pacmanWidth
        // pacmanY <= opinkCenterY <= pacmanY + pacmanHeight

        if(0 <= pinkCenterX && pinkCenterX <= pacmanSize && pacmanY <= pinkCenterY && pinkCenterY <= pacmanY + pacmanSize) {

            score += 50;
            pinkX = -10; // face ca bila roz sa dispara din ecran, pacman a adunat puncte
            sound.playHitSound();
        }

        // Black
        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        // 0 <= blackCenterX <= pacmanWidth
        // pacmanY <= blackCenterY <= pacmanY + pacmanHeight

        if(0 <= blackCenterX && blackCenterX <= pacmanSize && pacmanY <= blackCenterY && blackCenterY <= pacmanY + pacmanSize) {
            blackX = -10; // face ca bila neagra sa dispara din ecran, pacman a adunat puncte


            // Se opreste timerul atunci cand pacman prinde bila neagra
            timer.cancel();
            timer = null;

            sound.playOverSound();


            // Se afiseaza rezultatul
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }

    public boolean onTouchEvent(MotionEvent me)
    {
        if(start_flg == false){
            start_flg = true;
            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            pacmanY = (int) pacman.getY();
            pacmanSize = pacman.getHeight();
            startLabel.setVisibility(View.GONE);
            sound.playLoadIsCompleteSound();



            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;


            } else if(me.getAction()== MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
            return  true;
    }

    // Dezactivare buton BACK
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
