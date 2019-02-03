package nokia.tablefootball.tablefootballandroid.adapters

import android.app.AlertDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import nokia.tablefootball.tablefootballandroid.R
import nokia.tablefootball.tablefootballandroid.dto.TableModel

class ChildItemAdapter(private val context: Context, var dtosList: ArrayList<TableModel>) :
    RecyclerView.Adapter<ChildItemAdapter.TableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cardView = inflater.inflate(R.layout.list_item, parent, false)

        return TableViewHolder(cardView)

    }

    override fun getItemCount(): Int {
        return dtosList.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val tableDto = dtosList[position]
        holder.bindDto(tableDto)

    }

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tableImageView: ImageView = itemView.findViewById(R.id.table_imageview) as ImageView
        private var tableStateTextView: TextView = itemView.findViewById(R.id.table_state_textview) as TextView
        private var tableRoomTextView: TextView = itemView.findViewById(R.id.table_room_textview) as TextView

        private var tableModel: TableModel? = null

        fun bindDto(model: TableModel) {
            tableModel = model

            tableImageView.apply {
                setImageResource(
                    when (tableModel!!.online) {
                        false -> R.mipmap.table_inactive
                        true -> if (tableModel!!.occupied) R.mipmap.table_occupied else R.mipmap.table_free
                    })

                setOnLongClickListener(TableLongClickListener(tableModel!!))
            }

            tableRoomTextView.text = "Room: ${tableModel!!.room}"

            tableStateTextView.apply {
                if (!tableModel!!.online) {
                    text = context.getString(R.string.table_state_inactive)
                    setTextColor(ContextCompat.getColor(context, R.color.colorTableInactive))
                } else when (tableModel!!.occupied) {
                    false -> {
                        text = context.getString(R.string.table_state_free)
                        setTextColor(ContextCompat.getColor(context, R.color.colorTableFree))
                    }
                    true -> {
                        text = context.getString(R.string.table_state_occupied)
                        setTextColor(ContextCompat.getColor(context, R.color.colorTableOccupied))
                    }
                }
            }


        }

    }

    class TableLongClickListener(private val tableModel: TableModel) : View.OnLongClickListener {
        override fun onLongClick(view: View?): Boolean {
            val alertDialog: AlertDialog? = view?.let {
                val builder = AlertDialog.Builder(view.context)
                builder.apply {
                    setPositiveButton(R.string.dialog_ok, null)
                    setNegativeButton(R.string.dialog_cancel, null)
                }
                builder.create()
            }

            alertDialog?.show()

            return true
        }

    }


}