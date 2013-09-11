package humaj.michal.uilogic;

import humaj.michal.activity.GameActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

public class PauseDialog extends DialogFragment {

	public interface BackDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
	}

	GameActivity mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (GameActivity) activity;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		mActivity.onDialogDismiss();
		super.onDismiss(dialog);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Log.d("kktina", "Pause Dialog Created");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setMessage("Game Paused").setNeutralButton("Resume", null);
		return builder.create();
	}

}
