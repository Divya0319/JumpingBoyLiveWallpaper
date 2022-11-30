package com.fastturtle.wallpaperAquarium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by divya gupta on 04-11-2016.
 */
public class JumpingBoyWallpaper extends WallpaperService {
    double x_pos_fish_orange, y_pos_fish_orange, x_pos_cat_fish, y_pos_cat_fish, x_pos_goldfish_right, y_pos_goldfish_right, x_pos_goldfish_left, y_pos_goldfish_left, angle_fish, boy_x_coord, boy_y_coord, jumping_angle_boy = 0;
    int[] boy_sprites = {R.drawable.boy1, R.drawable.boy2, R.drawable.boy3, R.drawable.boy4, R.drawable.boy5, R.drawable.boy6, R.drawable.boy7, R.drawable.boy8};
    int[] orange_fish_sprites = {R.drawable.fish_orange1, R.drawable.fish_orange2, R.drawable.fish_orange3, R.drawable.fish_orange4,};
    int[] cat_fish_sprites = {R.drawable.cat_fish1, R.drawable.cat_fish2, R.drawable.cat_fish3, R.drawable.cat_fish4, R.drawable.cat_fish5, R.drawable.cat_fish6, R.drawable.cat_fish7};
    int fish_steps = 9, fish_steps2 = 9, steps_man = 23, steps_orange = 15, frame_boy = 0, frame_orange = 0, frame_cat = 0, jump = 0;

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    class MyWallpaperEngine extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };
        private boolean visible = true;
        private Bitmap img_fish_orange, img_cat_fish, img_goldfish_right, img_goldfish_left, backgroundImage, img_man;

        MyWallpaperEngine() {
            // get the fish and background image references
            img_fish_orange = BitmapFactory.decodeResource(getResources(), R.drawable.fish_orange1);
            img_cat_fish = BitmapFactory.decodeResource(getResources(), R.drawable.cat_fish1);
            img_goldfish_right = BitmapFactory.decodeResource(getResources(), R.drawable.goldfish_right);
            img_goldfish_left = BitmapFactory.decodeResource(getResources(), R.drawable.goldfish_left);
            backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);


            x_pos_fish_orange = -0.277 * Utils.screenWidth;
            y_pos_fish_orange = Utils.screenHeight * 0.35;
            x_pos_cat_fish = Utils.screenWidth;
            y_pos_cat_fish = Utils.screenHeight * 0.50;
            x_pos_goldfish_right = -0.555 * Utils.screenWidth;
            y_pos_goldfish_right = Utils.screenHeight * 0.75;
            x_pos_goldfish_left = Utils.screenWidth;
            y_pos_goldfish_left = Utils.screenHeight * 0.20;
            boy_x_coord = -0.694 * Utils.screenWidth;
            boy_y_coord = Utils.screenHeight * 0.52;
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            float touchPosition_x = event.getX();
            float touchPosition_y = event.getY();

            if (action == MotionEvent.ACTION_DOWN) {
                if (touchPosition_x >= boy_x_coord && touchPosition_x < (boy_x_coord + img_man.getWidth()) && touchPosition_y >= boy_y_coord && touchPosition_y < (boy_y_coord + img_man.getHeight())) {
                    jump = 1;
                }
            }

        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            // if screen wallpaper is visible then draw the image otherwise do not draw
            if (visible) {
                draw();
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            draw();
        }

        void draw() {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                // clear the canvas
                c.drawColor(Color.BLACK);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(backgroundImage, Utils.screenWidth, Utils.screenHeight, false);
                // draw the background image
                c.drawBitmap(scaledBitmap, 0, 0, null);

                // draw the orange fish
                c.drawBitmap(img_fish_orange, (float) x_pos_fish_orange, (float) y_pos_fish_orange, null);
                img_fish_orange = BitmapFactory.decodeResource(getResources(), orange_fish_sprites[frame_orange++]);

                //draw the cat fish
                c.drawBitmap(img_cat_fish, (float) x_pos_cat_fish, (float) y_pos_cat_fish, null);
                img_cat_fish = BitmapFactory.decodeResource(getResources(), cat_fish_sprites[frame_cat++]);

                c.drawBitmap(img_goldfish_right, (float) x_pos_goldfish_right, (float) y_pos_goldfish_right, null);
                c.drawBitmap(img_goldfish_left, (float) x_pos_goldfish_left, (float) y_pos_goldfish_left, null);

                if (jump == 1) {
                    boy_y_coord = boy_y_coord - (100 * Math.sin(Math.toDegrees(jumping_angle_boy)));
                    img_man = BitmapFactory.decodeResource(getResources(), R.drawable.boy4);
                    jumping_angle_boy++;
                    c.drawBitmap(img_man, (float) boy_x_coord, (float) boy_y_coord, null);
                    if (jumping_angle_boy == 9) {
                        jump = 0;
                    }
                } else {
                    boy_y_coord = Utils.screenHeight * 0.52;
                    img_man = BitmapFactory.decodeResource(getResources(), boy_sprites[frame_boy++]);
                    c.drawBitmap(img_man, (float) boy_x_coord, (float) boy_y_coord, null);
                }

                // if x crosses the width means  x has reached to right edge
                if (x_pos_fish_orange > Utils.screenWidth + (0.277 * Utils.screenWidth)) {
                    // assign initial value to start with
                    x_pos_fish_orange = -0.277 * Utils.screenWidth;
                }
                if (angle_fish > Math.toDegrees(180)) {
                    angle_fish = 0;
                }
                if (x_pos_cat_fish < -150) {
                    x_pos_cat_fish = Utils.screenWidth;
                }
                if (boy_x_coord > Utils.screenWidth + 100) {
                    boy_x_coord = -0.694 * Utils.screenWidth;
                }
                if (x_pos_goldfish_right > Utils.screenWidth + 100) {
                    x_pos_goldfish_right = -300;
                }
                if (x_pos_goldfish_left < -300) {
                    x_pos_goldfish_left = Utils.screenWidth;
                }
                if (frame_orange == 4) {
                    frame_orange = 0;
                }
                if (frame_cat == 7) {
                    frame_cat = 0;
                }
                if (frame_boy == 8) {
                    frame_boy = 0;
                }
                if (jumping_angle_boy == 9) {
                    jumping_angle_boy = 0;
                } else {
                    // change the x position/value by steps
                    x_pos_fish_orange = x_pos_fish_orange + steps_orange;
                    x_pos_cat_fish = x_pos_cat_fish - fish_steps2;
                    x_pos_goldfish_right = x_pos_goldfish_right + fish_steps;
                    x_pos_goldfish_left = x_pos_goldfish_left - fish_steps2;
                    boy_x_coord = boy_x_coord + steps_man;
                }
            } finally {
                if (c != null) {
                    try {
                        holder.unlockCanvasAndPost(c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                handler.removeCallbacks(drawRunner);
                if (visible) {
                    handler.postDelayed(drawRunner, 25);  // delay in milliseconds here
                }
            }
        }
    }
}
