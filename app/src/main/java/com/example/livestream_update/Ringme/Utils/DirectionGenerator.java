package com.example.livestream_update.Ringme.Utils;


import android.app.Activity;

import com.vtm.ringme.values.Direction;

import java.util.Random;

public class DirectionGenerator {


    /**
     * Gets the random pixel points in the given direction of the screen
     * @param activity - activity from where you are referring the random value.
     * @param direction - on among LEFT,RIGHT,TOP,BOTTOM,RANDOM
     * @return a pixel point {x,y} in the given direction.
     */
    public int[] getPointsInDirection(Activity activity, Direction direction, boolean isUserClick) {

        switch (direction) {

            case LEFT:
                return getRandomLeft(activity);
            case RIGHT:
                return getRandomRight(activity);
            case BOTTOM:
                return getRandomBottom(activity, isUserClick);
            case TOP:
                return getRandomTop(activity, isUserClick);

            default:
                Direction[] allDirections = new Direction[]{Direction.LEFT,Direction.TOP,Direction.BOTTOM,Direction.RIGHT};
                int index = new Random().nextInt(allDirections.length);
                return getPointsInDirection(activity, allDirections[index], isUserClick);

        }

    }

    /**
     * Gets the random pixel points in the left direction of the screen. The value will be of {0,y} where y will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomLeft(Activity activity) {

        int x = 0;

        int height = activity.getResources().getDisplayMetrics().heightPixels;

        Random random = new Random();
        int y = random.nextInt(height);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the top direction of the screen. The value will be of {x,0} where x will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomTop(Activity activity, boolean isUserClick) {



        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels - (width * 9/16);
        int y = 0;
        if (isUserClick) {
            y = height * 5 / 6;
        }

        Random random = new Random();
        int x = random.nextInt(width);
        if (isUserClick) x = width * 2 / 3;

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the right direction of the screen. The value will be of {screen_width,y} where y will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomRight(Activity activity) {


        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        int x = width ;

        Random random = new Random();
        int y = random.nextInt(height);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the bottom direction of the screen. The value will be of {x,screen_height} where x will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomBottom(Activity activity, boolean isUserClick) {


        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels - (width * 9/16);


        int y = height ;
        Random random = new Random();
        int x = random.nextInt(width);
        if (isUserClick) x = width * 2 / 3;

        return new int[]{x, y};
    }

    /**
     * Gets a random direction.
     * @return one among LEFT,RIGHT,BOTTOM,TOP
     */
    public Direction getRandomDirection() {
        Direction[] allDirections = new Direction[]{Direction.LEFT,Direction.TOP,Direction.BOTTOM,Direction.RIGHT};
        int index = new Random().nextInt(allDirections.length);
        return (allDirections[index]);
    }

    /**
     * Gets a random direction skipping the given direction.
     * @param toSkip a direction which should not be returned by this method.
     * @return one among LEFT,RIGHT,BOTTOM if TOP is provided as direction to skip,
     * one among TOP,RIGHT,BOTTOM if LEFT is provided as direction to skip
     * and so on.
     */
    public Direction getRandomDirection(Direction toSkip) {
        Direction[] allExceptionalDirections;
        switch (toSkip) {

            case LEFT:
                allExceptionalDirections = new Direction[]{Direction.TOP,Direction.BOTTOM,Direction.RIGHT};
                break;
            case RIGHT:
                allExceptionalDirections = new Direction[]{Direction.TOP,Direction.BOTTOM,Direction.LEFT};
                break;
            case BOTTOM:
                allExceptionalDirections = new Direction[]{Direction.TOP,Direction.LEFT,Direction.RIGHT};
                break;
            case TOP:
                allExceptionalDirections = new Direction[]{Direction.LEFT,Direction.BOTTOM,Direction.RIGHT};
                break;

            default:
                allExceptionalDirections = new Direction[]{Direction.LEFT,Direction.TOP,Direction.BOTTOM,Direction.RIGHT};


        }

        int index = new Random().nextInt(allExceptionalDirections.length);
        return (allExceptionalDirections[index]);
    }
}
