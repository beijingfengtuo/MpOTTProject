package cn.cibnmp.ott.player;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by sh on 17/3/21.
 */

public abstract class BasePlayerPage extends LinearLayout implements IPlayerHandle {

    public boolean need2pauseForAct = false;

    public boolean usetea = false;
    public boolean useteaforyouku = false;
    public int youkuCon = 0;
    public boolean teaPrepare = false;
    public boolean videoPrepare = false;

    public BasePlayerPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
