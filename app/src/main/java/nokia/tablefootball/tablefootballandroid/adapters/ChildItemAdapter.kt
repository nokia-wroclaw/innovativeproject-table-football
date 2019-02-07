package nokia.tablefootball.tablefootballandroid.adapters

import android.app.AlertDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

import nokia.tablefootball.tablefootballandroid.R
import nokia.tablefootball.tablefootballandroid.model.TableModel
import nokia.tablefootball.tablefootballandroid.network.ServerCommunicationAPIController
import nokia.tablefootball.tablefootballandroid.network.ServerCommunicationServiceImpl

import org.json.JSONObject

import java.util.*

class ChildItemAdapter(private val context: Context, var dtosList: ArrayList<TableModel>) :
    RecyclerView.Adapter<ChildItemAdapter.TableViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cardView = inflater.inflate(R.layout.list_item, parent, false)

        return TableViewHolder(cardView)
    }

    override fun getItemCount(): Int
    {
        return dtosList.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int)
    {
        val tableDto = dtosList[position]
        holder.bindDto(tableDto)
    }

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var tableImageView: ImageView = itemView.findViewById(R.id.table_imageview) as ImageView
        private var tableStateTextView: TextView = itemView.findViewById(R.id.table_state_textview) as TextView
        private var tableRoomTextView: TextView = itemView.findViewById(R.id.table_room_textview) as TextView

        private var tableModel: TableModel? = null

        fun bindDto(model: TableModel)
        {
            tableModel = model

            tableRoomTextView.text = "Room: ${tableModel!!.room}"

            tableImageView.apply {
                setImageResource(
                    when (tableModel!!.online)
                    {
                        false -> R.drawable.table_inactive
                        true -> if (tableModel!!.occupied) R.drawable.table_occupied else R.drawable.table_free
                    }
                )

                setOnLongClickListener(TableLongClickListener(tableModel!!, context))
            }

            tableStateTextView.apply {
                if (!tableModel!!.online)
                {
                    text = context.getString(R.string.table_state_inactive)
                    setTextColor(ContextCompat.getColor(context, R.color.colorTableInactive))
                }
                else when (tableModel!!.occupied)
                {
                    false ->
                    {
                        text = context.getString(R.string.table_state_free)
                        setTextColor(ContextCompat.getColor(context, R.color.colorTableFree))
                    }
                    true ->
                    {
                        text = context.getString(R.string.table_state_occupied)
                        setTextColor(ContextCompat.getColor(context, R.color.colorTableOccupied))
                    }
                }
            }
        }
    }

    class TableLongClickListener(private val tableModel: TableModel, private val context: Context) :
        View.OnLongClickListener
    {
        override fun onLongClick(view: View?): Boolean
        {
            val dialog = AlertDialog.Builder(context)
                .setTitle("Watch this table?")
                .setMessage("You will be notified when it becomes free")
                .setPositiveButton(
                    R.string.dialog_ok
                ) { dialog, which ->
                    obtainTokenThenRegister();
                }
                .setNegativeButton(R.string.dialog_cancel, null)
                .create()

            dialog.show()
            return true
        }

        private fun obtainTokenThenRegister(){
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        val token = task.result?.token
                        val jsonObject =
                            JSONObject("{\"fcm_token\":\"${token.toString()}\",\"sensors_id\":[\"${tableModel.id}\"]} ")

                        val serviceImpl = ServerCommunicationServiceImpl(context)
                        val controller = ServerCommunicationAPIController(serviceImpl)

                        controller.post("http://192.168.0.112:8080/register", jsonObject) { response ->
                            Toast.makeText(context, "You observe that table now", Toast.LENGTH_SHORT).show()
                        }

                    }
                    else
                    {
                        Log.w("FIREBASE", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                })
        }

    }


}