package edu.unc.sanjorge.eight_queens;

import android.content.Context;

/**
 * Created by sanjorge on 8/31/17.
 */

public class boardTile extends android.support.v7.widget.AppCompatButton {
    int xCor;
    int yCor;

    public boardTile(Context context, int i, int j) {
        super(context);
        this.xCor = i;
        this.yCor = j;
    }
}
