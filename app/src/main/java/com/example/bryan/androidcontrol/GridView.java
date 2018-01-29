package com.example.bryan.androidcontrol;

/**
 * Created by Bryan on 1/28/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;


public class GridView extends View {

    private static final int NUM_COLUMNS = 15;  //Range of X-axis
    private static final int NUM_ROWS = 20;     //Range of Y-axis

    private int cellWidth, cellHeight;

    private Paint whitePaint = new Paint();
    private Paint blackPaint = new Paint();
    private Paint bluePaint = new Paint();
    private Paint cyanPaint = new Paint();
    private Paint redPaint = new Paint();
    private Paint greenPaint = new Paint();
    private Paint yellowPaint = new Paint();
    private Paint magentaPaint = new Paint();
    private Paint grayPaint = new Paint();

    // ARENA VARIABLES
    private boolean isMapUnexplored = false;
    private int[] obstacleArray;
    private int[] exploredArray;
    private int[] waypoint = {-1, -1};

    // ROBOT VARIABLES
    private int[] robotFront = {1, 2};
    private int[] robotCenter = {1, 1};
    private int angle = 270;


    public GridView(Context context)
    {
        this(context, null);
    }

    public GridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        whitePaint.setColor(Color.WHITE);       // grid background

        blackPaint.setColor(Color.BLACK);       // grid lines
        blackPaint.setStrokeWidth(4f);

        redPaint.setColor(Color.RED);           // end point
        greenPaint.setColor(Color.GREEN);       // start point

        bluePaint.setColor(Color.BLUE);         // robot body
        cyanPaint.setColor(Color.CYAN);         // robot head

        yellowPaint.setColor(Color.YELLOW);     // waypoint

        magentaPaint.setColor(Color.MAGENTA);

        grayPaint.setColor(Color.GRAY);
    }



    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        calculateCellDimensions();

    }

    private void calculateCellDimensions() {

        cellWidth = getWidth() / NUM_COLUMNS;
        cellHeight = getHeight() / NUM_ROWS;

        if (cellWidth > cellHeight) {
            cellWidth = cellHeight;
        } else {
            cellHeight = cellWidth;
        }

        this.setLayoutParams(new LinearLayout.LayoutParams(cellWidth * NUM_COLUMNS, cellHeight * NUM_ROWS));
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isMapUnexplored) {
            canvas.drawColor(Color.GRAY);
        } else {
            // END POINT IS DRAWN HERE
            canvas.drawRect(12 * cellWidth, 0 * cellHeight,
                    15 * cellWidth, 3 * cellHeight, redPaint);
            // START POINT IS DRAWN HERE
            canvas.drawRect(0 * cellWidth, 17 * cellHeight,
                    3 * cellWidth, 20 * cellHeight, greenPaint);

        }

        // EXPLORED AREA IS DRAWN HERE
        if (exploredArray != null) {
            int obstaclePointer = 0;
            int y = -1;
            for (int i=0; i < exploredArray.length; i++) {

                int x = i%15;

                if (x == 0) {
                    y++;
                }

                if (exploredArray[i] == 1) {
                    String coordinates = x + "," + y;
                    switch (coordinates) {
                        // START POINT FOR EXPLORATION IS DRAWN HERE
                        case "0,0":
                        case "0,1":
                        case "0,2":
                        case "1,0":
                        case "1,1":
                        case "1,2":
                        case "2,0":
                        case "2,1":
                        case "2,2":
                            canvas.drawRect(x * cellWidth, (NUM_ROWS - 1 - y) * cellHeight,
                                    (x + 1) * cellWidth, (NUM_ROWS - y) * cellHeight, greenPaint);
                            break;
                        // END POINT FOR EXPLORATION IS DRAWN HERE
                        case "12,17":
                        case "12,18":
                        case "12,19":
                        case "13,17":
                        case "13,18":
                        case "13,19":
                        case "14,17":
                        case "14,18":
                        case "14,19":
                            canvas.drawRect(x * cellWidth, (NUM_ROWS - 1 - y) * cellHeight,
                                    (x + 1) * cellWidth, (NUM_ROWS - y) * cellHeight, redPaint);
                            break;
                        default:
                            canvas.drawRect(x * cellWidth, (NUM_ROWS - 1 - y) * cellHeight,
                                    (x + 1) * cellWidth, (NUM_ROWS - y) * cellHeight, whitePaint);
                    }


                    // OBSTACLES ARE DRAWN HERE
                    if (obstacleArray != null && obstacleArray[obstaclePointer] == 1) {
                        canvas.drawRect(x * cellWidth, (NUM_ROWS - 1 - y) * cellHeight,
                                (x + 1) * cellWidth, (NUM_ROWS - y) * cellHeight, blackPaint);
                    }
                    obstaclePointer++;
                }
            }
        }


        // WAYPOINT IS DRAWN HERE
        if (waypoint[0] >= 0) {
            canvas.drawRect(waypoint[0] * cellWidth, (NUM_ROWS - 1 - waypoint[1]) * cellHeight,
                    (waypoint[0] + 1) * cellWidth, (NUM_ROWS - waypoint[1]) * cellHeight, yellowPaint);
        }


        // GRID IS DRAWN HERE
        for (int c = 0; c < NUM_COLUMNS + 1; c++) {
            canvas.drawLine(c * cellWidth, 0, c * cellWidth, NUM_ROWS * cellHeight, blackPaint);
        }

        for (int r = 0; r < NUM_ROWS + 1; r++) {
            canvas.drawLine(0, r * cellHeight, NUM_COLUMNS * cellWidth, r * cellHeight, blackPaint);
        }

        // ROBOT IS DRAWN HERE
        if (robotCenter[0] >= 0) {
            canvas.drawCircle(robotCenter[0]*cellWidth+cellWidth/2,
                    (NUM_ROWS-robotCenter[1])*cellHeight-cellHeight/2, 1.3f*cellWidth, bluePaint);
            /**
             canvas.drawRect((robotCenter[0] - 1) * cellWidth, (NUM_ROWS - robotCenter[1] - 2) * cellHeight,
             (robotCenter[0] + 2) * cellWidth, (NUM_ROWS - robotCenter[1] + 1) * cellHeight,  bluePaint);
             */
        }
        if (robotFront[0] >= 0) {
            canvas.drawCircle(robotFront[0]*cellWidth+cellWidth/2,
                    (NUM_ROWS-robotFront[1])*cellHeight-cellHeight/2, 0.3f*cellWidth, cyanPaint);
            /**
             canvas.drawRect(robotFront[0] * cellWidth, (NUM_ROWS - 1 - robotFront[1]) * cellHeight,
             (robotFront[0] + 1) * cellWidth, (NUM_ROWS - robotFront[1]) * cellHeight, cyanPaint);
             */
        }


    }


    public void updateArrays(int[] obstacleArray, int[] exploredArray) {
        this.obstacleArray = obstacleArray;
        this.exploredArray = exploredArray;

        if (MainActivity.isAutoUpdateToggled) {
            invalidate();
        }
    }



    public void setMapUnexplored(boolean b) {
        isMapUnexplored = b;
        clearArrays();
        invalidate();
    }

    private void clearArrays() {

        robotFront[0] = 1;  //x value
        robotFront[1] = 2;  //y value

        robotCenter[0] = 1; //x value
        robotCenter[1] = 1; //y value
        angle = 270;

        exploredArray = null;
        obstacleArray = null;

    }



}