package com.saray.sinotik.dialog;

import com.saray.sinotk.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class ProgressDialog extends AlertDialog{

	private Context context;
	private View mView;

	public ProgressDialog(Context context,String title) {
		super(context);
		this.context = context;
		buildDialogContent(context,title);
	}
	
    public int buildDialogContent(Context context,String title) {
    	this.setTitle(title);
    	
        mView = this.getLayoutInflater().inflate(R.layout.sinotik_progress_dialog,null);
        this.setView(mView);
        
		return 0;
	}
 
 
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
        case BUTTON_POSITIVE:
            //open desk and send the desk number¡¢client number¡¢time¡¢waiter number to OrderTbl
 
            break;
        case BUTTON_NEGATIVE:
            //Don't need to do anything
            break;
        default:
        }
    }

}
