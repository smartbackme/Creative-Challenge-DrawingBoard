package com.kangaroo.drawingboard.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.kangaroo.drawingboard.data.model.DrawanPath;
import com.kangaroo.drawingboard.data.model.UserPath;
import com.kangaroo.drawingboard.tools.MqttUtil;
import com.kangaroo.drawingboard.tools.UStore;
import com.kangraoo.basektlib.tools.UUi;
import com.kangraoo.basektlib.tools.json.HJson;

import java.util.function.BiConsumer;

/**
 * @author shidawei
 * 创建日期：2021/7/29
 * 描述：
 */
public class DrawingBoardView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    // SurfaceHolder实例
    private SurfaceHolder mSurfaceHolder;
    // Canvas对象
    private Canvas mCanvas;
    // 控制子线程是否运行
    private boolean startDraw;
    // Path实例
    private Path mPath = new Path();
    // Paint实例
    private Paint mpaint = new Paint();

    public DrawingBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(); // 初始化
    }

    private String name;
    private int color;

    private void initView() {
        name = UStore.INSTANCE.getUser().getName();
        color = UStore.INSTANCE.getUser().getColor();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        this.setKeepScreenOn(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDraw = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        startDraw = false;
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mpaint.setStyle(Paint.Style.STROKE);

            mpaint.setStrokeWidth(UUi.dp2px(getContext(),2));
            mpaint.setColor(color);
            mCanvas.drawPath(mPath, mpaint);
            UStore.INSTANCE.getMap().forEach(new BiConsumer<String, UserPath>() {
                @Override
                public void accept(String s, UserPath userPath) {
                    mpaint.setColor(userPath.getColor());
                    mCanvas.drawPath(userPath.getPath(), mpaint);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            // 对画布内容进行提交
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    public void run() {
        // 如果不停止就一直绘制
        while (startDraw) {
            // 绘制
            draw();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();    //获取手指移动的x坐标
        int y = (int) event.getY();    //获取手指移动的y坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mPath.moveTo(x, y);
                MqttUtil.INSTANCE.message(MqttUtil.INSTANCE.getMove(), new DrawanPath(x,y,1,name,color).toJson());
                break;
            case MotionEvent.ACTION_MOVE:

                mPath.lineTo(x, y);
                MqttUtil.INSTANCE.message(MqttUtil.INSTANCE.getMove(), new DrawanPath(x,y,2,name,color).toJson());
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    // 重置画布
    public void reset() {
        mPath.reset();
//        UStore.INSTANCE.getMap().forEach(new BiConsumer<String, UserPath>() {
//            @Override
//            public void accept(String s, UserPath userPath) {
//                userPath.getPath().reset();
//            }
//        });
    }
}
