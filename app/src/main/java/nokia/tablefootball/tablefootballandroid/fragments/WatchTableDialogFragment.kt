package nokia.tablefootball.tablefootballandroid.fragments

import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import nokia.tablefootball.tablefootballandroid.R

class WatchTableDialogFragment : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Hello")
                .setPositiveButton(getString(R.string.dialog_ok),
                    DialogInterface.OnClickListener { dialog, id ->
                        // FIRE ZE MISSILES!
                    })
                .setNegativeButton(getString(R.string.dialog_cancel),
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}