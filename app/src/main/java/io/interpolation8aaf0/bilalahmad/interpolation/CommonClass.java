package io.interpolation8aaf0.bilalahmad.interpolation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

public class CommonClass {
    ProgressDialog progress = null;
    boolean returning = false;
    public boolean validateInput(EditText email, EditText password) {
        boolean check = true;
        String emailText  = email.getText().toString().replace(" ", "");
        if (!emailText.contains("@") || !emailText.contains(".")) {
            email.setError("Email is not valid!");
            check = false;
        }
        if (password != null) {
            if (password.getText().toString().length() < 6) {
                password.setError("min Password length is 6!");
                check = false;
            }
        }
        return check;
    }

    public void showDialog(Spanned title, Spanned given, int icon, Context context) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(given)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setIcon(icon)
                .show();
    }

    public boolean showConfirmation(Spanned title, Spanned given, int icon, Context context) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(given)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        returning = true;
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        returning = false;
                    }
                })
                .setIcon(icon)
                .show();
        return returning;
    }

    public void showProgress(Context context, String title, String display) {
        progress = new ProgressDialog(context);
        progress.setTitle(Html.fromHtml(title));
        progress.setMessage(display);
        progress.setCancelable(false);
        progress.show();
    }

    public void cancelProgress() {
        try {
            progress.dismiss();
            progress.cancel();
        } catch (Exception e) {}
    }


}
